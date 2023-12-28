import org.htmlunit.BrowserVersion;
import org.htmlunit.NicelyResynchronizingAjaxController;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GetPriceCommand implements CommandHandler{

    public static ArrayList<String> pricesList;

    @Override
    public SendMessage execute(String receivedText, Update update) {

        SendMessage message = new SendMessage();
        long chatId = update.getMessage().getChatId();
        message.setText("Enter a product name to check price:");
        message.setChatId(String.valueOf(chatId));
        return message;
    }


    public void savePage(String productName) {
        Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        String link = "https://www.ceneo.pl/;szukaj-" + productName;
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        HtmlPage htmlPage = null;

        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.waitForBackgroundJavaScriptStartingBefore(3000);

        try {
            htmlPage = webClient.getPage(link);
            webClient.waitForBackgroundJavaScript(3000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (htmlPage!=null){
            try (FileWriter fileWriter = new FileWriter("output.html")) {
                fileWriter.write(Jsoup.parse(htmlPage.asXml()).toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<String> listOfPrices() {
        String impr;
        pricesList = new ArrayList<>();

        try {
            File inputFile = new File("output.html");
            Document doc = Jsoup.parse(inputFile, "UTF-8");
            Elements scriptElements = doc.getElementsByTag("script");

            for (Element script : scriptElements) {
                String scriptContent = script.html();
                if (scriptContent.contains("var impressions")) {
                    impr = extractImpressions(scriptContent);
                    JSONArray jsonArray = new JSONArray(impr);

                    for (int i = 0; i < Math.min(4, jsonArray.length()); i++) { // First 4 prices on ceneo.pl shows the best result of searching
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String price = obj.getString("price");
                        pricesList.add(price);
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pricesList;
    }

    private static String extractImpressions(String scriptContent) {
        Pattern pattern = Pattern.compile("var impressions\\s*=\\s*(.*?);");
        Matcher matcher = pattern.matcher(scriptContent);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    public String priceOfProduct(ArrayList<String> prices){
        String price = null;
        double sum = 0;
        for(String element:prices){
            sum += Double.parseDouble(element);
        }
        price = String.format("%.2f", sum/prices.size());
        return price;
    }

}

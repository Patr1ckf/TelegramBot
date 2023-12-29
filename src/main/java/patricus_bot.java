import Commands.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.ChatLocation;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.HashMap;
import java.util.Map;

public class patricus_bot extends TelegramLongPollingBot{

    private final Map<String, CommandHandler> commands = new HashMap<>();
    private MakeListCommand makeListCommand = new MakeListCommand();
    private GetPriceCommand getPriceCommand = new GetPriceCommand();
    private boolean isMakingList = false;
    private boolean isCheckingPrice = false;

    public patricus_bot() {
        commands.put("/start", new StartCommand());
        commands.put("/makelist", new MakeListCommand());
        commands.put("/showlist", new ShowListCommand());
        commands.put("/checkprice", new GetPriceCommand());
        commands.put("/checkpricelist", new ShowPriceOfList());
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String received = update.getMessage().getText();

            if(received.startsWith("/")){
                CommandHandler command = commands.get(received);

                if(command != null){
                    if(received.equals("/makelist")){
                        isMakingList = true;
                    }
                    else if(received.equals("/checkprice")){
                        isCheckingPrice = true;
                    }
                    else {
                        isMakingList=false;
                        isCheckingPrice = false;
                    }
                    SendMessage message = command.execute(received, update);
                    message.setChatId(String.valueOf(chatId));

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    sendMessageIfUnknownCommand(chatId);
                }
            }
            else if(isMakingList){
                SendMessage message;
                if(received.equals("end")){
                    message = new SendMessage();
                    message.setChatId(String.valueOf(chatId));
                    message.setText("Your shopping list is done!");
                    isMakingList = false;
                }
                else{
                    makeListCommand.addItem(received);
                    message = new SendMessage();
                    message.setChatId(String.valueOf(chatId));
                    message.setText("Added " + received + " to the list\n\n" +
                            "What else would you like to add?\nType 'end' to end making a list");
                }

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else if(isCheckingPrice){
                SendMessage message;
                String priceFinal = null;
                boolean success = true;

                message = new SendMessage();
                message.setChatId(String.valueOf(chatId));
                message.setText("I'm checking the price, please wait a while");
                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

                message = new SendMessage();
                message.setChatId(String.valueOf(chatId));

                for(int i=0; i<3; i++){
                    getPriceCommand.savePage(received);
                    priceFinal = getPriceCommand.priceOfProduct(getPriceCommand.listOfPrices());
                    if(!priceFinal.equals("NaN")){
                        break;
                    }
                    else{
                        success = false;
                    }
                }
                if(success){
                    message.setText("The average cost of "+received+" is "+ priceFinal+" zł");
                }
                else{
                    message.setText("Sorry, I couldn't find this product :((");
                }

                isCheckingPrice=false;

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
            else{
                sendMessageIfUnknownCommand(chatId);
            }
        }

    }

    private void sendMessageIfUnknownCommand(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Unknown command :( \n\n" +
                "Try this:\n" +
                "/makelist -> To start making a shopping list\n" +
                "/showlist -> To show the last shopping last\n" +
                "/checkprice -> To check the price of the product\n"+
                "/checkpricelist -> To check the price of the last shopping list");
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "patricus_bot";
    }

    @Override
    public String getBotToken() {
        return "6952857306:AAFw9e2w7iN4nZc5NT9GyrnJFyIa-3WXClA";
    }
}

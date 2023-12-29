import Commands.*;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.HashMap;
import java.util.Map;

public class patricus_bot extends TelegramLongPollingBot{

    private final Map<String, CommandHandler> commands = new HashMap<>();
    private final MakeListCommand makeListCommand = new MakeListCommand();
    private final GetPriceCommand getPriceCommand = new GetPriceCommand();
    SendMessage message = new SendMessage();
    CommandHandler command;
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
                command = commands.get(received);

                if(command != null){
                    switch (received) {
                        case "/makelist" -> {
                            MakeListCommand.shoppingList.clear();
                            isMakingList = true;
                        }
                        case "/checkprice" -> isCheckingPrice = true;
                        case "/checkpricelist" -> {
                            message.setChatId(String.valueOf(chatId));
                            message.setText("I'm checking prices of products, please wait a while");
                            try {
                                execute(message);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        default -> {
                            isMakingList = false;
                            isCheckingPrice = false;
                        }
                    }
                    message = command.execute(received, update);
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
                if(received.equals("end")){
                    message.setChatId(String.valueOf(chatId));
                    message.setText("Your shopping list is done!");
                    isMakingList = false;
                }
                else{
                    makeListCommand.addItem(received);
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
                String priceFinal = null;
                boolean success = true;

                message.setChatId(String.valueOf(chatId));
                message.setText("I'm checking the price, please wait a while");

                try {
                    execute(message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

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
        message.setChatId(String.valueOf(chatId));
        message.setText("""
                Unknown command :(\s

                Try this:
                /makelist -> To start making a shopping list
                /showlist -> To show the last shopping last
                /checkprice -> To check the price of the product
                /checkpricelist -> To check the price of the last shopping list""");
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

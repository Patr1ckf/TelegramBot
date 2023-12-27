import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class patricus_bot extends TelegramLongPollingBot{

    private final Map<String, CommandHandler> commands = new HashMap<>();

    public patricus_bot() {
        commands.put("/start", new StartCommand());
        commands.put("/makelist", new MakeListCommand());
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String received = update.getMessage().getText();

            if(received.startsWith("/")){
                CommandHandler command = commands.get(received);

                if(command != null){
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
                "/makelist -> To start making a list of your shopping\n");
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

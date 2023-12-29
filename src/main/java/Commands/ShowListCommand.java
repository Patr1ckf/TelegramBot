package Commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ShowListCommand implements CommandHandler{
    @Override
    public SendMessage execute(String receivedText, Update update) {

        SendMessage message = new SendMessage();
        long chatId = update.getMessage().getChatId();

        StringBuilder shoppingList = new StringBuilder();
        for(String x:MakeListCommand.shoppingList){
            shoppingList.append(x+"\n");
        }

        message.setText("Your shopping list:\n" + shoppingList);
        message.setChatId(String.valueOf(chatId));

        return message;
    }
}

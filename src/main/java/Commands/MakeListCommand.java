package Commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class MakeListCommand implements CommandHandler {

    public static List<String> shoppingList;

    public MakeListCommand(){
        shoppingList = new ArrayList<>();
    }

    @Override
    public SendMessage execute(String receivedText, Update update) {

        SendMessage message = new SendMessage();
        long chatId = update.getMessage().getChatId();
        message.setText("What would you like to add to your shopping list?\n");
        message.setChatId(String.valueOf(chatId));
        return message;
    }

    public void addItem(String receivedText){
        shoppingList.add(receivedText);
    }

}

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

public class StartCommand implements CommandHandler{

    @Override
    public SendMessage execute(String receivedText, Update update) {

        SendMessage message = new SendMessage();
        User user = update.getMessage().getFrom();
        String firstName = user.getFirstName();
        message.setText("Hello " + firstName + "! Welcome to the chat!\n\n" +
                "Try this:\n" +
                "/makelist -> To start making a list of your shopping\n" +
                "/showlist -> To show the last shopping last\n");
        return message;
    }
}

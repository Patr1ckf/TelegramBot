import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class MakeListCommand implements CommandHandler {

    @Override
    public SendMessage execute(String receivedText, Update update) {
        SendMessage message = new SendMessage();
        message.setText("done");
        return message;
    }
}

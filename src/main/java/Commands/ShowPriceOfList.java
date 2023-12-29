package Commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ShowPriceOfList implements CommandHandler{
    GetPriceCommand getPrice;
    StringBuilder shoppingListPrices;
    String priceOfProduct;
    double sum =0;

    @Override
    public SendMessage execute(String receivedText, Update update) {

        getPrice = new GetPriceCommand();
        shoppingListPrices = new StringBuilder();
        shoppingListPrices.append("Your shopping list with prices:\n");
        SendMessage message = new SendMessage();
        long chatId = update.getMessage().getChatId();

        if(!MakeListCommand.shoppingList.isEmpty()){
            for(String x:MakeListCommand.shoppingList){
                getPrice.savePage(x);
                priceOfProduct = getPrice.priceOfProduct(getPrice.listOfPrices());
                priceOfProduct = priceOfProduct.replace(',','.');
                if(priceOfProduct.equals("NaN")){
                    shoppingListPrices.append(x);
                    shoppingListPrices.append(": price not found\n");
                }
                else {
                    shoppingListPrices.append(x);
                    shoppingListPrices.append(": ");
                    shoppingListPrices.append(priceOfProduct);
                    shoppingListPrices.append(" zł\n");
                    sum += Double.parseDouble(priceOfProduct);

                }
            }
            shoppingListPrices.append("\n");
            shoppingListPrices.append("Sum of your products: ");
            shoppingListPrices.append(sum);
            shoppingListPrices.append(" zł");
            message.setText(String.valueOf(shoppingListPrices));
        }
        else{
            message.setText("You haven't made any list yet");
        }

        message.setChatId(String.valueOf(chatId));
        return message;
    }
}

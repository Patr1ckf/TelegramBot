package Commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GetPriceCommandTest {

    GetPriceCommand getPriceCommandTest;

    @BeforeEach
    void setUp(){
        getPriceCommandTest = new GetPriceCommand();
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Test if savePage method create file with some content")
    void savePage() {
        getPriceCommandTest = new GetPriceCommand();
        String productName = "airpods";
        getPriceCommandTest.savePage(productName);
        File file = new File("outputs/fake_output.html");
        assertTrue(file.exists() && file.length() > 0);
    }


    @org.junit.jupiter.api.Test
    void priceOfProduct() {
        ArrayList<String> prices = new ArrayList<>();
        prices.add("100");
        prices.add("200");
        getPriceCommandTest = new GetPriceCommand();
        String calculatedPrice = getPriceCommandTest.priceOfProduct(prices);

        assertEquals("150,00", calculatedPrice);
    }

    @org.junit.jupiter.api.Test
    void extractImpressionTest(){
        String scriptContent = "var impressions = [{\"price\":\"100\"},{\"price\":\"200\"}];";
        getPriceCommandTest = new GetPriceCommand();
        String extractedImpressions = getPriceCommandTest.extractImpressions(scriptContent);

        assertEquals("[{\"price\":\"100\"},{\"price\":\"200\"}]", extractedImpressions);
    }
}
package pl.piotrak.output;

import org.springframework.stereotype.Service;
import pl.piotrak.product.Product;

import java.util.Map;

import static pl.piotrak.utils.Constants.NEW_SALE;

/**
 * LCD display output device
 */
@Service("lcdDisplay")
public class LCDDisplay implements OutputDevice {

    /**
     @inheritDoc
     */
    @Override
    public OutputDeviceType getDeviceType() {
        return OutputDeviceType.LCD;
    }

    /**
     @inheritDoc
     */
    @Override
    public void endSale(Map<Product, Integer> receipt, double total) {
        System.out.println("TOTAL: " + total);
    }

    /**
     @inheritDoc
     */
    @Override
    public void newSale() {
        System.out.println("Clearing screen...\n" + NEW_SALE);
    }

    /**
     @inheritDoc
     */
    @Override
    public void output(String text) {
        System.out.println(text);
    }


}

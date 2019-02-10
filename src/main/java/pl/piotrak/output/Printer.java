package pl.piotrak.output;

import org.springframework.stereotype.Service;
import pl.piotrak.product.Product;

import java.util.Iterator;
import java.util.Map;

import static pl.piotrak.utils.Constants.NEW_SALE;

/**
 * Printer output device
 */
@Service("printer")
public class Printer implements OutputDevice {

    /**
     @inheritDoc
     */
    @Override
    public OutputDeviceType getDeviceType() {
        return OutputDeviceType.PRINTER;
    }

    /**
     @inheritDoc
     */
    @Override
    public void endSale(Map<Product, Integer> receipt, double total) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("Thank you for shopping at our store\n\n");
        Iterator it = receipt.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            sb.append(pair.getValue()).append("\t\t").append(pair.getKey());
        }
        sb.append("TOTAL: ").append(total);
        System.out.println(sb.toString());
    }

    /**
     @inheritDoc
     */
    @Override
    public void newSale() {
        System.out.println("\n\n\n\n\n" + NEW_SALE);
    }

    /**
     * No messages need to be printed
     */
    @Override
    public void output(String text) {
        //do nothing
    }
}

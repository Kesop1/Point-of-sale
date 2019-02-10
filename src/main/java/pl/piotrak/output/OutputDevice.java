package pl.piotrak.output;

import pl.piotrak.product.Product;

import java.util.Map;

public interface OutputDevice {

    /**
     * Get type of the output device
     * @return type of the output device
     */
    OutputDeviceType getDeviceType();

    /**
     * Output end sale command
     * @param receipt Output receipt
     * @param total Ouput total amount
     */
    void endSale(Map<Product, Integer> receipt, double total);

    /**
     * Output new sale command
     */
    void newSale();

    /**
     * Output text message
     * @param text output
     */
    void output(String text);
}

package pl.piotrak.input;

import pl.piotrak.product.Product;
import pl.piotrak.utils.InputException;

public interface InputDevice {

    /**
     * Get device type used for input
     * @return device type used for input
     */
    InputDeviceType getDeviceType();

    /**
     * Get input command type
     * @param input command
     * @return input command type
     */
    InputActionType getInputType(String input);

    /**
     * Look for product
     * @param input code of the searched Product
     * @return Product from database
     * @throws InputException when code is not numeric
     */
    Product getProductByInput(String input) throws InputException;

}

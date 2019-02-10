package pl.piotrak;

import org.springframework.util.StringUtils;
import pl.piotrak.input.InputActionType;
import pl.piotrak.input.InputDevice;
import pl.piotrak.input.InputDeviceType;
import pl.piotrak.output.OutputDevice;
import pl.piotrak.product.Product;
import pl.piotrak.utils.InputException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static pl.piotrak.utils.Constants.INVALID_CODE;

/**
 * Cash register used to input and sell products.
 * It consists of input devices (used to scan products) and output devices (used to notify the user and seller of the transaction)
 */
public class CashRegister {

    private List<InputDevice> inputDevices;

    private List<OutputDevice> outputDevices;

    private Map<Product, Integer> receipt = new HashMap<>();

    public CashRegister(List<InputDevice> inputDevice, List<OutputDevice> outputDevices) {
        this.inputDevices = inputDevice;
        this.outputDevices = outputDevices;
    }

    /**
     * Input new command
     * @param deviceType Device type used to input the command
     * @param input Command
     */
    public void input(InputDeviceType deviceType, String input){
        for(InputDevice inputDevice: inputDevices){
            if(inputDevice.getDeviceType().equals(deviceType)){
                InputActionType actionType = inputDevice.getInputType(input);
                if(actionType.equals(InputActionType.EXIT)){
                    endSale();
                }else if(actionType.equals(InputActionType.NEW_SALE)){
                    newSale();
                }else{
                    scanProduct(inputDevice, input);
                }
                break;
            }
        }
    }

    /**
     * Command for beginning of a new transaction
     * A new receipt is created and all output devices produce output
     */
    private void newSale(){
        receipt = new HashMap<>();
        for(OutputDevice outputDevice: outputDevices){
            outputDevice.newSale();
        }
    }

    /**
     * Command for end of the transaction
     * The prices of all Products is summed, output devices produce output and a new transaction is started
     */
    private void endSale(){
        double total = 0.0;
        Iterator it = receipt.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            total += (Integer)pair.getValue() * ((Product)pair.getKey()).getPrice();
        }
        for(OutputDevice outputDevice: outputDevices){
            outputDevice.endSale(receipt, total);
        }
        newSale();
    }

    /**
     * Command for input of a Product
     * An output is always produced to teh respective devices
     */
    private void scanProduct(InputDevice inputDevice, String code){
        String msg = "";
        try {
            if (StringUtils.isEmpty(code)) {
                throw new InputException(INVALID_CODE);
            }
            Product product = inputDevice.getProductByInput(code);
            addProductToReceipt(product);
            msg = product.toString();
        }catch (InputException ie){
            msg = ie.getMessage();
        } finally {
            for(OutputDevice outputDevice: outputDevices){
                outputDevice.output(msg);
            }
        }
    }

    /**
     * Adds product to the sale receipt, if the same product was already input, its count is incremented
     * @param product Input to the receipt
     */
    private void addProductToReceipt(Product product){
        if(receipt.containsKey(product)){
            receipt.put(product, receipt.get(product) + 1);
        }else{
            receipt.put(product, 1);
        }
    }

    public List<InputDevice> getInputDevices() {
        return inputDevices;
    }

    public void setInputDevices(List<InputDevice> inputDevices) {
        this.inputDevices = inputDevices;
    }

    public List<OutputDevice> getOutputDevices() {
        return outputDevices;
    }

    public void setOutputDevices(List<OutputDevice> outputDevices) {
        this.outputDevices = outputDevices;
    }

    public Map<Product, Integer> getReceipt() {
        return receipt;
    }
}

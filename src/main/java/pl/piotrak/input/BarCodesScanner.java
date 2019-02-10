package pl.piotrak.input;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.piotrak.product.Product;
import pl.piotrak.product.ProductService;
import pl.piotrak.utils.InputException;

import static pl.piotrak.utils.Constants.*;

/**
 * Bar-code scanner input device
 */
@Service("barCodesScanner")
public class BarCodesScanner implements InputDevice {

    private ProductService productService;

    /**
     @inheritDoc
     */
    public InputDeviceType getDeviceType() {
        return InputDeviceType.BAR_CODE_SCANNER;
    }

    /**
     @inheritDoc
     */
    public InputActionType getInputType(String input) {
        if(EXIT_CODE.equals(input)){
            return InputActionType.EXIT;
        }else if(NEW_SALE_CODE.equals(input)){
            return InputActionType.NEW_SALE;
        }else {
            return InputActionType.PRODUCT;
        }
    }

    /**
    @inheritDoc
    */
    public Product getProductByInput(String input) throws InputException {
        long code;
        try {
            code = Long.parseLong(input);
        }catch (NumberFormatException nfe){
            throw new InputException(INVALID_CODE);
        }
        return productService.findByCode(code);
    }

    public ProductService getProductService() {
        return productService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}

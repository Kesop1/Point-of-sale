package pl.piotrak;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.piotrak.input.BarCodesScanner;
import pl.piotrak.input.InputDevice;
import pl.piotrak.input.InputDeviceType;
import pl.piotrak.output.LCDDisplay;
import pl.piotrak.output.OutputDevice;
import pl.piotrak.output.Printer;
import pl.piotrak.product.Product;
import pl.piotrak.product.ProductDAO;
import pl.piotrak.product.ProductService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static pl.piotrak.utils.Constants.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:applicationContext.xml" })
public class CashRegisterTest {

    @InjectMocks
    private ProductService productServiceMock;

    @Mock
    private ProductDAO productDAOMock;

    private List<InputDevice> inputDevices;

    private List<OutputDevice> outputDevices;

    @Autowired
    BarCodesScanner barCodeScanner;

    @Autowired
    LCDDisplay lcdDisplay;

    @Autowired
    Printer printer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        barCodeScanner.setProductService(productServiceMock);
        setDevices();
        mockDatabase();
    }

    @Test
    public void mockObjectsTest(){
        Assert.assertNotNull(productDAOMock);
    }

    @Test
    public void scanProductCodesTest() throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo));
        CashRegister cashRegister = new CashRegister(inputDevices, outputDevices);
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "1");
        bo.flush();
        String allWrittenLines = new String(bo.toByteArray());
        Assert.assertTrue(allWrittenLines.contains("PSX"));
        Assert.assertEquals(1, cashRegister.getReceipt().size());
        Assert.assertEquals(new Integer(1), cashRegister.getReceipt().get(new Product(1L, "PSX", 199.00)));

        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "1");
        Assert.assertEquals(1, cashRegister.getReceipt().size());
        Assert.assertEquals(new Integer(2), cashRegister.getReceipt().get(new Product(1L, "PSX", 199.00)));

        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "2");
        Assert.assertEquals(2, cashRegister.getReceipt().size());
        Assert.assertEquals(new Integer(2), cashRegister.getReceipt().get(new Product(1L, "PSX", 199.00)));
        Assert.assertEquals(new Integer(1), cashRegister.getReceipt().get(new Product(2L, "NES", 99.00)));

        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "1");
        Assert.assertEquals(2, cashRegister.getReceipt().size());
        Assert.assertEquals(new Integer(3), cashRegister.getReceipt().get(new Product(1L, "PSX", 199.00)));
        Assert.assertEquals(new Integer(1), cashRegister.getReceipt().get(new Product(2L, "NES", 99.00)));
    }

    @Test
    public void scanOtherCodesTest() throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo));
        CashRegister cashRegister = new CashRegister(inputDevices, outputDevices);
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "4");
        bo.flush();
        String allWrittenLines = new String(bo.toByteArray());
        Assert.assertTrue(allWrittenLines.contains(PRODUCT_NOT_FOUND));

        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "");
        bo.flush();
        allWrittenLines = new String(bo.toByteArray());
        Assert.assertTrue(allWrittenLines.contains(INVALID_CODE));

        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, NEW_SALE_CODE);
        bo.flush();
        allWrittenLines = new String(bo.toByteArray());
        Assert.assertTrue(allWrittenLines.contains(NEW_SALE));
    }

    @Test
    public void getFullPurchase() throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo));
        CashRegister cashRegister = new CashRegister(inputDevices, outputDevices);
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "1");
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "1");
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "2");
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "1");
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "3");
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "1");
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "4");
        Assert.assertEquals(3, cashRegister.getReceipt().size());
        Assert.assertEquals(new Integer(4), cashRegister.getReceipt().get(new Product(1L, "PSX", 199.00)));
        Assert.assertEquals(new Integer(1), cashRegister.getReceipt().get(new Product(2L, "NES", 99.00)));
        Assert.assertEquals(new Integer(1), cashRegister.getReceipt().get(new Product(3L, "Milk", 0.99)));
        Assert.assertNull(cashRegister.getReceipt().get(new Product(4L, "Dummy", 0.99)));
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, EXIT_CODE);
        bo.flush();
        String allWrittenLines = new String(bo.toByteArray());
        double total = 4*199.00 + 1*99.00 + 1*0.99;
        Assert.assertTrue(allWrittenLines.contains("" + total));
    }

    @Test
    public void getTwoPurchases() throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo));
        CashRegister cashRegister = new CashRegister(inputDevices, outputDevices);
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "1");
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "1");
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "2");
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, EXIT_CODE);
        bo.flush();
        String allWrittenLines = new String(bo.toByteArray());
        double total = 2*199.00 + 1*99.00;
        Assert.assertTrue(allWrittenLines.contains("" + total));

        ByteArrayOutputStream bo2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bo2));
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, NEW_SALE_CODE);
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "1");
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "1");
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "2");
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, "3");
        cashRegister.input(InputDeviceType.BAR_CODE_SCANNER, EXIT_CODE);
        bo2.flush();
        String allWrittenLines2 = new String(bo2.toByteArray());
        double total2 = 2*199.00 + 1*99.00 + 1*0.99;
        Assert.assertTrue(allWrittenLines2.contains("" + total2));
    }

    private void mockDatabase(){
        Mockito.when(productDAOMock.findByCode(1L)).thenReturn(new Product(1L, "PSX", 199.00));
        Mockito.when(productDAOMock.findByCode(2L)).thenReturn(new Product(2L, "NES", 99.00));
        Mockito.when(productDAOMock.findByCode(3L)).thenReturn(new Product(3L, "Milk", 0.99));
    }

    private void setDevices(){
        inputDevices = new ArrayList<>();
        inputDevices.add(barCodeScanner);

        outputDevices = new ArrayList<>();
        outputDevices.add(lcdDisplay);
        outputDevices.add(printer);
    }


}

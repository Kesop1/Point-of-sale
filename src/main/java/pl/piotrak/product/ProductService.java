package pl.piotrak.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.piotrak.utils.InputException;

import static pl.piotrak.utils.Constants.PRODUCT_NOT_FOUND;

/**
 * Service for maintaining the Products at the store
 */
@Service("productService")
public class ProductService {

    private ProductDAO productDAO;

    /**
     * Introduce new product to the store
     * @param entity new Product
     */
    public void persist(Product entity) {
        productDAO.persist(entity);
    }

    /**
     * Modify existing product sold at the store
     * @param entity Product
     */
    public void update(Product entity) {
        productDAO.update(entity);
    }

    /**
     * Search for a Product in the database
     * @param code of the Product
     * @return Product from the database
     * @throws InputException if the code is incorrect
     */
    public Product findByCode(long code) throws InputException {
        Product product = productDAO.findByCode(code);
        if(product == null){
            throw new InputException(PRODUCT_NOT_FOUND);
        }
        return product;
    }

    /**
     * Remove the Product from the database
     * @param code of the Product to be removed
     * @throws InputException if the code is incorrect
     */
    public void delete(long code) throws InputException {
        Product product = findByCode(code);
        if(product == null){
            throw new InputException(PRODUCT_NOT_FOUND);
        }
        productDAO.delete(product);
    }

    public ProductDAO getProductDAO() {
        return productDAO;
    }

    @Autowired
    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
}

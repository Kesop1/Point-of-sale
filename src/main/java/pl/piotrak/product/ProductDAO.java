package pl.piotrak.product;

public interface ProductDAO {

    /**
     * Introduce new product to the store
     * @param entity new Product
     */
    void persist(Product entity);

    /**
     * Modify existing product sold at the store
     * @param entity Product
     */
    void update(Product entity);

    /**
     * Search for a Product in the database
     * @param code of the Product
     * @return Product from the database
     */
    Product findByCode(long code);

    /**
     * Remove the Product from the database
     * @param entity Product
     */
    void delete(Product entity);

}

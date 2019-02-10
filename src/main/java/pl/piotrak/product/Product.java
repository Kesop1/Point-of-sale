package pl.piotrak.product;

import javax.persistence.*;
import java.util.Objects;

/**
 * Product sold at the store
 */
@Entity
@Table(name="PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CODE", unique = true, nullable = false)
    private long code;

    @Column(name = "NAME", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "PRICE", nullable = false)
    private double price;

    public Product() {
    }

    public Product(long code, String name, double price) {
        this.code = code;
        this.name = name;
        this.price = price;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return code == product.code;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return getName() + "\t\t" + getPrice();
    }
}

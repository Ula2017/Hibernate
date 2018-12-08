import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Products {
    @Id
    private String productName;
    private int unitsInStock;
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;
    @ManyToOne
    @JoinColumn(name = "Category")
    private Category category;
    private double price;

    @ManyToMany(mappedBy = "products", cascade = {CascadeType.PERSIST})
    private Set<Invoice> invoices  = new HashSet<>();

    public Products(String productName, int unitsInStock, double price){

        this.productName = productName;
        this.unitsInStock = unitsInStock;
        this.price = price;
    }
    public Products(){

    }


    public void setSupplier(Supplier s){
        this.supplier=s;
    }

    public void setCategory(Category category){
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getUnitsInStock() {
        return unitsInStock;
    }

    public void setUnitsInStock(int unitsInStock) {
        this.unitsInStock = unitsInStock;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public Category getCategory() {
        return category;
    }

    public void addInvoice(Invoice invoice) {
        this.invoices.add(invoice);
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    public void updateUnit(int quantity){
        this.unitsInStock-=quantity;
    }
}

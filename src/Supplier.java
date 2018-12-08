import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Supplier extends Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String bankAccountNumber;

    @OneToMany
    @JoinColumn(name = "supplier_id")
    private Set<Products> productsset = new HashSet<>();

    public Supplier(){

    }

    public Supplier(String companyName, String street, String city, String zipcode, String bankAccountNumber){
        super(companyName,street, city, zipcode);
        this.bankAccountNumber = bankAccountNumber;
    }

    public void setProduct(Products prod){
        this.productsset.add(prod);
        prod.setSupplier(this);
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }
}

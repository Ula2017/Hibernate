import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Customer extends Company {

    @Id
    @GeneratedValue
    private int id;
    private double discount;

    public Customer(String companyName, String street, String city, String zipcode, double discount) {
        super(companyName, street, city, zipcode);
        this.discount = discount;
    }

    public Customer() {}

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

}
import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String companyName;
    @Embedded
    private Address address;

    public Company(String companyName, String street, String city, String zipcode) {
        this.address = new Address(street,city,zipcode);
        this.companyName = companyName;
    }

    public Company() {

    }
    protected String getCompanyName() {
        return companyName;
    }

    protected String getStreet() {
        return address.getStreet();
    }

    protected String getCity() {
        return address.getCity();
    }

    protected String getZipcode() {
        return address.getZipCode();
    }


}
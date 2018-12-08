import javax.persistence.*;
import java.util.*;


@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int invoiceNumber;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    private Set<Products> products = new HashSet<>();
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
   @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name="Invoice_FK")
    private Set<OrderLine> lines;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    private Customer customer;

    public Invoice(Customer customer){
        this.customer = customer;
        lines = new HashSet<>();
        this.paymentStatus = PaymentStatus.New;
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -6);
        this.date = cal.getTime();
         //this.date= new Date();

    }

    public Invoice(){

    }


    public void addProduct(Products prod, int quantity ){
        if(prod.getUnitsInStock() >= quantity){
            this.products.add(prod);
            prod.addInvoice(this);
            System.out.println(quantity+"quntiti______________");
            System.out.println(prod.getProductName()+prod.getUnitsInStock());
            prod.updateUnit(quantity);
            System.out.println(prod.getProductName()+prod.getUnitsInStock());
            OrderLine order = new OrderLine(prod, quantity);
            lines.add(order);
        }else{
            System.out.println("There is no enouqh "+prod.getProductName());
        }
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void toStringInvoice(){
        System.out.println("Invoice "+this.invoiceNumber +" was created in "+this.date.toString()
                +" by "+this.customer.getCompanyName()+" and contains " + this.products.size()+" products. Status"+ getPaymentStatus());
    }

    public Date getDate(){

        return this.date;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Set<OrderLine> getLines(){
        return lines;
    }
}

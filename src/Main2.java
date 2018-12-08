import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main2 {
    public static void showProductsForCategory(String catName, EntityManager em){
        List<Products> products = em.createQuery("select p from Products as p " +
        "where p.category.Name =:catName", Products.class)
        .setParameter("catName", catName).getResultList();
        System.out.println("There is "+products.size() + " products in category "+ catName);
        if(products.size() > 0) {
            System.out.println("Products for category " + catName);
            for (Products prod : products) {
                System.out.println(prod.getProductName());
            }
        }
    }

    public static void showCategoryForProduct(String prodName, EntityManager em){
        Category category= em.createQuery("select c from Category as c " +
                    "join c.products as p where p.productName=:prodName", Category.class)
                    .setParameter("prodName",prodName).getSingleResult();
            System.out.println("For product "+prodName+" is category: "+category.getName());
    }

    public static void showInvoicesWithStatus(EntityManager em, PaymentStatus paymentStatus){
        List<Invoice> invoices = em.createQuery("select i from Invoice as " +
                "i where i.paymentStatus=:paymentStatus order by i.customer.companyName", Invoice.class)
                .setParameter("paymentStatus", paymentStatus).getResultList();
        System.out.println("Invoices with status "+paymentStatus.toString());
        for (Invoice i : invoices) {
            i.toStringInvoice();
        }

    }

    public static void cancelNewInvoicesForCustomerID(EntityManager em, int customerName){
        em.createQuery("update Invoice as i set i.paymentStatus=:newStatus "+
                "where i.paymentStatus=:oldStatus and i.customer.id=:company")
                .setParameter("company", customerName)
                .setParameter("oldStatus", PaymentStatus.New)
                .setParameter("newStatus", PaymentStatus.Cancel)
                .executeUpdate();
    }

    public static int getCustomerID(EntityManager em, String customerName){
        return (int)em.createQuery("select c.id from Customer as c where c.companyName=: customer")
                .setParameter("customer", customerName).getSingleResult();
    }
    public static void cancelNewInvoicesForCustomername(EntityManager em, String customerName){
        int c = getCustomerID(em, customerName);
        em.createQuery("update Invoice as i set i.paymentStatus=:newStatus "+
                "where i.paymentStatus=:oldStatus and i.customer.id=:company")
                .setParameter("company", c)
                .setParameter("oldStatus", PaymentStatus.New)
                .setParameter("newStatus", PaymentStatus.Cancel)
                .executeUpdate();

    }

    public static void totalSumForInvoice(EntityManager em, int id){
        double total =(double)em.createQuery("select (sum(o.quantity*p.price)) " +
                "from Invoice as i join i.lines as o join o.products as p where i.invoiceNumber =: id")
                .setParameter("id", id)
                .getSingleResult();
        System.out.println("Total result for invoice number "+id+" is "+total);

    }
//suma wszystkich faktur dla danego klienta w zaleznosci od statusu platnosci
    public static void totalSumForInvoicesCustomerID(EntityManager em, int id, PaymentStatus status){
        double total =(double)em.createQuery("select sum((1-c.discount)*o.quantity*p.price) " +
                "from Invoice as i join i.lines as o join o.products as p " +
                "join i.customer as c where c.id =: id and i.paymentStatus =:cancelStatus")
                .setParameter("id", id)
                .setParameter("cancelStatus", status)
                .getSingleResult();
        System.out.println("Total result for customerID "+id+" is "+total);

    }
    //suma wszystkich faktur dla danego klienta w zaleznosci od statusu platnosci per fatura
    public static void totalSumForInvoiceCustomerID(EntityManager em, int id, int invoiceID){
        double total =(double)em.createQuery("select sum((1-c.discount)*o.quantity*p.price) " +
                "from Invoice as i join i.lines as o join o.products as p " +
                "join i.customer as c where c.id =: id and i.invoiceNumber =:invoice")
                .setParameter("id", id)
                .setParameter("invoice", invoiceID)
                .getSingleResult();
        System.out.println("Invoice "+ invoiceID+" Total result for customerID "+id+" is "+total);

    }
    public static void totalSumForInvoicesCustomeName(EntityManager em, String CustomerName, PaymentStatus status){
        int customerID = getCustomerID(em, CustomerName);
        totalSumForInvoicesCustomerID(em, customerID, status);

    }
    public static void totalSumPerCustomerIDPaid(EntityManager em, int id){
        totalSumForInvoicesCustomerID(em,id, PaymentStatus.Paid);
    }

    public static void totalSumPerCustomerIDNew(EntityManager em, int id){
        totalSumForInvoicesCustomerID(em,id, PaymentStatus.New);
    }

    public static void totalSumPerCustomerNamePaid(EntityManager em, String CustomerName){
        totalSumForInvoicesCustomeName(em,CustomerName, PaymentStatus.Paid);
    }

    public static void totalSumPerCustomerNameNew(EntityManager em, String CustomerName){
        totalSumForInvoicesCustomeName(em,CustomerName, PaymentStatus.New);
    }


    public static void changeInvoiceStatus(EntityManager em, int id, PaymentStatus newStatus){
        PaymentStatus oldStatus = (PaymentStatus) em.createQuery("select i.paymentStatus " +
                "from Invoice as i" +" where i.invoiceNumber=: id")
                .setParameter("id", id).getSingleResult();
        if(oldStatus == PaymentStatus.Paid || ( oldStatus == PaymentStatus.Cancel
                && newStatus == PaymentStatus.Paid)){
            System.out.println("Can't change status from "+ oldStatus+" to "+newStatus);

        }else {
            em.createQuery("update Invoice as i set i.paymentStatus=:newStatus " +
                    "where i.invoiceNumber=:id")
                    .setParameter("newStatus", newStatus)
                    .setParameter("id", id).executeUpdate();
        }

    }

    public static boolean isEnoughProduct(EntityManager em, Products p, int quantity){
        int unit = (int)em.createQuery("select p.unitsInStock from Products as p where p=:product")
                .setParameter("product",p).getSingleResult();
        return unit > quantity;
    }
//anuluj faktury bez linii lub niezapłacone po terminie 3 dni od zamówienia
    public static void cancelIncorrectInvoices(EntityManager em){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -3);
        Date dateToCompare = cal.getTime();
        em.createQuery("update from Invoice as i set i.paymentStatus =:cancel " +
                "where size(i.lines) = 0 or i.date < :toCompare")
                .setParameter("cancel", PaymentStatus.Cancel)
                .setParameter("toCompare",dateToCompare, TemporalType.TIMESTAMP)
                .executeUpdate();
    }

    public static void avgInvoiceForCustomerID(EntityManager em, int custmerID, PaymentStatus status){
        double avg =(double)em.createQuery("select avg(o.quantity*p.price) " +
                "from Invoice as i join i.lines as o join o.products as p " +
                "join i.customer as c where c.id =: id and i.paymentStatus =:cancelStatus")
                .setParameter("id", custmerID)
                .setParameter("cancelStatus", status)
                .getSingleResult();
        System.out.println("The average cost of invoices for the customer "+custmerID+" is "+avg);

    }
    public static void avgInvoiceForCustomerName(EntityManager em, String customerName, PaymentStatus status){
        int customerID = getCustomerID(em, customerName);
        avgInvoiceForCustomerID(em, customerID, status);

    }

    public static void countNumberOfInvoices(EntityManager em){
        long counter = (long)em.createQuery("select count (*) from Invoice").getSingleResult();
        System.out.println("In database there is "+counter+" invoices.");
    }
    public static void countNumberOfInvoicesPerClient(EntityManager em, int clientID){
        long counter = (long)em.createQuery("select count (*) from Invoice as i where i.customer.id =:id")
                .setParameter("id", clientID)
                .getSingleResult();
        System.out.println("In database there is "+counter+" invoices for customer "+clientID);

    }
    public static void printingInvoices(EntityManager em, List<Invoice> invoices){
        for(Invoice invoice: invoices){
            invoice.toStringInvoice();
            int counter=0;
            for(OrderLine line: invoice.getLines()){
                System.out.println(counter+". Product: "+line.getProducts().getProductName()
                        +" Quantity: "+line.getQuantity());
                counter++;
            }
            totalSumForInvoice(em, invoice.getInvoiceNumber());
        }
    }

    public static void displayInvoices(EntityManager em){
        List<Invoice> invoices = em.createQuery("select i from Invoice as i " +
                "where i.paymentStatus != 'Cancel'", Invoice.class).getResultList();
        printingInvoices(em, invoices);

    }

    public static void displayInvoicesForCustomer(EntityManager em, int customerID){
        List<Invoice> invoices = em.createQuery("select i from Invoice as i " +
                "where i.paymentStatus != 'Cancel' " +
                "and i.customer.id =:id", Invoice.class)
                .setParameter("id", customerID)
                .getResultList();
        System.out.println("There is list of invoices for " +customerID);
        printingInvoices(em, invoices);

    }

    public static void changeProductUnitIfNew(EntityManager em, int id, Products products, int newQuantity){
        OrderLine line = (OrderLine)em.createQuery("select o from Invoice as i " +
                "join i.lines as o join o.products as p " +
                "where i.invoiceNumber=:id and p.productName =:name and i.paymentStatus = 'New'")
                .setParameter("id", id)
                .setParameter("name", products.getProductName())
                .getSingleResult();
        if(line == null){
            System.out.println("There is no line with this specification.");
            return;
        }
        int neededUnit = newQuantity - line.getQuantity();

        if (isEnoughProduct(em, products, neededUnit)){
            products.updateUnit(neededUnit);
            line.setQuantity(newQuantity);
            System.out.println("Changed successfully.");
        }else{
            System.out.println("There is no enouqh unit in stock for product: "+products.getProductName());
        }


    }

        public static void main(final String[] args) throws Exception {
            EntityManagerFactory emf = Persistence.
                    createEntityManagerFactory("myDatabaseConfig");
            EntityManager em = emf.createEntityManager();
            EntityTransaction etx = em.getTransaction();
            etx.begin();

            totalSumForInvoicesCustomerID(em,200, PaymentStatus.New);
            totalSumForInvoice(em, 199);
            totalSumForInvoicesCustomerID(em, 207,PaymentStatus.New);
            totalSumForInvoicesCustomeName(em, "Kot i ala", PaymentStatus.Paid);

            changeInvoiceStatus(em, 214, PaymentStatus.Paid);
            changeInvoiceStatus(em, 214, PaymentStatus.New);
            changeInvoiceStatus(em, 222, PaymentStatus.New);
            cancelIncorrectInvoices(em);
            displayInvoices(em);
            displayInvoicesForCustomer(em, 210);
            System.out.println("________________");
            countNumberOfInvoices(em);
            countNumberOfInvoicesPerClient(em, 203);
            avgInvoiceForCustomerName(em, "Kot i ala", PaymentStatus.New);
            avgInvoiceForCustomerID(em, 200, PaymentStatus.Cancel);

            Products prod2 = em.find(Products.class,"fridge");
            Invoice i = new Invoice(em.find(Customer.class, 200));
            i.addProduct(prod2, 10);
            em.persist(i);


            String bankAccountNumber = "12344556545676543";
            Supplier s = new Supplier("Makarun","Rostafinskiego 27 ","Krakow", "33-321", bankAccountNumber);
            Customer c = new Customer("Kot i ala", "Bławatkowa 21", "Gdansk" ,"12-232", 0.22);

            Category c1 = em.find(Category.class, 157);

            Supplier s1 = em.find(Supplier.class, 189);
            try {
            Products products = new Products("power bank", 111, 23.99);
            Invoice i1 = new Invoice(c);

            i1.addProduct(prod2,1);
            em.persist(i1);
            }catch (EntityExistsException e){
            System.out.println(e.getMessage());
            System.out.println(e.fillInStackTrace().toString());
            }

            Customer c = em.find(Customer.class, 210);
            Invoice i = new Invoice(c);
            i.addProduct(p1, 9);
            i.addProduct(p2, 10);
            i.addProduct(p3, 1);
            i.addProduct(p4, 2);
            i.addProduct(p5, 2);
            em.persist(i);
            showProductsForCategory("AGD/RTV",em);
            showCategoryForProduct("fridge", em);
            Products prod1 = em.find(Products.class,"xbox");
            changeProductUnitIfNew(em,240,prod1, 300);
            changeProductUnitIfNew(em,240,prod1, 17);

        etx.commit();
        em.close();

    }
}
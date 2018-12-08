import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import java.util.*;

public class Main {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(final String[] args) throws Exception {
        final Session session = getSession();
        try {

//            System.out.println("Enter product name");
//            Scanner inputScanner = new Scanner(System.in);
//            String prodName1 = inputScanner.nextLine();
//            System.out.println("Provide number of unit in stock:");
//            int units = Integer.parseInt(inputScanner.nextLine());

//
//            String catName = "AGD/RTV";
//            List<Products> products = session.createQuery("select p from Products as p " +
//                    "where p.category.Name =:catName", Products.class)
//                    .setParameter("catName", catName).getResultList();
//            System.out.println("Products for category "+catName);
//            for(Products prod : products){
//                System.out.println(prod.getProductName());
//            }
//            String prodName = "yogurt";
//            Category category= session.createQuery("select c from Category as c " +
//                    "join c.products as p where p.productName=:prodName", Category.class)
//                    .setParameter("prodName",prodName).getSingleResult();
//            System.out.println("For product "+prodName+" is category: "+category.getName());

            Transaction tx = session.beginTransaction();

            Category c = new Category("Dairy product");
//            Products prod1 = new Products("cottage cheese", 1000, 7.99);
//            Products prod2 = new Products("cheese", 1000, 24.66);
//            Products p1 = session.get(Products.class, "milk");
//            Products p2 = session.get(Products.class, "fridge");
//            c.addProduct(prod1);
//            c.addProduct(prod2);
//            s.setProduct(prod1);
//            s.setProduct(prod2);
            Supplier s = new Supplier("Laciate","Rostafinskiego 27 ","Krakow",
                    "33-321", "12345645353");
//
//            s.setProduct(prod1);
//            s.setProduct(prod2);

//            Invoice i1 = new Invoice();
//            i1.addProduct(prod1,10);
//            i1.addProduct(prod2,10);
//            invoiceProd.add(p1);
//            invoiceProd.add(p2);
//
//            Invoice i2 = new Invoice(invoiceProd, 4);
            session.save(c);
            session.save(s);
//            session.save(i1);
//            session.save(i2);
//            session.save(prod1);
//            session.save(prod2);

//            int invoiceNum = 5;
//            List<Products> products = session.createQuery("select p from Products as p " +
//                    "join p.invoices as invoices where invoices.invoiceNumber =:invoiceNum", Products.class)
//                    .setParameter("invoiceNum", invoiceNum).getResultList();
//            System.out.println("Products for invoice number "+invoiceNum);
//            for(Products prod : products){
//                System.out.println(prod.getProductName());
//            }
//            String prodName = "butter";
//            List<Invoice> invoices= session.createQuery("select i from Invoice as i " +
//                    "join i.products as p where p.productName=:prodName", Invoice.class)
//                    .setParameter("prodName",prodName).getResultList();
//            System.out.println("Invoices for products "+prodName);
//            for(Invoice i : invoices){
//                System.out.println(i.getInvoiceNumber());
//            }





            tx.commit();

        } finally {
            session.close();
        }
    }
}
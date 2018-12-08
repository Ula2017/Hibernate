import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int CategoryID;
    private String Name;
    @OneToMany
    @JoinColumn(name = "CATEGORY")
    private List<Products> products = new LinkedList<>();

    public Category(){

    }

    public Category(String name){
        this.Name = name;
    }

    public void addProduct(Products p){
        products.add(p);
        p.setCategory(this);
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

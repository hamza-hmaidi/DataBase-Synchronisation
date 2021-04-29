import java.util.UUID;

public class Product {

    public String id;
    public String name;
    public float price;
    public String brand;

    public static Product parseObjectFromString(String s)  {
        //System.out.println(s);
        String [] fields = s.split(" ");
        return new Product(fields[0],fields[1],Float.parseFloat(fields[2]),fields[3]);
    }
    @Override
    public String toString() {
        return this.id+" "+this.name+" "+this.price+" "+this.brand;
    }



    public Product(String id, String name, float price, String brand){
        this.id=id;
        this.name=name;
        this.price=price;
        this.brand=brand;
    }
    public Product( String name, float price, String brand){
        this.id = UUID.randomUUID().toString();
        this.name=name;
        this.price=price;
        this.brand=brand;
    }


}

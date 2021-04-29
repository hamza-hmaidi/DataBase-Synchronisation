import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class ManageData {

    public final static String HO_QUEUE = "ho_queue";
    public final static String HO_DBNAME = "hodb";

    public static void createDb(String dbName){
        try{
            java.sql.Connection con= DriverManager.getConnection("jdbc:mysql://localhost/?user=root&password=root");
            Statement stmt=con.createStatement();
            Boolean result = stmt.execute("CREATE DATABASE if not exists "+dbName);
            con.close();
        }catch( Exception e){
            System.out.println("error in creating data base "+ dbName);
            System.out.println(e);
        }
    }
    public static void createProductTable(String dbName){
        try{
            java.sql.Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName,"root","root");
            Statement stmt=con.createStatement();

            String sql = "create table if not exists product(\n" +
                    "\t\t\t\tid Varchar(100) primary key,\n" +
                    "                name varchar(50),\n" +
                    "                price float,\n" +
                    "                brand varchar(50) \n" +
                    "                );";
            stmt.executeUpdate(sql);
            con.close();
        }catch(Exception e){
            System.out.println("error in creating the product table in database "+ dbName);
            System.out.println(e);
        }
    }

    public static void sendToDB(Product p, String dbName){
        try{
            java.sql.Connection con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/"+dbName,"root","root");

            String sql = "replace into product (id,name,price,brand) values (?,?,?,?);";
            PreparedStatement stmt=con.prepareStatement(sql);
            stmt.setString(1,p.id);
            stmt.setString(2,p.name);
            stmt.setFloat(3,p.price);
            stmt.setString(4,p.brand);
            stmt.execute();
            con.close();
        }catch(Exception e){
            System.out.println("failed to send the product: " + p.name );
            System.out.println(e);
        }
    }

    public static String[][] getAllProducts(String dbName){
        String[][] products = new String[100][4];
        int i=0;
        try {
            java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root", "root");
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM product;";
            ResultSet product =  stmt.executeQuery(sql);
            while (product.next()){
                products[i][0] = product.getString(1);
                products[i][1] = product.getString(2);
                products[i][2] = Float.toString(product.getFloat(3));
                products[i][3] = product.getString(4);
                i++;
            }
        }catch (Exception e){
            System.out.println("Error in retrieving data from "+ dbName );
        }
        String[][] products1 = new String[i][4];
        for(int j=0 ; j<i; j++){
            products1[j][0] = products[j][0];
            products1[j][1] = products[j][1];
            products1[j][2] = products[j][2];
            products1[j][3] = products[j][3];
        }
        return products1;
    }

    public static void sendToHO(Product p) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(HO.HO_QUEUE, false, false, false, null);
            String msg = "ADD"+p.toString();
            channel.basicPublish("", HO.HO_QUEUE, null, msg.getBytes());
        }catch (Exception e){
            System.out.println("error sending data to HO ");
        }
    }

    public static void sendQueryToHO(String sql) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(HO.HO_QUEUE, false, false, false, null);
            String msg = sql.toString();
            channel.basicPublish("", HO.HO_QUEUE, null, msg.getBytes());
        }catch (Exception e){
            System.out.println("error sending data to HO ");
        }
    }

    public static void sendOldDataToHO(String dbName)  {
        try {
            java.sql.Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName, "root", "root");
            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM product;";
            ResultSet product =  stmt.executeQuery(sql);
            while (product.next()){
                String id = product.getString(1);
                String name = product.getString(2);
                float price = product.getFloat(3);
                String brand = product.getString(4);
                Product p = new Product(id,name,price,brand);
                sendToHO(p);
            }

        }catch (Exception e){
            System.out.println("Error in sending old data from "+ dbName + "to HO");
        }
    }



    public static void execQuery(String sql,String dbName){
        try{
            java.sql.Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName,"root","root");
            Statement stmt=con.createStatement();
            stmt.executeUpdate(sql);
            con.close();
        }catch(Exception e){
            System.out.println("error in executing  query : "+ sql);
        }
    }

    public static  void removeFromBD(String id, String dbName){
            String sql = "DELETE FROM product WHERE id = '"+id+"';";
            execQuery(sql,dbName);
    }

    public static void removeFromHO(String id){
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(HO.HO_QUEUE, false, false, false, null);
            channel.basicPublish("", HO.HO_QUEUE, null, ("DEL"+id).getBytes());
        }catch (Exception e){
            System.out.println("error sending data to HO ");
        }
    }


}

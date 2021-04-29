import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class AddProductGUI {
    public final String dbName;
    public AddProductGUI(String dbName){
        this.dbName = dbName;
        JFrame f= new JFrame(dbName+"database");

        JTextField t1,t2,t3,t4;
        JPanel p = new JPanel();
        t1 = new JTextField("product",16);
        JLabel productLabel = new JLabel("product");
        p.add(productLabel);
        p.add(t1);
        t2 = new JTextField(8);
        JLabel priceLabel = new JLabel("price");
        p.add(priceLabel);p.add(t2);
        t3 = new JTextField("brand",16);
        JLabel brandLabel = new JLabel("brand");
        p.add(brandLabel);p.add(t3);
        JLabel label = new JLabel("");
        p.add(label);
        t2.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent ke) {
                String value = t2.getText();
                int l = value.length();
                if (ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9') {
                    t2.setEditable(true);
                    label.setText("");
                } else {
                    t2.setEditable(false);
                    label.setText("* Enter only numeric digits(0-9) for price field");
                }
            }
        });
        JButton addBtn=new JButton("Add product");
        addBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String product = t1.getText();
                float price = Float.valueOf(t2.getText());
                String brand = t3.getText();
                Product p = new Product(product,price,brand);
                ManageData.sendToDB(p,dbName);
                ManageData.sendToHO(p);
                JFrame f = new JFrame("retrieved data");
                JPanel p0 = new JPanel();
                String column[] = {"ID","product","price","brand"};
                String[][] products = ManageData.getAllProducts(dbName);
                label.setText("* db updated with product "+ product);
                JOptionPane.showMessageDialog(null, "product"+ p.name+"added successfully!");
            }
        });

        JButton showBtn = new JButton("Show products");
        showBtn.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                new VisualiseData(dbName);
            }
        });

        p.add(addBtn); p.add(showBtn);

        f.add(p);
        f.setSize(600,150);
        f.setVisible(true);
    }
}

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class BOGUI extends JFrame {
    public static int count =1;
    public final String siteName= "BO"+this.count;
    public final String BO_DBNAME = "BO"+this.count;


    BOGUI(){
        ManageData.createDb(BO_DBNAME);
        ManageData.createProductTable(BO_DBNAME);
        ManageData.sendOldDataToHO(BO_DBNAME);
        new AddProductGUI(BO_DBNAME);
        count = count+1;
    }

    public static void main(String[] args) {
        new BOGUI();
        new BOGUI();
    }
}

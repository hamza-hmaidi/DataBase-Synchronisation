import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VisualiseData extends JFrame {
    public final String dbName;


    public VisualiseData(String dbName){
        this.dbName = dbName;
        this.setTitle("product table from " + dbName);

        Object[][] products = ManageData.getAllProducts(dbName);
        String column[] = {"ID","product","price","brand"};
        DefaultTableModel model = new DefaultTableModel(products,column);
        JTable table = new JTable(model);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        JButton button = new JButton("Remove");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                // check for selected row first
                if(table.getSelectedRow() != -1) {
                    // remove selected row from the model
                    String id = table.getValueAt(table.getSelectedRow(),0).toString();
                    ManageData.removeFromBD(id, dbName);
                    ManageData.removeFromHO(id);
                    model.removeRow(table.getSelectedRow());
                    JOptionPane.showMessageDialog(null, "Selected row deleted successfully");
                }
            }
        });


        add(new JScrollPane(table), BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}

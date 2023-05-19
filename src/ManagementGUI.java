import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileNotFoundException;
import java.util.List;




public class ManagementGUI extends JFrame{
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JPanel mainPanel;
    private JTextField txtBook;
    private JTextField txtAuthor;
    private JTextField txtPrice;
    private JTextField txtQuantity;
    private JButton saveButton;
    private JButton clearButton;
    private JButton deleteButton;
    private JButton editButton;
    private JTable dataTable;
    private JLabel debugLabel;
    private JTextField txtPublisher;
    private JButton browseButton;
    private JTextField searchField;
    private JLabel searchLabel;
    private JButton bblSortBtn;
    private JScrollPane tabel;
    private JButton mergeSortBtn;

    public ManagementGUI() {

        setContentPane(mainPanel);
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setVisible(true);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.lightGray);
        dataTable.setBackground(Color.gray);

        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        String filePath = "src/mydata.csv";

        File file = new File(filePath);

        dataTable.setVisible(false);

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtBook.getText().equals("") || txtAuthor.getText().equals("")  ||
                        txtPrice.getText().equals("") || txtQuantity.getText().equals(""))
                {
                    JOptionPane.showMessageDialog(mainPanel, "Enter all data!");
                }
                else
                {
                    if(dataTable.isVisible())
                    {
                        dataTable.setVisible(false);
                    }
                    String filePath = "src/mydata.csv";
                    try
                    {
                        FileWriter fileWriter = new FileWriter(filePath, true);
                        BufferedWriter bw = new BufferedWriter(fileWriter);
                        bw.write(txtBook.getText());
                        bw.write(",");
                        bw.write(txtAuthor.getText());
                        bw.write(",");
                        bw.write(txtPublisher.getText());
                        bw.write(",");
                        bw.write(txtPrice.getText());
                        bw.write(",");
                        bw.write(txtQuantity.getText());
                        bw.newLine();
                        // Always close files.
                        bw.close();
                        fileWriter.close();
                        // Display success message and clear textfields.
                        JOptionPane.showMessageDialog(mainPanel, "Data added successfully!");
                        txtBook.setText("");
                        txtAuthor.setText("");
                        txtPublisher.setText("");
                        txtPrice.setText("");
                        txtQuantity.setText("");

                    }

                    catch (IOException ex)
                    {
                        System.out.println("Error writing to file " + filePath);
                    }
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel)dataTable.getModel();

                try {
                    FileReader fr = new FileReader(filePath);
                    BufferedReader br = new BufferedReader(fr);

                    Object[] dataLines = br.lines().toArray();
                    String[] stringArr = Arrays.copyOf(dataLines, dataLines.length, String[].class);
                    //stringArr e doar ca sa convertesc din object in string array, poate fi sters.
                    String[] dataRow;
                    //System.out.println(Arrays.toString(dataLines));

                    FileWriter fw = new FileWriter(filePath, false);
                    BufferedWriter bw = new BufferedWriter(fw);
                    PrintWriter pw = new PrintWriter(bw);

                    System.out.println("Data lines length: " + dataLines.length);
                    int selected = dataTable.getSelectedRow() + 1;
                    if (dataTable.getSelectedRow() != -1)
                    {

                        System.out.println("Selected index is " + selected);
                        for (int i = 0; i < stringArr.length; i++)
                        {
                            String line = stringArr[i].trim();
                            dataRow = line.split(",");

                            System.out.println(String.join(",",dataRow) + "has index " +i);
                            //Arrays.Of(dataRow)
                            if (i != selected) {
                                pw.println(String.join(",",dataRow));
                            }
                        }
                    }
                    model.removeRow(dataTable.getSelectedRow());
                    pw.flush();
                    pw.close();
                    fr.close();
                    br.close();
                    bw.close();
                    fw.close();
                }catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                JOptionPane.showMessageDialog(mainPanel, "Data deleted!");
                System.out.println("Data table has this many rows -> " + dataTable.getRowCount());
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                debugLabel.setText("Cleared!");
                dataTable.setVisible(false);
                DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
                model.setRowCount(0);
            }
        });
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataTable.setVisible(true);
                DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
                model.setRowCount(0);

                try
                {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String firstLine = br.readLine().trim();
                    String[] columnsName = firstLine.split(",");
                    model.setColumnIdentifiers(columnsName);

                    Object[] tableLines = br.lines().toArray();

                    for (Object tableLine : tableLines) {
                        String line = tableLine.toString().trim();
                        String[] dataRow = line.split(",");
                        model.addRow(dataRow);
                    }
                    br.close();
                } catch (FileNotFoundException ex)
                {
                    Logger.getLogger(ManagementGUI.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                DefaultTableModel tableModel = (DefaultTableModel) dataTable.getModel();
                TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(tableModel);
                dataTable.setRowSorter(tr);
                tr.setRowFilter(RowFilter.regexFilter(searchField.getText().trim()));
            }
        });

        bblSortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
                model.setRowCount(0);
                List<String> csvData;
                csvData = BubbleSortKt.readCSV("src/mydata.csv");
                List<String> sortedData = BubbleSortKt.bubbleSort(csvData);

                    for(int i = 1; i < sortedData.size(); i++)
                    {
                        String line = sortedData.get(i);//tableLines[i].toString().trim();
                        String[] dataRow = line.split(",");
                        model.addRow(dataRow);
                    }
            }
        });
        mergeSortBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
                model.setRowCount(0);
                List<String> csvData;
                csvData = MergeSortKt.readCSVMrg("src/mydata.csv");
                List<String> sortedData = MergeSortKt.mergeSort(csvData);

                for (String line : sortedData) {
                    String[] dataRow = line.split(",");
                    model.addRow(dataRow);
                }
            }
        });
    }

    public static void main(String[] args) {
        ManagementGUI myGUI = new ManagementGUI();
    }

}

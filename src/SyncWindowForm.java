import com.intellij.openapi.diagnostic.Logger;
import csdt.CSDPUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

/**
 * Created by NB20308 on 04/01/2016.
 */
public class SyncWindowForm extends JDialog {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane1;
    private JButton csSyncSelectionToWorkspaceButton;
    private JButton csSyncAndCloseButton;
    private JTable cstable;
    private JTextField dsRegextextField;
    private JTable dstable;
    private JButton dsSyncSelectionToWebCenterSites;
    private JButton dsSyncToWebCenterSitesAndCloseButton;
    private JTextField csSyncToWorkspacetextField;
    private JButton csSearchButton;
    private JButton helpButton;
    private JScrollPane csscrollPane;
    private JScrollPane dsscrollPane;
    private JTextField csRegextextField;
    private JButton helpButton1;
    private Container relativeContainer;

    private static Logger LOG = Logger.getInstance(SyncWindowForm.class);

    public SyncWindowForm(){
        LOG.debug("Initializing Sync Window form");
        LOG.info("Initializing Sync Window form");
        ArrayList<String[]> listing = null;
        updateCSTable();
        updateDSTable();

        setTitle("Oracle WebCenter Sites Synchronization tool");
        setContentPane(mainPanel);
        setModal(true);

        csSyncToWorkspacetextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked Search button");
                updateCSTable();
            }
        });
        csSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Clicked Search button");
                updateCSTable();
            }
        });

        csSyncSelectionToWorkspaceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                syncToWorkspace();
                System.out.println("Clicked Sync to workspace button");
            }
        });

        csSyncAndCloseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                syncToWorkspace();
                dispose();
                System.out.println("Clicked Sync to workspace button");
            }
        });

        csRegextextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTableWithRegex(cstable,csRegextextField.getText());
            }
        });

        dsSyncToWebCenterSitesAndCloseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                syncToWebCenterSites();
                dispose();
                System.out.println("Clicked Sync to webcenter sites button");
            }
        });

        dsRegextextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTableWithRegex(dstable,dsRegextextField.getText());
            }
        });

        dsSyncSelectionToWebCenterSites.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                syncToWebCenterSites();
                System.out.println("Clicked Sync to workspace button");
            }
        });


    }
    private void filterTableWithRegex(final JTable table, String regex){
        try {
            System.out.println("Regex text: "+ regex);

            final TableRowSorter sorter = new TableRowSorter(table.getModel());
            //Add row filter to the tablerowsorter (regex)
            sorter.setRowFilter(RowFilter.regexFilter(regex));
            //Apply the results to the output table
            SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    table.setRowSorter(sorter);
                }

            });


        } catch (PatternSyntaxException pse) {
            System.err.println("Bad regex pattern");
        }
    }

    private void syncToWebCenterSites() {
        int[] selectedRows = dstable.getSelectedRows();

        Map<String, java.util.List<String>> csHashMap= new HashMap<String, List<String>>();
        for (int row:selectedRows){

            String type = dstable.getModel().getValueAt(dstable.convertRowIndexToModel(row),0).toString();
            String id = dstable.getModel().getValueAt(dstable.convertRowIndexToModel(row),1).toString();
            System.out.print("type: "+type+" - id: "+id);
            if(csHashMap.containsKey(type)) {
                ((List)csHashMap.get(type)).add(id);
            } else {
                csHashMap.put(type, new ArrayList());
                ((List)csHashMap.get(type)).add(id);
            }
        }
        try {
            CSDPUtil.callImport(csHashMap, (String) null, true);
        }catch(Exception exception){
            System.out.println("exception: "+exception);
        }
    }

    private void syncToWorkspace() {
        int[] selectedRows = cstable.getSelectedRows();
        System.out.print(selectedRows);
        Map<String, java.util.List<String>> csHashMap= new HashMap<String, List<String>>();
        for (int row:selectedRows){

            String type = cstable.getModel().getValueAt(cstable.convertRowIndexToModel(row),0).toString();
            String id = cstable.getModel().getValueAt(cstable.convertRowIndexToModel(row),1).toString();
            System.out.print("type: "+type+" - id: "+id);
            if(csHashMap.containsKey(type)) {
                ((List)csHashMap.get(type)).add(id);
            } else {
                csHashMap.put(type, new ArrayList());
                ((List)csHashMap.get(type)).add(id);
            }
        }
        try {
            CSDPUtil.callExport(csHashMap, (String) null, true);
        }catch(Exception exception){
                System.out.println("exception: "+exception);
            }

    }

    private void updateTable(JScrollPane scrollPane, JTable table, ArrayList<String[]> listing, String[] col){

        Map<String, java.util.List<String>> csHashMap= new HashMap<String, List<String>>();
        DefaultTableModel tableModel = new DefaultTableModel(col, 0);
        for (final String[] item:listing){
            tableModel.addRow(item);
            String type=item[1];
            String id=item[1];
            if(csHashMap.containsKey(type)) {
                ((List)csHashMap.get(type)).add(id);
            } else {
                csHashMap.put(type, new ArrayList());
                ((List)csHashMap.get(type)).add(id);
            }
            //   System.out.println(Arrays.toString(item));
        }
        table.setModel(tableModel);

        table.setRowSorter(new TableRowSorter<TableModel>(tableModel));

        scrollPane.setViewportView(table);
    }

    private void updateCSTable() {
        ArrayList<String[]> listing = null;
        try {
            listing = CSDPUtil.getCSListing(csSyncToWorkspacetextField.getText().split(","));
            String col[] = {"Resource Type","Resource Id","Name", "Description", "Modified Date", "Site"};
            updateTable(csscrollPane, cstable,listing,col);
        } catch (Exception var32) {
            //MessageDialog.openError(this.getParent(), "Error Populating The List", "Error while getting the list of files form conent server  " + var32.getMessage() + ", Please see the log file for more details");
            System.out.println(" \"Error Populating The List\", \"Error while getting the list of files form conent server  \" + var32.getMessage() + \", Please see the log file for more details\"");
        }
    }
    private void updateDSTable() {
        ArrayList<String[]> listing = null;
        try {
            listing = CSDPUtil.getDSListing();
            String col[] = {"Resource Type","Resource Id","Name","Element (if any)", "Description", "Modified Date", "Sites"};
            updateTable(dsscrollPane, dstable,listing,col);
        } catch (Exception var32) {
            //MessageDialog.openError(this.getParent(), "Error Populating The List", "Error while getting the list of files form conent server  " + var32.getMessage() + ", Please see the log file for more details");
            System.out.println(" \"Error Populating The List\", \"Error while getting the list of files form conent server  \" + var32.getMessage() + \", Please see the log file for more details\"");
        }
    }

    public void refresh() {
        pack();
        setLocationRelativeTo(relativeContainer);
    }

    public void display(Container relativeContainer) {
        this.relativeContainer = relativeContainer;
        refresh();
        setMinimumSize(new Dimension(900,600));
        setVisible(true);
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}

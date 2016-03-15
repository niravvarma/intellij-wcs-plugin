package com.intellij;

import com.intellij.csdt.CSDPUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

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
    private static Logger LOG = Logger.getInstance(SyncWindowForm.class);
    private Project project;
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

    public SyncWindowForm(Project project) {
        this.project = project;
    }
    public SyncWindowForm() {
        LOG.debug("Initializing Sync Window form");
        updateCSTable();
        updateDSTable();
//        project = Preferences.getProject();



        setTitle("Oracle WebCenter Sites Synchronization tool");
        setContentPane(mainPanel);
        setModal(true);

        csSyncToWorkspacetextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateCSTable();
            }
        });
        csSearchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateCSTable();
            }
        });

        csSyncSelectionToWorkspaceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                syncToWorkspace();
            }
        });

        csSyncAndCloseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                syncToWorkspace();
                dispose();
            }
        });

        csRegextextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTableWithRegex(cstable, csRegextextField.getText());
            }
        });

        dsSyncToWebCenterSitesAndCloseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                syncToWebCenterSites();
                dispose();
            }
        });

        dsRegextextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterTableWithRegex(dstable, dsRegextextField.getText());
            }
        });

        dsSyncSelectionToWebCenterSites.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                syncToWebCenterSites();
            }
        });


    }

    private void filterTableWithRegex(final JTable table, String regex) {
        try {
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


        } catch (PatternSyntaxException e) {
            LOG.error("Bad regex pattern " + e);
        }
    }

    private void syncToWebCenterSites() {
        int[] selectedRows = dstable.getSelectedRows();

        final Map<String, java.util.List<String>> csHashMap = new HashMap<String, List<String>>();
        for (int row : selectedRows) {

            String type = dstable.getModel().getValueAt(dstable.convertRowIndexToModel(row), 0).toString();
            String id = dstable.getModel().getValueAt(dstable.convertRowIndexToModel(row), 1).toString();
            if (csHashMap.containsKey(type)) {
                ((List) csHashMap.get(type)).add(id);
            } else {
                csHashMap.put(type, new ArrayList());
                ((List) csHashMap.get(type)).add(id);
            }
        }

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "WCS import to WebCenter Sites") {
            public void run(@NotNull ProgressIndicator progressIndicator) {
                try {
                    progressIndicator.setFraction(0.33);
                    progressIndicator.setText("Syncing to WCS");
                    String result = CSDPUtil.callImport(csHashMap, null, true);
                    LOG.info("Import complete");
                    LOG.debug("result: " + result.replaceAll("(?m)^[ \t]*\r?\n", ""));
                    // Finished
                    progressIndicator.setFraction(1.0);
                    progressIndicator.setText("Done");
                } catch (Exception exception) {
                    LOG.error("Import Exception: " + exception);
                }
            }

            public void onSuccess() {
                LOG.debug("Background export task Success");
                Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Success", "Successfully imported to WebCenter Sites", NotificationType.INFORMATION));
            }
        });
    }

    private void syncToWorkspace() {
        int[] selectedRows = cstable.getSelectedRows();
        final Map<String, java.util.List<String>> csHashMap = new HashMap<String, List<String>>();
        for (int row : selectedRows) {

            String type = cstable.getModel().getValueAt(cstable.convertRowIndexToModel(row), 0).toString();
            String id = cstable.getModel().getValueAt(cstable.convertRowIndexToModel(row), 1).toString();
            if (csHashMap.containsKey(type)) {
                ((List) csHashMap.get(type)).add(id);
            } else {
                csHashMap.put(type, new ArrayList());
                ((List) csHashMap.get(type)).add(id);
            }
        }
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "WCS export to workspace") {
            public void run(@NotNull ProgressIndicator progressIndicator) {
                try {
                    progressIndicator.setFraction(0.33);
                    progressIndicator.setText("Syncing from WCS");
                    String result = CSDPUtil.callExport(csHashMap, null, true);
                    LOG.info("Export complete");
                    LOG.debug("result: " + result.replaceAll("(?m)^[ \t]*\r?\n", ""));
                    // Finished
                    progressIndicator.setFraction(1.0);
                    progressIndicator.setText("Done");
                } catch (Exception exception) {
                    LOG.error("Export Exception: " + exception);
                }
            }

            public void onSuccess() {
                LOG.debug("Background export task Success");
                Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Success", "Successfully exported from WebCenter Sites", NotificationType.INFORMATION));
            }

        });


    }

    private void updateTable(JScrollPane scrollPane, JTable table, ArrayList<String[]> listing, String[] col) {

        Map<String, java.util.List<String>> csHashMap = new HashMap<String, List<String>>();
        DefaultTableModel tableModel = new DefaultTableModel(col, 0);
        for (final String[] item : listing) {
            tableModel.addRow(item);
            String type = item[1];
            String id = item[1];
            if (csHashMap.containsKey(type)) {
                ((List) csHashMap.get(type)).add(id);
            } else {
                csHashMap.put(type, new ArrayList());
                ((List) csHashMap.get(type)).add(id);
            }
        }
        table.setModel(tableModel);

        table.setRowSorter(new TableRowSorter<TableModel>(tableModel));

        scrollPane.setViewportView(table);
    }

    private void updateCSTable() {
        ArrayList<String[]> listing = null;
        try {
            listing = CSDPUtil.getCSListing(csSyncToWorkspacetextField.getText().split(","));
            String col[] = {"Resource Type", "Resource Id", "Name", "Description", "Modified Date", "Site"};
            updateTable(csscrollPane, cstable, listing, col);
        } catch (Exception var32) {
            LOG.error(" \"Error Populating The List\", \"Error while getting the list of files form conent server  \" + var32.getMessage() + \", Please see the log file for more details\"");
        }
    }

    private void updateDSTable() {
        ArrayList<String[]> listing = null;
        try {
            listing = CSDPUtil.getDSListing();
            String col[] = {"Resource Type", "Resource Id", "Name", "Element (if any)", "Description", "Modified Date", "Sites"};
            updateTable(dsscrollPane, dstable, listing, col);
        } catch (Exception var32) {
            LOG.error(" \"Error Populating The List\", \"Error while getting the list of files form conent server  \" + var32.getMessage() + \", Please see the log file for more details\"");
        }
    }

    public void refresh() {
        pack();
        setLocationRelativeTo(relativeContainer);
    }

    public void display(Container relativeContainer) {
        this.relativeContainer = relativeContainer;
        refresh();
        setMinimumSize(new Dimension(900, 600));
        setVisible(true);
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}

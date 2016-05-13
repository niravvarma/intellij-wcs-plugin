package com.intellij.forms.element;

import com.fatwire.csdt.valueobject.enumeration.MapParametersType;
import com.fatwire.csdt.valueobject.service.MapParameter;
import com.fatwire.csdt.valueobject.ui.Element;
import com.fatwire.wem.sso.SSOException;
import com.intellij.configurations.WebCenterSitesPluginModuleConfigurationData;
import com.intellij.forms.parameter.AddParameterForm;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NB20308 on 15/04/2016.
 */
public class NewElementStep2 extends DialogWrapper {
    private static Logger LOG = Logger.getInstance(NewElementStep2.class);


    private final Element element;
    private final NewElementStep2 newElementStep2;
    private final WebCenterSitesPluginModuleConfigurationData webCenterSitesPluginModuleConfigurationData;
    private final Project project;
    private final String[] col;
    private ArrayList retval;
    private JPanel mainPanel;
    private JLabel errorLabel;
    private JTable table1;
    private JButton addParameterButton;
    private JButton editParameterButton;
    private JButton removeParametersButton;
    private JScrollPane jScrollPane;

    protected NewElementStep2(@Nullable final Project project, final Element element) {
        super(project);
        this.element = element;
        setTitle("Oracle WebCenter Sites New Element");
        webCenterSitesPluginModuleConfigurationData = WebCenterSitesPluginModuleConfigurationData.getInstance(project);
        newElementStep2 = this;
        this.project = project;
        setOKButtonText("Finish");
        init();

        col = new String[]{"Key", "Value", "Types", "Site"};
        retval = new ArrayList();
        for (MapParameter mapParameter : element.getMapParameters()) {
            String[] item = {mapParameter.getKey(), mapParameter.getValue(), mapParameter.getType().label(), mapParameter.getSite()};
            retval.add(item);
        }

        updateTable(jScrollPane, table1, retval, col);

        addParameterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    new AddParameterForm(project, element, jScrollPane, table1, retval, col, null, 0);
                } catch (SSOException e1) {
                    LOG.debug("SSO Exception", e1);
                }
            }
        });

        table1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                LOG.info("selected");
                removeParametersButton.setEnabled(true);
                editParameterButton.setEnabled(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
//                LOG.info("de selected");
//                removeParametersButton.setEnabled(false);
//                editParameterButton.setEnabled(false);
            }
        });
        removeParametersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LOG.info("removed clicked");

                MapParameter mapParameter = new MapParameter();
                mapParameter.setKey(table1.getValueAt(table1.getSelectedRow(), 0).toString());
                mapParameter.setValue(table1.getValueAt(table1.getSelectedRow(), 1).toString());
                mapParameter.setSite(table1.getValueAt(table1.getSelectedRow(), 3).toString());
                mapParameter.setType(MapParametersType.valueByLabel(table1.getValueAt(table1.getSelectedRow(), 2).toString()));
                element.getMapParameters().remove(mapParameter);
                if (!element.getMapParameters().contains(mapParameter)) {
                    table1.removeAll();

                    retval = new ArrayList();
                    for (MapParameter mapParameter1 : element.getMapParameters()) {
                        String[] item = {mapParameter1.getKey(), mapParameter1.getValue(), mapParameter1.getType().label(), mapParameter1.getSite()};
                        retval.add(item);
                    }
                    updateTable(jScrollPane, table1, retval, col);
                }
            }
        });


        editParameterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    MapParameter mapParameter = new MapParameter();
                    mapParameter.setKey(table1.getValueAt(table1.getSelectedRow(), 0).toString());
                    mapParameter.setValue(table1.getValueAt(table1.getSelectedRow(), 1).toString());
                    mapParameter.setSite(table1.getValueAt(table1.getSelectedRow(), 3).toString());
                    mapParameter.setType(MapParametersType.valueByLabel(table1.getValueAt(table1.getSelectedRow(), 2).toString()));
                    new AddParameterForm(project, element, jScrollPane, table1, retval, col, mapParameter, table1.getSelectedRow());
                } catch (SSOException e1) {
                    LOG.debug("SSO Exception", e1);
                }

            }
        });
        show();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }

    @Override
    protected Action[] createActions() {
        return this.getHelpId() == null ? (SystemInfo.isMac ? new Action[]{this.getCancelAction(), this.getPreviousAction(), this.getOKAction()} : new Action[]{this.getPreviousAction(), this.getOKAction(), this.getCancelAction()}) : (SystemInfo.isMac ? new Action[]{this.getHelpAction(), this.getCancelAction(), this.getPreviousAction(), this.getOKAction()} : new Action[]{this.getPreviousAction(), this.getOKAction(), this.getCancelAction(), this.getHelpAction()});
    }

    @NotNull
    protected Action getOKAction() {
        return new NewElementStep2.OkAction("Finish");
    }

    @NotNull
    protected Action getPreviousAction() {
        return new NewElementStep2.PreviousAction("Previous");
    }

    private void updateTable(JScrollPane scrollPane, JTable table, ArrayList<String[]> listing, String[] col) {

        Map<String, List<String>> csHashMap = new HashMap<String, List<String>>();
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

//        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
//        table.setRowSorter(sorter);
//
//        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
//        int columnIndexToSort = Arrays.asList(col).indexOf("Modified Date");
//        sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.DESCENDING));
//        sorter.setSortKeys(sortKeys);
//        sorter.sort();

        scrollPane.setViewportView(table);
    }

    protected class OkAction extends DialogWrapper.DialogWrapperAction {
        protected OkAction(@NotNull String name) {
            super(name);
        }

        @Override
        protected void doAction(ActionEvent actionEvent) {
            LOG.debug("Clicked Finish Action");
            dispose();
            CreateElement.createElement(project, element);
        }
    }

    public class PreviousAction extends DialogWrapper.DialogWrapperAction {
        protected PreviousAction(@NotNull String name) {
            super(name);
        }

        @Override
        protected void doAction(ActionEvent actionEvent) {
            LOG.debug("Clicked Previous Action");
            dispose();
            new NewElementStep1(project, element);
        }
    }

}

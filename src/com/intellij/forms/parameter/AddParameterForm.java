package com.intellij.forms.parameter;

import com.fatwire.csdt.valueobject.enumeration.MapParametersType;
import com.fatwire.csdt.valueobject.service.MapParameter;
import com.fatwire.csdt.valueobject.ui.Element;
import com.fatwire.wem.sso.SSOException;
import com.intellij.csdt.CSDPUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.SystemInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NB20308 on 28/04/2016.
 */
public class AddParameterForm extends DialogWrapper {

    private static Logger LOG = Logger.getInstance(AddParameterForm.class);
    private final JTable table1;
    private final JScrollPane jScrollPane;
    private final String[] col;
    private final Element element;
    private ArrayList retval;
    private int selectedRow = 0;
    private boolean injected = false;

    private JPanel mainPanel;
    private JTextField textFieldName;
    private JTextField textFieldValue;
    private JComboBox comboBoxSite;
    private JComboBox comboBoxType;

    public AddParameterForm(@Nullable Project project, Element element, JScrollPane jScrollPane, JTable table1, ArrayList retval, String[] col, @Nullable MapParameter mapParameter, @Nullable int selectedRow) throws SSOException {
        super(project);
        setTitle("Add parameter");
        init();
        String[] usersites = CSDPUtil.getUserSitenames();
        for (String s : usersites) {
            comboBoxSite.addItem(s);
        }
        String[] labels = MapParametersType.labels();
        for (String s : labels) {
            comboBoxType.addItem(s);
        }

        this.element = element;
        this.jScrollPane = jScrollPane;
        this.table1 = table1;
        this.retval = retval;
        this.col = col;

        if (mapParameter != null) {
            this.injected = true;
            textFieldName.setText(mapParameter.getKey());
            textFieldValue.setText(mapParameter.getValue());
            comboBoxSite.setSelectedItem(mapParameter.getSite());
            comboBoxType.setSelectedItem(mapParameter.getType().label());
            this.selectedRow = selectedRow;
            setOKButtonText("save");
        }
        show();

    }


    @Override
    protected Action[] createActions() {
        return this.getHelpId() == null ? (SystemInfo.isMac ? new Action[]{this.getCancelAction(), this.getAddAction()} : new Action[]{this.getAddAction(), this.getCancelAction()}) : (SystemInfo.isMac ? new Action[]{this.getHelpAction(), this.getCancelAction(), this.getAddAction()} : new Action[]{this.getAddAction(), this.getCancelAction(), this.getHelpAction()});
    }


    @NotNull
    protected Action getAddAction() {
        return new AddParameterForm.AddAction("Add");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
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
        scrollPane.setViewportView(table);
    }

    protected class AddAction extends DialogWrapper.DialogWrapperAction {
        protected AddAction(@NotNull String name) {
            super(name);
        }

        @Override
        protected void doAction(ActionEvent actionEvent) {

            LOG.debug("Clicked Add Action");
            MapParameter mapParameter = new MapParameter();
            mapParameter.setKey(textFieldName.getText());
            mapParameter.setValue(textFieldValue.getText());
            mapParameter.setSite(comboBoxSite.getSelectedItem().toString());
            mapParameter.setType(MapParametersType.valueByLabel(comboBoxType.getSelectedItem().toString()));
            if (!injected && !element.getMapParameters().contains(mapParameter)) {
                element.getMapParameters().add(mapParameter);
                retval = new ArrayList();
                table1.removeAll();
                for (MapParameter mapParameter1 : element.getMapParameters()) {
                    String[] item = {mapParameter1.getKey(), mapParameter1.getValue(), mapParameter1.getType().label(), mapParameter1.getSite()};
                    retval.add(item);
                }

                updateTable(jScrollPane, table1, retval, col);
            } else {
                element.getMapParameters().remove(selectedRow);
                element.getMapParameters().add(selectedRow, mapParameter);
                retval = new ArrayList();
                table1.removeAll();
                for (MapParameter mapParameter1 : element.getMapParameters()) {
                    String[] item = {mapParameter1.getKey(), mapParameter1.getValue(), mapParameter1.getType().label(), mapParameter1.getSite()};
                    retval.add(item);
                }
                updateTable(jScrollPane, table1, retval, col);
            }
            dispose();
        }
    }
}

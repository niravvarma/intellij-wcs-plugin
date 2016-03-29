package com.intellij.forms.newtemplate;


import com.fatwire.csdt.valueobject.enumeration.CacheRule;
import com.fatwire.csdt.valueobject.service.PageletParameter;
import com.fatwire.csdt.valueobject.ui.SiteEntry;
import com.fatwire.csdt.valueobject.ui.Template;
import com.intellij.csdt.CSDPUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NB20308 on 18/03/2016.
 */
public class NewTemplateStep2 extends JDialog {

    private static Logger LOG = Logger.getInstance(NewTemplateStep2.class);
    private NewTemplateStep1 newTemplateStep1;
    private NewTemplateStep2 newTemplateStep2;
    private JPanel mainPanel;
    private JLabel errorLabel;
    private JButton backButton;
    private JButton cancelButton;
    private JButton nextButton;
    private JButton finishButton;
    private JTextField textFieldRootElement;
    private JRadioButton uncachedRadioButton;
    private JRadioButton cachedRadioButton;
    private JRadioButton advancedRadioButton;
    private JTextField textFieldCacheCriteria;
    private JTextField textFieldPageName;
    private JTable tablePageletParameters;
    private JButton addParameterButton;
    private JButton editParameterButton;
    private JButton removeParametersButton;
    private JList listAccessControl;
    private JTextField textFieldSitesCache;
    private JTextField textFieldSatCache;
    private JScrollPane scrollPanePagelet;
    private JPanel panelAdvancedCaching;
    private NewTemplateStep3 newTemplateStep3;
    private Template template;
    private SiteEntry siteEntry;

    public NewTemplateStep2(final JFrame frame, final Project project, final Template template, final NewTemplateStep1 newTemplateStep1) {
        setTitle("New Template");

        this.newTemplateStep2 = this;
        this.newTemplateStep1 = newTemplateStep1;
        this.template = template;

        newTemplateStep1.setNewTemplateStep2(this);
        setContentPane(mainPanel);
        setModal(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Log.info("clicked back");
                newTemplateStep2.setVisible(false);
                newTemplateStep1.setVisible(true);
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                newTemplateStep2.setVisible(false);
//                Project project = event.getData(PlatformDataKeys.PROJECT);
                if (newTemplateStep3 == null) {
                    JFrame frame = WindowManager.getInstance().getFrame(project);
                    final NewTemplateStep3 newElementCatalog = new NewTemplateStep3(frame, project, template, newTemplateStep2);
                } else {
                    newTemplateStep3.setVisible(true);
                }


            }
        });

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Loading New Template Dialog") {
            public void run(@NotNull ProgressIndicator progressIndicator) {
                LOG.info("Getting acls");
                String[] acls = CSDPUtil.getACLS();
                listAccessControl.setListData(acls);

            }

            public void onSuccess() {
                panelAdvancedCaching.setVisible(false);

                siteEntry = defaultSiteEntry(template);
                setDefaultProperties();
                updatePageletParameterTable();
                textFieldRootElement.setText(template.getRootElement());
                textFieldPageName.setText(template.getPageName(template.getSite()));
                LOG.info("Background Loading New Template Dialog Success");
                setTitle("Oracle WebCenter Sites New Template");
                setContentPane(mainPanel);
                setModal(true);
                newTemplateStep2.display(frame);
            }

        });


        listAccessControl.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                LOG.info("ACS value changed");
                template.getAcls().clear();
                template.getAcls().addAll(listAccessControl.getSelectedValuesList());
//                template.setAcl();
//                template.set
            }
        });
        textFieldSitesCache.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
//                template.setCacheRule();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }
        });
        cachedRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (cachedRadioButton.isSelected()) {

                    LOG.info("cachedRadioButton");
                    panelAdvancedCaching.setVisible(false);
                    template.setCacheRule(CacheRule.CACHED);

                }
            }
        });
        uncachedRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (uncachedRadioButton.isSelected()) {
                    LOG.info("uncachedRadioButton");
                    panelAdvancedCaching.setVisible(false);
                    template.setCacheRule(CacheRule.UNCACHED);
                }
            }
        });
        advancedRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (advancedRadioButton.isSelected()) {

                    LOG.info("advancedRadioButton");
                    panelAdvancedCaching.setVisible(true);
                    template.setCacheRule(CacheRule.ADVANCED);
                }
            }
        });

        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateTemplate.createTemplate(project, template);
            }
        });
        textFieldCacheCriteria.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                template.setCacheCriteria(textFieldCacheCriteria.getText());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                template.setCacheCriteria(textFieldCacheCriteria.getText());
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void updatePageletParameterTable() {
        ArrayList<String[]> listing = null;
        try {
            String col[] = {"Name", "Value"};
            updateTable(scrollPanePagelet, tablePageletParameters, siteEntry.getPageletParameters(), col);
        } catch (Exception var32) {
            LOG.error(" \"Error Populating The List\", \"Error while getting the list of files form conent server  \" + var32.getMessage() + \", Please see the log file for more details\"");
        }
    }

    private void updateTable(JScrollPane scrollPane, JTable table, List<PageletParameter> listing, String[] col) {

        Map<String, java.util.List<String>> csHashMap = new HashMap<String, List<String>>();
        DefaultTableModel tableModel = new DefaultTableModel(col, 0);
        for (PageletParameter item : listing) {
            tableModel.addRow(new String[]{item.getName(), item.getValue()});
//            String type = item[1];
//            String id = item[1];
//            if (csHashMap.containsKey(type)) {
//                ((List) csHashMap.get(type)).add(id);
//            } else {
//                csHashMap.put(type, new ArrayList());
//                ((List) csHashMap.get(type)).add(id);
//            }
        }
        table.setModel(tableModel);

        table.setRowSorter(new TableRowSorter<TableModel>(tableModel));

        scrollPane.setViewportView(table);
    }

    private SiteEntry defaultSiteEntry(Template template) {
        SiteEntry defaultSiteEntry = new SiteEntry();
        List<PageletParameter> pParams = defaultSiteEntry.getPageletParameters();
        pParams.clear();
        PageletParameter p = new PageletParameter();
        p.setName("rendermode");
        p.setValue("live");
        pParams.add(p);
        p = new PageletParameter();
        p.setName("site");
        p.setValue(template.getSite());
        pParams.add(p);
        return defaultSiteEntry;
    }

    private void setDefaultProperties() {
        template.setCacheCriteria("c,cid,context,p,d,rendermode,site,sitepfx,ft_ss,deviceid");
        template.setCacheRule(CacheRule.CACHED);
        template.getAcls().add("Any");
    }

    public void refresh() {
        pack();
        setLocationRelativeTo(null);
    }

    public void display(Container relativeContainer) {
        refresh();
        URL iconURL = getClass().getClassLoader().getResource("icons/newtemplate_wiz.gif");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());
        setResizable(false);
        setSize(new Dimension(650, 600));
        setVisible(true);
    }

    public NewTemplateStep3 getNewTemplateStep3() {
        return newTemplateStep3;
    }

    public void setNewTemplateStep3(NewTemplateStep3 newTemplateStep3) {
        this.newTemplateStep3 = newTemplateStep3;
    }
}

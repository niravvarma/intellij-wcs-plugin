package com.intellij.forms.newtemplate;


import com.fatwire.csdt.valueobject.enumeration.ElementFileType;
import com.fatwire.csdt.valueobject.enumeration.TemplateUsage;
import com.fatwire.csdt.valueobject.ui.Template;
import com.fatwire.wem.sso.SSOException;
import com.intellij.csdt.CSDPUtil;
import com.intellij.csdt.rest.RestProvider;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by NB20308 on 16/03/2016.
 */
public class NewTemplateStep1 extends JDialog {

    private static Logger LOG = Logger.getInstance(NewTemplateStep1.class);
    public Template template;
    private boolean autoPopulateRootElement = true;
    private boolean autoPopulateStoragePath = true;
    private NewTemplateStep1 newTemplateStep1;
    private Project project;
    private JButton finishButton;
    private JButton backButton;
    private JButton cancelButton;
    private JButton nextButton;
    private JComboBox comboBoxSite;
    private JTextField textFieldNewTemplateName;
    private JTextField textFieldDescription;
    private JList listSubtypes;
    private JRadioButton XMLRadioButton;
    private JRadioButton JSPRadioButton;
    private JRadioButton HTMLRadioButton;
    private JRadioButton existingElementRadioButton;
    private JTextField textFieldRootElement;
    private JTextField textFieldStoragePath;
    private JTextField textField6;
    private JComboBox comboBoxAssetType;
    private JComboBox comboBoxUsage;
    private JTextField textField7;
    private JTextField textField3;
    private JPanel mainPanel;
    private JLabel errorLabel;
    private List<com.fatwire.rest.beans.Type> allTypes;
    private NewTemplateStep2 newTemplateStep2;


    public NewTemplateStep1(Project project) {
        this.project = project;
    }


//    private class DropDownType{
//    com.fatwire.rest.beans.Type item;
//
//    public DropDownType(com.fatwire.rest.beans.Type item) {
//        this.item = item;
//    }
//
//    public com.fatwire.rest.beans.Type getItem() {
//        return item;
//    }
//
//    public void setItem(com.fatwire.rest.beans.Type item) {
//        this.item = item;
//    }
//
//    @Override
//    public String toString() {
//        return item.getName();
//    }
//}

    public NewTemplateStep1(final JFrame frame) {
        setTitle("Oracle WebCenter Sites New Template");
        newTemplateStep1 = this;
        errorLabel.setVisible(false);

        this.autoPopulateStoragePath = true;
        this.autoPopulateRootElement = true;


        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Loading New Template Dialog") {


            public void run(@NotNull ProgressIndicator progressIndicator) {
                LOG.info("Getting Sites for current user");
                try {
                    String[] usersites = CSDPUtil.getUserSitenames();
                    for (String s : usersites) {
                        comboBoxSite.addItem(s);
                    }

                    LOG.info("Getting types for current user");

                    allTypes = RestProvider.getAllAssetTypes();
                } catch (SSOException exception) {
                    LOG.debug("SSOException - is WCS running?", exception);
                    Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error opening the New Template Window", "Is Oracle WebCenter Sites running?<br>" + exception.getLocalizedMessage(), NotificationType.ERROR), project);
                } catch (RuntimeException exception) {
                    LOG.debug("RuntimeException -" + exception.getMessage(), exception);
                    Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error opening the New Template Window", exception.getMessage(), NotificationType.ERROR), project);
                }
            }

            public void onSuccess() {

                comboBoxSite.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        populateAssetType(null);
                        template.setSite(comboBoxSite.getSelectedItem().toString());
                    }
                });

                template = new Template();
                setDefaultProperties();
                LOG.info("Background Loading New Template Dialog Success");

                setContentPane(mainPanel);
                setModal(true);
                newTemplateStep1.display(frame);

            }

        });

        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateTemplate.createTemplate(project, template);
            }
        });

        comboBoxUsage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                template.setUsage(TemplateUsage.valueByTemplateDescription(comboBoxUsage.getSelectedItem().toString()));
            }
        });

        comboBoxAssetType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                listSubtypes.removeAll();
                listSubtypes.removeAll();
                if (comboBoxAssetType.getSelectedItem() != null) {
                    populateSubType();
                    LOG.info("comboBoxAssetType actionPerformed");

                    try {
                        populateElementFields();
                    } catch (IOException exception) {
                        LOG.debug("IOException", exception);
                        Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error opening the New Template Window", exception.getLocalizedMessage(), NotificationType.ERROR), project);
                    }
                }
            }

        });

        listSubtypes.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                template.getSubtypes().clear();
                template.getSubtypes().addAll(listSubtypes.getSelectedValuesList());
//                listSubtypes.getSelectedValue().toString();
                //TODO: Nothing to do with this yet

            }
        });

        textFieldNewTemplateName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                updateTemplateNameEvent();

            }
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                updateTemplateNameEvent();
            }

            private void updateTemplateNameEvent() {
                try {
                    template.setName(textFieldNewTemplateName.getText());
                    populateElementFields();
                    finishButton.setEnabled(isPageComplete());
                    nextButton.setEnabled(isPageComplete());
                } catch (IOException exception) {
                    LOG.debug("IOException", exception);
                    Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error opening the New Template Window", exception.getLocalizedMessage(), NotificationType.ERROR), project);
                }
            }


        });

        textFieldDescription.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                template.setDescription(textFieldDescription.getText());
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                LOG.info("clicked next buton");
                newTemplateStep1.setVisible(false);
//                Project project = event.getData(PlatformDataKeys.PROJECT);
                if (newTemplateStep2 == null) {
                    JFrame frame = WindowManager.getInstance().getFrame(project);
                    final NewTemplateStep2 newElementCatalog = new NewTemplateStep2(frame, project, template, newTemplateStep1);
                } else {
                    newTemplateStep2.setVisible(true);
                }


            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void setDefaultProperties() {
        template.setSite(comboBoxSite.getSelectedItem().toString());
        template.setUsage(TemplateUsage.UNSPECIFIED);
        template.setElementType(ElementFileType.JSP_T);
    }

    private void populateAssetType(String assetTypeName) {
        try {
            String siteName = comboBoxSite.getSelectedItem().toString();
            String[] assetTypes = CSDPUtil.addDefault(CSDPUtil.getEnabledTypesForSite(siteName), "Can apply to any asset type");

            comboBoxAssetType.removeAllItems();
            for (String asseType : assetTypes) {
                comboBoxAssetType.addItem(asseType);
            }
//        listSubtypes.setListData(assetTypes);
            String targetAssetTypeName = StringUtils.defaultString(assetTypeName);
            targetAssetTypeName = ArrayUtils.contains(assetTypes, targetAssetTypeName) ? targetAssetTypeName : "Can apply to any asset type";
            comboBoxAssetType.setSelectedItem(targetAssetTypeName);
//        listSubtypes.setText(targetAssetTypeName);
        } catch (SSOException exception) {
            LOG.debug("SSOException", exception);
            Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error opening the New Template Window", "Is Oracle WebCenter Sites running?<br>" + exception.getLocalizedMessage(), NotificationType.ERROR), project);
        }
    }

    private void populateSubType() {
        String assetTypeName = comboBoxAssetType.getSelectedItem().toString();
        if (comboBoxAssetType.getSelectedIndex() != 0) {
            template.setAssetType(comboBoxAssetType.getSelectedItem().toString());
        } else {
            template.setAssetType(null);
        }
        listSubtypes.setListData(CSDPUtil.addDefault(CSDPUtil.getAssetSubTypes(allTypes, assetTypeName), "Any"));
//        this.subType.setItems(this.subTypes);
    }

    private void populateElementFields() throws IOException {
        String rootElementName = CSDPUtil.buildRootElement(textFieldNewTemplateName.getText(), template.getAssetType());
        String storagePathValue = CSDPUtil.buildStoragePath(rootElementName, ElementFileType.JSP_T);
        if (this.autoPopulateStoragePath) {
            textFieldStoragePath.setText(storagePathValue);
            template.setStoragePath(storagePathValue);
        }

        if (this.autoPopulateRootElement) {
            textFieldRootElement.setText(rootElementName);
            template.setRootElement(rootElementName);
        }

        String pageName = CSDPUtil.buildPagenameFromRootElement(comboBoxSite.getSelectedItem().toString(), rootElementName);
        LOG.info("pageName: " + pageName);
        template.setPageName(comboBoxSite.getSelectedItem().toString(), pageName);
    }


    public boolean isPageComplete() {
        return this.nameNotBlank() && this.rootElementNotBlank() && this.storagePathNotBlank();
    }

    private boolean nameNotBlank() {
        return StringUtils.isNotBlank(this.template.getName());
    }

    private boolean storagePathNotBlank() {
        return StringUtils.isNotBlank(this.template.getStoragePath());
    }

    private boolean rootElementNotBlank() {
        return StringUtils.isNotBlank(this.template.getRootElement());
    }
//    public boolean isValid(){
//        return textFieldNewTemplateName==null || textFieldNewTemplateName.getText()!=null;
//    }

//    public void activateNextStep() {
//        errorLabel.setVisible(false);
//        finishButton.setEnabled(true);
//        nextButton.setEnabled(true);
//    }

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

    public NewTemplateStep2 getNewTemplateStep2() {
        return newTemplateStep2;
    }

    public void setNewTemplateStep2(NewTemplateStep2 newTemplateStep2) {
        this.newTemplateStep2 = newTemplateStep2;
    }
}

package com.intellij.forms;

import com.fatwire.cs.core.http.Post;
import com.fatwire.csdt.valueobject.enumeration.ElementFileType;
import com.fatwire.csdt.valueobject.ui.ElementCatalogEntry;
import com.fatwire.wem.sso.SSOException;
import com.intellij.configurations.WebCenterSitesPluginModuleConfigurationData;
import com.intellij.csdt.CSDPUtil;
import com.intellij.csdt.Template;
import com.intellij.csdt.util.WizardUtil;
import com.intellij.csdt.valueobject.enumeration.ContentTemplates;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by NB20308 on 15/03/2016.
 */
public class NewElementCatalog extends DialogWrapper {

    private static Logger LOG = Logger.getInstance(NewElementCatalog.class);
    private final WebCenterSitesPluginModuleConfigurationData webCenterSitesPluginModuleConfigurationData;
    private final NewElementCatalog newElementCatalog;
    private Project project;
    private JTextField textFieldElementCatalogName;
    private ButtonGroup buttonGroup1;
    private JButton finishButton;
    private JTextField textFieldElementCatalogDescription;
    private JTextField textFieldElementCatalogStoragePath;
    private JTextField textFieldElementCatalogElementParameter;
    private JTextField textFieldElementCatalogAdditionalParameters;
    private JPanel mainPanel;
    private JRadioButton XMLRadioButton;
    private JRadioButton JSPRadioButton;
    private JRadioButton groovyRadioButton;
    private JRadioButton HTMLRadioButton;
    private JRadioButton existingElementRadioButton;
    private JLabel labelErrorMessage;
    private Container relativeContainer;

    private ElementCatalogEntry elementCatalogEntry;


    public NewElementCatalog(Project project, final JFrame frame) {
        super(project);

        setTitle("Oracle WebCenter Sites New Element");
        webCenterSitesPluginModuleConfigurationData = WebCenterSitesPluginModuleConfigurationData.getInstance(project);
        newElementCatalog = this;

        this.elementCatalogEntry = new ElementCatalogEntry();
        setDefaultProperties();

        setOKButtonText("Finish");
        setOKActionEnabled(isPageComplete());

        init();
        setSize(600, 550);
        textFieldElementCatalogName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                populateElementFields();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                populateElementFields();
            }
        });

        textFieldElementCatalogElementParameter.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                elementCatalogEntry.setElementParameters(textFieldElementCatalogElementParameter.getText());
                setPageError();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                elementCatalogEntry.setElementParameters(textFieldElementCatalogElementParameter.getText());
                setPageError();
            }
        });
        textFieldElementCatalogAdditionalParameters.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                elementCatalogEntry.setAdditionalElementParameters(textFieldElementCatalogAdditionalParameters.getText());
                setPageError();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                elementCatalogEntry.setAdditionalElementParameters(textFieldElementCatalogAdditionalParameters.getText());
                setPageError();
            }
        });
        XMLRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (XMLRadioButton.isSelected()) {
                    elementCatalogEntry.setElementType(ElementFileType.XML_EC);
                    populateElementFields();
                }
            }
        });
        JSPRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (JSPRadioButton.isSelected()) {
                    elementCatalogEntry.setElementType(ElementFileType.JSP_EC);
                    populateElementFields();
                }
            }
        });
        groovyRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (groovyRadioButton.isSelected()) {
                    elementCatalogEntry.setElementType(ElementFileType.GROOVY);
                    populateElementFields();
                }
            }
        });
        HTMLRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (HTMLRadioButton.isSelected()) {
                    elementCatalogEntry.setElementType(ElementFileType.HTML);
                    populateElementFields();
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

    private void setDefaultProperties() {
        elementCatalogEntry.setElementType(ElementFileType.JSP_EC);
    }


    private void populateElementFields() {
        this.elementCatalogEntry.setElementName(textFieldElementCatalogName.getText());
        String rootElementName = textFieldElementCatalogName.getText();
        String storagePathValue = CSDPUtil.buildStoragePath(rootElementName, this.elementCatalogEntry.getElementType());
        textFieldElementCatalogStoragePath.setText(storagePathValue);
        this.elementCatalogEntry.setStoragePath(storagePathValue);
        setOKActionEnabled(isPageComplete());
        setPageError();
    }


    @Override
    public void doOKAction() {

        ProgressManager.getInstance().run(new Task.Backgroundable(NewElementCatalog.this.project, "Creating Element Catalog") {
            public void run(@NotNull ProgressIndicator progressIndicator) {
                Post request = null;
                try {
                    request = CSDPUtil.buildPostRequest("OpenMarket/Xcelerate/PrologActions/Publish/csdt/ElementCatalogSave");

                    Template serviceObject = new Template();
                    serviceObject.setName(elementCatalogEntry.getElementName());
                    serviceObject.setDescription(StringUtils.defaultString(elementCatalogEntry.getElementDescription()));
                    progressIndicator.setFraction(0.3);

                    ElementFileType type = elementCatalogEntry.getElementType();
                    boolean existing = type == ElementFileType.EXISTING;
                    serviceObject.setUseExisting(String.valueOf(existing));
                    String e3;
                    if (!existing) {
                        e3 = elementCatalogEntry.getStoragePath();
                        serviceObject.setUrlSpec(e3);
                    }

                    progressIndicator.setFraction(0.4);

                    serviceObject.setResdetails1(StringUtils.defaultString(elementCatalogEntry.getElementParameters()));
                    serviceObject.setResdetails2(StringUtils.defaultString(elementCatalogEntry.getAdditionalElementParameters()));
                    progressIndicator.setFraction(0.8);
                    try {
                        e3 = "";
                        if (!existing) {
                            switch (elementCatalogEntry.getElementType()) {
                                case XML_EC:
                                    e3 = StringUtils.defaultIfEmpty(elementCatalogEntry.getContent(), ContentTemplates.XML_ELEMENT.defaultContent());
                                    break;
                                case JSP_EC:
                                    e3 = StringUtils.defaultIfEmpty(elementCatalogEntry.getContent(), ContentTemplates.JSP_ELEMENT.defaultContent());
                                    break;
                                case GROOVY:
                                    e3 = StringUtils.defaultIfEmpty(elementCatalogEntry.getContent(), ContentTemplates.GROOVY_ELEMENT.defaultContent());
                                    break;
                                case HTML:
                                    e3 = StringUtils.defaultIfEmpty(elementCatalogEntry.getContent(), ContentTemplates.HTML_TEMPLATE.defaultContent());
                                    break;
                                default:
                                    e3 = StringUtils.defaultIfEmpty(elementCatalogEntry.getContent(), ContentTemplates.JSP_ELEMENT.defaultContent());
                                    break;
                            }
                        }

                        serviceObject.setUrl(e3);
                    } catch (IOException exception) {
                        LOG.debug("IOException", exception);
                        Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error while saving Element Catalog Entry", exception.getLocalizedMessage(), NotificationType.ERROR), project);

                    } catch (URISyntaxException exception) {
                        LOG.debug("URISyntaxException", exception);
                        Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error while saving Element Catalog Entry", exception.getLocalizedMessage(), NotificationType.ERROR), project);

                    }

                    progressIndicator.setFraction(0.9);


                    try {
                        final String saveElementCatalogOutput = WizardUtil.saveElementCatalog(serviceObject);
                        if (saveElementCatalogOutput.contains("Save Error")) {
                            LOG.debug("Error while saving Element Catalog Entry", "Error while saving Template", saveElementCatalogOutput);
                            Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error while saving Element Catalog Entry", "Error while saving Template", NotificationType.ERROR), project);
                        } else if (saveElementCatalogOutput.contains("Insufficient Privileges")) {
                            LOG.debug("Insufficient Privileges", "You do not have sufficient privileges to perform this operation", saveElementCatalogOutput);
                            Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error while saving Element Catalog Entry", "Insufficient Privileges<br>You do not have sufficient privileges to perform this operation", NotificationType.ERROR), project);
                        } else if (saveElementCatalogOutput.contains("Login Error")) {
                            LOG.debug("Error while saving Element Catalog Entry", "Please review the credentials configured in your preferences", saveElementCatalogOutput);
                            Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error while saving Element Catalog Entry", "Please review the credentials configured in your preferences", NotificationType.ERROR), project);
                        } else {
                            String output = CSDPUtil.callExport(saveElementCatalogOutput);
                            LOG.info("output: " + output);
                            progressIndicator.setFraction(1);
                            this.finalize();
                        }
                    } catch (SSOException exception) {
                        LOG.debug("SSOException", exception);
                        Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error while saving Element Catalog Entry", "Is Oracle WebCenter Sites running?<br>" + exception.getLocalizedMessage(), NotificationType.ERROR), project);
                    } catch (IOException exception) {
                        LOG.debug("IOException", exception);
                        Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error while saving Element Catalog Entry", exception.getLocalizedMessage(), NotificationType.ERROR), project);
                    } catch (Throwable exception) {
                        LOG.debug("Throwable", exception);
                        Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error while saving Element Catalog Entry", exception.getLocalizedMessage(), NotificationType.ERROR), project);
                    }
                } catch (SSOException exception) {
                    LOG.debug("SSOException", exception);
                    Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error while saving Element Catalog Entry", "Is Oracle WebCenter Sites running?<br>" + exception.getLocalizedMessage(), NotificationType.ERROR), project);

                }
            }


            public void onSuccess() {
                Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Success", "Successfully created element Catalog", NotificationType.INFORMATION), project);
            }
        });

        dispose();

    }

    public void disposeDialog() {
        dispose();
    }

    public boolean isPageComplete() {
        return this.nameNotBlank() && this.storagePathNotBlank();
    }

    private boolean nameNotBlank() {
        return StringUtils.isNotBlank(this.elementCatalogEntry.getElementName());
    }

    private void setNameError() {
        this.setErrorMessage(this.nameNotBlank() ? null : "Please provide a name");
    }

    private void setErrorMessage(String s) {
        labelErrorMessage.setText(s);
    }

    private boolean storagePathNotBlank() {
        return StringUtils.isNotBlank(this.elementCatalogEntry.getStoragePath());
    }

    private void setStoragePathError() {
        this.setErrorMessage(this.storagePathNotBlank() ? null : "Please provide the storage path or re-select element type to restore");
    }

    private void setPageError() {
        this.setNameError();
        if (this.nameNotBlank()) {
            this.setStoragePathError();
            if (this.storagePathNotBlank()) {
            }
        }
    }
}

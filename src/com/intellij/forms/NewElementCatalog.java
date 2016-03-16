package com.intellij.forms;

import com.fatwire.cs.core.http.Post;
import com.fatwire.wem.sso.SSOException;
import com.intellij.csdt.CSDPUtil;
import com.intellij.csdt.Template;
import com.intellij.csdt.util.WizardUtil;
import com.intellij.csdt.valueobject.enumeration.ContentTemplates;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by NB20308 on 15/03/2016.
 */
public class NewElementCatalog extends JDialog {

    private static Logger LOG = Logger.getInstance(NewElementCatalog.class);
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
    private Container relativeContainer;

    public NewElementCatalog(Project project) {
        this.project = project;
    }

    public NewElementCatalog() {


        setTitle("Oracle WebCenter Sites New Element");
        setContentPane(mainPanel);
        setModal(true);

        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //CSDTUtil.

                Post request = null;
                try {
                    request = CSDPUtil.buildPostRequest("OpenMarket/Xcelerate/PrologActions/Publish/csdt/ElementCatalogSave");

                    Template serviceObject = new Template();
                    serviceObject.setName(textFieldElementCatalogName.getText());
                    serviceObject.setDescription(StringUtils.defaultString(textFieldElementCatalogDescription.getText()));
                   /* if(monitor != null) {
                        monitor.worked(30);
                    }*/

                   /*ElementFileType type = ElementFileType.JSP_EC;
                    boolean existing = type == ElementFileType.EXISTING;
                    serviceObject.setUseExisting(String.valueOf(existing));
                    String e3;

                    if(!existing) {*/
                    boolean existing = false;
                    String e3 = textFieldElementCatalogStoragePath.getText();
                    serviceObject.setUrlSpec(e3);
                  /*  }*/

                   /*if(monitor != null) {
                        monitor.worked(40);
                    }*/

                    serviceObject.setResdetails1(StringUtils.defaultString(textFieldElementCatalogElementParameter.getText()));
                    serviceObject.setResdetails2(StringUtils.defaultString(textFieldElementCatalogAdditionalParameters.getText()));
                   /* if(monitor != null) {
                        monitor.worked(80);
                    }*/

                    try {
                        e3 = "";
                        if (!existing) {
                            e3 = StringUtils.defaultIfEmpty("", ContentTemplates.JSP_ELEMENT.defaultContent());
                        }

                        serviceObject.setUrl(e3);
                    } catch (IOException var18) {
                        //  Log.error(var18);
                        throw new RuntimeException(var18);
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }

                    /*if(monitor != null) {
                        monitor.worked(90);
                    }*/

                    try {
                        String saveElementCatalogOutput = WizardUtil.saveElementCatalog(serviceObject);
                        if (saveElementCatalogOutput.contains("Save Error")) {
                            LOG.error("Save Error", saveElementCatalogOutput);
                            //   MessageDialog.openError(shell, "Error while saving Element Catalog Entry", "Error while saving Template");
                        } else if (saveElementCatalogOutput.contains("Insufficient Privileges")) {
                            LOG.error("Insufficient Privileges", saveElementCatalogOutput);
                            //MessageDialog.openError(shell, "Error while saving Element Catalog Entry", "You do not have sufficient privileges to perform this operation");
                        } else if (saveElementCatalogOutput.contains("Login Error")) {
                            LOG.error("Error while saving Element Catalog Entry", "Please review the credentials configured in your preferences", saveElementCatalogOutput);
                            //  MessageDialog.openError(shell, "Error while saving Element Catalog Entry", "Please review the credentials configured in your preferences");
                        } else {
                            String output = CSDPUtil.callExport(saveElementCatalogOutput);
                            LOG.info("ouput: " + output);
                           /* CSDPUtil.openElement(workbench, uiObject);
                            CSDPUtil.refreshTree();
                            CSDPUtil.refreshIResource();*/
                        }
                    } catch (SSOException var14) {
                        LOG.error("SSOException", var14);
                    } catch (IOException var15) {
                        LOG.error("IOException", var15);
                        //handleException(shell, var15);
                    } finally {
                        /*if(monitor != null) {
                            monitor.worked(100);
                            monitor.done();
                        }*/

                    }

                } catch (SSOException e1) {
                    LOG.error("SSOException", e1);
                }

            }
        });
        textFieldElementCatalogName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                textFieldElementCatalogStoragePath.setText(textFieldElementCatalogName.getText() + ".jsp");
            }
        });
    }


    public void refresh() {
        pack();
        setLocationRelativeTo(relativeContainer);
    }

    public void display(Container relativeContainer) {
        this.relativeContainer = relativeContainer;
        refresh();
        setMinimumSize(new Dimension(650, 320));
        setVisible(true);
    }

}

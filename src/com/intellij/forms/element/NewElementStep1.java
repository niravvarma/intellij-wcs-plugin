package com.intellij.forms.element;

import com.fatwire.csdt.valueobject.enumeration.ElementFileType;
import com.fatwire.csdt.valueobject.ui.Element;
import com.fatwire.wem.sso.SSOException;
import com.intellij.configurations.WebCenterSitesPluginModuleConfigurationData;
import com.intellij.csdt.CSDPUtil;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.util.SystemInfo;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by NB20308 on 15/04/2016.
 */
public class NewElementStep1 extends DialogWrapper {
    private static Logger LOG = Logger.getInstance(NewElementStep1.class);

    private final WebCenterSitesPluginModuleConfigurationData webCenterSitesPluginModuleConfigurationData;
    private final NewElementStep1 newElementStep1;
    private final Project project;
    private boolean injected;
    private boolean autoPopulateStoragePath = true;
    private boolean autoPopulateRootElement = true;
    private Element element;
    private JLabel errorLabel;
    private JComboBox comboBoxSite;
    private JTextField textFieldNewTemplateName;
    private JTextField textFieldDescription;
    private JRadioButton XMLRadioButton;
    private JRadioButton JSPRadioButton;
    private JRadioButton groovyRadioButton;
    private JRadioButton HTMLRadioButton;
    private JRadioButton existingElementRadioButton;
    private JTextField textFieldRootElement;
    private JTextField textFieldStoragePath;
    private JTextField textFieldElementParameters;
    private JTextField textFielAdditionalElementParameters;
    private JPanel mainPanel;
    private JTextField textFieldElementDescription;
    private JLabel textFieldErrorMessage;
    private String[] usersites;
    private NextAction nextAction;

    public NewElementStep1(Project project, final Element element) {
        super(project);

        setTitle("Oracle WebCenter Sites New Element");
        webCenterSitesPluginModuleConfigurationData = WebCenterSitesPluginModuleConfigurationData.getInstance(project);
        newElementStep1 = this;
        this.project = project;

        setOKButtonText("Finish");

        init();
        this.element = element;
        preFetch(project);

    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }

    @Override
    protected Action[] createActions() {
        return this.getHelpId() == null ? (SystemInfo.isMac ? new Action[]{this.getCancelAction(), this.getNextAction(), this.getOKAction()} : new Action[]{this.getNextAction(), this.getOKAction(), this.getCancelAction()}) : (SystemInfo.isMac ? new Action[]{this.getHelpAction(), this.getCancelAction(), this.getNextAction(), this.getOKAction()} : new Action[]{this.getNextAction(), this.getOKAction(), this.getCancelAction(), this.getHelpAction()});
    }

    @NotNull
    protected Action getOKAction() {
        return new NewElementStep1.OkAction("Finish");
    }

    @NotNull
    protected Action getNextAction() {
        if (this.nextAction == null) {
            this.nextAction = new NewElementStep1.NextAction("Next");
        }
        return this.nextAction;
    }

    private void preFetch(final Project project) {
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Loading New Element Dialog") {
                                              public void run(@NotNull ProgressIndicator progressIndicator) {
                                                  LOG.info("Getting Sites for current user");
                                                  try {
                                                      String[] usersites = CSDPUtil.getUserSitenames();
                                                      for (String s : usersites) {
                                                          comboBoxSite.addItem(s);
                                                      }
                                                  } catch (SSOException exception) {
                                                      LOG.debug("SSOException", exception);
                                                      Notifications.Bus.notify(new Notification("intellij-wcs-plugin", "Error opening the New Template Window", "Is Oracle WebCenter Sites running?<br>" + exception.getLocalizedMessage(), NotificationType.ERROR), project);
                                                  }

                                              }

                                              public void onSuccess() {
                                                  getNextAction().setEnabled(false);
                                                  getOKAction().setEnabled(false);
                                                  textFieldErrorMessage.setVisible(false);
                                                  populateElement();
                                                  addListeners();
                                                  show();

                                              }
                                          }
        );
    }

    private void populateElement() {
        if (injected = element == null) {
            element = new Element();
            this.setDefaultProperties();
        } else {
            textFieldNewTemplateName.setText(element.getName());
            textFieldDescription.setText(element.getDescription());
            textFieldElementDescription.setText(element.getElementDescription());
            textFieldRootElement.setText(element.getRootElement());
            textFieldStoragePath.setText(element.getStoragePath());
            textFieldElementParameters.setText(element.getElementParameters());
            textFielAdditionalElementParameters.setText(element.getAdditionalElementParameters());
            deselectRadioButton(HTMLRadioButton);
            switch (element.getElementType()) {
                case XML_C:
                    XMLRadioButton.setSelected(true);
                    break;
                case JSP_C:
                    JSPRadioButton.setSelected(true);
                    break;
                case GROOVY:
                    groovyRadioButton.setSelected(true);
                    break;
                case HTML:
                    HTMLRadioButton.setSelected(true);
                    break;
                case EXISTING:
                    existingElementRadioButton.setSelected(true);
                    break;
                default:
                    JSPRadioButton.setSelected(true);
                    break;
            }
            validateElementPage();
        }
    }

    private void setDefaultProperties() {
        this.element.setSite(this.defaultSite());
        this.element.setElementType(ElementFileType.JSP_EC);
    }

    private String defaultSite() {
        return comboBoxSite.getSelectedItem().toString();
    }

    private void populateElementFields() {
        this.element.setName(textFieldNewTemplateName.getText());
        String rootElementName = CSDPUtil.buildRootElement(this.element.getName());
        String storagePathValue = CSDPUtil.buildStoragePath(rootElementName, this.element.getElementType());
        if (this.autoPopulateStoragePath) {
            textFieldStoragePath.setText(storagePathValue);
            this.element.setStoragePath(storagePathValue);
        }

        if (this.autoPopulateRootElement) {
            textFieldRootElement.setText(rootElementName);
            this.element.setRootElement(rootElementName);
        }

    }

    private void addListeners() {
        textFieldNewTemplateName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                populateElementFields();
                validateElementPage();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                populateElementFields();
                validateElementPage();
            }
        });
        comboBoxSite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                element.setSite(comboBoxSite.getSelectedItem().toString());
                validateElementPage();
            }
        });
        textFieldDescription.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                element.setDescription(textFieldDescription.getText());
                validateElementPage();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                element.setDescription(textFieldDescription.getText());
                validateElementPage();
            }
        });
        textFieldElementDescription.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                element.setElementDescription(textFieldElementDescription.getText());
                validateElementPage();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                element.setElementDescription(textFieldElementDescription.getText());
                validateElementPage();
            }
        });
        XMLRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (XMLRadioButton.isSelected()) {
                    deselectRadioButton(XMLRadioButton);
                    element.setElementType(ElementFileType.XML_C);
                }
            }
        });
        JSPRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (JSPRadioButton.isSelected()) {
                    deselectRadioButton(JSPRadioButton);
                    element.setElementType(ElementFileType.JSP_C);
                }
            }
        });
        groovyRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                if (groovyRadioButton.isSelected()) {
                    deselectRadioButton(groovyRadioButton);
                    element.setElementType(ElementFileType.GROOVY);
                }
            }
        });
        HTMLRadioButton.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (HTMLRadioButton.isSelected()) {
                    deselectRadioButton(HTMLRadioButton);
                    element.setElementType(ElementFileType.HTML);
                }
            }
        });
        textFieldRootElement.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                element.setRootElement(textFieldRootElement.getText());
                validateElementPage();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                element.setRootElement(textFieldRootElement.getText());
                validateElementPage();
            }
        });
        textFieldStoragePath.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                element.setStoragePath(textFieldStoragePath.getText());
                validateElementPage();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                element.setStoragePath(textFieldStoragePath.getText());
                validateElementPage();
            }
        });
        textFieldElementParameters.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                element.setElementParameters(textFieldElementParameters.getText());
                validateElementPage();
            }
        });
        textFieldElementParameters.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                element.setElementParameters(textFieldElementParameters.getText());
                validateElementPage();
            }
        });
        textFielAdditionalElementParameters.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                element.setAdditionalElementParameters(textFielAdditionalElementParameters.getText());
                validateElementPage();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                element.setAdditionalElementParameters(textFielAdditionalElementParameters.getText());
                validateElementPage();
            }
        });
    }

    private void deselectRadioButton(JRadioButton radioButton) {
        if (!radioButton.getText().equals("XML")) {
            XMLRadioButton.setSelected(false);
        }
        if (!radioButton.getText().equals("JSP")) {
            JSPRadioButton.setSelected(false);
        }
        if (!radioButton.getText().equals("Groovy")) {
            groovyRadioButton.setSelected(false);
        }
        if (!radioButton.getText().equals("HTML")) {
            this.HTMLRadioButton.setSelected(false);
        }
        if (!radioButton.getText().equals("Existing Element")) {
            existingElementRadioButton.setSelected(false);
        }
    }

    private void validateElementPage() {
        this.setNameError();
        if (this.nameNotBlank()) {
            this.setRootElementError();
            if (this.rootElementNotBlank()) {
                this.setStoragePathError();
                if (this.storagePathNotBlank()) {
                    getNextAction().setEnabled(true);
                    getOKAction().setEnabled(true);
                }
            }
        }
    }

    private void setErrorMessage(String s) {
        if (s != null) {
            textFieldErrorMessage.setText(s);
            textFieldErrorMessage.setVisible(true);
        } else {
            textFieldErrorMessage.setVisible(false);
        }
    }

    private void setStoragePathError() {
        this.setErrorMessage(this.storagePathNotBlank() ? null : "Please provide the storage path or re-select element type to restore");
        getNextAction().setEnabled(this.storagePathNotBlank());
        getOKAction().setEnabled(this.storagePathNotBlank());
    }

    private void setRootElementError() {
        this.setErrorMessage(this.rootElementNotBlank() ? null : "Please provide the root element or re-select element type to restore");
        getNextAction().setEnabled(this.rootElementNotBlank());
        getOKAction().setEnabled(this.rootElementNotBlank());
    }

    private void setNameError() {
        this.setErrorMessage(this.nameNotBlank() ? null : "Please provide a name");
        getNextAction().setEnabled(this.nameNotBlank());
        getOKAction().setEnabled(this.nameNotBlank());
    }

    private boolean rootElementNotBlank() {
        return StringUtils.isNotBlank(this.element.getRootElement());
    }

    private boolean storagePathNotBlank() {
        return StringUtils.isNotBlank(this.element.getStoragePath());
    }

    private boolean nameNotBlank() {
        return StringUtils.isNotBlank(textFieldNewTemplateName.getText());
    }

    protected class NextAction extends DialogWrapper.DialogWrapperAction {
        protected NextAction(@NotNull String name) {
            super(name);
        }

        @Override
        protected void doAction(ActionEvent actionEvent) {
            LOG.debug("Clicked Next Action");
            dispose();
            new NewElementStep2(project, element);
        }
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
}

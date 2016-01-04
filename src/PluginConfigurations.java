import com.fatwire.wem.sso.SSOException;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.module.ModuleComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import configurations.WebCenterSitesPluginModuleConfigurationData;
import csdt.CSDPUtil;
import csdt.TicketMaster;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by NB20308 on 28/12/2015.
 */
public class PluginConfigurations implements ModuleComponent, Configurable {
    private JComponent myComponent;
    private JPanel myPanel;
    private JTextField instanceTextField;
    private JButton browseButton;
    private JTextField moduleNameTextField;
    private JTextField usernameTextField;
    private JTextField passwordTextField;
    private JTextField hostnameTextField;
    private JTextField portTextField;
    private JTextField webContextPathTextField;
    private JTextField sitesContextPathTextField;
    private JTextField workspaceTextField;
    private JTextField datastoreTextField;
    private JCheckBox enabledCheckBox;
    private JLabel statusJLabel;
    private WebCenterSitesPluginModuleConfigurationData webCenterSitesPluginModuleConfigurationData;


    @Nls
    @Override
    public String getDisplayName() {
        return "Oracle WebCenter Sites";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "preferences.lookFeel";
    }

    @Nullable
    @Override
    public JComponent createComponent() {

        System.out.println("createComponent");

        enabledCheckBox.setSelected(WebCenterSitesPluginModuleConfigurationData.isPluginActive());
        instanceTextField.setText(WebCenterSitesPluginModuleConfigurationData.getInstance());
        moduleNameTextField.setText(WebCenterSitesPluginModuleConfigurationData.getModuleName());
        usernameTextField.setText(WebCenterSitesPluginModuleConfigurationData.getUsername());
        passwordTextField.setText(WebCenterSitesPluginModuleConfigurationData.getPassword());
        hostnameTextField.setText(WebCenterSitesPluginModuleConfigurationData.getHostName());
        portTextField.setText(WebCenterSitesPluginModuleConfigurationData.getPort());
        webContextPathTextField.setText(WebCenterSitesPluginModuleConfigurationData.getWebContextPath());
        sitesContextPathTextField.setText(WebCenterSitesPluginModuleConfigurationData.getSitesContextPath());
        workspaceTextField.setText(WebCenterSitesPluginModuleConfigurationData.getWorkspace());
        datastoreTextField.setText(WebCenterSitesPluginModuleConfigurationData.getDataStoreName());

        // Add listener to the Default Font button
        /*MyButtonListener actionListener = new MyButtonListener();
        actionListener.myFontName = myFontName;
        actionListener.myFontSize = myFontSize;
        MyDefaultFontButton.addActionListener(actionListener);
        // Define a set of possible values for combo boxes.
        UISettings settings = UISettings.getInstance();
        myFontName.setModel(new DefaultComboBoxModel(UIUtil.getValidFontNames(false)));
        myFontSize.setModel(new DefaultComboBoxModel(UIUtil.getStandardFontSizes()));
        myFontName.setSelectedItem(settings.FONT_FACE);
        myFontSize.setSelectedItem(String.valueOf(settings.FONT_SIZE));*/


        myComponent = (JComponent) myPanel;
        return myComponent;

    }

    @Override
    public boolean isModified() {
        System.out.println("Settings Changed");
        usernameTextField.setEnabled(enabledCheckBox.isSelected());
        passwordTextField.setEnabled(enabledCheckBox.isSelected());
        instanceTextField.setEnabled(enabledCheckBox.isSelected());
        moduleNameTextField.setEnabled(enabledCheckBox.isSelected());
        sitesContextPathTextField.setEnabled(enabledCheckBox.isSelected());
        workspaceTextField.setEnabled(enabledCheckBox.isSelected());
        datastoreTextField.setEnabled(enabledCheckBox.isSelected());
        if (enabledCheckBox.isSelected()) {
            if (instanceTextField.getText().isEmpty()) {
                statusJLabel.setText("The Instance field cannot be empty.");
                WebCenterSitesPluginModuleConfigurationData.setConfigValid(false);
            } else if (moduleNameTextField.getText().isEmpty()) {
                statusJLabel.setText("The Module name field cannot be empty.");
                WebCenterSitesPluginModuleConfigurationData.setConfigValid(false);
            } else if (usernameTextField.getText().isEmpty()) {
                statusJLabel.setText("The Username field cannot be empty.");
                WebCenterSitesPluginModuleConfigurationData.setConfigValid(false);
            } else if (passwordTextField.getText().isEmpty()) {
                statusJLabel.setText("The Password field cannot be empty.");
                WebCenterSitesPluginModuleConfigurationData.setConfigValid(false);
            } else if (sitesContextPathTextField.getText().isEmpty()) {
                statusJLabel.setText("The Sites Context Path field cannot be empty.");
                WebCenterSitesPluginModuleConfigurationData.setConfigValid(false);
            } else if (workspaceTextField.getText().isEmpty()) {
                statusJLabel.setText("The Workspace field cannot be empty.");
                WebCenterSitesPluginModuleConfigurationData.setConfigValid(false);
            } else if (datastoreTextField.getText().isEmpty()) {
                statusJLabel.setText("The DataStore field cannot be empty.");
                WebCenterSitesPluginModuleConfigurationData.setConfigValid(false);
            } else {
                statusJLabel.setText("Checking configurations...");
                PropertiesComponent.getInstance().setValue("wcs-plugin-active", true);
                PropertiesComponent.getInstance().setValue("wcs-instance", instanceTextField.getText());
                PropertiesComponent.getInstance().setValue("wcs-module-name", moduleNameTextField.getText());
                PropertiesComponent.getInstance().setValue("wcs-username", usernameTextField.getText());
                PropertiesComponent.getInstance().setValue("wcs-password", passwordTextField.getText());
                PropertiesComponent.getInstance().setValue("wcs-context-path", sitesContextPathTextField.getText());
                PropertiesComponent.getInstance().setValue("wcs-workspace", workspaceTextField.getText());
                PropertiesComponent.getInstance().setValue("wcs-datastore", datastoreTextField.getText());
                    WebCenterSitesPluginModuleConfigurationData.setConfigValid(checkConfigurations());
            }
        }

        return true;
    }

    private boolean checkConfigurations() {
        try {
            String t = usernameTextField.getText();
            String passStr = passwordTextField.getText();
            String hostname = hostnameTextField.getText();
            String port = portTextField.getText();
            String webContext = webContextPathTextField.getText();
            if (null != t && t.trim().length() != 0 && null != passStr && passStr.trim().length() != 0) {
                String baseUrl = CSDPUtil.getBaseUrl(hostname, port, webContext);
                String csUrl = baseUrl + "/" + "ContentServer" + "?" + "pagename" + "=" + "OpenMarket/Xcelerate/PrologActions/Publish/csdt/CSDTService";
                String query = CSDPUtil.buildQuery(new String[][]{{"username", t}, {"password", passStr}, {"_charset_", "UTF-8"}, {"assettype", "SiteEntry"}, {"command", "listcs"}});
                String data = "&" + query;
                String response = "";

                try {
                    TicketMaster.TicketParams ex = new TicketMaster.TicketParams();
                    ex.setUrl(baseUrl);
                    ex.setUsername(t);
                    ex.setPassword(passStr);
                    HttpURLConnection con = (HttpURLConnection) (new URL(csUrl)).openConnection();
                    con.setConnectTimeout(1500);
                    con.setDoOutput(true);
                    con.setRequestProperty("X-CSRF-Token", TicketMaster.getTicket(ex));
                    con.connect();
                    OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                    writer.write(data);
                    writer.flush();
                    writer.close();
                    response = CSDPUtil.generateResponse(con.getInputStream());
                } catch (SSOException var11) {
                    statusJLabel.setText("Connect error: " + var11.getMessage());
                    return false;
                } catch (Exception var12) {
                    statusJLabel.setText("Connection failed.\nIs Sites down?"+var12.getMessage());
                    return false;
                }

                if (null != response && response.contains("Success")) {
                    statusJLabel.setText("Web Center Sites is running");
                    return true;
                } else {
                    response = response == null ? "" : response.trim();
                    statusJLabel.setText("Connect error:\n" + response);
                    return false;
                }
            } else {
                statusJLabel.setText("Expecting login credentials ...");
            }
        } catch (Throwable var13) {
            statusJLabel.setText("Sites Ping status:\nUnknown error");
        }
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {
        System.out.println("Clicked OK");
        if (WebCenterSitesPluginModuleConfigurationData.isConfigValid()) {
            WebCenterSitesPluginModuleConfigurationData.setPluginActive(enabledCheckBox.isSelected());
            //configurations.WebCenterSitesPluginModuleConfigurationData.setPluginActive();
            WebCenterSitesPluginModuleConfigurationData.setInstance(instanceTextField.getText());
            WebCenterSitesPluginModuleConfigurationData.setModuleName(moduleNameTextField.getText());
            WebCenterSitesPluginModuleConfigurationData.setUsername(usernameTextField.getText());
            WebCenterSitesPluginModuleConfigurationData.setPassword(passwordTextField.getText());
            WebCenterSitesPluginModuleConfigurationData.setContextPath(sitesContextPathTextField.getText());
            WebCenterSitesPluginModuleConfigurationData.setWorkspace(workspaceTextField.getText());
            WebCenterSitesPluginModuleConfigurationData.setDataStoreName(datastoreTextField.getText());
        }
        /*UISettings settings = UISettings.getInstance();
        LafManager lafManager = LafManager.getInstance();
        String _fontFace = (String) myFontName.getSelectedItem();
        String _fontSize_STR = (String) myFontSize.getSelectedItem();
        int _fontSize = Integer.parseInt(_fontSize_STR);

        if (_fontSize != settings.FONT_SIZE || !settings.FONT_FACE.equals(_fontFace)) {
            settings.FONT_SIZE = _fontSize;
            settings.FONT_FACE = _fontFace;
            settings.fireUISettingsChanged();
            lafManager.updateUI();
        }*/
    }

    @Override
    public void reset() {
        System.out.println("Clicked Cancel");
    }

    @Override
    public void disposeUIResources() {
        System.out.println("Should dispose all resources");

    }

    @Override
    public void projectOpened() {
        System.out.println("projectOpened");
    }

    @Override
    public void projectClosed() {
        System.out.println("projectClosed");
    }

    @Override
    public void moduleAdded() {
        System.out.println("moduleAdded");
    }

    @Override
    public void initComponent() {
        System.out.println("initComponent " + this.getClass());
    }

    @Override
    public void disposeComponent() {
        System.out.println("disposeComponent");

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "getComponentNamee";
    }
}

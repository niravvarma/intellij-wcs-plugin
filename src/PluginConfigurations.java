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

        return myPanel;

    }

    @Override
    public boolean isModified() {
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
    }

    @Override
    public void reset() {
    }

    @Override
    public void disposeUIResources() {
        this.disposeComponent();
    }

    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void moduleAdded() {

    }

    @Override
    public void initComponent() {

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "IntelliJ WebCenter Sites Addon";
    }
}

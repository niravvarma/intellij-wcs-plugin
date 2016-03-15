package com.intellij;

import com.intellij.configurations.WebCenterSitesPluginModuleConfigurationData;
import com.intellij.csdt.CSDPUtil;
import com.intellij.csdt.Preferences;
import com.intellij.openapi.components.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by NB20308 on 28/12/2015.
 */

@State(
        name = "intellij-wcs-plugin-configuration",
        storages = {
                @Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
                @Storage(id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/intellij-wcs-plugin.xml", scheme = StorageScheme.DIRECTORY_BASED)
        }
)
public class WebCenterSitesPlugin implements ProjectComponent, PersistentStateComponent<Element> {

    private static Logger LOG = Logger.getInstance(WebCenterSitesPlugin.class);
    private final Project project;
    private PluginConfigurations form;
    private WebCenterSitesPluginModuleConfigurationData webCenterSitesPluginModuleConfigurationData;

    public WebCenterSitesPlugin(Project project) {
        webCenterSitesPluginModuleConfigurationData = WebCenterSitesPluginModuleConfigurationData.getInstance(project);
        this.project = project;
        Preferences.setProject(project);

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
        return "Intellij WCS Plugin";
    }

    @Override
    public void projectOpened() {
    }

    @Override
    public void projectClosed() {
    }

    @Nullable
    @Override
    public Element getState() {
        LOG.debug("Saving addon configuration file");

        final Element element = new Element("intellij-addon-plugin");

        // meta configuration information
        final Element stateElement = new Element("config");
        stateElement.setAttribute("wcs-plugin-active", String.valueOf(webCenterSitesPluginModuleConfigurationData.isPluginActive()));
        if (webCenterSitesPluginModuleConfigurationData.isPluginActive()) {
            if (webCenterSitesPluginModuleConfigurationData.getInstance() != null) {
                stateElement.setAttribute("wcs-instance", webCenterSitesPluginModuleConfigurationData.getInstance());
            }
            if (webCenterSitesPluginModuleConfigurationData.getModuleName() != null) {
                stateElement.setAttribute("wcs-module-name", webCenterSitesPluginModuleConfigurationData.getModuleName());
            }
            if (webCenterSitesPluginModuleConfigurationData.getUsername() != null) {
                stateElement.setAttribute("wcs-username", webCenterSitesPluginModuleConfigurationData.getUsername());
            }
            if (webCenterSitesPluginModuleConfigurationData.getPassword() != null) {
                stateElement.setAttribute("wcs-password", webCenterSitesPluginModuleConfigurationData.getPassword());
            }
            if (webCenterSitesPluginModuleConfigurationData.getContextPath() != null) {
                stateElement.setAttribute("wcs-context-path", webCenterSitesPluginModuleConfigurationData.getContextPath());
            }
            if (webCenterSitesPluginModuleConfigurationData.getWorkspace() != null) {
                stateElement.setAttribute("wcs-workspace", webCenterSitesPluginModuleConfigurationData.getWorkspace());
            }
            if (webCenterSitesPluginModuleConfigurationData.getDataStoreName() != null) {
                stateElement.setAttribute("wcs-datastore", webCenterSitesPluginModuleConfigurationData.getDataStoreName());
            }
        }
        element.addContent(stateElement);

        return element;
    }

    @Override
    public void loadState(Element element) {
        LOG.debug("Loading addon configuration file");

        // meta configuration information
        Element stateElement = element.getChild("config");
        // version not needed yet
        String pluginActive = stateElement.getAttributeValue("wcs-plugin-active");
        String instance = stateElement.getAttributeValue("wcs-instance");
        String moduleName = stateElement.getAttributeValue("wcs-module-name");
        String username = stateElement.getAttributeValue("wcs-username");
        String password = stateElement.getAttributeValue("wcs-password");
        String contextPath = stateElement.getAttributeValue("wcs-context-path");
        String workspace = stateElement.getAttributeValue("wcs-workspace");
        String datastore = stateElement.getAttributeValue("wcs-datastore");
        LOG.debug("pluginActive: " + pluginActive +
                "\ninstance: " + instance +
                "\nmoduleName: " + moduleName +
                "\nusername: " + username +
                "\npassword: " + password +
                "\ncontextPath: " + contextPath +
                "\nworkspace: " + workspace +
                "\ndatastore: " + datastore
        );

        webCenterSitesPluginModuleConfigurationData.setPluginActive(Boolean.valueOf(pluginActive));
        if (webCenterSitesPluginModuleConfigurationData.isPluginActive()) {
            webCenterSitesPluginModuleConfigurationData.setInstance(instance);
            webCenterSitesPluginModuleConfigurationData.setModuleName(moduleName);
            webCenterSitesPluginModuleConfigurationData.setUsername(username);
            webCenterSitesPluginModuleConfigurationData.setPassword(password);
            webCenterSitesPluginModuleConfigurationData.setContextPath(contextPath);
            webCenterSitesPluginModuleConfigurationData.setWorkspace(workspace);
            webCenterSitesPluginModuleConfigurationData.setDataStoreName(datastore);
            CSDPUtil.setConfigurationData(webCenterSitesPluginModuleConfigurationData);
        }
    }
}

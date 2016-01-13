import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import configurations.WebCenterSitesPluginModuleConfigurationData;
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

    public WebCenterSitesPlugin(Project project) {
        this.project = project;
        csdt.Preferences.setProject(project);
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
        stateElement.setAttribute("wcs-plugin-active", String.valueOf(WebCenterSitesPluginModuleConfigurationData.isPluginActive()));
        if (PropertiesComponent.getInstance().getBoolean("wcs-plugin-active")) {
            if (PropertiesComponent.getInstance().getValue("wcs-instance") != null) {
                stateElement.setAttribute("wcs-instance", PropertiesComponent.getInstance().getValue("wcs-instance"));
            }
            if (PropertiesComponent.getInstance().getValue("wcs-module-name") != null) {
                stateElement.setAttribute("wcs-module-name", PropertiesComponent.getInstance().getValue("wcs-module-name"));
            }
            if (PropertiesComponent.getInstance().getValue("wcs-username") != null) {
                stateElement.setAttribute("wcs-username", PropertiesComponent.getInstance().getValue("wcs-username"));
            }
            if (PropertiesComponent.getInstance().getValue("wcs-password") != null) {
                stateElement.setAttribute("wcs-password", PropertiesComponent.getInstance().getValue("wcs-password"));
            }
            if (PropertiesComponent.getInstance().getValue("wcs-context-path") != null) {
                stateElement.setAttribute("wcs-context-path", PropertiesComponent.getInstance().getValue("wcs-context-path"));
            }
            if (PropertiesComponent.getInstance().getValue("wcs-workspace") != null) {
                stateElement.setAttribute("wcs-workspace", PropertiesComponent.getInstance().getValue("wcs-workspace"));
            }
            if (PropertiesComponent.getInstance().getValue("wcs-datastore") != null) {
                stateElement.setAttribute("wcs-datastore", PropertiesComponent.getInstance().getValue("wcs-datastore"));
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

        WebCenterSitesPluginModuleConfigurationData.setPluginActive(Boolean.valueOf(pluginActive));
        if (WebCenterSitesPluginModuleConfigurationData.isPluginActive()) {
            WebCenterSitesPluginModuleConfigurationData.setInstance(instance);
            WebCenterSitesPluginModuleConfigurationData.setModuleName(moduleName);
            WebCenterSitesPluginModuleConfigurationData.setUsername(username);
            WebCenterSitesPluginModuleConfigurationData.setPassword(password);
            WebCenterSitesPluginModuleConfigurationData.setContextPath(contextPath);
            WebCenterSitesPluginModuleConfigurationData.setWorkspace(workspace);
            WebCenterSitesPluginModuleConfigurationData.setDataStoreName(datastore);
        }
    }
}

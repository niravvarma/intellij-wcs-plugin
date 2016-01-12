import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.components.*;
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

    private final Project project;

    private SessionState sessionState;
    private PluginConfigurations form;

    public WebCenterSitesPlugin(Project project) {
        this.project = project;
        csdt.Preferences.setProject(project);
        sessionState = new SessionState();
    }

    public SessionState getSessionState() {
        return sessionState;
    }


    @Override
    public void initComponent() {

        /*WebCenterSitesPluginModuleConfigurationData.setPluginActive(PropertiesComponent.getInstance().getBoolean("wcs-plugin-active"));
        if (PropertiesComponent.getInstance().getBoolean("wcs-plugin-active")) {
            WebCenterSitesPluginModuleConfigurationData.setInstance(PropertiesComponent.getInstance().getValue("wcs-instance"));
            WebCenterSitesPluginModuleConfigurationData.setModuleName(PropertiesComponent.getInstance().getValue("wcs-module-name"));
            WebCenterSitesPluginModuleConfigurationData.setUsername(PropertiesComponent.getInstance().getValue("wcs-username"));
            WebCenterSitesPluginModuleConfigurationData.setPassword(PropertiesComponent.getInstance().getValue("wcs-password"));
            WebCenterSitesPluginModuleConfigurationData.setContextPath(PropertiesComponent.getInstance().getValue("wcs-context-path"));
            WebCenterSitesPluginModuleConfigurationData.setWorkspace(PropertiesComponent.getInstance().getValue("wcs-workspace"));
            WebCenterSitesPluginModuleConfigurationData.setDataStoreName(PropertiesComponent.getInstance().getValue("wcs-datastore"));
        }*/
    }

    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "WebCenterSitesPlugin getComponentName";
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
        System.out.println("saving state");
        //LOG.debug("Saving state");

        final Element element = new Element("intellij-addon-plugin");

        // meta configuration information
        final Element stateElement = new Element("config");
        stateElement.setAttribute("wcs-plugin-active", String.valueOf(WebCenterSitesPluginModuleConfigurationData.isPluginActive()));
        if(PropertiesComponent.getInstance().getBoolean("wcs-plugin-active")) {
            if (PropertiesComponent.getInstance().getValue("wcs-instance") != null){
                stateElement.setAttribute("wcs-instance", PropertiesComponent.getInstance().getValue("wcs-instance"));
            }
            if (PropertiesComponent.getInstance().getValue("wcs-module-name") != null){
                stateElement.setAttribute("wcs-module-name", PropertiesComponent.getInstance().getValue("wcs-module-name"));
            }
            if (PropertiesComponent.getInstance().getValue("wcs-username") != null){
                stateElement.setAttribute("wcs-username", PropertiesComponent.getInstance().getValue("wcs-username"));
            }
            if (PropertiesComponent.getInstance().getValue("wcs-password") != null){
                stateElement.setAttribute("wcs-password", PropertiesComponent.getInstance().getValue("wcs-password"));
            }
            if (PropertiesComponent.getInstance().getValue("wcs-context-path") != null){
                stateElement.setAttribute("wcs-context-path", PropertiesComponent.getInstance().getValue("wcs-context-path"));
            }
            if (PropertiesComponent.getInstance().getValue("wcs-workspace") != null){
                stateElement.setAttribute("wcs-workspace", PropertiesComponent.getInstance().getValue("wcs-workspace"));
            }
            if (PropertiesComponent.getInstance().getValue("wcs-datastore") != null){
                stateElement.setAttribute("wcs-datastore", PropertiesComponent.getInstance().getValue("wcs-datastore"));
            }
        }
        element.addContent(stateElement);

        return element;
    }

    @Override
    public void loadState(Element element) {
        System.out.println("Loading state");

        sessionState = new SessionState();

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
        System.out.println("pluginActive: " + pluginActive+
                           "\ninstance: " + instance+
                "\nmoduleName: " + moduleName+
                "\nusername: " + username+
                "\npassword: " + password+
                "\ncontextPath: " + contextPath+
                "\nworkspace: " + workspace+
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

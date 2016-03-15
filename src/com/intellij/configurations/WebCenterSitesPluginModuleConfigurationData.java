package com.intellij.configurations;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import java.util.HashMap;

/**
 * Created by NB20308 on 28/12/2015.
 */
public class WebCenterSitesPluginModuleConfigurationData {

    public String workspace;
    public String username;
    public String password;
    public String port;
    public String host;
    public String contextPath;
    public String dataStoreName;
    private String webContextPath;
    private String moduleName;
    private boolean pluginActive;
    private boolean configValid;
    private String instance;

    public static WebCenterSitesPluginModuleConfigurationData getInstance(Project project) {
        return ServiceManager.getService(project, WebCenterSitesPluginModuleConfigurationData.class);

    }

    public static HashMap<String, String> processInstanceString(String instance) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (instance.toLowerCase().startsWith("http://")) {
            String _aux = instance.replace("http://", "");
            String[] splittedByDots = _aux.split(":");
            String[] splittedByBar = splittedByDots[1].split("/");
            hashMap.put("host", splittedByDots[0]);
            hashMap.put("port", splittedByBar[0]);
            hashMap.put("webcontextpath", splittedByBar[1]);

        } else if (instance.toLowerCase().startsWith("https://")) {
            String _aux = instance.replace("https://", "");
            String[] splittedByDots = _aux.split(":");
            String[] splittedByBar = splittedByDots[1].split("/");
            hashMap.put("host", splittedByDots[0]);
            hashMap.put("port", splittedByBar[0]);
            hashMap.put("webcontextpath", splittedByBar[1]);
        }
        return hashMap;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHostName() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getDataStoreName() {
        return dataStoreName;
    }

    public void setDataStoreName(String dataStoreName) {
        this.dataStoreName = dataStoreName;
    }

    public String getWebContextPath() {
        return webContextPath;
    }

    public void setWebContextPath(String webContextPath) {
        this.webContextPath = webContextPath;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        HashMap<String, String> processedInstanceString = processInstanceString(instance);
        setHost(processedInstanceString.get("host"));
        setPort(processedInstanceString.get("port"));
        setWebContextPath(processedInstanceString.get("webcontextpath"));
        this.instance = instance;

    }

    public boolean isPluginActive() {
        return pluginActive;
    }

    public void setPluginActive(boolean pluginActive) {
        this.pluginActive = pluginActive;
    }

    public boolean isConfigValid() {
        return configValid;
    }

    public void setConfigValid(boolean configValid) {
        this.configValid = configValid;
    }
}

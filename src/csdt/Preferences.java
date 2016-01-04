package csdt;

/**
 * Created by NB20308 on 22/12/2015.
 */
public  class Preferences {

    public static String workspace;
    public static String username;
    public static String password;
    public static String port;
    public static String host;
    public static String contextPath;
    public static String dataStoreName;

    public static void setDataStoreName(String dataStoreName) {
        Preferences.dataStoreName = dataStoreName;
    }

    public static String getDataStoreName() {

        return dataStoreName;
    }

    public static void setWorkspace(String workspace) {
        Preferences.workspace = workspace;
    }

    public static void setUsername(String username) {
        Preferences.username = username;
    }

    public static void setPassword(String password) {
        Preferences.password = password;
    }

    public static void setPort(String port) {
        Preferences.port = port;
    }

    public static void setHost(String host) {
        Preferences.host = host;
    }

    public static void setContextPath(String contextPath) {
        Preferences.contextPath = contextPath;
    }

    public static String getWorkspace() {

        return workspace;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static String getPort() {
        return port;
    }

    public static String getHost() {
        return host;
    }

    public static String getContextPath() {
        return contextPath;
    }
}

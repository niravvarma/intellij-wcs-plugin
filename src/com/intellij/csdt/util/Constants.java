package com.intellij.csdt.util;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.util.regex.Pattern;

public final class Constants {
    public static final String CS_CONTEXT_DIR = "CS_CONTEXTDIR";
    public static final String REMOTE = "fw.mode";
    public static final String CS_INSTALLDIR = "CS_INSTALLDIR";
    public static final String LOG_FILE_PATH = "LOG_FILE_PATH";
    public static final String CSIP = "fw.csip";
    public static final String CS_CONTEXTPATH = "fw.cscontextPath";
    public static final String CSPORT = "fw.csport";
    public static final String USERNAME = "fw.username";
    public static final String PASSWORD = "fw.password";
    public static final String PROJNAME = "fw.projname";
    public static final String DATASTORENAME = "fw.datastorename";
    public static final String WORKSPACE = "fw.workspace";
    public static final String FT_INI = "futuretense.ini";
    public static final String NAME = "name";
    public static final String ROOT_ELEMENT = "rootelement";
    public static final String SUB_TYPE = "subtype";
    public static final String DESCRIPTION = "description";
    public static final String ELEMENT = "&element";
    public static final String _ELEMENT = "element";
    public static final String ELEMENT_NAME = "elementname";
    public static final String ELEMENT_DESCRIPTION = "elementdescription";
    public static final String URL = "url";
    public static final String URL_FILE = "url_file";
    public static final String URL_FOLDER = "url_folder";
    public static final String RESDETAILS1 = "resdetails1";
    public static final String RESDETAILS2 = "resdetails2";
    public static final String APPLICABLE_SUB_TYPES = "applicablesubtypes";
    public static final String ACL = "acl";
    public static final String PAGE_CRITERIA = "pagecriteria";
    public static final String PAGELETONLY = "pageletonly";
    public static final String COMPOSITION = "&composition";
    public static final String T_TYPE = "ttype";
    public static final String ASSOCIATION_NAMED = "&Association-named";
    public static final String CALLED = "&Called";
    public static final String ARGUMENTS = "arguments";
    public static final String MAPPING = "mapping";
    public static final String USEEXISTING = "useExistingElementCatalog";
    public static final String PUB_ID = "pubid";
    public static final String DATA_STORE = "datastore";
    public static final String cscacheinfo = "cscacheinfo";
    public static final String sscacheinfo = "sscacheinfo";
    public static final String URL_SPEC = "urlspec";
    public static final String PAGENAME = "pagename1";
    public static final String TEMPLATE = "&template";
    public static final String _TEMPLATE = "template";
    public static final String CSELEMENT_ID = "cselement_id";
    public static final String CS_WRAPPER = "cs_wrapper";
    public static final String USE_EXISTING_SITE_CATALOG = "useExistingSiteCatalog";
    public static final String CONFIG_PATH = "/cs/Config";
    public static final String PRODUCT_NAME = "WebCenter Sites";
    public static final String NEW_LINE = System.getProperty("line.separator");
    public static final String TOKEN_SLASH = "/";
    public static final String TOKEN_QUESTION = "?";
    public static final String TOKEN_EQUALS = "=";
    public static final String TOKEN_AMPERSAND = "&";
    public static final String TOKEN_UNDERSCORE = "_";
    public static final String TOKEN_ASTERISK = "*";
    public static final String TOKEN_COMMA = ",";
    public static final String TOKEN_TILDE = "~";
    public static final String TOKEN_COLON = ":";
    public static final String TOKEN_HASH = "#";
    public static final String TYPE_DEVICE_GROUP = "DeviceGroup";
    public static final String TYPE_TEMPLATE = "Template";
    public static final String TYPE_ELEMENT = "CSElement";
    public static final String TYPE_SITE_ENTRY = "SiteEntry";
    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_DESCRIPTION = "description";
    public static final String ATTRIBUTE_DEVICEGROUPSUFFIX = "devicegroupsuffix";
    public static final String ATTRIBUTE_ROOTELEMENT = "rootelement";
    public static final String LOGIN_PAGE = "OpenMarket/Xcelerate/UIFramework/LoginPage";
    public static final String CAS_INFO = "fatwire/wem/sso/casInfo";
    public static final String TICKET_VALIDATOR = "fatwire/wem/ui/Ping";
    public static final String TEMPLATE_SAVE = "OpenMarket/Xcelerate/PrologActions/Publish/csdt/TemplateSave";
    public static final String ELEMENT_SAVE = "OpenMarket/Xcelerate/PrologActions/Publish/csdt/CSElementSave";
    public static final String ELEMENT_CATALOG_SAVE = "OpenMarket/Xcelerate/PrologActions/Publish/csdt/ElementCatalogSave";
    public static final String SITE_CATALOG_SAVE = "OpenMarket/Xcelerate/PrologActions/Publish/csdt/SiteCatalogSave";
    public static final String SITE_ENTRY_SAVE = "OpenMarket/Xcelerate/PrologActions/Publish/csdt/SiteEntrySave";
    public static final String GET_ELEMENTS = "OpenMarket/Xcelerate/PrologActions/Publish/csdt/GetCSElements";
    public static final String SHARE_ASSET = "OpenMarket/Xcelerate/PrologActions/Publish/csdt/ShareAsset";
    public static final String CSDT_SERVICE = "OpenMarket/Xcelerate/PrologActions/Publish/csdt/CSDTService";
    public static final String STREAM = "OpenMarket/Xcelerate/PrologActions/Publish/csdt/Stream";
    public static final String SITEENTRY_TEMPLATE = "OpenMarket/SiteEntryTemplate";
    public static final String DEFAULT_HTTP_PORT = "80";
    public static final String DEFAULT_DEVICE_GROUP = "Desktop";
    public static final String DEFAULT_CAN_APPLY_TO_ANY_ASSET_TYPE = "Can apply to any asset type";
    public static final String DEFAULT_ANY = "Any";
    public static final String DEFAULT_CACHE_CRITERIA_TEMPLATE = "c,cid,context,p,d,rendermode,site,sitepfx,ft_ss,deviceid";
    public static final String DEFAULT_CACHE_CRITERIA_SITE_CATALOG_ENTRY = "c,cid,context,p,d,rendermode,site,sitepfx,ft_ss";
    public static final String DEFAULT_CACHE_CRITERIA_SITE_ENTRY = "rendermode,site,sitepfx,seid";
    public static final String DEFAULT_RENDERMODE = "live";
    public static final String DEFAULT_LOCATION_RESOURCES = "resources/";
    public static final String DEFAULT_LOCATION_SOURCE = "src";
    public static final String DEFAULT_LOCATION_JSP_ROOT = "/src/jsp/cs_deployed";
    public static final String DEFAULT_LOCATION_ELEMENT_ROOT = "/src/_metadata/ELEMENTS";
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final String SEARCH_OP_EQUALS = "equals";
    public static final Pattern REGEX_STRIP_LEADING_TRAILING_SLASH = Pattern.compile("\\b/*.+/*\\b");
    public static final Pattern REGEX_RULE_NAME_EXTRACTOR = Pattern.compile("(.*?)_?(?!.*_(?=(?:,\\d*)?))(.+?)((?:,\\d*)?\\..+)");
    public static final Pattern REGEX_CACHING_PARSER = Pattern.compile("((?:true)|(?:false)),~(\\d+)");
    public static final Pattern REGEX_FILE_EXTENSION_EXTRACTOR = Pattern.compile(".*(\\..+)");
    public static final String REGEX_FILE_PATH_SANITIZER = "/\\s*/+";
    public static final Pattern REGEX_PAGE_NAME_PARSER = Pattern.compile("([\\w]+)/(?:([\\w]+)/)?([\\w]+)");
    public static final String MESSAGE_SYNC = "Please synchronize your workspace with the WebCenter Sites instance to fix this problem";
    public static final String ERROR_TITLE_PREFERENCES_CONFIGURATION = "Preferences Configuration Error";
    public static final String ERROR_TITLE_COMMUNICATION = "Communication Error";
    public static final String ERROR_TITLE_AUTHENTICATION = "Authentication Error";
    public static final String ERROR_TITLE_SAVE = "Save Error";
    public static final String ERROR_TITLE_INSUFFICIENT_PRIVILEGES = "Insufficient Privileges";
    public static final String ERROR_TITLE_LOGIN = "Login Error";
    public static final String ERROR_INSUFFICIENT_PRIVILEGES = "You do not have sufficient privileges to perform this operation";
    public static final String ERROR_CONFIGURE_PREFERENCES = "Please configure the preferences for WebCenter Sites";
    public static final String ERROR_CANNOT_GET_MULTITICKET = "Could not get a ticket to perform this operation";
    public static final String ERROR_CANNOT_CLOSE = "Could not close resource";
    public static final String ERROR_TEMPLATE_EXISTS = "A template by this name already exists in WebCenter Sites. Please synchronize your workspace with the WebCenter Sites instance to fix this problem";
    public static final String ERROR_SITE_DOES_NOT_EXIST = "The following site does not exist in WebCenter Sites: ";
    public static final String ERROR_ASSETTYPE_DOES_NOT_EXIST = "The following asset type does not exist in WebCenter Sites: ";
    public static final String ERROR_CANNOT_READ_SITEINFO = "Could not read the site information from WebCenter Sites:";
    public static final String ERROR_REVIEW_CREDENTIALS = "Please review the credentials configured in your preferences";
    public static final String ERROR_TEMPLATE_SAVE = "Error while saving Template";
    public static final String ERROR_ELEMENT_SAVE = "Error while saving Element";
    public static final String ERROR_SITE_ENTRY_SAVE = "Error while saving Site Entry";
    public static final String ERROR_ELEMENT_CATALOG_ENTRY_SAVE = "Error while saving Element Catalog Entry";
    public static final String ERROR_SITE_CATALOG_ENTRY_SAVE = "Error while saving Site Catalog Entry";
    public static final String ERROR_ASSET_SHARE = "Error while sharing Asset";
    public static final String ERROR_FILE_NOT_FOUND = "File not found: ";
    public static final String SCHEME_HTTP = "http://";
    public static final String METHOD_GET = "GET";
    public static final String SERVLET_CONTENTSERVER = "ContentServer";
    public static final String SERVLET_SATELLITE = "Satellite";
    public static final String SERVLET_REST = "REST";
    public static final String PATH_WELCOME_PAGE = "wem/fatwire/wem/Welcome";
    public static final String PATH_USERS = "users";
    public static final String PATH_TYPES = "types";
    public static final String PATH_SITES = "sites";
    public static final String PATH_ACLS = "acls";
    public static final String PATH_SEARCH = "search";
    public static final String SEARCHENGINE_DBBASIC = "dbbasic";
    public static final String PARAM_USERNAME = "username";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_PAGENAME = "pagename";
    public static final String PARAM_MULTITICKET = "multiticket";
    public static final String PARAM_SEARCHENGINE = "searchengine";
    public static final String PARAM_FIELDS = "fields";
    public static final String PARAM_RENDERMODE = "rendermode";
    public static final String PARAM_SITE = "site";
    public static final String PARAM_CHARSET = "_charset_";
    public static final String PARAM_ASSETTYPE = "assettype";
    public static final String PARAM_COMMAND = "command";
    public static final String COMMAND_EXPORT = "export";
    public static final String COMMAND_IMPORT = "import";
    public static final String COMMAND_LIST = "listds";
    public static final String COMMAND_PEEK = "listcs";
    public static final String HEADER_X_CSRF_TOKEN = "X-CSRF-Token";
    public static final String HEADER_PRAGMA = "Pragma";
    public static final String HEADER_VALUE_AUTH_REDIRECT_FALSE = "auth-redirect=false";
    public static final String TITLE_NEW_TEMPLATE = "New Template";
    public static final String TITLE_NEW_ELEMENT = "New Element";
    public static final String TITLE_NEW_SITE_ENTRY = "New Site Entry";
    public static final String TITLE_NEW_ELEMENT_CATALOG_ENTRY = "New Element Catalog Entry";
    public static final String TITLE_NEW_SITE_CATALOG_ENTRY = "New Site Catalog Entry";
    public static final String TITLE_TEMPLATE_EXISTS = "Template already exists";
    public static final String TITLE_SITE_DOES_NOT_EXIST = "Site does not exist";
    public static final String TITLE_ASSETTYPE_DOES_NOT_EXIST = "Asset type does not exist";
    public static final String TITLE_FILE_NOT_FOUND = "File not found";
    public static final String PAGE_WIZARD_TEMPLATE_NEW_TEMPLATE = "Create a New Template";
    public static final String PAGE_WIZARD_TEMPLATE_SITE_ENTRY = "Site Entry";
    public static final String PAGE_WIZARD_MAP_PARAMETERS = "Mapped Parameters";
    public static final String PAGE_WIZARD_ELEMENT_CATALOG_ENTRY_NEW_ELEMENT_CATALOG_ENTRY = "Create a New Element Catalog entry";
    public static final String PAGE_WIZARD_SITE_CATALOG_ENTRY_NEW_SITE_CATALOG_ENTRY = "Create a New Site Catalog entry";
    public static final String PAGE_WIZARD_ELEMENT_NEW_ELEMENT = "Create a New Element";
    public static final String PAGE_WIZARD_SITE_ENTRY_NEW_SITE_ENTRY = "Create a New Site entry";
    public static final String GROUP_ELEMENT_TYPE = "Element Type";
    public static final String GROUP_PAGELET_ONLY = "Pagelet only";
    public static final String GROUP_WRAPPER_PAGE = "Wrapper page";
    public static final String GROUP_CACHE_RULES = "Cache Rules";
    public static final String GROUP_ADVANCED_CACHING = "Advanced Caching";
    public static final String GROUP_PAGELET_PARAMETERS = "Pagelet Parameters";
    public static final String GROUP_MAPPED_PARAMETERS = "Mapped Parameters";
    public static final String LABEL_SITE = "Site:";
    public static final String LABEL_NAME = "Name:";
    public static final String LABEL_DESCRIPTION = "Description:";
    public static final String LABEL_ASSET_TYPE = "Asset type:";
    public static final String LABEL_SUBTYPE = "Subtype:";
    public static final String LABEL_USAGE = "Usage:";
    public static final String LABEL_ELEMENT_NAME = "Element Name:";
    public static final String LABEL_ELEMENT_DESCRIPTION = "Element Description:";
    public static final String LABEL_ROOT_ELEMENT = "Root Element:";
    public static final String LABEL_STORAGE_PATH = "Storage path:";
    public static final String LABEL_ELEMENT_PARAMETER = "Element Parameter:";
    public static final String LABEL_ADDITIONAL_ELEMENT_PARAMETERS = "Additional Element Parameters:";
    public static final String LABEL_ARGUMENTS = "Arguments:";
    public static final String LABEL_ADDITIONAL_ARGUMENTS = "Additional Arguments:";
    public static final String LABEL_CACHE_CRITERIA = "Cache Criteria:";
    public static final String LABEL_ACCESS_CONTROL_LIST = "Access Control List:";
    public static final String LABEL_PAGE_NAME = "Page Name:";
    public static final String LABEL_MAP_EXISTING_SITE_CATALOG = "Map to existing Site Catalog entry with this pagename";
    public static final String LABEL_PAGELET_PARAMETERS = "Pagelet Parameters:";
    public static final String LABEL_CANCEL = "Cancel";
    public static final String LABEL_ADD_PARAMETER = "Add Parameter";
    public static final String LABEL_ADD = "Add";
    public static final String LABEL_REMOVE_PARAMETERS = "Remove Parameters";
    public static final String LABEL_EDIT_PARAMETER = "Edit Parameter";
    public static final String LABEL_SAVE = "Save";
    public static final String LABEL_HEADER_NAME = "Name";
    public static final String LABEL_HEADER_VALUE = "Value";
    public static final String LABEL_HEADER_KEY = "Key";
    public static final String LABEL_HEADER_TYPES = "Types";
    public static final String LABEL_HEADER_SITES = "Sites";
    public static final String TASK_SAVE_TEMPLATE = "Save Template";
    public static final String TASK_SAVE_ELEMENT_CATALOG_ENTRY = "Save Element Catalog Entry";
    public static final String TASK_SAVE_SITE_CATALOG_ENTRY = "Save Site Catalog Entry";
    public static final String TASK_SAVE_SITE_ENTRY = "Save Site Entry";
    public static final String TASK_SAVE_ELEMENT = "Save Element";
    /* public static final ImageDescriptor IMAGE_TEMPLATE_BIG = Activator.getImageDescriptor("/icons/Template_big.png");
     public static final ImageDescriptor IMAGE_ELEMENT_BIG = Activator.getImageDescriptor("/icons/CSElement_big.png");
     public static final ImageDescriptor IMAGE_ELEMENT_CATALOG_ENTRY_BIG = Activator.getImageDescriptor("/icons/ecatalog_big.png");
     public static final ImageDescriptor IMAGE_SITE_ENTRY_BIG = Activator.getImageDescriptor("/icons/siteentry_big.png");
     public static final ImageDescriptor IMAGE_SITE_CATALOG_ENTRY_BIG = Activator.getImageDescriptor("/icons/scatalog_big.png");
     public static final ImageDescriptor IMAGE_TEMPLATE = Activator.getImageDescriptor("icons/Template.png");
     public static final ImageDescriptor IMAGE_ELEMENT = Activator.getImageDescriptor("icons/CSElement.png");
     public static final ImageDescriptor IMAGE_ELEMENT_CATALOG_ENTRY = Activator.getImageDescriptor("icons/ecatalog.png");
     public static final ImageDescriptor IMAGE_SITE_ENTRY = Activator.getImageDescriptor("icons/siteentry.png");
     public static final ImageDescriptor IMAGE_SITE_CATALOG_ENTRY = Activator.getImageDescriptor("/icons/scatalog.png");*/
    public static final String RENDERMODE_LIVE = "live";

    private Constants() {
    }

    public static String getYesNoText(boolean option) {
        return getBooleanText(option, "Yes", "No");
    }

    public static String getYNText(boolean option) {
        return getBooleanText(option, "Y", "N");
    }

    public static String getTrueFalseText(boolean option) {
        return getBooleanText(option, "True", "False");
    }

    public static String getTFText(boolean option) {
        return getBooleanText(option, "T", "F");
    }

    public static String getBooleanText(boolean option, String trueText, String falseText) {
        return option ? trueText : falseText;
    }
}

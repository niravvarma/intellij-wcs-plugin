//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.csdt.valueobject.enumeration;

import com.intellij.csdt.util.Constants;
import com.intellij.openapi.diagnostic.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public enum ContentTemplates {
    JSP_TEMPLATE("resources/jsp_template.txt"),
    JSP_CSELEMENT("resources/jsp_cselement.txt"),
    JSP_ELEMENT("resources/jsp_element.txt"),
    GROOVY_TEMPLATE("resources/groovy_template.txt"),
    GROOVY_ELEMENT("resources/groovy_element.txt"),
    XML_TEMPLATE("resources/xml_template.txt"),
    XML_CSELEMENT("resources/xml_cselement.txt"),
    XML_ELEMENT("resources/xml_element.txt"),
    HTML_TEMPLATE("resources/html_template.txt"),
    HELP("resources/help_template.txt"),
    ERROR("resources/error_template.txt");

    private static Logger LOG = Logger.getInstance(ContentTemplates.class);
    private String defaultContent;

    ContentTemplates(String defaultContent) {
        this.defaultContent = defaultContent;
    }

    public String defaultContent() throws IOException, URISyntaxException {
        BufferedReader reader = null;

        try {
            StringBuilder e = new StringBuilder();

            URL file = getClass().getClassLoader().getResource(this.defaultContent).toURI().toURL();
            InputStream inputStream = file.openStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String tempStr = null;

            while ((tempStr = reader.readLine()) != null) {
                e.append(tempStr);
                e.append(Constants.NEW_LINE);
            }

            String var7 = e.toString();
            return var7;
        } catch (MalformedURLException var16) {
            // Log.error(var16);
            throw new RuntimeException(var16);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException var15) {
                    //       Log.error("Could not close resource", var15);
                }
            }

        }
    }
}

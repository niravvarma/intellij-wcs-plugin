//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.csdt.valueobject.enumeration;

import com.fatwire.csdt.util.Constants;
import com.fatwire.csdt.valueobject.enumeration.ContentTemplates;
import com.fatwire.csdt.valueobject.enumeration.ElementType;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.EnumSet;
import java.util.regex.Matcher;

public enum ElementFileType {
    XML_T("XML_T"),
    XML_C("XML_C"),
    XML_EC("XML_EC"),
    JSP_T("JSP_T"),
    JSP_C("JSP_C"),
    JSP_EC("JSP_EC"),
    GROOVY("GROOVY"),
    HTML("HTML"),
    EXISTING("EXISTING");

    private String label;
    private String extension;
    private ContentTemplates template;
    private String rootLocation;

    ElementFileType(String label) {
        this.label = label;
    }

    ElementFileType(String label, String extension, ContentTemplates template, String rootLocation) {
        this.label = label;
        this.extension = extension;
        this.template = template;
        this.rootLocation = rootLocation;
    }

    public static ElementFileType guessByFileName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return null;
        } else {
            Matcher m = Constants.REGEX_FILE_EXTENSION_EXTRACTOR.matcher(fileName);
            return m.find() ? guessByFileExtension(m.group(1)) : null;
        }
    }

    public static ElementFileType guessByFileExtension(String extension) {
        if (extension == null) {
            return null;
        } else if (StringUtils.isBlank(extension)) {
            return EXISTING;
        } else {
            ElementFileType[] arr$ = values();
            int len$ = arr$.length;

            for (int i$ = 0; i$ < len$; ++i$) {
                ElementFileType each = arr$[i$];
                if (each.extension().equals(extension)) {
                    return each;
                }
            }

            return EXISTING;
        }
    }

    public static ElementFileType[] values(ElementType type) {
        EnumSet temp = null;
        if (type.equals(ElementType.CSELEMENT)) {
            temp = EnumSet.complementOf(EnumSet.of(JSP_EC, XML_EC, JSP_T, XML_T));
        } else if (type.equals(ElementType.TEMPLATE)) {
            temp = EnumSet.complementOf(EnumSet.of(JSP_EC, XML_EC, JSP_C, XML_C));
        } else if (type.equals(ElementType.ELEMENTCATALOG)) {
            temp = EnumSet.complementOf(EnumSet.of(JSP_T, XML_T, JSP_C, XML_C));
        }

        return temp == null ? values() : (ElementFileType[]) temp.toArray(new ElementFileType[temp.size()]);
    }

    public String label() {
        return this.label;
    }

    public String extension() {
        return this.extension;
    }

    public String defaultContent() throws IOException {
        return this.template.defaultContent();
    }

    public String rootLocation() {
        return this.rootLocation;
    }

}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.csdt.valueobject.enumeration;

public enum ElementType {
    TEMPLATE("TEMPLATE"),
    CSELEMENT("CSELEMENT"),
    ELEMENTCATALOG("ELEMENTCATALOG");

    private String type;

    ElementType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.rest.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "type",
        propOrder = {"href", "name", "subtypes"}
)
public class Type {
    protected String href;
    @XmlElement(
            required = true
    )
    protected String name;
    protected List<String> subtypes;

    public Type() {
    }

    public String getHref() {
        return this.href;
    }

    public void setHref(String value) {
        this.href = value;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public List<String> getSubtypes() {
        if (this.subtypes == null) {
            this.subtypes = new ArrayList();
        }

        return this.subtypes;
    }

    @Override
    public String toString() {
        return name;
    }
}


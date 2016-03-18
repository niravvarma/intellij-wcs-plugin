//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.rest.beans;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"name", "id"}
)
@XmlRootElement(
        name = "userBean"
)
public class UserBean {
    @XmlElement(
            required = true
    )
    protected String name;
    @XmlElement(
            required = true
    )
    protected String id;

    public UserBean() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }

}

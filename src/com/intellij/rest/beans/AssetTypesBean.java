//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.rest.beans;

import com.fatwire.rest.beans.Type;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "",
        propOrder = {"types"}
)
@XmlRootElement(
        name = "assetTypesBean"
)
public class AssetTypesBean {
    @XmlElement(
            name = "type"
    )
    protected List<Type> types;

    public AssetTypesBean() {
    }

    public List<Type> getTypes() {
        if (this.types == null) {
            this.types = new ArrayList();
        }

        return this.types;
    }
}

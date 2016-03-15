package com.intellij.csdt.util;

import com.fatwire.cs.core.http.Post;
import com.fatwire.wem.sso.SSOException;
import com.intellij.csdt.CSDPUtil;
import com.intellij.csdt.Template;

import java.io.IOException;

/**
 * Created by NB20308 on 15/03/2016.
 */
public class WizardUtil {

    public static String saveElementCatalog(Template valObj) throws SSOException, IOException {
        Post request = CSDPUtil.buildPostRequest("OpenMarket/Xcelerate/PrologActions/Publish/csdt/ElementCatalogSave");
        request.addParameter("name", valObj.getName());
        request.addParameter("description", valObj.getDescription());
        request.addParameter("url_file", valObj.getUrl_file());
        request.addParameter("url_folder", valObj.getUrl_folder());
        request.addParameter("urlspec", valObj.getUrlSpec());
        request.addParameter("url", valObj.getUrl());
        request.addParameter("resdetails1", valObj.getResdetails1());
        request.addParameter("resdetails2", valObj.getResdetails2());
        request.addParameter("datastore", CSDPUtil.getDatastoreName());
        return CSDPUtil.post(request);
    }
}

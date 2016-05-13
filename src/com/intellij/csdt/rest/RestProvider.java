package com.intellij.csdt.rest;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.fatwire.rest.beans.*;
import com.fatwire.wem.sso.SSOException;
import com.intellij.csdt.CSDPUtil;
import com.intellij.csdt.TicketMaster;
import com.intellij.csdt.valueobject.service.RootElement;
import com.intellij.openapi.diagnostic.Logger;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RestProvider {
    private static Logger LOG = Logger.getInstance(RestProvider.class);

    public RestProvider() {
    }

    public static List<UserSite> getUserSites(String username) throws SSOException {


        Client client = Client.create();
        WebResource resource = client.resource(CSDPUtil.getRESTServletUrl());
        resource = resource.path("users").path(username);


            resource = resource.queryParam("multiticket", TicketMaster.getTicket());


        WebResource.Builder builder = resource.accept("application/xml");
        builder = builder.header("Pragma", "auth-redirect=false");
        ClientResponse r = builder.get(ClientResponse.class);
        ClientResponse.Status st = r.getClientResponseStatus();
        String message = "Reading sites for user: " + username + " returned status: " + st;
        if (st == ClientResponse.Status.OK) {
            UserBean user = r.getEntity(UserBean.class);
            return user.getSites();
        } else {
            LOG.error(message);
//            com.fatwire.csdt.util.Log.error(message);
            throw new RuntimeException(message);
        }
    }

    public static List<Type> getAllAssetTypes() throws SSOException, RuntimeException {

        Client client = Client.create();
        WebResource resource = client.resource(CSDPUtil.getRESTServletUrl());
        resource = resource.path("types");

            resource = resource.queryParam("multiticket", TicketMaster.getTicket());


        WebResource.Builder builder = resource.accept("application/xml");
        builder = builder.header("Pragma", "auth-redirect=false");
        ClientResponse r = builder.get(ClientResponse.class);
        ClientResponse.Status st = r.getClientResponseStatus();
        String message = "Reading all asset types returned status: " + st;
        if (st == ClientResponse.Status.OK) {
            AssetTypesBean types = r.getEntity(AssetTypesBean.class);
            return types.getTypes();
        } else {
            LOG.error(message);
            throw new RuntimeException(message);
        }
    }

    public static List<EnabledType> getEnabledTypes(String siteName) {
        Client client = Client.create();
        WebResource resource = client.resource(CSDPUtil.getRESTServletUrl());
        resource = resource.path("sites").path(siteName).path("types");

        try {
            resource = resource.queryParam("multiticket", TicketMaster.getTicket());
        } catch (SSOException var8) {
            LOG.error(var8);
//            com.fatwire.csdt.util.Log.error(var8);
            throw new RuntimeException(var8);
        }

        WebResource.Builder builder = resource.accept("application/xml");
        builder = builder.header("Pragma", "auth-redirect=false");
        ClientResponse r = builder.get(ClientResponse.class);
        ClientResponse.Status st = r.getClientResponseStatus();
        String message = "Reading enabled types for site: " + siteName + " returned status: " + st;
        if (st == ClientResponse.Status.OK) {
            EnabledTypesBean enabledTypes = r.getEntity(EnabledTypesBean.class);
            return enabledTypes.getTypes();
        } else {
            LOG.error(message);
//            com.fatwire.csdt.util.Log.error(message);
            throw new RuntimeException(message);
        }
    }

    public static List<String> getACLs() {
        Client client = Client.create();
        WebResource resource = client.resource(CSDPUtil.getRESTServletUrl());
        resource = resource.path("acls");

        try {
            resource = resource.queryParam("multiticket", TicketMaster.getTicket());
        } catch (SSOException var7) {
            LOG.error(var7);
//            com.fatwire.csdt.util.Log.error(var7);
            throw new RuntimeException(var7);
        }

        WebResource.Builder builder = resource.accept("application/xml");
        builder = builder.header("Pragma", "auth-redirect=false");
        ClientResponse r = builder.get(ClientResponse.class);
        ClientResponse.Status st = r.getClientResponseStatus();
        String message = "Reading ACLs returned status: " + st;
        if (st == ClientResponse.Status.OK) {
            AclsBean acls = r.getEntity(AclsBean.class);
            return acls.getAcls();
        } else {
            LOG.error(message);
//            com.fatwire.csdt.util.Log.error(message);
            throw new RuntimeException(message);
        }
    }

    public static SiteBean getSite(String siteName) {
        Client client = Client.create();
        WebResource resource = client.resource(CSDPUtil.getRESTServletUrl());
        resource = resource.path("sites").path(siteName);

        try {
            resource = resource.queryParam("multiticket", TicketMaster.getTicket());
        } catch (SSOException var7) {
            LOG.error(var7);
//            com.fatwire.csdt.util.Log.error(var7);
            throw new RuntimeException(var7);
        }

        WebResource.Builder builder = resource.accept("application/xml");
        builder = builder.header("Pragma", "auth-redirect=false");
        ClientResponse r = builder.get(ClientResponse.class);
        ClientResponse.Status st = r.getClientResponseStatus();
        String message = "Reading ACLs returned status: " + st;
        if (st == ClientResponse.Status.OK) {
            return r.getEntity(SiteBean.class);
        } else {
            LOG.error(message);
//            com.fatwire.csdt.util.Log.error(message);
            throw new RuntimeException(message);
        }
    }

    public static List<RootElement> getElements(String siteName) {
        ArrayList elements = new ArrayList();
        Client c = Client.create();
        WebResource w = c.resource(CSDPUtil.getRESTServletUrl());
        w = w.path("sites").path(siteName);
        w = w.path("types").path("CSElement").path("search");
        w = w.queryParam("searchengine", "dbbasic");
        w = w.queryParam("fields", CSDPUtil.delimited(",", Arrays.asList("id", "rootelement")));

        try {
            w = w.queryParam("multiticket", TicketMaster.getTicket());
        } catch (SSOException var17) {
            LOG.error(var17);
//            com.fatwire.csdt.util.Log.error(var17);
            throw new RuntimeException(var17);
        }

        WebResource.Builder builder = w.accept("application/xml");
        builder = builder.header("Pragma", "auth-redirect=false");
        ClientResponse r = builder.get(ClientResponse.class);
        ClientResponse.Status st = r.getClientResponseStatus();
        String message = "Reading Elements returned status: " + st;
        if (st != ClientResponse.Status.OK) {
            LOG.error(message);
//            com.fatwire.csdt.util.Log.error(message);
            throw new RuntimeException(message);
        } else {
            AssetsBean results = r.getEntity(AssetsBean.class);
            List resultAssets = results.getAssetinfos();
            Iterator i$ = resultAssets.iterator();

            while (true) {
                List fields;
                do {
                    if (!i$.hasNext()) {
                        return elements;
                    }

                    AssetInfo each = (AssetInfo) i$.next();
                    fields = each.getFieldinfos();
                } while (fields == null);

                RootElement eachElement = new RootElement();
                Iterator i$1 = fields.iterator();

                while (i$1.hasNext()) {
                    FieldInfo eachField = (FieldInfo) i$1.next();
                    String fieldName = eachField.getFieldname();
                    if ("id".equalsIgnoreCase(fieldName)) {
                        eachElement.setId(eachField.getData());
                    }

                    if ("rootelement".equalsIgnoreCase(eachField.getFieldname())) {
                        eachElement.setElement(eachField.getData());
                    }
                }

                elements.add(eachElement);
            }
        }
    }
}

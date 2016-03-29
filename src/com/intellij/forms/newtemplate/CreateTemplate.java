package com.intellij.forms.newtemplate;

import com.fatwire.cs.core.http.Post;
import com.fatwire.csdt.util.Log;
import com.fatwire.csdt.valueobject.enumeration.ElementFileType;
import com.fatwire.csdt.valueobject.service.MapParameter;
import com.fatwire.csdt.valueobject.service.PageletParameter;
import com.fatwire.csdt.valueobject.ui.Template;
import com.fatwire.wem.sso.SSOException;
import com.intellij.csdt.CSDPUtil;
import com.intellij.csdt.valueobject.enumeration.ContentTemplates;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by NB20308 on 22/03/2016.
 */
public class CreateTemplate {
    private static Logger LOG = Logger.getInstance(CreateTemplate.class);

    public static void createTemplate(Project project, final Template uiObject) {

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Loading Synchronization tool") {
            public void run(@NotNull ProgressIndicator progressIndicator) {
                progressIndicator.setFraction(0);
                com.fatwire.csdt.valueobject.service.Template serviceObject = new com.fatwire.csdt.valueobject.service.Template();
                serviceObject.setName(uiObject.getName());
                serviceObject.setDescription(StringUtils.defaultString(uiObject.getDescription()));
                progressIndicator.setFraction(0.05);

                serviceObject.setSubType(StringUtils.defaultString(uiObject.getAssetType()));
                progressIndicator.setFraction(0.10);

                String applicableSubtypes = "*";
                List subtypes = uiObject.getSubtypes();
                boolean existing;
                if (subtypes != null && subtypes.size() != 0 && !subtypes.contains("Any")) {
                    StringBuilder type = new StringBuilder();
                    existing = true;

                    String acls;
                    for (Iterator acl = uiObject.getSubtypes().iterator(); acl.hasNext(); type.append(acls)) {
                        acls = (String) acl.next();
                        if (existing) {
                            existing = false;
                        } else {
                            type.append(",");
                        }
                    }

                    applicableSubtypes = type.toString();
                }

                serviceObject.setApplicableSubtypes(applicableSubtypes);
                progressIndicator.setFraction(0.20);

                serviceObject.setTtype(uiObject.getUsage().code());
                serviceObject.setElementDescription(StringUtils.defaultString(uiObject.getElementDescription()));
                serviceObject.setRootElement(uiObject.getRootElement());
                progressIndicator.setFraction(0.30);

                ElementFileType type1 = uiObject.getElementType();
                existing = type1 == ElementFileType.EXISTING;
                serviceObject.setUseExisting(String.valueOf(existing));
                if (!existing) {
                    serviceObject.setUrlSpec(uiObject.getStoragePath());
                }

                progressIndicator.setFraction(0.40);

                serviceObject.setResdetails1(StringUtils.defaultString(uiObject.getElementParameters()));
                serviceObject.setResdetails2(StringUtils.defaultString(uiObject.getAdditionalElementParameters()));

                progressIndicator.setFraction(0.50);

                serviceObject.setPubId(CSDPUtil.getPubId(uiObject.getSite()));
                serviceObject.setPageCriteria(StringUtils.defaultString(uiObject.getCacheCriteria()));
                progressIndicator.setFraction(0.60);

                String acl1 = "*";
                List acls1 = uiObject.getAcls();
                if (acls1 != null && acls1.size() != 0 && !acls1.contains("Any")) {
                    StringBuilder pageletParams = new StringBuilder();
                    boolean sb = true;

                    String mapParams;
                    for (Iterator first = uiObject.getAcls().iterator(); first.hasNext(); pageletParams.append(mapParams)) {
                        mapParams = (String) first.next();
                        if (sb) {
                            sb = false;
                        } else {
                            pageletParams.append(",");
                        }
                    }

                    acl1 = pageletParams.toString();
                }

                serviceObject.setAcl(acl1);
                progressIndicator.setFraction(0.70);

//                serviceObject.setCsCacheInfo(((String)uiObject.getCacheRules().get(CacheRule.CachingSystem.SITES)).toString());
//                serviceObject.setSsCacheInfo(((String)uiObject.getCacheRules().get(CacheRule.CachingSystem.SATELLITE)).toString());
                serviceObject.setCsCacheInfo(uiObject.getCacheRule().globalCaching());
                serviceObject.setSsCacheInfo(uiObject.getCacheRule().globalCaching());
                serviceObject.setPageName(uiObject.getPageName(uiObject.getSite()));
                List pageletParams1 = uiObject.getPageletParameters(uiObject.getSite());
                serviceObject.setTotalPageletParams(String.valueOf(pageletParams1.size()));
                StringBuilder sb1 = new StringBuilder();
                boolean first1 = true;

                PageletParameter e;
                for (Iterator mapParams1 = pageletParams1.iterator(); mapParams1.hasNext(); sb1.append(e.getName()).append(":").append(e.getValue())) {
                    e = (PageletParameter) mapParams1.next();
                    if (first1) {
                        first1 = false;
                    } else {
                        sb1.append(",");
                    }
                }

                serviceObject.setArguments(sb1.toString());
                progressIndicator.setFraction(0.80);

                List mapParams2 = uiObject.getMapParameters();
                sb1 = new StringBuilder();
                first1 = true;

                MapParameter each;
                for (Iterator e1 = mapParams2.iterator(); e1.hasNext(); sb1.append(CSDPUtil.buildMapParameterString(each))) {
                    each = (MapParameter) e1.next();
                    if (first1) {
                        first1 = false;
                    } else {
                        sb1.append(",");
                    }
                }

                serviceObject.setMapping(sb1.toString());

                String e2;
                try {
                    e2 = "";
                    if (!existing) {
                        e2 = StringUtils.defaultIfEmpty(uiObject.getContent(), ContentTemplates.JSP_ELEMENT.defaultContent());
                    }

                    serviceObject.setUrl(e2);
                } catch (IOException var27) {
                    Log.error(var27);
                    throw new RuntimeException(var27);
                } catch (URISyntaxException e1) {
                    Log.error("URISyntaxException", e1);
                }

                progressIndicator.setFraction(0.90);

                try {
                    e2 = saveTemplate(serviceObject);
                    if (e2.contains("Save Error")) {
                        LOG.error("Error while saving Template", "Error while saving Template", e2);
                    } else if (e2.contains("Insufficient Privileges")) {
                        LOG.error("Error while saving Template", "You do not have sufficient privileges to perform this operation", e2);
                    } else if (e2.contains("Login Error")) {
                        LOG.error("Error while saving Template", "Please review the credentials configured in your preferences", e2);
                    } else {
                        CSDPUtil.callExport(e2);
//                        CSDPUtil.openTemplate(workbench, uiObject);
//                        CSDPUtil.refreshTree();
//                        CSDPUtil.refreshIResource();
                    }
                } catch (SSOException var23) {
                    LOG.error("SSOException", var23);
                } catch (IOException var24) {
                    LOG.error("IOException", var24);
                } finally {
                    progressIndicator.setFraction(1);

                }


            }

            public void onSuccess() {

            }

        });


    }

    public static String saveTemplate(com.fatwire.csdt.valueobject.service.Template valObj) throws SSOException, IOException {
        Post request = CSDPUtil.buildPostRequest("OpenMarket/Xcelerate/PrologActions/Publish/csdt/TemplateSave");
        request.addParameter("name", valObj.getName());
        request.addParameter("rootelement", valObj.getRootElement());
        request.addParameter("subtype", valObj.getSubType());
        request.addParameter("description", valObj.getDescription());
        request.addParameter("element", valObj.getRootElement());
        request.addParameter("applicablesubtypes", valObj.getApplicableSubtypes());
        request.addParameter("elementname", valObj.getRootElement());
        request.addParameter("ttype", valObj.getTtype());
        request.addParameter("elementdescription", valObj.getElementDescription());
        request.addParameter("urlspec", valObj.getUrlSpec());
        request.addParameter("url", valObj.getUrl());
        request.addParameter("url_file", valObj.getUrl_file());
        request.addParameter("url_folder", valObj.getUrl_folder());
        request.addParameter("resdetails1", valObj.getResdetails1());
        request.addParameter("resdetails2", valObj.getResdetails2());
        request.addParameter("cscacheinfo", valObj.getCsCacheInfo());
        request.addParameter("sscacheinfo", valObj.getSsCacheInfo());
        request.addParameter("pagename1", valObj.getPageName());
        request.addParameter("arguments", valObj.getArguments());
        request.addParameter("acl", valObj.getAcl());
        request.addParameter("pagecriteria", valObj.getPageCriteria());
        request.addParameter("mapping", valObj.getMapping());
        request.addParameter("useExistingElementCatalog", valObj.getUseExisting());
        request.addParameter("pubid", valObj.getPubId());
        request.addParameter("datastore", CSDPUtil.getDatastoreName());
        return CSDPUtil.post(request);
    }
}

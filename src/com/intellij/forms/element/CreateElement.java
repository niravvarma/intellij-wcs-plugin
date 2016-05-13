package com.intellij.forms.element;

import com.fatwire.cs.core.http.Post;
import com.fatwire.csdt.util.Log;
import com.fatwire.csdt.valueobject.enumeration.ElementFileType;
import com.fatwire.csdt.valueobject.service.MapParameter;
import com.fatwire.csdt.valueobject.service.Template;
import com.fatwire.csdt.valueobject.ui.Element;
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
 * Created by NB20308 on 08/05/2016.
 */
public class CreateElement {
    private static Logger LOG = Logger.getInstance(CreateElement.class);

    public static void createElement(Project project, final Element uiObject) {

        LOG.info("creating element");

        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Creating Element") {
            public void run(@NotNull ProgressIndicator progressIndicator) {

                progressIndicator.setFraction(0);

                Template serviceObject = new Template();
                serviceObject.setName(uiObject.getName());
                serviceObject.setDescription(StringUtils.defaultString(uiObject.getDescription()));
                progressIndicator.setFraction(0.20);

                serviceObject.setElementDescription(StringUtils.defaultString(uiObject.getElementDescription()));
                serviceObject.setRootElement(uiObject.getRootElement());
                progressIndicator.setFraction(0.40);

                ElementFileType type = uiObject.getElementType();
                boolean existing = type == ElementFileType.EXISTING;
                serviceObject.setUseExisting(String.valueOf(existing));
                if (!existing) {
                    serviceObject.setUrlSpec(uiObject.getStoragePath());
                }

                serviceObject.setResdetails1(StringUtils.defaultString(uiObject.getElementParameters()));
                serviceObject.setResdetails2(StringUtils.defaultString(uiObject.getAdditionalElementParameters()));
                serviceObject.setPubId(CSDPUtil.getPubId(uiObject.getSite()));
                progressIndicator.setFraction(0.80);

                List mapParams = uiObject.getMapParameters();
                StringBuilder sb = new StringBuilder();
                boolean first = true;

                MapParameter each;
                for (Iterator e = mapParams.iterator(); e.hasNext(); sb.append(CSDPUtil.buildMapParameterString(each))) {
                    each = (MapParameter) e.next();
                    if (first) {
                        first = false;
                    } else {
                        sb.append(",");
                    }
                }

                serviceObject.setMapping(sb.toString());

                String e1;
                try {
                    e1 = "";
                    if (!existing) {
                        switch (uiObject.getElementType()) {
                            case XML_EC:
                                e1 = StringUtils.defaultIfEmpty(uiObject.getContent(), ContentTemplates.XML_ELEMENT.defaultContent());
                                break;
                            case JSP_EC:
                                e1 = StringUtils.defaultIfEmpty(uiObject.getContent(), ContentTemplates.JSP_ELEMENT.defaultContent());
                                break;
                            case GROOVY:
                                e1 = StringUtils.defaultIfEmpty(uiObject.getContent(), ContentTemplates.GROOVY_ELEMENT.defaultContent());
                                break;
                            case HTML:
                                e1 = StringUtils.defaultIfEmpty(uiObject.getContent(), ContentTemplates.HTML_TEMPLATE.defaultContent());
                                break;
                            default:
                                e1 = StringUtils.defaultIfEmpty(uiObject.getContent(), ContentTemplates.JSP_ELEMENT.defaultContent());
                                break;
                        }
                    }

                    serviceObject.setUrl(e1);
                } catch (IOException var22) {
                    Log.error(var22);
                    throw new RuntimeException(var22);
                } catch (URISyntaxException e) {
                    Log.error("URISyntaxException", e);


                }

                progressIndicator.setFraction(0.90);

                try {
                    e1 = saveElement(serviceObject);
                    if (e1.contains("Save Error")) {
                        LOG.error("Error while saving Element", "Error while saving Element");
                    } else if (e1.contains("Insufficient Privileges")) {
                        LOG.error("Error while saving Element", "You do not have sufficient privileges to perform this operation");
                    } else if (e1.contains("Login Error")) {
                        LOG.error("Error while saving Element", "Please review the credentials configured in your preferences");
                    } else {
                        CSDPUtil.callExport(e1);
//                        CSDPUtil.openElement(workbench, uiObject);
//                        CSDPUtil.refreshTree();
//                        CSDPUtil.refreshIResource();
                    }
                } catch (SSOException var18) {
                    LOG.error("SSOException", var18);
                } catch (IOException var19) {
                    LOG.error("SSOException", var19);
                } finally {
                    progressIndicator.setFraction(1);
                }

            }
        });
    }

    private static String saveElement(Template serviceObject) throws SSOException, IOException {
        Post request = CSDPUtil.buildPostRequest("OpenMarket/Xcelerate/PrologActions/Publish/csdt/CSElementSave");
        request.addParameter("name", serviceObject.getName());
        request.addParameter("description", serviceObject.getDescription());
        request.addParameter("rootelement", serviceObject.getRootElement());
        request.addParameter("elementdescription", serviceObject.getElementDescription());
        request.addParameter("url", serviceObject.getUrl());
        request.addParameter("url_file", serviceObject.getUrl_file());
        request.addParameter("url_folder", serviceObject.getUrl_folder());
        request.addParameter("urlspec", serviceObject.getUrlSpec());
        request.addParameter("resdetails1", serviceObject.getResdetails1());
        request.addParameter("resdetails2", serviceObject.getResdetails2());
        request.addParameter("mapping", serviceObject.getMapping());
        request.addParameter("useExistingElementCatalog", serviceObject.getUseExisting());
        request.addParameter("pubid", serviceObject.getPubId());
        request.addParameter("datastore", CSDPUtil.getDatastoreName());
        return CSDPUtil.post(request);
    }
}

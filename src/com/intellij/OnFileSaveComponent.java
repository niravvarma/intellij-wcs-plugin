package com.intellij;

import com.fatwire.wem.sso.SSOException;
import com.intellij.configurations.WebCenterSitesPluginModuleConfigurationData;
import com.intellij.csdt.CSDPUtil;
import com.intellij.csdt.log.CSDTIntelliJLog;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileDocumentManagerAdapter;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by NB20308 on 23/12/2015.
 */
public class OnFileSaveComponent implements ProjectComponent {
    private static Logger LOG = Logger.getInstance(CSDTIntelliJLog.class);
    private final Project project;

    private WebCenterSitesPluginModuleConfigurationData webCenterSitesPluginModuleConfigurationData;

    public OnFileSaveComponent(Project project) {
        this.project = project;
        webCenterSitesPluginModuleConfigurationData = WebCenterSitesPluginModuleConfigurationData.getInstance(project);
    }


    @Override
    public void initComponent() {
        if (webCenterSitesPluginModuleConfigurationData.isPluginActive()) {
            MessageBus bus = ApplicationManager.getApplication().getMessageBus();

            MessageBusConnection connection = bus.connect();
            connection.subscribe(AppTopics.FILE_DOCUMENT_SYNC,
                    new FileDocumentManagerAdapter() {

                        @Override
                        public void beforeDocumentSaving(Document document) {

                            FileDocumentManager fileDocumentManager = FileDocumentManager.getInstance();
                            VirtualFile virtualFile = fileDocumentManager.getFile(document);
                            Module moduleName = ProjectRootManager.getInstance(project).getFileIndex().getModuleForFile(virtualFile);
                            if (moduleName != null && moduleName.getName().equals(webCenterSitesPluginModuleConfigurationData.getModuleName()) && "JSP".equals(virtualFile.getFileType().getName())) {
                                LOG.info("Save listener synchronization started");
                                final String[] filename = virtualFile.getPath().split(webCenterSitesPluginModuleConfigurationData.getWorkspace().replace("\\", "/"));

                                LOG.debug("Filename: " + filename[1]);

                                LOG.debug("Background task runner started");
                                ProgressManager.getInstance().run(new Task.Backgroundable(project, "Title") {
                                    public void run(@NotNull ProgressIndicator progressIndicator) {

                                        // start your process

                                        // Set the progress bar percentage and text
                                        progressIndicator.setFraction(0.33);
                                        progressIndicator.setText("Syncing to WCS");
                                        try {
                                            String result = CSDPUtil.callImport(filename[1], false);

                                            LOG.debug("result: " + result.replaceAll("(?m)^[ \t]*\r?\n", ""));
                                        } catch (IOException e) {
                                            LOG.error("Background task runner IOException: " + e);
                                        } catch (SSOException e) {
                                            LOG.error("Background task runner SSOException: " + e);
                                        }
                                        progressIndicator.setFraction(1.0);
                                        progressIndicator.setText("Done");
                                        LOG.info("Background task runner complete");
                                    }

                                    public void onSuccess() {
                                        LOG.debug("Background task Success");
                                    }

                                });
                            }

                        }

                    });
        }
    }


    @Override
    public void disposeComponent() {
    }

    @NotNull
    @Override
    public String getComponentName() {
        return "Intellij wcs synchronization OnSave listener component";
    }

    @Override
    public void projectOpened() {

    }

    @Override
    public void projectClosed() {

    }
}

package com.intellij.actions;

import com.intellij.SyncWindowForm;
import com.intellij.configurations.WebCenterSitesPluginModuleConfigurationData;
import com.intellij.forms.NewElementCatalog;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by NB20308 on 15/03/2016.
 */
public class MenuGroup extends ActionGroup {
    private AnAction[] children;

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        children = new AnAction[2];

        children[0] = new AnAction("Synchronize Data with WebCenter Sites") {
            public void actionPerformed(AnActionEvent event) {
                Project project = event.getData(PlatformDataKeys.PROJECT);
                JFrame frame = WindowManager.getInstance().getFrame(project);
                final SyncWindowForm syncWindowForm = new SyncWindowForm();
                syncWindowForm.display(frame);
            }

            @Override
            public void update(AnActionEvent e) {
                if (WebCenterSitesPluginModuleConfigurationData.getInstance(e.getProject()).isPluginActive()) {
                    Object navigatable = e.getData(CommonDataKeys.PROJECT);
                    e.getPresentation().setEnabledAndVisible(navigatable != null);
                } else {
                    return;
                }
            }
        };

        children[1] = new AnAction("Create Element Catalog Entry") {
            public void actionPerformed(AnActionEvent event) {
                Project project = event.getData(PlatformDataKeys.PROJECT);
                JFrame frame = WindowManager.getInstance().getFrame(project);
                final NewElementCatalog newElementCatalog = new NewElementCatalog();
                newElementCatalog.display(frame);
            }

            @Override
            public void update(AnActionEvent e) {
                if (WebCenterSitesPluginModuleConfigurationData.getInstance(e.getProject()).isPluginActive()) {
                    Object navigatable = e.getData(CommonDataKeys.PROJECT);
                    e.getPresentation().setEnabledAndVisible(navigatable != null);
                } else {
                    return;
                }
            }
        };

        return this.children;
    }
}

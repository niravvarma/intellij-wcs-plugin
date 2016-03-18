package com.intellij.actions;

import com.intellij.SyncWindowForm;
import com.intellij.configurations.WebCenterSitesPluginModuleConfigurationData;
import com.intellij.forms.NewElementCatalog;
import com.intellij.forms.newtemplate.NewTemplateStep1;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.net.URL;

/**
 * Created by NB20308 on 15/03/2016.
 */
public class MenuGroup extends ActionGroup {
    private AnAction[] children;

    @NotNull
    @Override
    public AnAction[] getChildren(@Nullable AnActionEvent anActionEvent) {
        children = new AnAction[3];

        URL syncIconURL = getClass().getClassLoader().getResource("icons/sync.gif");
        ImageIcon syncIcon = new ImageIcon(syncIconURL);
        children[0] = new AnAction("Synchronize Data with WebCenter Sites", null, syncIcon) {
            public void actionPerformed(AnActionEvent event) {
                Project project = event.getData(PlatformDataKeys.PROJECT);
                JFrame frame = WindowManager.getInstance().getFrame(project);
                final SyncWindowForm syncWindowForm = new SyncWindowForm(frame);

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

        //children[0].registerCustomShortcutSet(new CustomShortcutSet(KeyStroke.getKeyStroke(KeyEvent.CTRL_MASK+KeyEvent.SHIFT_MASK+KeyEvent.VK_W, 0)),null);

        URL newElementCatalogIconURL = getClass().getClassLoader().getResource("icons/newelementcatalog_wiz.gif");
        ImageIcon newElementCatalogIcon = new ImageIcon(newElementCatalogIconURL);
        children[1] = new AnAction("Create Element Catalog Entry", null, newElementCatalogIcon) {
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

        URL newTemplateIconURL = getClass().getClassLoader().getResource("icons/newtemplate_wiz.gif");
        ImageIcon newTemplateIcon = new ImageIcon(newTemplateIconURL);
        children[2] = new AnAction("Create Template", null, newTemplateIcon) {
            public void actionPerformed(AnActionEvent event) {
                Project project = event.getData(PlatformDataKeys.PROJECT);
                JFrame frame = WindowManager.getInstance().getFrame(project);
                final NewTemplateStep1 newElementCatalog = new NewTemplateStep1(frame);
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

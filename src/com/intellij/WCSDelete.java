package com.intellij;

import com.intellij.csdt.dialog.SimpleDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;

/**
 * Created by NB20308 on 13/05/2016.
 */
public class WCSDelete extends AnAction {

    private Project project;

    @Override
    public void actionPerformed(AnActionEvent e) {
        this.project = e.getProject();
        Editor editor = e.getData(LangDataKeys.EDITOR);

        DialogWrapper dialog = new SimpleDialog(project, "", "");




        // TODO: insert action logic here
        String path = ((EditorImpl) editor).getVirtualFile().getPath();
//        if(CSDPUtil.isInDatastore(path)){
        System.out.println("pth " + path);
//        }


    }
}

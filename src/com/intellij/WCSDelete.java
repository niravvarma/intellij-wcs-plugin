package com.intellij;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.impl.EditorImpl;

/**
 * Created by NB20308 on 13/05/2016.
 */
public class WCSDelete extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getData(LangDataKeys.EDITOR);
        // TODO: insert action logic here
        String path = ((EditorImpl) editor).getVirtualFile().getPath();
//        if(CSDPUtil.isInDatastore(path)){
        System.out.println("pth " + path);
//        }


    }
}

package com.intellij.csdt.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by NB20308 on 13/05/2016.
 */
public class SimpleDialog extends DialogWrapper {
    public SimpleDialog(@Nullable Project project, boolean canBeParent) {
        super(project, canBeParent);
    }


    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return null;
    }
}

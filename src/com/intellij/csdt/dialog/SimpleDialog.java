package com.intellij.csdt.dialog;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by NB20308 on 13/05/2016.
 */
public class SimpleDialog extends DialogWrapper {
    private JLabel textFieldTitle;
    private JLabel textFieldMessage;
    private JPanel mainPanel;

    public SimpleDialog(@Nullable Project project, String title, String message) {
        super(project);
        this.textFieldTitle.setText(title);
        this.textFieldMessage.setText(message);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }
}

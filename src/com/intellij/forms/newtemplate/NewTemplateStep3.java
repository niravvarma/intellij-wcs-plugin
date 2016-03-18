package com.intellij.forms.newtemplate;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by NB20308 on 18/03/2016.
 */
public class NewTemplateStep3 extends JDialog {
    private final NewTemplateStep3 newTemplateStep3;
    private final NewTemplateStep2 newTemplateStep2;
    private JLabel errorLabel;
    private JPanel mainPanel;
    private JButton backButton;
    private JButton cancelButton;
    private JButton finishButton;
    private JTable table1;
    private JButton addParameterButton;
    private JButton editParameterButton;
    private JButton removeParametersButton;

    public NewTemplateStep3(JFrame frame, Project project, final NewTemplateStep2 newTemplateStep2) {
        this.newTemplateStep3 = this;
        this.newTemplateStep2 = newTemplateStep2;

        newTemplateStep2.setNewTemplateStep3(this);
        setTitle("New Template");
        setContentPane(mainPanel);
        setModal(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Log.info("clicked back");
                newTemplateStep3.setVisible(false);
                newTemplateStep2.setVisible(true);
            }
        });
        newTemplateStep3.display(frame);
    }


    public void refresh() {
        pack();
        setLocationRelativeTo(null);
    }

    public void display(Container relativeContainer) {
        refresh();
        setResizable(false);
        setVisible(true);
    }

}

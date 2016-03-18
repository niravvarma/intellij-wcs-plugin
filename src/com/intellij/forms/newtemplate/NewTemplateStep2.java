package com.intellij.forms.newtemplate;


import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.WindowManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by NB20308 on 18/03/2016.
 */
public class NewTemplateStep2 extends JDialog {
    private NewTemplateStep1 newTemplateStep1;
    private NewTemplateStep2 newTemplateStep2;
    private JPanel mainPanel;
    private JLabel errorLabel;
    private JButton backButton;
    private JButton cancelButton;
    private JButton nextButton;
    private JButton finishButton;
    private JTextField textField1;
    private JRadioButton uncachedRadioButton;
    private JRadioButton cacchedRadioButton;
    private JRadioButton advancedRadioButton;
    private JTextField cCidContextPTextField;
    private JTextField textField3;
    private JTable table1;
    private JButton addParameterButton;
    private JButton editParameterButton;
    private JButton removeParametersButton;
    private JList list1;
    private JTextField true0TextField;
    private JTextField true0TextField1;
    private NewTemplateStep3 newTemplateStep3;

    public NewTemplateStep2(JFrame frame, final Project project, final NewTemplateStep1 newTemplateStep1) {
        this.newTemplateStep2 = this;
        this.newTemplateStep1 = newTemplateStep1;
        newTemplateStep1.setNewTemplateStep2(this);
        setTitle("New Template");
        setContentPane(mainPanel);
        setModal(true);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Log.info("clicked back");
                newTemplateStep2.setVisible(false);
                newTemplateStep1.setVisible(true);
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                newTemplateStep2.setVisible(false);
//                Project project = event.getData(PlatformDataKeys.PROJECT);
                if (newTemplateStep3 == null) {
                    JFrame frame = WindowManager.getInstance().getFrame(project);
                    final NewTemplateStep3 newElementCatalog = new NewTemplateStep3(frame, project, newTemplateStep2);
                } else {
                    newTemplateStep3.setVisible(true);
                }


            }
        });

        newTemplateStep2.display(frame);
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

    public NewTemplateStep3 getNewTemplateStep3() {
        return newTemplateStep3;
    }

    public void setNewTemplateStep3(NewTemplateStep3 newTemplateStep3) {
        this.newTemplateStep3 = newTemplateStep3;
    }
}

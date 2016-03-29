package com.intellij.forms.newtemplate;

import com.fatwire.csdt.valueobject.ui.Template;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

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

    public NewTemplateStep3(JFrame frame, final Project project, final Template template, final NewTemplateStep2 newTemplateStep2) {
        setTitle("New Template");
        this.newTemplateStep3 = this;
        this.newTemplateStep2 = newTemplateStep2;

        newTemplateStep2.setNewTemplateStep3(this);
        setContentPane(mainPanel);
        setModal(true);

        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                CreateTemplate.createTemplate(project, template);
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Log.info("clicked back");
                newTemplateStep3.setVisible(false);
                newTemplateStep2.setVisible(true);
            }
        });


        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
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
        URL iconURL = getClass().getClassLoader().getResource("icons/newtemplate_wiz.gif");
        ImageIcon icon = new ImageIcon(iconURL);
        setIconImage(icon.getImage());
        setResizable(false);
        setSize(new Dimension(650, 600));
        setVisible(true);
    }

}

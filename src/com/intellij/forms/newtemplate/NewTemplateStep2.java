package com.intellij.forms.newtemplate;


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

    public NewTemplateStep2(JFrame frame, final NewTemplateStep1 newTemplateStep1) {
        this.newTemplateStep2 = this;
        this.newTemplateStep1 = newTemplateStep1;

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
        newTemplateStep2.display(frame);
    }

    public void refresh() {
        pack();
        setLocationRelativeTo(null);
    }

    public void display(Container relativeContainer) {
        refresh();
        setResizable(false);
        setMinimumSize(new Dimension(650, 320));
        setVisible(true);
    }
}

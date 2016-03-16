package com.intellij.forms;

import javax.swing.*;
import java.awt.*;

/**
 * Created by NB20308 on 16/03/2016.
 */
public class NewTemplate extends JDialog {
    private JButton finishButton;
    private JButton backButton;
    private JButton cancelButton;
    private JButton nextButton;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JTextField textField2;
    private JList list1;
    private JRadioButton XMLRadioButton;
    private JRadioButton JSPRadioButton;
    private JRadioButton HTMLRadioButton;
    private JRadioButton existingElementRadioButton;
    private JTextField textField4;
    private JTextField textField5;
    private JTextField textField6;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JTextField textField7;
    private JTextField textField3;
    private JPanel mainPanel;

    public NewTemplate(JFrame frame) {


        setTitle("Oracle WebCenter Sites New Template");
        setContentPane(mainPanel);
        setModal(true);
        this.display(frame);

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

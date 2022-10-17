import javax.swing.*;
import java.awt.*;

public class TextBoxPresent {
    public TextBox control;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    String[] fontList = ge.getAvailableFontFamilyNames();

    int textWidth;
    int textHeight;
    public TextBoxPresent(TextBox controller) {
        control = controller;
        control.setFont(control.getInputFont());
        textWidth=0;
        textHeight=0;
    }
    public void textInputPrompt(){
        //create custom option pane to enter text and font details
         TextBoxDialog();
         control.repaint();
    }


    public void TextBoxDialog(){
        JTextField inputText= new JTextField(control.getInputText());
        JComboBox inputFontFamily = new JComboBox(fontList);;
        JTextField inputFontSize = new JTextField("16");
        GridBagConstraints constraints = new GridBagConstraints();
        JPanel inputPanel = new JPanel(new GridBagLayout());
        constraints.gridx=0;
        constraints.gridy=0;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        inputPanel.add(new JLabel("Text:"),constraints);
        constraints.gridx=1;
        constraints.gridy=0;
        constraints.gridwidth=3;
        constraints.gridheight=1;
        constraints.fill=GridBagConstraints.HORIZONTAL;
        inputPanel.add(inputText,constraints);
        constraints.gridx=0;
        constraints.gridy=1;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        inputPanel.add(new JLabel("Font Family:"),constraints);
        constraints.gridx=1;
        constraints.gridy=1;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        inputPanel.add(inputFontFamily,constraints);
        constraints.gridx=2;
        constraints.gridy=1;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        inputPanel.add(new JLabel("Font Size:"),constraints);
        constraints.gridx=3;
        constraints.gridy=1;
        constraints.gridwidth=1;
        constraints.gridheight=1;
        inputPanel.add(inputFontSize,constraints);

        int userSelect = JOptionPane.showConfirmDialog(null, inputPanel,
                "Please enter text details", JOptionPane.OK_CANCEL_OPTION);
        if (userSelect == JOptionPane.OK_OPTION) {
            control.completed=true;
            control.setInputText(inputText.getText());
            control.setInputSize(Integer.parseInt(inputFontSize.getText()));
            control.setInputFontFamily((String) inputFontFamily.getSelectedItem());
        }
        else {control.completed =false;}
    }
}

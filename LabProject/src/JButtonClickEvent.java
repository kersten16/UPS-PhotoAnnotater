import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JButtonClickEvent implements ActionListener {
    public AnnotatePanel control;
    public JButtonClickEvent(JButton clickableButton, AnnotatePanel controller) {

        clickableButton.addActionListener(this);
        control=controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getActionCommand() == "Close") {
           control.frame.parent.view.toolHeads.clearSelection();
           control.frame.parent.view.drawOptions.setVisible(false);
           control.setTool(-1);
           control.setColor(Color.BLACK);
           control.setThickness(1);
           control.frame.flipPhoto();
        }
       else if (e.getActionCommand() == "Color") {
           Color choice= JColorChooser.showDialog(control,"Colour Chooser",control.view.currColor);
           control.setColor(choice);
           control.changeSelectedColor();
       }
       else if (e.getActionCommand() == "Stroke") {
           JOptionPane optionPane = new JOptionPane();
           optionPane.setPreferredSize(new Dimension(300,100));
           StrokeSlider strokeslider= new StrokeSlider(control.view.stroke.getLineWidth());
           ChangeListener changeListener = new ChangeListener() {
               public void stateChanged(ChangeEvent changeEvent) {
                   StrokeSlider theSlider = (StrokeSlider) changeEvent.getSource();
                   if (!theSlider.getValueIsAdjusting()) {
                       optionPane.setInputValue(theSlider.getValue());
                   }
               }
           };
           strokeslider.addChangeListener(changeListener);
           optionPane.setMessage(strokeslider);
           optionPane.setOptionType(JOptionPane.OK_CANCEL_OPTION);
           JDialog dialog = optionPane.createDialog(control, "Stroke Diameter Select");
           dialog.setVisible(true);
           if((int)optionPane.getValue()==0) {
               control.setThickness((float) ( (double)(int)optionPane.getInputValue() / 10));
               control.changeSelectedThickness();
           }

       }
       else if (e.getActionCommand() == "Clear") {
           control.view.clear();
           control.repaint();

       }
    }


}

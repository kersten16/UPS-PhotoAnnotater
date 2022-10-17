import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JToggleClickEvent implements ActionListener {
    //Controller
    //If a toggle filter is selected, will change which images are displayed
    //For now, displays progress message to confirm the button has been pressed.
    public appControl control;
    public AnnotatePanel annotatePanel;
        public JToggleClickEvent(JToggleButton toggleItem, appControl controller) {
            toggleItem.addActionListener(this);
            control=controller;
        }
        public JToggleClickEvent(JToggleButton toggleItem, AnnotatePanel annPanel) {
            toggleItem.addActionListener(this);
            annotatePanel=annPanel;
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //System.out.println(actionEvent.getActionCommand());
            if (actionEvent.getActionCommand()== "People") {
            //
                appPresent.setProgress("People pressed");
            }

            else if ( actionEvent.getActionCommand()=="Places"){
            //
                appPresent.setProgress("Places pressed");
            }
            else if ( actionEvent.getActionCommand()=="Food"){
                //do nothing right now
                appPresent.setProgress("Food pressed");
            }
            else if ( actionEvent.getActionCommand()=="Animals"){
                //do nothing right now
                appPresent.setProgress("Animals pressed");
            }
            else if (actionEvent.getActionCommand() == "Draw") {
                AbstractButton buttonPressed = (AbstractButton) actionEvent.getSource();
                if (buttonPressed.getModel().isSelected()) {
                    annotatePanel.setTool(0);
                } else {
                    annotatePanel.setTool(-1);
                }
            }
            else if (actionEvent.getActionCommand() == "Text") {
                AbstractButton buttonPressed = (AbstractButton) actionEvent.getSource();
                if (buttonPressed.getModel().isSelected()) {
                    annotatePanel.setTool(2);
                } else {
                    annotatePanel.setTool(-1);
                }
            }
            else if (actionEvent.getActionCommand() == "Line") {
                AbstractButton buttonPressed = (AbstractButton) actionEvent.getSource();
                if (buttonPressed.getModel().isSelected()) {
                    annotatePanel.setTool(1);
                } else {
                    annotatePanel.setTool(-1);
                }
            }

        }

}

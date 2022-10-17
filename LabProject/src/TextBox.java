import java.awt.*;

import javax.swing.*;

public class TextBox extends JPanel {
   //input via Dialog?
    //could make movable in the future
    public TextBoxModel model;
    public TextBoxPresent view;
    public boolean completed =true;


    AnnotatePanel container;

    public TextBox(AnnotatePanel owner, int mouseX, int mouseY) {
        model= new TextBoxModel(owner,new Point(mouseX, mouseY));
        view = new TextBoxPresent(this);
        container=owner;
        view.textInputPrompt();
    }
    
    @Override
    public Dimension getPreferredSize() {
        return model.getPreferredSize();
    }

    public Font getInputFont() {
        return model.getInputFont();
    }

    public String getInputText() {
        return model.getInputText();
    }
    public int getInputSize() {
        return model.inputSize;
    }

    public Dimension getSizeLimit(){
        return model.getSizeLimit();
    }
    public void setInputText(String input) {
        model.setInputText(input);
    }


    public void setInputSize(int size) {
        model.inputSize=size;
    }

    public void setInputFontFamily(String fontFam) {
        model.inputFontFamily=fontFam;
    }

    public Point getMousePoint(){
        return model.mousePoint;
    }


}
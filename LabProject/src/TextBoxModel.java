import java.awt.*;


public class TextBoxModel {
    public Point mousePoint;
    public Point textPoint ;
    public String input;
    int inputSize;
    String inputFontFamily;
    AnnotatePanel container;
    Dimension sizeLimit;
    public TextBoxModel(AnnotatePanel owner,Point initialPt){
        container=owner;
        mousePoint=initialPt;
        textPoint=new Point(mousePoint.x, mousePoint.y);
        sizeLimit=getPreferredSize();
        inputFontFamily="Serif";
        inputSize=16;
        input="Text Here";
    }

    public Font getInputFont(){
        return new Font(inputFontFamily, Font.PLAIN, inputSize);
    }

    public Dimension getPreferredSize() {
        Dimension containerSize = container.getPreferredSize();
        return new Dimension(containerSize.width- mousePoint.x,containerSize.height- mousePoint.y);
    }

    public Dimension getSizeLimit() {
        return sizeLimit;
    }

    public String getInputText(){
        return input;
    }

    public void setInputText(String input) {
        this.input=input;
    }
}

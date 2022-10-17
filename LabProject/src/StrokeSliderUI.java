import javafx.scene.shape.Circle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StrokeSliderUI extends BasicSliderUI {
    Dimension thumbSize;
    public StrokeSlider strokeSlider;
    public StrokeSliderUI(StrokeSlider strokeSlider ) {
        super(strokeSlider);
        this.strokeSlider=strokeSlider;
        thumbSize=new Dimension(this.strokeSlider.getValue()/10,this.strokeSlider.getValue()/10);
        this.strokeSlider.addChangeListener(new SliderListener());
    }
    @Override
    public void paintThumb(Graphics pen){
        Graphics2D pen2D=(Graphics2D)pen;
        pen2D.setColor(Color.WHITE);
        pen2D.fillRect(thumbRect.x-5, thumbRect.y, 10, 10);
        pen2D.setColor(Color.BLACK);
        pen2D.fillRect(thumbRect.x-(thumbSize.width/2), thumbRect.y+(5- thumbSize.height/2), thumbSize.width, thumbSize.height);
    }
    @Override
    public Dimension getThumbSize(){
        return new Dimension(10,10);
    }

    @Override
    public void paintLabels(Graphics pen){
        List<String> labels=new ArrayList<>();
        for(int i=10; i<=100;i+=10){
            labels.add(((JLabel)strokeSlider.getLabelTable().get(i)).getText());
        }
        int displacement=labelRect.width/10;
        for(String label:labels ) {
            pen.drawString(label, labelRect.x+displacement, labelRect.y + 10);
            displacement+=labelRect.width/10;
        }
    }

    private class SliderListener implements ChangeListener {
        @Override
        public void stateChanged(ChangeEvent e) {
            int value = strokeSlider.getValue();
            thumbSize=new Dimension(value/10,value/10);
            strokeSlider.repaint();
        }
    }
}

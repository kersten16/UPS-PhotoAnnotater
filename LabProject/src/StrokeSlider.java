import javax.swing.*;
import java.util.Hashtable;

public class StrokeSlider extends JSlider {
    public StrokeSlider(float lineWidth) {
        super(1,100,(int) lineWidth*10);
        setUI(new StrokeSliderUI(this));
        setMajorTickSpacing(10);
        setSnapToTicks(false);

        Hashtable labelTable = new Hashtable();
        labelTable.put( 10, new JLabel("1") );
        labelTable.put( 20, new JLabel("2") );
        labelTable.put( 30, new JLabel("3") );
        labelTable.put( 40, new JLabel("4") );
        labelTable.put( 50, new JLabel("5") );
        labelTable.put( 60, new JLabel("6")  );
        labelTable.put( 70, new JLabel("7")  );
        labelTable.put( 80, new JLabel("8") );
        labelTable.put( 90, new JLabel("9")  );
        labelTable.put( 100, new JLabel("10") );
        this.setLabelTable( labelTable );
        this.setPaintTicks(true);
        this.setPaintLabels(true);
    }
}

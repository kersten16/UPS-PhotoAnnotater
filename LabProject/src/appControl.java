import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicToolBarUI;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class appControl extends JFrame {
    private static appModel model;
    static appPresent view;

    public appControl(){
        super("Photo Browser");
        model= new appModel();
        model.addChangeListener(e -> view.changeToolBarVisible());
        view= new appPresent(this);
        view.show();
    }

public void addComponent(JComponent addOn){
        view.addComponent(addOn);
}
    public static void setProgress(String update) {
        view.setProgress(update);
    }
    public void triggerChangeListeners(){
        model.triggerChangeListeners();
    }

    public static void main(String [] args){

        appControl application = new appControl();
    }


    public String getStatus() {
        return view.getStatus();
    }
}

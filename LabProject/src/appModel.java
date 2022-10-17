import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

public class appModel {
    private List<ChangeListener>cListeners = new ArrayList<>();
    public int prefHeight;
    public int prefWidth;

    public appModel(){
//        imageListEmpty = true;
//        viewSetting = true;
        prefHeight=800;
        prefWidth=500;

    }
    public void addChangeListener(ChangeListener listener) {
        cListeners.add(listener);
        //figure out from example from class
    }
    public void triggerChangeListeners() {

        for (ChangeListener listener : cListeners) {
            listener.stateChanged(new ChangeEvent(this));
        }
    }
}

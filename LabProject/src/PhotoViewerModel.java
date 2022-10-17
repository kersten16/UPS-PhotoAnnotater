import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class PhotoViewerModel {
    private final List<ActionListener> aListeners= new ArrayList<>();
    private final List<ChangeListener> cListeners= new ArrayList<>();
    private static boolean viewSetting; //true = solo, false = browser
    private static List<Image> imageList = new ArrayList<>();
    private static List<List<Shape>> annList;
    private static boolean flipped;
    private PhotoViewer control;
    public AnnotatePanel annPanel;

    private int prefHeight;
    private int prefWidth;
    public PhotoViewerModel (PhotoViewer container){
        viewSetting = true;
        flipped=false;
        setPrefSize(700,500);
        this.control =container;
        annPanel=new AnnotatePanel(prefWidth,prefHeight, this.control);
        annList=annPanel.getAnnotationList();
    }




    public static boolean getListEmpty(){
        return imageList.isEmpty();
    }

    public void setViewSetting(){
        viewSetting=!viewSetting;
        triggerChangeListeners();
    }

    public static boolean getViewSetting(){
        return viewSetting;
    }

    public void addPhoto(Image photo ) {

        imageList.add(photo);
        //newAnnotation.setPreferredSize(new Dimension(, ));
        annPanel.addImage();
        annList=annPanel.getAnnotationList();
        triggerChangeListeners();
    }

    public static List<Image> getImageList() {
        return imageList;
    }

    public void removePhoto(Image toDelete) {
        annList.remove(imageList.indexOf(toDelete));
        imageList.remove(toDelete);

        triggerChangeListeners();

    }

    public void flipPhoto() {
        flipped=!flipped;
        if(isFlipped()) {
            annPanel.setPreferredSize(new Dimension(control.getImage().getWidth(null),control.getImage().getHeight(null)));
            annPanel.setCanvas();
            annPanel.setVisible(true);
            annPanel.requestFocus();
            control.parent.view.drawOptions.setVisible(true);
            annPanel.openImage(imageList.indexOf(control.getImage()));
        }else{
            annPanel.saveImage();
            control.parent.view.drawOptions.setVisible(false);
            annPanel.setVisible(false);
        }
        triggerChangeListeners();
    }
    public boolean isFlipped(){
        return flipped;
    }

    public List<List<Shape>> getAnnList() {
        return annList;
    }

    public void saveAnnotation (List<Shape> annots, Image currImage){
        int index= imageList.indexOf(currImage);
        annList.add(index,annots);
    }
    public void addActionListener(ActionListener listener) {
        aListeners.add(listener);
    }
    public void removeActionListener(ActionListener listener) {
        aListeners.remove(listener);
    }
    private void fireActionListeners() {
        for (ActionListener listener : aListeners) {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_FIRST, "fire"));
        }
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

    public Dimension getPrefSize() {
        return new Dimension(prefWidth,prefHeight);
    }

    void setPrefSize(int height, int width) {
        prefHeight=height;
        prefWidth=width;
    }
}

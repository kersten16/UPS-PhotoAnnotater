
import fr.lri.swingstates.canvas.CStateMachine;
import fr.lri.swingstates.sm.State;
import fr.lri.swingstates.sm.Transition;
import fr.lri.swingstates.sm.jtransitions.*;
import fr.lri.swingstates.sm.transitions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class PhotoViewer extends JComponent {
    public static PhotoViewerModel model;
    public static PhotoViewerPresent view;
    public static Image currImage=null;
    public appControl parent;

    public PhotoViewer(appControl application) {

        model = new PhotoViewerModel(this);
        view = new PhotoViewerPresent(this);
        parent = application;
        model.addChangeListener(e -> repaint());

        this.addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e) {
                getAnnotate().dispatchEvent(e);
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                getAnnotate().dispatchEvent(e);
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                getAnnotate().dispatchEvent(e);
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && getViewSetting()&& !getImageListEmpty()) {
                    flipPhoto();
                }
                getAnnotate().dispatchEvent(e);
            }
            @Override
            public void mousePressed(MouseEvent e){
                getAnnotate().dispatchEvent(e);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                getAnnotate().dispatchEvent(e);;
            }

        });

    }



    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        view.paint((Graphics2D) g);
    }
    @Override
    public Dimension getMinimumSize(){
        return getPreferredSize();
    }
    @Override
    public Dimension getMaximumSize(){
        return getPreferredSize();
    }
    @Override
    public Dimension getPreferredSize() {
        return model.getPrefSize();
    }
    public void setPrefSize(int updateWidth, int updateHeight){
        model.setPrefSize(updateWidth,updateHeight);
    }

    public  static List<Image> getImageList() {
        return model.getImageList();
    }

    public static void removePhoto(Image toDelete){
        model.removePhoto(toDelete);

    }
    public static void addPhoto(Image toAdd){
       // Image scaled= toAdd.getScaledInstance(Math.min(toAdd.getWidth(null),2048),Math.min(toAdd.getHeight(null),1536), Image.SCALE_DEFAULT);
        currImage=toAdd;
        model.addPhoto(toAdd);

        //view.loadImage(toAdd);
    }
    public static boolean getImageListEmpty(){
        return model.getListEmpty();
    }

    public static void setViewSetting() {
        model.setViewSetting();
    }
    private boolean getViewSetting() {
        return model.getViewSetting();
    }

    public void flipPhoto() {
        model.flipPhoto();
        parent.triggerChangeListeners();
    }


    public boolean isFlipped() {
        return model.isFlipped();
    }

    public AnnotatePanel getAnnotate() {
        return model.annPanel;
    }

    public Image getImage() {
        return currImage;
    }

    public void setProgress(String update) {
        parent.setProgress(update);
    }

    public static void viewImages() {
        view.viewImages();
    }



    public String getStatus() {
        return parent.getStatus();
    }
    public void nextImage() {
        List<Image> images=getImageList();
        int index=images.indexOf(currImage);
        index+=1;
        if (index > images.size() - 1) {
            index = 0;
        }
        currImage=images.get(index);
    }

    public void previousImage() {
        List<Image> images=getImageList();
        int index=images.indexOf(currImage);
        index-=1;
        if (index < 0) {
            index =  images.size() - 1;
        }
        currImage=images.get(index);
    }
}

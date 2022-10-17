import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PhotoViewerPresent {
    public static PhotoViewer control;

    public PhotoViewerPresent(PhotoViewer photoViewer) {
        control=photoViewer;
        SpringLayout layout =new SpringLayout();
        control.setLayout(layout);
        control.add(control.getAnnotate());
        layout.putConstraint(SpringLayout.NORTH,control.getAnnotate(),10,SpringLayout.NORTH,control);
        layout.putConstraint(SpringLayout.WEST,control.getAnnotate(),20,SpringLayout.WEST,control);
    }

    static void viewImages() {
        if (control.getImageListEmpty()){
            return;
        }
        List<Image> imageList = control.getImageList();
        control.removeAll();

        GridLayout layout = new GridLayout(0,4);
        layout.setHgap(3);
        layout.setVgap(3);

        control.setLayout(layout);
        for (int i = 0; i< imageList.size();i++){
            control.add(new JLabel(new ImageIcon(imageList.get(i))));
        }

        control.repaint();
    }
    public void paint(Graphics2D g) {
        g.setColor(Color.DARK_GRAY);
        Image img= control.getImage();
        if(img!=null)
            control.setPrefSize(img.getWidth(null),img.getHeight(null));
        g.fillRect(0, 0, control.getWidth(), control.getHeight());
        if (!control.getImageListEmpty()) {
            if (control.isFlipped()) {
                control.getAnnotate().repaint();
            }
            else {
                g.drawImage(img, 20, 10, img.getWidth(null), img.getHeight(null), null);
            }
        }
        g.dispose();

    }


}

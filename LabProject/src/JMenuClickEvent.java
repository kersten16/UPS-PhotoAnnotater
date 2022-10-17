//Controller
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class JMenuClickEvent implements ActionListener  {
    //If menu bar items are selected, this class will trigger the associated action
    //Any unimplemented items are displayed in the progress label when pressed as feedback
    appControl parent;
    public JMenuClickEvent(JMenuItem clickableItem, appControl parent) {
        this.parent =parent;
        clickableItem.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand()== "Quit") {
            String ObjButtons[] = {"Yes", "No"};
            int confQuit = JOptionPane.showOptionDialog(null, "Are you sure you want to close the application?", "Quit", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons, ObjButtons[1]);
            if (confQuit == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
        else if ( e.getActionCommand()=="Import"){
            BufferedImage image = null;
            JFileChooser openFile= new JFileChooser();
            openFile.addChoosableFileFilter(new ImageFilter());
            openFile.setAcceptAllFileFilterUsed(false);

            int result =openFile.showOpenDialog(null);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = openFile.getSelectedFile();
                try {
                    image=ImageIO.read(file);
                    //System.out.println(image.getWidth()+", "+ image.getHeight());
                    PhotoViewer.addPhoto(image);
                } catch (IOException exc) {
                    exc.printStackTrace();
                }
            }

            appPresent.setProgress("Import pressed");
        }
        else if ( e.getActionCommand()=="Delete"){
            appControl.setProgress("Delete pressed");
            //fix this
            PhotoViewer.removePhoto(PhotoViewer.currImage);
        }
        else if ( e.getActionCommand()=="Photo Viewer"){
            PhotoViewer.setViewSetting();
            appControl.setProgress("Focus View");
        }
        else if ( e.getActionCommand()=="Browser"){
            System.out.println("yes");
            PhotoViewer.setViewSetting();
            appControl.setProgress("Browser View");
            PhotoViewer.viewImages();

        }
    }
}

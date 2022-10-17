import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AnnotatePanelModel {

    public int canvasWidth;
    public int canvasHeight;
    public int prefWidth;
    public int prefHeight;
    public int drawingIndex;
    static List<List<Shape>> annList = new ArrayList<List<Shape>>();
    public AnnotatePanel control;
    public AnnotatePanelModel(int setWidth, int setHeight, AnnotatePanel panel){
        control=panel;
        setPrefSize(setWidth,setHeight);
        canvasHeight =prefHeight;
        canvasWidth =prefWidth;

    }

    public void removeAnnotation(Image image){
        annList.remove(image);
    }

    public void saveImage() {

        annList.set(drawingIndex, List.copyOf(control.shapes));
        control.view.clear();
    }
    public void openImage(int index){
        drawingIndex = index;
        for (Shape img: annList.get(drawingIndex)){
            control.shapes.push(img);
        }

    }

    public Dimension getDimension() {
        return new Dimension(prefWidth,prefHeight);
    }

    public void addImage() {
        annList.add(new ArrayList<Shape>());
        drawingIndex=annList.size()-1;
    }

    public void setPrefSize(int prefWidth, int prefHeight) {
        this.prefWidth=prefWidth;
        this.prefHeight=prefHeight;
    }
}

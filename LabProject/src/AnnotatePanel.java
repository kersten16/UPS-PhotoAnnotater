import fr.lri.swingstates.debug.StateMachineVisualization;
import fr.lri.swingstates.sm.*;
import fr.lri.swingstates.sm.transitions.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//add List<Shape? selected
// on mouse click, check if point is in shape (can make this region bigger by checking places arounf the point
//search through list backwards
//check for shift for multi select
//maybe add attribute to shape "translate. is 0,0 on new but can change based on drag? or just erase and redraw?
public class AnnotatePanel extends JPanel {
    public AnnotatePanelPresent view;
    public AnnotatePanelModel model;
    public PhotoViewer frame;
    private boolean dragged = false;
    public Stack<Shape> shapes;
    public List<Shape> selectedShapes =new ArrayList<>();
    private List<Shape> draggedShapes=new ArrayList<>();
    Image paintCursor = new ImageIcon("Images/paint-roller.png").getImage();
    JStateMachine sm1 = new JStateMachine(){
       Shape currentDrawing;


        public State start = new State() {
            public void enter() {
                currentDrawing = null;
                requestFocusInWindow();
            }

            Transition drawShape = new Press(BUTTON1, "=> drawing") {
                @Override
                public boolean guard() {
                    return view.activeTool == view.PEN_TOOL || view.activeTool==view.LINE_TOOL;
                }

                public void action() {
                    deselectAll();
                }
            };

            Transition writeText = new Click(BUTTON1){
                public boolean guard(){return view.activeTool == view.TEXT_TOOL;}
                public void action () {
                    createShape(getMousePosition());
                    deselectAll();
                }
            };

            Transition clickOnShape = new Click(BUTTON1) {
                @Override
                public boolean guard() {
                    return getShapeAt(getMousePosition()) != null && view.activeTool == view.CURSOR_TOOL;
                }
                public void action() {
                    Shape selected=getShapeAt(getMousePosition());
                    deselectAll();
                    select(selected);
                    requestFocusInWindow();
                }
            };
            Transition styleCopy = new Click(BUTTON3,"=> styleCopy"){
                @Override
                public boolean guard() {
                    return getShapeAt(getMousePosition()) != null;
                }
            };
            Transition deleteShape = new KeyPress(KeyEvent.VK_BACK_SPACE){
                public boolean guard() {
                    return selectedShapes.size() != 0;
                }
                public void action(){
                    deleteSelected(selectedShapes);
                    repaint();
                    requestFocusInWindow();
                }
            };
            Transition clickOnCanvas = new Click(BUTTON1){
                public void action () {
                    deselectAll();
                    requestFocusInWindow();
                }
            };
            Transition dragShapes = new Drag (BUTTON1, "=> dragShape"){
                @Override
                public boolean guard(){return selectedShapes.size()>0;}
            };

            Transition shiftPressed= new KeyPress(KeyEvent.VK_SHIFT,"=> startShiftPressed"){};

        };
        public State drawing = new State(){

            Shape currentShape;
            @Override
            public void enter(){
                List<Point> line = new ArrayList<>();
                line.add(getMousePosition());
                currentShape=createShape(line);
            }

            Transition draw = new Drag(BUTTON1){
                public void action(){
                    currentShape.addPoint(getMousePosition());
                    repaint();
                }
            };
            Transition finishDraw = new Release(BUTTON1,"=> start"){
                public void action(){
                   currentShape.addPoint(getMousePosition());
                   repaint();
                }
            };

        };
        public State startShiftPressed = new State() {

            Transition clickOnShape = new Click(BUTTON1) {
                @Override
                public boolean guard() {
                    return getShapeAt(getMousePosition()) != null;
                }
                @Override
                public void action() {
                    Shape clickedShape = getShapeAt(getMousePosition());
                    toggleSelectionOf(clickedShape);
                }
            };

            Transition clickOnCanvas = new Click(BUTTON1) {
                @Override
                public void action() {
                    deselectAll();
                }
            };

            Transition dragging = new Drag(BUTTON1, "=> dragShape") {};

            Transition releaseShift = new KeyRelease(KeyEvent.VK_SHIFT, "=> start") {};
        };

        public State dragShape = new State(){
            Point lastMousePosition;
            @Override
            public void enter() {
                Point currentMousePosition = getMousePosition();
                lastMousePosition = currentMousePosition;
                startDraggingShapes(selectedShapes);
            }

            @Override
            public void leave() {
                stopDraggingShapes();
            }

            Transition drag = new Drag() {
                @Override
                public void action() {
                    Point currentMousePosition = getMousePosition();
                    dragShapes(currentMousePosition, lastMousePosition);
                    lastMousePosition = currentMousePosition;
                }
            };

            Transition stopDraggingShiftPressed = new Release(BUTTON1, "=> startShiftPressed") {
                @Override
                public boolean guard() {
                    return getMouseEvent().isShiftDown();
                }
            };

            Transition stopDragging = new Release(BUTTON1, "=> start") {};
        };

        public State styleCopy = new State(){
            Font copyFont;
            Color copyColor;
            BasicStroke copyThick;
            List<Shape> styleShapes=new ArrayList<>();
            public void enter(){
                Shape selected=getShapeAt(getMousePosition());
                if(selected.getShape()==2){copyFont=selected.getFont();}
                copyColor=selected.getColor();
                copyThick=selected.getStroke();
                //change cursor
                Toolkit toolkit = Toolkit.getDefaultToolkit();
                Cursor c = toolkit.createCustomCursor(paintCursor , new Point(0,0), "style dropper");
                setCursor (c);
            }
            public void leave(){
                setCursor(Cursor.getDefaultCursor());
                repaint();
            }
            Transition transferStyle = new Click(BUTTON1, "=> start"){
                @Override
                public boolean guard() {
                    return getShapeAt(getMousePosition()) != null;
                }
                public void action(){
                    Shape toChange=getShapeAt(getMousePosition());
                    if(toChange.getSelected()){
                       styleShapes=List.copyOf(selectedShapes);
                    }else{
                        styleShapes.add(toChange);
                    }
                    for (Shape shape: styleShapes){
                        shape.setColor(copyColor);
                        shape.setStroke(copyThick);
                        if(shape.getShape()==2&& copyFont!=null){shape.setFont(copyFont);}
                    }
                }
            };

            Transition clickOnCanvas = new Click(BUTTON1, "=> start"){
                public boolean guard(){return getShapeAt(getMousePosition())==null;}
            };
            Transition rightClick = new Click(BUTTON2, "=> start"){
            };
        };

    };



    public AnnotatePanel(int setWidth, int setHeight, PhotoViewer container){
       model = new AnnotatePanelModel(setWidth,setHeight,this);
       view = new AnnotatePanelPresent(this);
       sm1.attachTo(this);
       this.frame = container;
       this.setVisible(false);
       this.setFocusable(true);

    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        view.paint(g);
    }


    public void addImage(){
       model.addImage();
    }
    public void saveImage() {
        model.saveImage();
    }
    public void setCanvas(){
       view.setCanvas(getPreferredSize().width, getPreferredSize().height);
    }

    private Shape createShape(List<Point> line) {
        Color primary = view.currColor;
        Shape shape=new Shape(line,primary,view.stroke,view.activeTool);
        shapes.push(shape);
        return shape;
    }
    private void createShape(Point current) {
        Color primary = view.currColor;
        if (view.activeTool == view.TEXT_TOOL) {
            TextBox getInput = new TextBox(this, (int)current.getX(), (int)current.getY());
            if(getInput.completed){shapes.push(new Shape(getInput, primary, view.stroke, 2));}
        }
        this.repaint();

    }
    public void setTool(int tool){
        view.setTool(tool);
    }
    @Override
   public void setPreferredSize(Dimension size){
       model.setPrefSize(size.width, size.height);
   }
   @Override
   public Dimension getPreferredSize(){
       return model.getDimension();

   }
    public void openImage(int index){
        model.openImage(index);
    }

    public List<Shape> getImage() {
       return model.annList.get(model.drawingIndex);
    }

    public List<List<Shape>> getAnnotationList() {
       return model.annList;
    }

    public void setColor(Color inkColor) {
        view.setColor(inkColor);
    }

    public void setThickness(float choice) { view.setThick(choice); }

    //method getShapeAt() from demonstration in Adv. Programming of Interactive Systems
    public Shape getShapeAt(Point position) {
        for (int i = shapes.size() - 1; i >= 0; i--) {
            Shape shape = shapes.get(i);
            if (shape.contains(position)) {
                return shape;
            }
        }

        return null;
    }


    public void select(Shape shape) {
        if (!shapes.contains(shape)) {
            return;
        }
        selectedShapes.add(shape);
        shape.setSelected(true);
        repaint();

    }

    public void deselect(Shape shape) {
        if (!shapes.contains(shape)) {
            return;
        }
        selectedShapes.remove(shape);
        shape.setSelected(false);
        repaint();

    }

    public void toggleSelectionOf(Shape shape) {
        if (!shapes.contains(shape)) {
            return;
        }

        if (selectedShapes.contains(shape)) {
            deselect(shape);
        } else {
            select(shape);
        }
    }

    public void deselectAll() {
        List<Shape> shapesToDeselect = List.copyOf(selectedShapes);
        for (Shape shape : shapesToDeselect) {
            deselect(shape);
        }

    }
    private void deleteSelected(List<Shape> selectedShapes) {
        List<Shape> shapesToDelete = List.copyOf(selectedShapes);
        for(Shape shape: shapesToDelete) {
            deselect(shape);
            shapes.remove(shape);
        }
    }


    private void startDraggingShapes(List<Shape> shapesToDrag) {
        this.draggedShapes.addAll(shapesToDrag);
    }

    private void stopDraggingShapes() {
        this.draggedShapes.clear();
    }

    private void dragShapes(Point currentMousePosition, Point previousMousePosition) {
        double xDistance = currentMousePosition.getX() - previousMousePosition.getX();
        double yDistance = currentMousePosition.getY() - previousMousePosition.getY();
        for (Shape shape : selectedShapes) {
            shape.translateBy(xDistance, yDistance);
            repaint();
        }
    }

    public void changeSelectedColor() {
        if(selectedShapes.size()==0){return;}
        for(Shape s : selectedShapes){
            s.setColor(view.currColor);
        }
        repaint();
    }

    public void changeSelectedThickness() {
        if(selectedShapes.size()==0){return;}
        for(Shape s : selectedShapes){
            s.setStroke(view.stroke);
        }
        repaint();
    }
}

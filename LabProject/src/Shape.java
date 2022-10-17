import java.awt.*;
import java.util.ArrayList;
import java.util.List;

//adapted from https://github.com/azkevin/swing-drawing-app/blob/master/src/Shape.java#L3
public class Shape {

    private int startX, endX, startY, endY, adjustedStartY;
    private List<Point> line;
    private Color color;
    private BasicStroke stroke;
    private String text;

    private int shape;
    private Font font;
    private boolean selected;
    private boolean notBounded;
    TextBox textBox;


    public Shape(List<Point> line, Color color,BasicStroke stroke, int shape){
        this.line=line;
        startX=(int)line.get(0).getX();
        startY=(int)line.get(0).getY();
        adjustedStartY=startY;
        endX=(int)line.get(0).getX();
        endY=(int)line.get(0).getY();
        this.color = color;
        this.stroke = stroke;
        this.shape = shape;
        selected= false;
    }
    public Shape(TextBox textBox, Color color, BasicStroke stroke, int shape){
        this.textBox=textBox;
        this.startX = textBox.getMousePoint().x;
        this.startY = textBox.getMousePoint().y;
        this.endX = startX+textBox.view.textWidth;
        this.font = textBox.getInputFont();
        this.endY =startY+textBox.view.textHeight;
        this.color = color;
        this.stroke = stroke;
        this.shape = shape;
        this.text = textBox.getInputText();
        notBounded=true;
        selected= false;
    }

    public int getShape(){
        return this.shape;
    }
    public String getText() {
        return this.text;
    }
    public Font getFont() {
        return this.font;
    }
    public int getStartX(){
        return this.startX;
    }
    public int getEndX(){
        return this.endX;
    }
    public int getStartY(){
        return this.startY;
    }
    public int getEndY(){
        return this.endY;
    }
    public Color getColor(){
        return this.color;
    }
    public BasicStroke getStroke(){
        return this.stroke;
    }


    public boolean contains(Point position) {
        int x = position.x;
        int y = position.y;
        if(x>=Math.min(this.startX,this.endX) && x<=Math.max(this.startX,this.endX) && y>= Math.min(this.adjustedStartY,this.endY) && y<=Math.max(this.adjustedStartY,this.endY) ){
            return true;
        }
        return false;
    }

    public void translateBy(double xDistance, double yDistance) {
        if(this.shape!=2){
            List<Point> oldLine=List.copyOf(line);
            line.clear();
            for (Point p : oldLine){
                line.add(new Point((int)(p.getX()+xDistance),(int)(p.getY()+yDistance)));
            }
        }
        startX = startX + (int) xDistance;
        startY = startY + (int) yDistance;
        adjustedStartY=adjustedStartY+(int) yDistance;
        endX = endX + (int) xDistance;
        endY = endY + (int) yDistance;

    }

    public void setSelected(boolean b) {
        selected=b;
    }

    public boolean getSelected() {
        return selected;
    }

    public List <Point> getLine() {
        return line;
    }

    public void addPoint(Point point){
        if(this.shape==0) {
            line.add(point);
            startX = Math.min(startX, (int) point.getX());
            startY = Math.min(startY, (int) point.getY());
            adjustedStartY=startY;
            endX = Math.max(endX, (int) point.getX());
            endY = Math.max(endY, (int) point.getY());
        }else if(this.shape==1) {
            Point start=line.get(0);
            line.clear();
            line.add(start);
            line.add(point);
            startX = Math.min(startX, (int) point.getX());
            startY = Math.min(startY, (int) point.getY());
            adjustedStartY=startY;
            endX = Math.max(endX, (int) point.getX());
            endY = Math.max(endY, (int) point.getY());
        }

    }

    public void setStroke(BasicStroke stroke) {
        this.stroke=stroke;
    }

    public void setColor(Color currColor) {
        this.color=currColor;
    }

    public void setFont(Font newFont) {
        this.font=newFont;
    }

    public void updateBounds(int xCoord, int yCoord) {
        //worked but moved with xCoord, yCoord=end values
        if(notBounded) {
            endX = xCoord + Math.min(textBox.view.textWidth, textBox.getSizeLimit().width);
            adjustedStartY = startY - (textBox.view.textHeight);
            endY = yCoord;
        }
        notBounded=false;
    }

    public int getAdjustedY() {
        return adjustedStartY;
    }
}
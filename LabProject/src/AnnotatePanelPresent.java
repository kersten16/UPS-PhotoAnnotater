import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;
import java.util.Stack;
import java.util.List;

public class AnnotatePanelPresent {
    public final int CURSOR_TOOL = -1;
    public final int PEN_TOOL = 0;
    public final int LINE_TOOL = 1;
    public final int TEXT_TOOL = 2;

    public final int FREE = 0;
    public final int LINE = 1;
    public final int TEXT = 2;

    public BasicStroke stroke = new BasicStroke((float) 2);
    public BufferedImage canvas;
    Graphics2D graphics2D;
    public int activeTool = -1;

    public Color currColor;
    public AnnotatePanel control;
    public AnnotatePanelPresent(AnnotatePanel controller) {
        control = controller;
        control.setDoubleBuffered(true);
        control.setBackground(Color.WHITE);
        currColor = Color.BLACK;
        control.setFocusable(true);
        control.requestFocus();
        control.shapes = new Stack<Shape>();
    }

    public void paint(Graphics g) {
        if(canvas == null){
            canvas = (BufferedImage) control.frame.getImage();
            graphics2D = canvas.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            clear();
        }
        g.drawImage(canvas, 0, 0, null);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for(Shape s : control.shapes) {
            if (s.getSelected()) {
                g2.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke((float) 2));
                g2.draw(new Rectangle(s.getStartX(), s.getAdjustedY(), Math.abs(s.getStartX()-s.getEndX()), Math.abs(s.getAdjustedY()-s.getEndY())));
            }
            g2.setColor(s.getColor());
            g2.setStroke(s.getStroke());
            if (s.getShape() == LINE || s.getShape()==FREE) {
                List<Point> line = s.getLine();

                for (int index = 1; index < line.size(); index++) {
                    int currX = (int) line.get(index).getX();
                    int currY = (int) line.get(index).getY();
                    g2.drawLine((int) line.get(index - 1).getX(), (int) line.get(index - 1).getY(), currX, currY);
                }
            } else if (s.getShape() == TEXT) {
                //flag for off case when a very long word is the last word to add to the line
                boolean wordNotPrinted=true;
                int xCoord = s.getStartX();
                int yCoord = s.getStartY();
                String[] words = s.getText().split(" ");
                String currLine = "";
                g.setFont(s.getFont());
                s.textBox.view.textWidth = g.getFontMetrics().stringWidth(s.getText());
                s.textBox.view.textHeight = g.getFontMetrics().getHeight();
                s.textBox.setFont(s.getFont());
                for(String word : words) {
                    //if what has been read plus the next word is too long
                    if( g.getFontMetrics().stringWidth(currLine + " " + word) >= s.textBox.getSizeLimit().width ) {
                        //if we have already read some words (not big chunk)
                        if(currLine != "") {
                            g.drawString(currLine,xCoord, yCoord);
                            yCoord+=s.textBox.view.textHeight;
                            currLine = word;
                            continue;
                        }
                        //big chunk of letters
                        else{
                            //split letter by letter, add full line and then start currLine as left over
                            String[]chars=word.split("(?!^)");
                            for(String character: chars){
                                //if next letter is too big
                                if( g.getFontMetrics().stringWidth(currLine + " " + character) >= s.textBox.getSizeLimit().width ) {
                                    if (currLine != "") {
                                        g.drawString(currLine, xCoord, yCoord);
                                        yCoord += s.textBox.view.textHeight;
                                        currLine = character;
                                        continue;
                                    }
                                    //if a single letter is too big, error message
                                    else{
                                        JDialog warning = new JDialog(control.frame.parent,"Error");
                                        warning.add(new JLabel("<html>The image is not large enough for your request. Please choose a smaller font size.</html>"));
                                        return;
                                    }
                                }
                                //if its not too big, add it to the line
                                currLine = currLine + character;
                            }
                            wordNotPrinted=false;
                        }

                    }
                    if(currLine != ""){
                        currLine = currLine + " ";
                    }
                    if (wordNotPrinted) {
                        currLine = currLine + word;
                    }
                    wordNotPrinted=true;
                }
                //add any leftovers at end
                if(currLine != ""){
                    if( g.getFontMetrics().stringWidth(currLine) >= s.textBox.getSizeLimit().width ) {
                        //split letter by letter, add full line and then start currLine as left over
                        String[] chars = currLine.split("(?!^)");
                        currLine = "";
                        for (String character : chars) {
                            //if next letter is too big
                            if (g.getFontMetrics().stringWidth(currLine + character) >= s.textBox.getSizeLimit().width) {
                                if (currLine != "") {
                                    g.drawString(currLine, xCoord, yCoord);
                                    yCoord += s.textBox.view.textHeight;
                                    currLine = character;
                                    continue;
                                }
                                //if a single letter is too big, error message
                                else {
                                    JDialog warning = new JDialog(control.frame.parent, "Error");
                                    warning.add(new JLabel("<html>The image is not large enough for your request. Please choose a smaller font size.</html>"));
                                    return;
                                }
                            }
                            //if its not too big, add it to the line
                            currLine = currLine + character;

                        }
                    }
                    g.drawString(currLine, xCoord, yCoord);
                    s.updateBounds(xCoord,yCoord);
                }
            }

        }


    }


    public void setTool(int tool) {
        this.activeTool = tool;
        if( tool>=0){
            control.frame.setProgress("Colour :"+currColor+"  Thickness: "+stroke.getLineWidth());
        }
        else{
            control.frame.setProgress("Cursor Mode");
        }
    }
    public void clear(){
//        graphics2D.setPaint(Color.WHITE);
//        graphics2D.fillRect(20, 10, control.getPreferredSize().width, control.getPreferredSize().height);
        control.shapes.removeAllElements();
        graphics2D.setColor(currColor);
    }

    public void setColor(Color choice){
        currColor = choice;
        control.frame.setProgress("Colour :"+currColor+"  Thickness: "+stroke.getLineWidth());
        graphics2D.setColor(currColor);

    }
    public void setCanvas(int width, int height){
        canvas = (BufferedImage) control.frame.getImage();
        graphics2D = canvas.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        clear();

    }

    public void setThick(float choice) {
        stroke=new BasicStroke(choice);
        control.frame.setProgress("Colour :"+currColor+"  Thickness: "+stroke.getLineWidth());
    }
}
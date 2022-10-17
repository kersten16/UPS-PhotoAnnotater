//VIEW
import javax.swing.*;
import javax.swing.plaf.basic.BasicToolBarUI;
import java.awt.*;
//create menu bar, statusbar,photocomponent, and tool bar in here
//stores reference to controller or pass as attribute
//add listeners to buttons here as well

public class appPresent {
    public ImageIcon strokeIcon=new ImageIcon(new ImageIcon("Images/line-thickness.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
    public ImageIcon colorIcon= new ImageIcon(new ImageIcon("Images/wheel.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));
    public ImageIcon lineIcon= new ImageIcon(new ImageIcon("Images/line.png").getImage().getScaledInstance(30, 30, Image.SCALE_DEFAULT));

    static JLabel progressLabel;
    static PhotoViewer imgPanel;
    public JPanel screenPanel;
    public static JToolBar annotateTools;

    public static JToolBar drawOptions;
    static appControl control;

    static ButtonGroup toolHeads ;

    public appPresent(appControl controller){
        control=controller;
        //windowUI = new JFrame("Photo Browser");
        createUI();

    }




    public void createUI (){
        control.setPreferredSize(new Dimension(800,600));
        control.setMinimumSize(new Dimension(200,100));
        control.setMaximumSize(new Dimension(2000,1000));
        screenPanel= new JPanel(new BorderLayout());
        JMenuBar menu=addMenu();
        control.setJMenuBar(menu);

        JToolBar toolbar= addToolBar();
        screenPanel.add(toolbar, BorderLayout.WEST);

        imgPanel = new PhotoViewer(control );
        //imgPanel.setPreferredSize(new Dimension(windowUI.getWidth(),windowUI.getHeight()));
        screenPanel.add(new JScrollPane(imgPanel), BorderLayout.CENTER);
        createDrawToolBar();
        createAnnotateToolBar();
        screenPanel.add(drawOptions, BorderLayout.EAST);
        screenPanel.add(annotateTools, BorderLayout.NORTH);
        if ( imgPanel.isFlipped()){
            annotateTools.setVisible(true);
            drawOptions.setVisible(false);
        }
        else {
            annotateTools.setVisible(false);
            drawOptions.setVisible(false);
        }
        progressLabel = new JLabel ("temp Progress");
        screenPanel.add(progressLabel, BorderLayout.SOUTH);

        control.add(screenPanel);
        control.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    }


    public void addComponent( JComponent addOn){
        control.add(addOn,BorderLayout.NORTH);
    }

    public void show(){
        control.pack();
        control.setVisible(true);
    }


    private JToolBar addToolBar() {
        JToolBar toolBar= new JToolBar("Filter", 1);
        JToggleButton people = new JToggleButton("People");
        people.setMargin(new Insets(0, 3, 0, 3));
        JToggleButton places = new JToggleButton("Places");
        places.setMargin(new Insets(0, 4, 0, 4));
        JToggleButton food = new JToggleButton("Food");
        food.setMargin(new Insets(0, 9, 0, 9));
        JToggleButton animals = new JToggleButton("Animals");
        animals.setMargin(new Insets(0, 0, 0, 1));

        JToggleClickEvent peopleToggle= new JToggleClickEvent(people,control);
        JToggleClickEvent placesToggle= new JToggleClickEvent(places, control);
        JToggleClickEvent foodToggle= new JToggleClickEvent(food, control);
        JToggleClickEvent animalsToggle= new JToggleClickEvent(animals, control);

        toolBar.add(people);
        toolBar.add(places);
        toolBar.add(food);
        toolBar.add(animals);

        return toolBar;
    }
    static void createAnnotateToolBar() {
        annotateTools = new JToolBar();
        //add text option, draw option (maybe different pens)
        JToggleButton drawBtn = new JToggleButton("Draw");

        JToggleButton textBtn = new JToggleButton("Text");
        JButton clearBtn = new JButton("Clear");
        JToggleClickEvent drawClick = new JToggleClickEvent(drawBtn,imgPanel.getAnnotate());
        JToggleClickEvent textClick = new JToggleClickEvent(textBtn, imgPanel.getAnnotate());
        JButtonClickEvent clearClick = new JButtonClickEvent(clearBtn, imgPanel.getAnnotate());

        //CHOSE A CLOSE BUTTON IN ADDITION TO DOUBLE CLICK TO CONSIDER PEOPLE WHO WANT TO FREE HAND DRAW DOTS
        // SUCH AS DOTTING THE LETTER I
        JButton closeAnn = new JButton("Close");
        JButtonClickEvent closeAnnClick= new JButtonClickEvent(closeAnn, imgPanel.getAnnotate());
        toolHeads = new ButtonGroup(){
            @Override
            public void setSelected(ButtonModel model, boolean selected) {
                if (selected) {
                    super.setSelected(model, selected);
                } else {
                    clearSelection();
                }
            }
        };
        toolHeads.add(drawBtn);
        toolHeads.add(textBtn);

        annotateTools.add(drawBtn);
        annotateTools.add(textBtn);
        annotateTools.add(clearBtn);
        annotateTools.add(closeAnn);
    }


    private JMenuBar addMenu(){
        JMenuBar menuBar = new JMenuBar();

        // create a menu
        JMenu fileMenu = new JMenu("File");
        JMenu viewMenu = new JMenu("View");

        // create menuitems
        JMenuItem Import = new JMenuItem("Import");
        JMenuItem Delete = new JMenuItem("Delete");
        JMenuItem Quit = new JMenuItem("Quit");
        JRadioButtonMenuItem soloButton = new JRadioButtonMenuItem("Photo Viewer");
        JRadioButtonMenuItem gridButton = new JRadioButtonMenuItem("Browser");
        JMenuClickEvent viewClick= new JMenuClickEvent(soloButton,control);
        JMenuClickEvent viewClick2= new JMenuClickEvent(gridButton,control);
        JMenuClickEvent exitClick= new JMenuClickEvent(Quit,control);
        JMenuClickEvent importClick= new JMenuClickEvent(Import,control);
        JMenuClickEvent deleteClick= new JMenuClickEvent(Delete,control);
        // add menu items to menu
        fileMenu.add(Import);
        fileMenu.add(Delete);
        fileMenu.add(Quit);

        ButtonGroup viewBtns = new ButtonGroup();
        viewBtns.add(soloButton);
        viewBtns.add(gridButton);
        soloButton.setSelected(true);
        viewMenu.add(soloButton);
        viewMenu.add(gridButton);

        // add menu to menu bar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);

        // add menubar to frame
        return menuBar;
    }
    public void createDrawToolBar() {
        drawOptions = new JToolBar(1);
        drawOptions.setFloatable(true);
        JButton inkBtn = new JButton(colorIcon);
        inkBtn.setActionCommand("Color");
        JButton thickBtn = new JButton(strokeIcon);
        thickBtn.setActionCommand("Stroke");
        JButtonClickEvent inkClick = new JButtonClickEvent(inkBtn,imgPanel.getAnnotate());
        JButtonClickEvent thickClick = new JButtonClickEvent(thickBtn,imgPanel.getAnnotate());
        JToggleButton lineBtn = new JToggleButton(lineIcon);
        lineBtn.setActionCommand("Line");
        JToggleClickEvent lineClick = new JToggleClickEvent(lineBtn,imgPanel.getAnnotate());
        drawOptions.add(inkBtn);
        drawOptions.add(thickBtn);
        drawOptions.add(lineBtn);

    }

public void changeToolBarVisible(){
        annotateTools.setVisible(!annotateTools.isVisible());
}
    public static void setProgress(String update){
        progressLabel.setText(update);
    }
    public static String getStatus(){
        return progressLabel.getText();
    }


}

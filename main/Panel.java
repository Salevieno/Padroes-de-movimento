import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;



public class Panel extends JPanel implements MouseListener
{

    private final Dimension borderOffset = new Dimension(100, 100) ;
    public final Dimension innerSize = Util.subtractDim(Main.size, Util.sumDim(Main.topBarSize, borderOffset)) ;
    private final Rectangle innerScreen ;
    private Point mousePos = new Point(0, 0) ;

    private int showForce = 0 ;
    private int showAcc = 0 ;
    private int showSpeed = 0 ;


    // private static final Image redBallButton = Util.loadImage("./redBallButton.png") ;
    // private static final Image redBallSelectedButton = Util.loadImage("./redBallSelectedButton.png") ;
    // private static final Image blueBallButton = Util.loadImage("./blueBallButton.png") ;
    // private static final Image blueBallSelectedButton = Util.loadImage("./blueBallSelectedButton.png") ;
    // private static final Image greenBallButton = Util.loadImage("./greenBallButton.png") ;
    // private static final Image greenBallSelectedButton = Util.loadImage("./greenBallSelectedButton.png") ;

    private List<Entity> entities;

    public Panel()
    {
        addMouseListener(this) ;
        innerScreen = new Rectangle(new Point(borderOffset.width / 2, borderOffset.height / 2), innerSize) ;
        entities = new ArrayList<>() ;

        new CustomButton(new Point(100, 0), "viewForce", "", () -> { showForce = (showForce + 1) % 3 ;}) ;
        new CustomButton(new Point(160, 0), "viewAcc", "", () -> { showAcc = (showAcc + 1) % 3 ;}) ;
        new CustomButton(new Point(220, 0), "viewSpeed", "", () -> { showSpeed = (showSpeed + 1) % 3 ;}) ;
        new CustomButton(new Point(400, 0), "redBall", "", () -> { entities.add(new Entity(randomPos(), 0, Color.red)) ;}) ;
        new CustomButton(new Point(340, 0), "blueBall", "", () -> { entities.add(new Entity(randomPos(), 1, Color.blue).setSpeed(new Point2D.Double(10.0, 0.0))) ;}) ;
        // new CustomButton(new Point(280, 0), "greenBall", "", () -> { entities.add(new Entity(randomPos(), 2, Color.green).setSpeed(new Point2D.Double(2.0, 0.0))) ;}) ;
        new CustomButton(new Point(280, 0), "greenBall", "", () -> { entities.add(new Entity(randomPos(), 3, Color.orange.darker()).setSpeed(new Point2D.Double(2.0, 0.0))) ;}) ;
    }

    public Point randomPos() { return Util.randomPos(innerScreen.getLocation(), innerSize) ;}

    public Rectangle getInnerScreen() { return innerScreen ;}

    public void run()
    {
        updateMousePos() ;

        Draw.rect(new Point(0, 0), getSize(), 0, Color.black, Color.black);
        Draw.rect(new Point(borderOffset.width / 2, borderOffset.height / 2), innerSize, 2, null, Color.white);
        entities.forEach(Entity::updateForce);
        entities.forEach(Entity::applyForce);
        entities.forEach(Entity::applyAcceleration);
        // entities.forEach(entity -> entity.updateSpeedIfHitScreen(new Point(borderOffset.width / 2, borderOffset.height / 2), innerSize)) ;
        entities.forEach(Entity::move);
        entities.forEach(Entity::display);
        entities.forEach(entity -> entity.drawForce(showForce)) ;
        entities.forEach(entity -> entity.drawAcceleration(showAcc)) ;
        entities.forEach(entity -> entity.drawSpeed(showSpeed)) ;
        
        CustomButton.getAll().forEach(button -> button.display(mousePos)) ;

    }

    public void updateMousePos() { mousePos = Util.getMousePos(this) ;}
    
    @Override
    protected void paintComponent(Graphics graphs)
    {

        super.paintComponent(graphs);
        Draw.setGraphs(graphs);
        run();

    }

    @Override
    public void mouseClicked(MouseEvent event)
    {
        CustomButton.getAll().stream().filter(button -> button.isHovered(mousePos)).forEach(CustomButton::act) ;
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        
    }

    @Override
    public void mousePressed(MouseEvent arg0) {

    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        
    }
}
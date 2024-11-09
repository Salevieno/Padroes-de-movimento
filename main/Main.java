import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Main extends JFrame implements ActionListener
{
    private Panel panel = new Panel();
    private Timer timer = new Timer(30, this) ;
    private static final Point topLeft = new Point(100, 100) ;
    public static final Dimension size = new Dimension(1000, 800) ;
    public static final Dimension topBarSize = new Dimension(10, 45) ;

    public Main()
    {
        setLocation(topLeft);
        setSize(size);
        add(panel);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        timer.start();
    }

    public static void main(String[] args)
    {
        new Main();
    }

    public static Point getMousePos() { return Util.translate(MouseInfo.getPointerInfo().getLocation(), -topLeft.x - topBarSize.width, -topLeft.y - topBarSize.height) ;}

    @Override
    public void actionPerformed(ActionEvent event)
    {
        repaint() ;
    }
}
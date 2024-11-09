import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;

public class Entity
{
    private int time;
    private Point pos ;
    private Point2D.Double speed ;
    private Point2D.Double acc ;
    private Point2D.Double force ;
    private double mass ;
    private int movePattern ;
    private Color color ;

    private static final int cicleTime = 360;
    private static Set<Entity> allEntities = new HashSet<>();

    public Entity(Point pos, int movePattern, Color color)
    {

        this.pos = new Point(pos);
        this.movePattern = movePattern;
        this.color = color;

        time = 0;
        speed = new Point2D.Double(0, 0) ;
        acc = new Point2D.Double(0, 0) ;
        force = new Point2D.Double(0, 0) ;
        mass = 1.0 ;

        allEntities.add(this) ;

    }

    public void drawForce(int showForce)
    {
        switch (showForce)
        {
            case 0: return ;
            case 1: Draw.vector(pos, scaledForce(), Color.magenta, true, true) ; return ;
            default: Draw.vector(pos, scaledForce(), Color.magenta, false, true) ; return ;
        }
    }
    public void drawAcceleration(int showAcc)
    {
        switch (showAcc)
        {
            case 0: return ;
            case 1: Draw.vector(pos, scaledAcceleration(), Color.yellow, true, true) ; return ;
            default: Draw.vector(pos, scaledAcceleration(), Color.yellow, false, true) ; return ;
        }
    }
    public void drawSpeed(int showSpeed)
    {
        switch (showSpeed)
        {
            case 0: return ;
            case 1: Draw.vector(pos, scaledSpeed(), Color.blue, true, true) ; return ;
            default: Draw.vector(pos, scaledSpeed(), Color.blue, false, true) ; return ;
        }
    }

    private Point2D.Double scaledForce() { return new Point2D.Double(100 * force.x, 100 * force.y) ;}
    private Point2D.Double scaledAcceleration() { return new Point2D.Double(80 * acc.x, 80 * acc.y) ;}
    private Point2D.Double scaledSpeed() { return new Point2D.Double(5 * speed.x, 5 * speed.y) ;}

    private void incTime(int cicleTime) { time = (time + 1) % cicleTime ;}

    public void updateForce()
    {
        switch(movePattern)
        {
            case 0:
                double angle = Util.calcAngle(Main.getMousePos(), pos) ;
                force.x = 0.1 * -Math.cos(angle) ;
                force.y = 0.1 * -Math.sin(angle) ;
                return ;

            case 1:
                angle = Util.calcAngle(new Point(300, 300), pos) ;
                force.x = 0.6 * -Math.cos(angle) ;
                force.y = 0.6 * -Math.sin(angle) ;
                return ;

            case 2:
                force.x = 0 ;
                force.y = 0.6 * Math.sin(10 * 2 * Math.PI * time / 360 + Math.PI / 2) ;
                return ;

            case 3:
                force.x = 0 ;
                force.y = 0 ;
                allEntities.forEach(entity -> {
                    if (!this.equals(entity))
                    {
                        force.x += Math.signum(entity.pos.x - pos.x) * 0.01 * mass * 1.0 / Math.pow(Util.dist1D(pos.x, entity.pos.x), 2) ;
                        force.y += Math.signum(entity.pos.y - pos.y) * 0.01 * mass * 1.0 / Math.pow(Util.dist1D(pos.y, entity.pos.y), 2) ;
                    }
                });
                return ;

            default: return ;
        }
    }

    public void applyForce()
    {
        acc.x = force.x / mass ;
        acc.y = force.y / mass ;
    }

    public void applyAcceleration()
    {
        speed.x += acc.x * 1 ;
        speed.y += acc.y * 1 ;
    }

    public void move()
    {
        incTime(cicleTime) ;
        pos.x += speed.x * 1 ;
        pos.y += speed.y * 1 ;
    }

    // public void updateSpeedIfHitScreen(Point screenTopLeft, Dimension screenSize)
    // {
    //     if (pos.x <= screenTopLeft.x | screenTopLeft.x + screenSize.width <= pos.x)
    //     {
    //         speed.x *= -1 ;
    //     }
    //     if (pos.y <= screenTopLeft.y | screenTopLeft.y + screenSize.height <= pos.y)
    //     {
    //         speed.y *= -1 ;
    //     }
    // }

    // public void moveWithinScreen(Point topLeft, Dimension screenSize)
    // {
    //     incTime(cicleTime0) ;
    //     Point newPos = new Point((int) (pos.x + speed.x * 1), (int) (pos.y + speed.y * 1)) ;
    //     // pos.x += speed.x * 1 ;
    //     // pos.y += speed.y * 1 ;
    //     if (Util.isInside(newPos, topLeft, screenSize))
    //     {
    //         System.out.println("outside screen");
    //         pos.x = newPos.x ;
    //         pos.y = newPos.y ;
    //     }
    // }

    public void display() {Draw.circle(pos, 20, color) ;}

    public Entity setSpeed(Point2D.Double speed) { this.speed = speed ; return this ;}

    // public void displayTrajectory(Point mousePos)
    // {
    //     List<Point> points = new ArrayList<>() ;
    //     for (int i = 0 ; i <= 10 - 1; i += 1)
    //     {
    //         double x = pos.x + (mousePos.x - pos.x) * i / 9.0 ;
    //         double y = Util.calcPointInSymmetricParabola(pos, mousePos, x) ;
    //         points.add(new Point((int) x, (int) y)) ;
    //     }
        
    //     Draw.dashedPolyline(points, Color.white) ;
    // }

}
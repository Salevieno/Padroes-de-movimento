import java.awt.Point;

public class TestUtil
{
    
    public static void testCalcAngle()
    {
        Point refPoint = new Point(300, 300) ;
        int radius = 100 ;
        System.out.println(Util.calcAngle(refPoint, Util.polarToCartesian(refPoint, radius, 0)) * 360 / (2 * Math.PI) + " : " + 0.0);
        System.out.println(Util.calcAngle(refPoint, Util.polarToCartesian(refPoint, radius, Math.PI / 4)) * 360 / (2 * Math.PI) + " : " + 45.0);
        System.out.println(Util.calcAngle(refPoint, Util.polarToCartesian(refPoint, radius, Math.PI / 2)) * 360 / (2 * Math.PI) + " : " + 90.0);
        System.out.println(Util.calcAngle(refPoint, Util.polarToCartesian(refPoint, radius, 3 * Math.PI / 4)) * 360 / (2 * Math.PI) + " : " + 135.0);
        System.out.println(Util.calcAngle(refPoint, Util.polarToCartesian(refPoint, radius, Math.PI)) * 360 / (2 * Math.PI) + " : " + 180.0);
        System.out.println(Util.calcAngle(refPoint, Util.polarToCartesian(refPoint, radius, 7 * Math.PI / 6)) * 360 / (2 * Math.PI) + " : " + 210.0);
        System.out.println(Util.calcAngle(refPoint, Util.polarToCartesian(refPoint, radius, 5 * Math.PI / 4)) * 360 / (2 * Math.PI) + " : " + 225.0);
        System.out.println(Util.calcAngle(refPoint, Util.polarToCartesian(refPoint, radius, 3 * Math.PI / 2)) * 360 / (2 * Math.PI) + " : " + 270.0);
        System.out.println(Util.calcAngle(refPoint, Util.polarToCartesian(refPoint, radius, 7 * Math.PI / 4)) * 360 / (2 * Math.PI) + " : " + 315.0);
    }
}
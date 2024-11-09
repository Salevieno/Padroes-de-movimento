import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.List;



public abstract class Draw
{

    private static Graphics2D graphs;

    public static void setGraphs(Graphics newGraphs) { graphs = (Graphics2D) newGraphs ;}
    
    public static void image(Image image, Point pos, Align align)
	{
		image(image, pos, 0, Scale.unit, false, false, align, 1) ;
	}

    public static void image(Image image, Point pos, double angle, Scale scale, boolean flipH, boolean flipV, Align align, double alpha)
	{       
		if (image == null) { System.out.println("Tentando desenhar imagem nula na pos " + pos) ; return ; }
		
		Dimension size = new Dimension((int)(scale.x * image.getWidth(null)), (int)(scale.y * image.getHeight(null))) ;
		size = new Dimension ((!flipH ? 1 : -1) * size.width, (!flipV ? 1 : -1) * size.height) ;
		Point offset = Util.offsetForAlignment(align, size) ;
		AffineTransform backup = graphs.getTransform() ;
		graphs.transform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, pos.x, pos.y)) ;
		graphs.setComposite(AlphaComposite.SrcOver.derive((float) alpha)) ;
		
		graphs.drawImage(image, pos.x + offset.x, pos.y + offset.y, size.width, size.height, null) ;
		
		graphs.setComposite(AlphaComposite.SrcOver.derive((float) 1.0)) ;
        graphs.setTransform(backup) ;
	}

    public static void text(Point pos,  double angle, String text, Font font, Color color)
	{
		// by default starts at the left bottom
		// Dimension size = new Dimension(UtilG.TextL(text, font, graphs), UtilG.TextH(font.getSize())) ;
		// Point offset = UtilG.offsetForAlignment(align, size) ;
		// AffineTransform backup = graph2D.getTransform() ;		
		
		graphs.transform(AffineTransform.getRotateInstance(-angle * Math.PI / 180, pos.x, pos.y)) ;

		graphs.setColor(color) ;
		graphs.setFont(font) ;
		graphs.drawString(text, pos.x, pos.y) ;
        
		// graphs.setTransform(backup) ;
	}
    
    public static void line(Point p1, Point p2, Color color)
    {
        graphs.setStroke(new BasicStroke(3));
        graphs.setColor(color) ;
        graphs.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    public static void polyline(List<Point> points, Color color)
    {
        int[] xValues = points.stream().mapToInt(p -> p.x).toArray() ;
        int[] yValues = points.stream().mapToInt(p -> p.y).toArray() ;
        graphs.setStroke(new BasicStroke(3));
        graphs.setColor(color) ;
        graphs.drawPolyline(xValues, yValues, xValues.length) ;
    }

    public static void rect(Point pos, Dimension size, int stroke, Color color, Color contourColor)
	{
		// Rectangle by default starts at the left top
		// Point offset = UtilG.offsetForAlignment(align, size) ;
		int[] Corner = new int[] {pos.x, pos.y} ;
		graphs.setStroke(new BasicStroke(stroke)) ;
		if (color != null)
		{
			graphs.setColor(color) ;
			graphs.fillRect(Corner[0], Corner[1], size.width, size.height) ;
		}
		if (contourColor != null)
		{
			graphs.setColor(contourColor) ;
			graphs.drawRect(Corner[0], Corner[1], size.width, size.height) ;
		}
		graphs.setStroke(new BasicStroke(2)) ;
	}

    public static void circle(Point center, int radius, Color color)
    {
        graphs.setStroke(new BasicStroke(2));
        graphs.setColor(color);
        graphs.fillArc(center.x - radius / 2, center.y - radius / 2, radius, radius, 0, 360) ;
    }

    public static void arrow(Point pos, double angle, double size, Color color)
    {
        double armAngle = Math.PI / 6 ;
        int armSize = (int) (size / 3) ;
        angle = -angle ;
        Point head = Util.polarToCartesian(pos, size, angle) ;
        Point eastArm = Util.polarToCartesian(head, armSize, -Math.PI + angle + armAngle) ;
        Point westArm = Util.polarToCartesian(head, armSize, Math.PI + angle - armAngle) ;

        Draw.line(pos, head, color);
        Draw.line(head, eastArm, color);
        Draw.line(head, westArm, color);
    }

    public static void vector(Point pos, Point2D.Double values, Color color, boolean combined, boolean showText)
    {
        double minSize = 10 ;
        double maxSize = 200 ;
        if (combined)
        {
            Point head = new Point((int) (pos.x + values.x), (int) (pos.y + values.y)) ;
            double intensity = Math.sqrt(Math.pow(values.x, 2) + Math.pow(values.y, 2)) ;
            double size = Math.min(Math.max(intensity, minSize), maxSize) ;

            Draw.arrow(pos, -Util.calcAngle(pos, head), size, color);

            if (!showText) { return ;}
            
            return ;
        }

        Draw.arrow(pos, 0, values.x, color);
        Draw.arrow(pos, -Math.PI / 2, values.y, color);

        if (!showText) { return ;}

    }

    public static void dashedPolyline(List<Point> points, Color color)
    {
        for (int i = 0 ; i <= points.size() - 2; i += 2)
        {
            Draw.line(points.get(i), points.get(i + 1), color) ;
        }
    }
}
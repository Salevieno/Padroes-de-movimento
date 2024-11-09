import java.awt.Color ;
import java.awt.Dimension;
import java.awt.Font ;
import java.awt.FontMetrics ;
import java.awt.Graphics ;
import java.awt.Graphics2D ;
import java.awt.Image ;
import java.awt.MouseInfo ;
import java.awt.Point;
import java.awt.image.BufferedImage ;
import java.io.BufferedReader ;
import java.io.File;
import java.io.FileInputStream ;
import java.io.FileNotFoundException ;
import java.io.IOException ;
import java.io.InputStreamReader ;
import java.math.BigDecimal ;
import java.math.RoundingMode ;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel ;


public abstract class Util
{

	public static Point getMousePos(JPanel panel)
 	{
		Point mousePos = MouseInfo.getPointerInfo().getLocation() ;
		mousePos.translate(-panel.getLocationOnScreen().x, -panel.getLocationOnScreen().y) ;
		return mousePos ;
	} 	

	public static double randomMult(double amplitude) { return Math.max(0, 1 - amplitude + 2 * amplitude * Math.random()) ;}

	public static int randomInt(int min, int max) { return ThreadLocalRandom.current().nextInt(min, max + 1) ;}
	
	public static boolean chance(double chance) { return Math.random() <= chance ;}
	
	public static int randomFromChanceList(List<Double> chances)
	{		
		int number = randomInt(0,  100) ;
		
		int cum = 0 ;
		for (int i = 0 ; i <= chances.size() - 1 ; i += 1)
		{
			if (cum <= number & number <= cum + 100 * chances.get(i)) { return i ;}
			cum += 100 * chances.get(i) ;
		}
		
		return chances.size() - 1 ;
	}
	
	public static boolean isNumeric(String str) { return str.matches("-?\\d+(\\.\\d+)?") ;}  // match a number with optional '-' and decimal.
	
	public static double round(double num, int decimals) { return BigDecimal.valueOf(num).setScale(decimals, RoundingMode.HALF_EVEN).doubleValue() ;}
	
	// public static double calcPointInSymmetricParabola(Point p1, Point p2, double x)
	// {
	// 	if (p1 == null | p2 == null) { return 0 ;}
	// 	if (p1.x == p2.x) { return p1.y ;}
		
	// 	double a, b, c ;
	// 	a = 0.005 ;
	// 	b = (p1.y - p2.y + a * (p2.x * p2.x - p1.x * p1.x)) / (double) (p1.x - p2.x) ;
	// 	c = p1.y - a * p1.x * p1.x - b * p1.x ;

	// 	return a * x * x + b * x + c ;
	// }

	// input output file
	
	public static List<String[]> readcsvFile(String FileName)
	{
        List<String[]> fileContent = new ArrayList<>() ;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(FileName), "UTF-8")))
        {
        	String separator = "," ;
            String line = br.readLine() ;
            while ((line = br.readLine()) != null) 
            {
                fileContent.add(line.split(separator)) ;
            }
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace() ;
        }
        catch (IOException e)
        {
            e.printStackTrace() ;
        }
        return fileContent ;
	}
	
	public static void saveImage(BufferedImage img, String path) throws FileNotFoundException, IOException
    {
		ImageIO.write(img, "png", new File(path + ".png")) ;
    }
	
	public static Image loadImage(String filePath)
	{
		// this is not throwing an exception because it's not loading a file, it's creating a new ImageIcon
		Image image = new ImageIcon(filePath).getImage() ;
		if (image.getWidth(null) == -1 & image.getHeight(null) == -1) { System.out.println("Image not found at " + filePath) ; return null ;}
		
		return image ;

	}
	

	// image and color

	public static BufferedImage toBufferedImage(Image img)
	{
	    if (img instanceof BufferedImage) { return (BufferedImage) img ;}

	    // create a buffered image with transparency
	    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB) ;

	    // draw the image on to the buffered image
	    Graphics2D bGr = bimage.createGraphics() ;
	    bGr.drawImage(img, 0, 0, null) ;
	    bGr.dispose() ;

	    return bimage ;
	}

	public static Color pixelColor(BufferedImage bufferedimage, Point pos)
	{
		int pixel = bufferedimage.getRGB(pos.x, pos.y) ; 
		int red = (pixel & 0x00ff0000) >> 16 ;
		int green = (pixel & 0x0000ff00) >> 8 ;
		int blue = pixel & 0x000000ff ;
		return new Color(red, green, blue) ;
	}
	
	public static Color pixelColor(Image image, Point pos) { return pixelColor(toBufferedImage(image), pos) ;}
	
	public static boolean pixelIsTransparent(Image image, Point pos)
	{
		int pixel = toBufferedImage(image).getRGB(pos.x, pos.y);
		return pixel >> 24 == 0x00 ;
	}
	

	// dimension

	public static Dimension getSize(Image image)
	{
		if (image == null) { return new Dimension() ;}
		
		return new Dimension(image.getWidth(null), image.getHeight(null));
	}
	
    public static Dimension sumDim(Dimension d1, Dimension d2) { return new Dimension(d1.width + d2.width, d1.height + d2.height) ;}

    public static Dimension subtractDim(Dimension d1, Dimension d2) { return new Dimension(d1.width - d2.width, d1.height - d2.height) ;}


	// offset and alignment
	
	public static Point translate(Point originalPoint, int dx, int dy)
	{
		Point newPoint = new Point(originalPoint);
		return new Point(newPoint.x + dx, newPoint.y + dy) ;
	}

	public static Point translate(Point originalPoint, Point delta) { return translate(originalPoint, delta.x, delta.y) ;}

	public static Point translate(Point originalPoint, Dimension size) { return translate(originalPoint, size.width, size.height) ;}
	
	public static Point offsetForAlignment(Align alignment, Dimension size)
	{
		if (size == null) { System.out.println("Offset from pos with null size!") ; return new Point() ;}
		
		switch (alignment)
		{
			case topLeft: return new Point(0, 0) ;
			case centerLeft: return new Point(0, -size.height / 2) ;
			case bottomLeft: return new Point(0, -size.height) ;
			case topCenter: return new Point(-size.width / 2, 0) ;
			case center: return new Point(-size.width / 2, -size.height / 2) ;
			case bottomCenter: return new Point(-size.width / 2, -size.height) ;
			case topRight: return new Point(-size.width, 0) ;
			case centerRight: return new Point(-size.width,  -size.height / 2) ;
			case bottomRight: return new Point(-size.width,  -size.height) ;
			default: return new Point(0, 0) ;
		}
	}

	public static Point calcTopLeft(Point pos, Align alignment, Dimension size)
	{
		if (size == null) { System.out.println("trying to calc top left with null size!") ; return new Point() ;}
		
		Point offset = offsetForAlignment(alignment, size) ;

		return translate(pos, offset) ;
	}
	

	// text

	public static int textL(String text, Font font, Graphics graphs)
	{
		if (graphs == null) { System.out.println("trying to calculate text length with null graphs"); return 0 ;}
		
		FontMetrics metrics = graphs.getFontMetrics(font) ;
		return (int) (metrics.stringWidth(text)) ;
	}
	
	public static int textH(int fontSize) { return (int)(0.8*fontSize) ;}
		
	public static List<String> fitText(String inputText, int maxNumberChars)
	{
		List<String> newstring = new ArrayList<String>() ;
		int CharsExeeding = 0 ;		
		int i = 0 ;
		int FirstChar = 0 ;
		int LastChar = 0 ;
		do
		{
			int af = Math.min(maxNumberChars, Math.min((i + 1)*maxNumberChars, inputText.length() - i*maxNumberChars) + CharsExeeding) ;
			FirstChar = i*maxNumberChars - CharsExeeding ;
			LastChar = FirstChar + af ;
			char[] chararray = new char[maxNumberChars] ;
			inputText.getChars(FirstChar, LastChar, chararray, 0) ;
			if (chararray[LastChar - FirstChar - 1] != ' ' & chararray[LastChar - FirstChar - 1] != '.' & chararray[LastChar - FirstChar - 1] != '?' & chararray[LastChar - FirstChar - 1] != '!' & chararray[LastChar - FirstChar - 1] != '/' & chararray[LastChar - FirstChar - 1] != ':')
			{
				for (int j = chararray.length - 1 ; 0 <= j ; j += -1)
				{
					CharsExeeding += 1 ;
					LastChar += -1 ;
					if (chararray[j] == ' ' | chararray[j] == '.' | chararray[j] == '?' | chararray[j] == '!' | chararray[j] == '/' | chararray[j] == ':')
					{
						int af2 = Math.min(Math.max(0, FirstChar), inputText.length()) ;
						char[] chararray2 = new char[LastChar - af2] ;
						inputText.getChars(Math.min(Math.max(0, FirstChar), inputText.length()), LastChar, chararray2, 0) ;
						newstring.add(String.valueOf(chararray2)) ;
						CharsExeeding += -1 ;
						j = 0 ;
					}
				}
			}
			else
			{
				chararray = new char[LastChar - FirstChar] ;
				inputText.getChars(FirstChar, LastChar, chararray, 0) ;
				newstring.add(String.valueOf(chararray)) ;
			}
			i += 1 ;
		} while(LastChar != inputText.length() & i != inputText.length()) ;
		List<String> newstring2 = new ArrayList<>() ;
		for (int j = 0 ; j <= i - 1 ; j += 1)
		{
			newstring2.add(newstring.get(j)) ;
		}
		return newstring2 ;
	}
	

	// spacing

	public static double calcOffset(int n, double width, double size, double spacing) { return (width - n * size - (n - 1) * spacing) / 2 ;}
	
	/** @return center to center spacing */
	public static double spacing(double width, int n, double size, double offset) { return clearSpacing(width, n, size, offset) + size ;}
	
	/** @return clear spacing */
	public static double clearSpacing(double width, int n, double size, double offset)
	{
		
		if (n <= 0) { return -1 ;}
		if (n == 1)
		{
			return width - 2 * offset ;
		}

		return (width - 2 * offset - n * size) / (n - 1) ;
		
	}
	
	public static Point calcGrid(int numberItems, int maxNumberRows)
	{
		int nCols = Math.max((int) (Math.ceil(numberItems / (double)maxNumberRows)), 1) ;
		int nRows = numberItems / nCols + 1 ;
		
		return new Point(nRows, nCols) ;
	}
	

	// geometry

	public static int randomPos1D(double minPos, int amplitude, int step) { return (int)((amplitude*Math.random() + minPos)/step)*step ;}
	
	public static Point randomPos(Point minPos, Dimension range, Point step) {return new Point(randomPos1D(minPos.x, range.width, step.x), randomPos1D(minPos.y, range.height, step.y)) ;}

	public static Point randomPos(Point minCoord, Dimension range) { return randomPos(minCoord, range, new Point(1, 1)) ;}
	
	public static double dist1D(int pos1, int pos2) { return Math.abs(pos1 - pos2) ;}
	
	public static double dist(Point pos1, Point pos2) { return Math.hypot(pos2.x - pos1.x, pos2.y - pos1.y) ;}
	
    public static double calcAngle(Point center, Point pos)
    {
        if (pos.x == center.x)
        {
            if (center.y == pos.y) { return 0 ;}
            if (center.y <= pos.y) { return Math.PI / 2 ;}
            if (pos.y <= center.y) { return 3 * Math.PI / 2 ;}
        }

        double angle = Math.atan((pos.y - center.y) / (double)(pos.x - center.x)) ;
        if (pos.x <= center.x) { return angle + Math.PI ;}
        if (pos.y <= center.y) { return angle + 2 * Math.PI ;}

        return angle ;
    }

    public static double calcAngle(Point vector) { return calcAngle(new Point(0, 0), vector) ;}
		
    public static Point polarToCartesian(Point center, double radius, double angle) { return new Point((int) (center.x + radius * Math.cos(angle)), (int) (center.y + radius * Math.sin(angle))) ;}

	public static boolean isInside(Point pos, Point rectTopLeft, Dimension size) { return (rectTopLeft.x <= pos.x & pos.x <= rectTopLeft.x + size.width & pos.y <= rectTopLeft.y + size.height & rectTopLeft.y <= pos.y) ;}
	
	public static boolean isWithinRadius(Point objPos, Point targetPos, double radius) { return dist(objPos, targetPos) <= radius ;}

}
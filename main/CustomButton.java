import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

public class CustomButton
{
    private Point pos ;
    private Image image ;
    private Image selectedImage ;
    private Dimension size ;
    private String text ;
    private ButtonAction perform ;

    private static final String imgPath = "../assets/" ;

    private static Set<CustomButton> allButtons = new HashSet<>();
    private static final Font stdFont = new Font("Comic Sans", Font.BOLD, 13) ;
    private static final Image stdImage = Util.loadImage(imgPath + "stdButton.png") ;
    private static final Image stdSelectedImage = Util.loadImage(imgPath + "stdSelectedButton.png") ;

    public CustomButton(Point pos, ButtonAction action)
    {
        new CustomButton(pos, "", "", action) ;
    }

    public CustomButton(Point pos, String imageName, String text, ButtonAction action)
    {
        this.pos = pos ;
        this.image = Util.loadImage(imgPath + imageName + "Button.png") ;
        this.selectedImage = Util.loadImage(imgPath + imageName + "SelectedButton.png") ;
        this.image = this.image != null ? this.image : stdImage ;
        this.selectedImage = this.selectedImage != null ? this.selectedImage : stdSelectedImage ;
        this.size = Util.getSize(this.image) ;
        this.text = text ;
        allButtons.add(this) ;
        this.perform = action ;
    }

    public boolean isHovered(Point mousePos) { return Util.isInside(mousePos, pos, size) ;}

    public void act()
    {
        perform.action() ;
    }

    public void display(Point mousePos)
    {
        Image displayImage = isHovered(mousePos) ? selectedImage : image ;
        Draw.image(displayImage, pos, Align.topLeft) ;
        if (!text.trim().isEmpty())
        {
            Draw.text(pos, 0, text, stdFont, Color.black) ;
        }
    }

    public static Set<CustomButton> getAll() { return new HashSet<CustomButton>(allButtons) ;}
}
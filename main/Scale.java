
public class Scale
{
    public double x ;
    public double y ;

    public static Scale unit = new Scale(1, 1) ;

    public Scale(double x, double y)
    {
        this.x = x ;
        this.y = y ;
    }

}
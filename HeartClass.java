import java.awt.*;

public class HeartClass extends SuitClass
{
    public void draw (Graphics g)
    {
	//Draws a heart
	int iPointsX[] = new int [3];
	int iPointsY[] = new int [3];

	iPointsX [0] = getCentreX () - (int) (getWidth () / 2);
	iPointsY [0] = getCentreY () - (int) (getHeight () / 4);
	iPointsX [1] = getCentreX () + (int) (getWidth () / 2);
	iPointsY [1] = getCentreY () - (int) (getHeight () / 4);
	iPointsX [2] = getCentreX ();
	iPointsY [2] = getCentreY () + (int) (getHeight () / 2);

	g.setColor (getColor ());
	g.fillPolygon (iPointsX, iPointsY, 3);

	g.fillArc (getCentreX () - (int) (getWidth () / 2), getCentreY () - (int) (getHeight () / 2.1),
		(int) (getWidth () / 2), (int) (getHeight () / 2), 0, 180);

	g.fillArc (getCentreX (), getCentreY () - (int) (getHeight () / 2.1),
		(int) (getWidth () / 2), (int) (getHeight () / 2), 0, 180);
    }
}

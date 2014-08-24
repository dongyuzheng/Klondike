import java.awt.*;

public class ClubClass extends SuitClass
{
    public void draw (Graphics g)
    {
	//Draws a club
	int iPointsX[] = new int [3];
	int iPointsY[] = new int [3];

	iPointsX [0] = getCentreX ();
	iPointsY [0] = getCentreY () - (int) (getHeight () / 6);
	iPointsX [1] = getCentreX () + (int) (getWidth () / 6);
	iPointsY [1] = getCentreY () + (int) (getHeight () / 2);
	iPointsX [2] = getCentreX () - (int) (getWidth () / 6);
	iPointsY [2] = getCentreY () + (int) (getHeight () / 2);

	g.setColor (getColor ());
	g.fillPolygon (iPointsX, iPointsY, 3);

	g.fillOval (getCentreX () - (int) (getWidth () / 2), getCentreY () - (int) (getHeight () / 4),
		(int) (getWidth () / 2), (int) (getHeight () / 2));

	g.fillOval (getCentreX (), getCentreY () - (int) (getHeight () / 4),
		(int) (getWidth () / 2), (int) (getHeight () / 2));
	g.fillOval (getCentreX () - (int) (getWidth () / 4), getCentreY () - (int) (getHeight () / 2),
		(int) (getWidth () / 2), (int) (getHeight () / 2));
    }
}

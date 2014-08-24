import java.awt.*;

//Abstract class ShapeClass
public abstract class ShapeClass
{
    //Declare encapsulated data
    private int centreX = 100, centreY = 100,
	width = 50, height = 50;
    private Color iColor = Color.blue;


    //Sets the centre coordinates of the shape to specified inputs (set method)
    public void setCentre (int inputX, int inputY)
    {
	centreX = inputX;
	centreY = inputY;
    }


    //Returns the value of the centreX variable (get method)
    public int getCentreX ()
    {
	return centreX;
    }


    //Returns the value of the centreY variable (get method)
    public int getCentreY ()
    {
	return centreY;
    }


    //Sets the width of the shape to specified value (set method)
    public void setWidth (int input)
    {
	width = input;
    }


    //Returns the value of the width variable (get method)
    public int getWidth ()
    {
	return width;
    }


    //Sets the height of the shape to specified value (set method)
    public void setHeight (int input)
    {
	height = input;
    }


    //Returns the value of the height variable (get method)
    public int getHeight ()
    {
	return height;
    }


    //Sets the color of the shape to the specified value (set method)
    public void setColor (Color input)
    {
	iColor = input;
    }


    //Returns the color of the shape (get method)
    public Color getColor ()
    {
	return iColor;
    }


    //Abstract draw method
    public abstract void draw (Graphics g);

    //Erases the shape
    public void erase (Graphics g)
    {
	Color tempColor = iColor; //creates a temp color that is equal to the shape's current color
	iColor = Color.white; //sets the shape's color to white
	draw (g); //draws the shape in white
	iColor = tempColor; //sets the shape's color equal to the temp color
    }
}

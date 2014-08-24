import java.awt.*;

public class CardClass extends ShapeClass
{
    //Declare encapsulated data
    private int suitValue = 1;
    private int faceValue = 1;
    private Color suitColor = Color.red;
    private boolean faceUp = true;

    //Constructor method
    public CardClass ()
    {
	setCentre (320, 250);
	setHeight (100);
	setWidth (70);
    }


    //Sets the card's suit value to the specified value (set method)
    //Also sets the color accordingly
    //ie. If the user sets the suit to a heart, then the suit color is automatically set to red
    public void setSuitValue (int input)
    {
	suitValue = input;
	//If the suit is a heart or diamond, set the suit's color to red
	if (suitValue == 1 || suitValue == 2)
	{
	    suitColor = Color.red;
	}
	//If the suit is a spade or a club, set the suit's color to black
	else if (suitValue == 3 || suitValue == 4)
	{
	    suitColor = Color.black;
	}
    }


    //Returns the card's suit value
    public int getSuitValue ()
    {
	return suitValue;
    }


    //Sets the card's face value to the specified value
    public void setFaceValue (int input)
    {
	faceValue = input;
    }


    //Returns the card's face value
    public int getFaceValue ()
    {
	return faceValue;
    }


    //Sets the card's size accordingly to one of 4 inputs
    public void setCardSize (int input)
    {
	if (input == 1)
	{
	    setHeight (60);
	    setWidth (42);
	}
	else if (input == 2)
	{
	    setHeight (80);
	    setWidth (56);
	}
	else if (input == 3)
	{
	    setHeight (100);
	    setWidth (70);
	}
	else //4,etc.
	{
	    setHeight (120);
	    setWidth (84);
	}
    }


    //Set the card face up or face down
    public void setFaceUp (boolean input)
    {
	faceUp = input;
    }


    //Returns the card's faceUp value
    public boolean getFaceUp ()
    {
	return faceUp;
    }


    //Converts the card's suit value to a string and returns it
    private String SuitValueToString ()
    {
	if (faceValue == 1)
	{
	    return "A";
	}
	else if (faceValue == 11)
	{
	    return "J";
	}
	else if (faceValue == 12)
	{
	    return "Q";
	}
	else if (faceValue == 13)
	{
	    return "K";
	}
	else if (faceValue >= 2 && faceValue <= 10)
	{
	    return Integer.toString (faceValue);
	}
	else
	{
	    return "";
	}

    }


    //Card's draw method
    public void draw (Graphics g)
    {
	if (faceUp == true) //If the card's faceUp value is set to true, the program will draw the card
	{
	    g.setColor (Color.white);
	    g.fillRect (getCentreX () - getWidth () / 2, getCentreY () - getHeight () / 2,
		    getWidth () + 1, getHeight () + 1);
	    g.setColor (Color.black);
	    g.drawRect (getCentreX () - getWidth () / 2, getCentreY () - getHeight () / 2,
		    getWidth (), getHeight ());

	    g.setColor (suitColor);

	    //Determine's what suit to draw based off of the encapsulated data suitValue
	    if (suitValue == 1)
	    {
		HeartClass heart = new HeartClass ();
		heart.setHeight ((int) getHeight () / 4);
		heart.setCentre (getCentreX (), getCentreY ());
		heart.setColor (suitColor);
		heart.draw (g);
	    }
	    else if (suitValue == 2)
	    {
		DiamondClass diamond = new DiamondClass ();
		diamond.setHeight ((int) getHeight () / 4);
		diamond.setCentre (getCentreX (), getCentreY ());
		diamond.setColor (suitColor);
		diamond.draw (g);
	    }
	    else if (suitValue == 3)
	    {
		SpadeClass spade = new SpadeClass ();
		spade.setHeight ((int) getHeight () / 4);
		spade.setCentre (getCentreX (), getCentreY ());
		spade.setColor (suitColor);
		spade.draw (g);
	    }
	    else if (suitValue == 4)
	    {
		ClubClass club = new ClubClass ();
		club.setHeight ((int) getHeight () / 4);
		club.setCentre (getCentreX (), getCentreY ());
		club.setColor (suitColor);
		club.draw (g);
	    }

	    //Draws the face value of the card in the top left corner
	    g.setFont (new Font ("SanSerif", Font.BOLD, (getHeight () / 4)));
	    g.drawString (SuitValueToString (), getCentreX () - (int) (getWidth () * 0.4),
		    getCentreY () - (int) (getHeight () / 4));
	}
	else //If the card's faceUp value is set to false, the program will draw a blue face down card
	{
	    g.setColor (Color.blue);
	    g.fillRect (getCentreX () - getWidth () / 2, getCentreY () - getHeight () / 2,
		    getWidth (), getHeight ());
	    g.setColor (Color.black);
	    g.drawRect (getCentreX () - getWidth () / 2, getCentreY () - getHeight () / 2,
		    getWidth (), getHeight ());
	}
    }


    //Overrides the erase method in ShapeClass
    //Erases the card by drawing a white rectangle over displayed card
    public void erase (Graphics g)
    {
	g.setColor (Color.white);

	g.fillRect (getCentreX () - getWidth () / 2, getCentreY () - getHeight () / 2,
		getWidth () + 1, getHeight () + 1);
    }
}

import java.awt.*;

public class FoundationClass extends DeckClass
{
    private char foundationSuit = 'h'; //the default suit of foundation is heart
    //h for heart, s for spade, etc.
    public FoundationClass ()
    {
    }


    public FoundationClass (char fType)  //constructor that allows user to set foundation suit
    {
	foundationSuit = fType;
    }


    //////////////////////////////////////////////////////
    public void setFoundationSuit (char input)  //set gets for foundation suit
    {
	foundationSuit = input;
    }


    public char getFoundationSuit ()
    {
	return foundationSuit;
    }


    //////////////////////////////////////////////////////
    public boolean allowCardPlace (CardClass testCard)
    { //checks if card being placed is one greater than the current top card
	if (getDeckSize () + 1 == testCard.getFaceValue ())
	{ //checks if the card's suit matches the foundation's suit
	    if ((testCard.getSuitValue () == 1 && foundationSuit == 'h') ||
		    (testCard.getSuitValue () == 2 && foundationSuit == 'd') ||
		    (testCard.getSuitValue () == 3 && foundationSuit == 's') ||
		    (testCard.getSuitValue () == 4 && foundationSuit == 'c'))
	    {
		return true;
	    }
	}
	return false;
    }


    //////////////////////////////////////////////////////
    public void drawTop (Graphics g)
    {
	if (getDeckSize () > 0)
	{ //draws the first card in foundation
	    CardClass tempCard = getCard (0);
	    tempCard.setCentre (getDeckCentreX (), getDeckCentreY ());
	    tempCard.draw (g);
	}
	else
	{ //if foundation is empty, a large suit shape is drawn (ie. large diamond in the middle of the empty deck)
	    g.setColor (Color.black);
	    g.drawRect (getDeckCentreX () - 35, getDeckCentreY () - 50, 70, 100);
	    if (foundationSuit == 'h')
	    {
		HeartClass heart = new HeartClass ();
		heart.setHeight (75);
		heart.setCentre (getDeckCentreX (), getDeckCentreY ());
		heart.setColor (Color.red);
		heart.draw (g);
	    }
	    else if (foundationSuit == 'd')
	    {
		DiamondClass diamond = new DiamondClass ();
		diamond.setHeight (75);
		diamond.setCentre (getDeckCentreX (), getDeckCentreY ());
		diamond.setColor (Color.red);
		diamond.draw (g);
	    }
	    else if (foundationSuit == 's')
	    {
		SpadeClass spade = new SpadeClass ();
		spade.setHeight (75);
		spade.setCentre (getDeckCentreX (), getDeckCentreY ());
		spade.setColor (Color.black);
		spade.draw (g);
	    }
	    else if (foundationSuit == 'c')
	    {
		ClubClass club = new ClubClass ();
		club.setHeight (75);
		club.setCentre (getDeckCentreX (), getDeckCentreY ());
		club.setColor (Color.black);
		club.draw (g);
	    }
	}
    }
}

import java.awt.*;

public class TableauClass extends DeckClass
{
    public boolean allowCardPlace (CardClass testCard)
    {
	CardClass tempTopCard = getCard (0); //get the card on top of the tableau, where other cards will be placed
	if (getDeckSize () != 0)
	{
	    if (testCard.getFaceValue () + 1 == tempTopCard.getFaceValue ())
	    { //card being placed must be of alternating color from the top card
		if (tempTopCard.getSuitValue () >= 3 && testCard.getSuitValue () <= 2)
		{
		    return true;
		}
		else if (tempTopCard.getSuitValue () <= 2 && testCard.getSuitValue () >= 3)
		{
		    return true;
		}
	    }
	}
	else if (testCard.getFaceValue () == 13)
	{ //if deck is empty, the card being placed must be a king
	    return true;
	}
	return false;
    }


    public void drawTableau (Graphics g)
    { //draws the tableau
	if (getDeckSize () > 0)
	{
	    for (int i = getDeckSize () - 1 ; i >= 0 ; i--)
	    {
		CardClass tempCard = getCard (i);
		tempCard.setCentre (getDeckCentreX (), getDeckCentreY () + (getDeckSize () - i - 1) * 30); //spacing of 30 pixels,y
		tempCard.draw (g);
	    }
	}
	else
	{ //if empty, draw blank rectangle, same as empty deck
	    g.setColor (Color.black);
	    g.drawRect (getDeckCentreX () - 35, getDeckCentreY () - 50, 70, 100);
	}
    }


    public void eraseTableau (Graphics g)
    { //draws white rectangle over the tableau's dimensions
	g.setColor (Color.white);
	g.fillRect (getDeckCentreX () - 35, getDeckCentreY () - 50, 71, 101 + 30 * (getDeckSize () - 1));
    }


    public int isPointInsideNthCard (int mouseX, int mouseY)
    { //returns which card, n'th card, the x,y values are within
	if (mouseX >= (getDeckCentreX () - 35) && mouseX <= (getDeckCentreX () + 35) &&
		mouseY >= 250 && mouseY <= ((getDeckSize () - 1) * 30 + 350)) //checks if mouse is inside the tableau
	{
	    //Check if top card
	    if ((mouseY >= ((getDeckSize () - 1) * 30 + 250)) && mouseY <= ((getDeckSize () - 1) * 30 + 350))
	    {
		return 1;
	    }
	    else
	    { //otherwise
		return getDeckSize () - (int) Math.floor ((mouseY - 250) / 30);
	    }
	}
	return -1; //if x,y not inside this tableau, return -1
    }
}

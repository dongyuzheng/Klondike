import java.awt.*;
import java.util.*;

public class DeckClass
{
    //Declares encapsulated data
    //Declares a vector
    private Vector deck = new Vector (0, 1);
    private int cx = -100;
    private int cy = -100;

    //Constructor method for an empty deck
    public DeckClass ()
    {
    }


    //Constructor method for a standard deck of 52 cards
    public DeckClass (char deckType)
    {
	if (deckType == 's') // std deck
	{
	    for (int x = 1 ; x <= 4 ; x++)
	    {
		for (int y = 1 ; y <= 13 ; y++)
		{
		    CardClass card = new CardClass ();

		    card.setCardSize (3);
		    card.setSuitValue (x);
		    card.setFaceValue (y);
		    card.setFaceUp (false);

		    addCard (card, 51);
		}
	    }
	}
    }


    //Method for generating a random number between two specified numbers
    private int RandInt (int Min, int Max)
    {
	return Min + (int) (Math.random () * ((Max - Min) + 1));
    }


    //Returns the deck size (number of cards in the deck)
    public int getDeckSize ()
    {
	return deck.size ();
    }


    //Adds the specified card to the specified position in the deck
    public void addCard (CardClass cardToAdd, int Pos)
    {
	if (Pos == 0 || deck.size () == 0) //If the deck is empty, adds the card to the top of the deck
	{
	    deck.insertElementAt (cardToAdd, 0);
	}
	else if (Pos > deck.size ()) //If designated position if greater than the number of cards in the deck, the card is added to the end of the deck
	{
	    deck.insertElementAt (cardToAdd, deck.size ());
	}
	else
	{
	    deck.insertElementAt (cardToAdd, Pos); //Adds the card to the specified location
	}
    }


    //Removes the card at the specified location
    public void deleteCard (int Pos)
    {
	if (getDeckSize () > 0)
	{
	    deck.removeElementAt (Pos);
	}
    }


    //Returns the card at the specified location
    public CardClass getCard (int Pos)
    {
	if (getDeckSize () > 0)
	{
	    return (CardClass) deck.elementAt (Pos);
	}
	else
	{
	    return null;
	}
    }


    //Sets the deck's centre coordinates to the specified input
    public void setDeckCentre (int inputx, int inputy)
    {
	cx = inputx;
	cy = inputy;
    }


    //Returns the deck's centreX coordinate
    public int getDeckCentreX ()
    {
	return cx;
    }


    //Returns the deck's centreY coordinate
    public int getDeckCentreY ()
    {
	return cy;
    }


    //Shuffles the deck by removing random cards and readding them back into the deck at a random location
    public void shuffle ()
    {
	if (getDeckSize () > 0)
	{
	    int randPos;
	    for (int i = 0 ; i <= (getDeckSize () * getDeckSize ()) ; i++)
	    {
		randPos = RandInt (0, getDeckSize () - 1);

		CardClass tempCard = getCard (randPos);
		deleteCard (randPos);
		addCard (tempCard, getDeckSize ());
	    }
	}
    }


    //Draws the top card of the deck
    public void drawTop (Graphics g)
    {
	if (getDeckSize () > 0)
	{
	    CardClass tempCard = getCard (0);
	    tempCard.setCentre (cx, cy);
	    tempCard.draw (g);
	}
	else
	{
	    g.setColor (Color.black);
	    g.drawRect (cx - 35, cy - 50, 70, 100);
	}
    }


    //Determines if a certain point is inside the deck's drawn area
    public boolean isPointInside (int inX, int inY)
    {
	if (inX >= cx - 35 && inX <= cx + 35 &&
		inY >= cy - 50 && inY <= cy + 50)
	{
	    return true;
	}
	else
	{
	    return false;
	}
    }
}

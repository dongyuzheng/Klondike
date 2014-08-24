// Klondike Applet Main
// Programmed by Gary Zheng and Ian Yuan
import java.awt.*;
import java.applet.*;
import java.awt.event.*;

public class KlondikeApplet extends Applet
    implements ActionListener, MouseListener, MouseMotionListener

{ //instance variables
    Graphics g;
    BorderLayout lm = new BorderLayout ();

    Panel pNorth = new Panel (); //contains all buttons
    Panel pSouth = new Panel (); //contains all stats excluding mouse stats
    Panel pS1 = new Panel (); //used for spacing
    Panel pS2 = new Panel ();
    Panel pS3 = new Panel ();
    Panel pS4 = new Panel ();
    Panel pS5 = new Panel ();
    Panel pWest = new Panel (); //contains mouse stats

    GridBagConstraints gbc = new GridBagConstraints ();

    Button bNewGame = new Button ("NEW GAME"); //game buttons
    Button bRestart = new Button ("RESTART");
    Button bQuit = new Button ("QUIT");

    Label lPass = new Label ("Pass:"); //stat labels
    Label lStack = new Label ("Stack:");
    Label lTime = new Label ("Time:");
    Label lScore = new Label ("Score:");
    Label lMouse = new Label ("Mouse:");
    Label lMouseX = new Label ("mX:");
    Label lMouseY = new Label ("mY:");

    TextField tfPass = new TextField (2); //stat textfields
    TextField tfStack = new TextField (2);
    TextField tfTime = new TextField (5);
    TextField tfScore = new TextField (2);
    TextField tfMouse = new TextField (8);
    TextField tfMouseX = new TextField (1);
    TextField tfMouseY = new TextField (1);

    int passValue = -1; //stat values
    int stackValue = -1;
    long startTime = System.currentTimeMillis () * -1;
    int scoreValue = -1;
    int mValX = 0;
    int mValY = 0;

    String dragType = ""; //used in placement, dropping, and dragging

    CardClass tempCard; //has variety of purposes, ie. dragging, determining a tableau's top card, etc.
    TableauClass tempTableau; //for >1 tableau dragging

    DeckClass stock; //top left, face down, user draws cards from here
    DeckClass waste; //right of stock, where stock is moved to after being drawn, cards here are face up
    DeckClass restartDeck; //used in Restart button action, to store the original deck used to set up the game

    FoundationClass fd; //foundations top right
    FoundationClass fc;
    FoundationClass fh;
    FoundationClass fs;

    TableauClass t[]; //array of tableau objects

    /////////////////////////////////////////////////////
    public void delay (int iDelayTime)  //used in waste to stock animation
    {
	long lFinalTime = System.currentTimeMillis () + iDelayTime;
	do
	{
	}
	while (lFinalTime >= System.currentTimeMillis ());
    }


    /////////////////////////////////////////////////////
    public void init ()
    { //initialization method
	setSize (800, 800);
	setLayout (lm);
	g = getGraphics ();

	/////////////////////////////////////////////////////
	tempTableau = new TableauClass (); //instantiates objects that will be used

	stock = new DeckClass ();
	waste = new DeckClass ();

	fd = new FoundationClass ('d'); //d = diamond
	fc = new FoundationClass ('c'); //c = club
	fh = new FoundationClass ('h'); //h = heart
	fs = new FoundationClass ('s'); //s = spade

	t = new TableauClass [7]; //7 tableaus
	for (int x = 0 ; x < 7 ; x++)
	{
	    t [x] = new TableauClass (); //keep in mind that arrays start at 0
	}

	/////////////////////////////////////////////////////
	pNorth.setLayout (new GridBagLayout ()); //adds buttons to north of layout with gridbag
	gbc.anchor = GridBagConstraints.CENTER;
	gbc.weightx = 1;
	gbc.weighty = 1;
	gbc.ipadx = 50;
	gbc.ipady = 30;

	pNorth.add (bNewGame, gbc);
	pNorth.add (bRestart, gbc);
	pNorth.add (bQuit, gbc);
	add ("North", pNorth);

	/////////////////////////////////////////////////////
	pSouth.setLayout (new GridLayout ()); //adds stats to south of layout with flow

	pS1.setLayout (new FlowLayout (FlowLayout.LEFT, 1, 20));
	pS1.add (lPass);
	pS1.add (tfPass);

	pS2.setLayout (new FlowLayout (FlowLayout.LEFT, 1, 20));
	pS2.add (lStack);
	pS2.add (tfStack);

	pS3.setLayout (new FlowLayout (FlowLayout.LEFT, 1, 20));
	pS3.add (lTime);
	pS3.add (tfTime);

	pS4.setLayout (new FlowLayout (FlowLayout.LEFT, 1, 20));
	pS4.add (lScore);
	pS4.add (tfScore);

	pS5.setLayout (new FlowLayout (FlowLayout.LEFT, 1, 20));
	pS5.add (lMouse);
	pS5.add (tfMouse);

	pSouth.add (pS1);
	pSouth.add (pS2);
	pSouth.add (pS3);
	pSouth.add (pS4);
	pSouth.add (pS5);

	add ("South", pSouth);

	/////////////////////////////////////////////////////
	pWest.setLayout (new GridLayout (22, 1, 3, 3)); //adds mouse stats west of layout with grid

	pWest.add (lMouseX);
	pWest.add (tfMouseX);
	pWest.add (lMouseY);
	pWest.add (tfMouseY);
	add ("West", pWest);

	/////////////////////////////////////////////////////
	bNewGame.addActionListener (this); //starts the button and mouse listeners
	bRestart.addActionListener (this);
	bQuit.addActionListener (this);
	addMouseListener (this);
	addMouseMotionListener (this);
    } // init method


    /////////////////////////////////////////////////////
    public void refreshStatsRepaint ()
    {
	if (stock.isPointInside (mValX, mValY) == true) //if mouse is inside the stock,
	{ //set the stat stackValue to the number of cards in the stock
	    stackValue = stock.getDeckSize ();
	}
	else if (waste.isPointInside (mValX, mValY) == true) //works same as above, for waste
	{
	    stackValue = waste.getDeckSize ();
	}
	else if (fd.isPointInside (mValX, mValY) == true) //for diamond foundation
	{
	    stackValue = fd.getDeckSize ();
	}
	else if (fh.isPointInside (mValX, mValY) == true) //heart foundation
	{
	    stackValue = fh.getDeckSize ();
	}
	else if (fc.isPointInside (mValX, mValY) == true) //club foundation
	{
	    stackValue = fc.getDeckSize ();
	}
	else if (fs.isPointInside (mValX, mValY) == true) //spade foundation
	{
	    stackValue = fs.getDeckSize ();
	}
	else if (t [0].isPointInsideNthCard (mValX, mValY) != -1) //tableau 1,
	{ //the isPointInsideNthCard(x,y) returns -1 if the mouse is not inside any card within the tableau
	    stackValue = t [0].getDeckSize ();
	}
	else if (t [1].isPointInsideNthCard (mValX, mValY) != -1) //tableau 2
	{
	    stackValue = t [1].getDeckSize ();
	}
	else if (t [2].isPointInsideNthCard (mValX, mValY) != -1)
	{
	    stackValue = t [2].getDeckSize ();
	}
	else if (t [3].isPointInsideNthCard (mValX, mValY) != -1)
	{
	    stackValue = t [3].getDeckSize ();
	}
	else if (t [4].isPointInsideNthCard (mValX, mValY) != -1)
	{
	    stackValue = t [4].getDeckSize ();
	}
	else if (t [5].isPointInsideNthCard (mValX, mValY) != -1)
	{
	    stackValue = t [5].getDeckSize ();
	}
	else if (t [6].isPointInsideNthCard (mValX, mValY) != -1) //tableau 7
	{
	    stackValue = t [6].getDeckSize ();
	}
	else
	{
	    stackValue = -1;
	}

	tfPass.setText (Integer.toString (passValue)); //sets the stat textfields
	tfStack.setText (Integer.toString (stackValue));
	tfTime.setText (Integer.toString ((int) ((System.currentTimeMillis () - startTime) / 1000)));
	tfScore.setText (Integer.toString (scoreValue));
	tfMouseX.setText (Integer.toString (mValX));
	tfMouseY.setText (Integer.toString (mValY));
	repaint (); //repaints the applet
    }


    /////////////////////////////////////////////////////
    public void mouseClicked (MouseEvent e)
    {
    }


    public void mouseEntered (MouseEvent e)
    {
    }


    public void mouseExited (MouseEvent e)
    {
	tfMouse.setText ("Exited");
	mValX = -1;
	mValY = -1;
	refreshStatsRepaint ();
    }


    public void mousePressed (MouseEvent e)
    {
	tfMouse.setText ("Pressed");

	if (stock.isPointInside (mValX, mValY) == true) //move stock to waste
	{
	    if (stock.getDeckSize () != 0)
	    {
		tempCard = stock.getCard (0);
		stock.deleteCard (0);
		tempCard.erase (g);
		tempCard.setFaceUp (true);
		waste.addCard (tempCard, 0);
	    }
	    else
	    {
		while (waste.getDeckSize () != 0) //move waste to stock
		{
		    tempCard = waste.getCard (0);
		    waste.deleteCard (0);
		    tempCard.erase (g);
		    tempCard.setFaceUp (false);
		    stock.addCard (tempCard, 0);
		    refreshStatsRepaint ();
		    delay (60);
		}
		passValue += 1; //increment pass stat by 1
	    }
	}

	if (dragType == "") //not currently dragging
	{
	    if (waste.isPointInside (mValX, mValY) == true && waste.getDeckSize () != 0)
	    { //if mouse is inside waste, and waste is not empty, set dragType to "waste" and let tempCard = card clicked,
		dragType = "waste";
		tempCard = waste.getCard (0); //temp card will be used in dragging
		waste.deleteCard (0);
	    }
	    else if (t [0].isPointInsideNthCard (mValX, mValY) != -1 && t [0].getDeckSize () != 0)
	    { //if mouse inside tableau 1, and it's not empty
		if (t [0].isPointInsideNthCard (mValX, mValY) == 1)
		{ //if first card
		    dragType = "1t0"; //drag single card
		    tempCard = t [0].getCard (0); //temp card will be used in dragging
		    t [0].deleteCard (0);
		}
		else
		{
		    tempCard = t [0].getCard (t [0].isPointInsideNthCard (mValX, mValY) - 1); //used below in getFaceUp()
		    if (tempCard.getFaceUp () == true) //cards dragged must be face up
		    {
			dragType = "t0"; //drag multiple card
			for (int i = 0 ; i < t [0].isPointInsideNthCard (mValX, mValY) ; i++) //copy selected cards to tempTableau
			{
			    tempCard = t [0].getCard (i);
			    tempTableau.addCard (tempCard, i); //used for dragging
			}
			for (int i = 1 ; i <= tempTableau.getDeckSize () ; i++) //deletes copied cards
			{
			    t [0].deleteCard (0);
			}
		    }
		}
	    }
	    else if (t [1].isPointInsideNthCard (mValX, mValY) != -1 && t [1].getDeckSize () != 0) //tableau 2, works same as above
	    {
		if (t [1].isPointInsideNthCard (mValX, mValY) == 1)
		{
		    dragType = "1t1";
		    tempCard = t [1].getCard (0);
		    t [1].deleteCard (0);
		}
		else
		{
		    tempCard = t [1].getCard (t [1].isPointInsideNthCard (mValX, mValY) - 1);
		    if (tempCard.getFaceUp () == true)
		    {
			dragType = "t1";
			for (int i = 0 ; i < t [1].isPointInsideNthCard (mValX, mValY) ; i++)
			{
			    tempCard = t [1].getCard (i);
			    tempTableau.addCard (tempCard, i);
			}
			for (int i = 1 ; i <= tempTableau.getDeckSize () ; i++)
			{
			    t [1].deleteCard (0);
			}
		    }
		}
	    }
	    else if (t [2].isPointInsideNthCard (mValX, mValY) != -1 && t [2].getDeckSize () != 0) //tableau 3
	    {
		if (t [2].isPointInsideNthCard (mValX, mValY) == 1)
		{
		    dragType = "1t2";
		    tempCard = t [2].getCard (0);
		    t [2].deleteCard (0);
		}
		else
		{
		    tempCard = t [2].getCard (t [2].isPointInsideNthCard (mValX, mValY) - 1);
		    if (tempCard.getFaceUp () == true)
		    {
			dragType = "t2";
			for (int i = 0 ; i < t [2].isPointInsideNthCard (mValX, mValY) ; i++)
			{
			    tempCard = t [2].getCard (i);
			    tempTableau.addCard (tempCard, i);
			}
			for (int i = 1 ; i <= tempTableau.getDeckSize () ; i++)
			{
			    t [2].deleteCard (0);
			}
		    }
		}
	    }
	    else if (t [3].isPointInsideNthCard (mValX, mValY) != -1 && t [3].getDeckSize () != 0) //tableau 4
	    {
		if (t [3].isPointInsideNthCard (mValX, mValY) == 1)
		{
		    dragType = "1t3";
		    tempCard = t [3].getCard (0);
		    t [3].deleteCard (0);
		}
		else
		{
		    tempCard = t [3].getCard (t [3].isPointInsideNthCard (mValX, mValY) - 1);
		    if (tempCard.getFaceUp () == true)
		    {
			dragType = "t3";
			for (int i = 0 ; i < t [3].isPointInsideNthCard (mValX, mValY) ; i++)
			{
			    tempCard = t [3].getCard (i);
			    tempTableau.addCard (tempCard, i);
			}
			for (int i = 1 ; i <= tempTableau.getDeckSize () ; i++)
			{
			    t [3].deleteCard (0);
			}
		    }
		}
	    }
	    else if (t [4].isPointInsideNthCard (mValX, mValY) != -1 && t [4].getDeckSize () != 0) //tableau 5
	    {
		if (t [4].isPointInsideNthCard (mValX, mValY) == 1)
		{
		    dragType = "1t4";
		    tempCard = t [4].getCard (0);
		    t [4].deleteCard (0);
		}
		else
		{
		    tempCard = t [4].getCard (t [4].isPointInsideNthCard (mValX, mValY) - 1);
		    if (tempCard.getFaceUp () == true)
		    {
			dragType = "t4";
			for (int i = 0 ; i < t [4].isPointInsideNthCard (mValX, mValY) ; i++)
			{
			    tempCard = t [4].getCard (i);
			    tempTableau.addCard (tempCard, i);
			}
			for (int i = 1 ; i <= tempTableau.getDeckSize () ; i++)
			{
			    t [4].deleteCard (0);
			}
		    }
		}
	    }
	    else if (t [5].isPointInsideNthCard (mValX, mValY) != -1 && t [5].getDeckSize () != 0) //tableau 6
	    {
		if (t [5].isPointInsideNthCard (mValX, mValY) == 1)
		{
		    dragType = "1t5";
		    tempCard = t [5].getCard (0);
		    t [5].deleteCard (0);
		}
		else
		{
		    tempCard = t [5].getCard (t [5].isPointInsideNthCard (mValX, mValY) - 1);
		    if (tempCard.getFaceUp () == true)
		    {
			dragType = "t5";
			for (int i = 0 ; i < t [5].isPointInsideNthCard (mValX, mValY) ; i++)
			{
			    tempCard = t [5].getCard (i);
			    tempTableau.addCard (tempCard, i);
			}
			for (int i = 1 ; i <= tempTableau.getDeckSize () ; i++)
			{
			    t [5].deleteCard (0);
			}
		    }
		}
	    }
	    else if (t [6].isPointInsideNthCard (mValX, mValY) != -1 && t [6].getDeckSize () != 0) //tableau 7
	    {
		if (t [6].isPointInsideNthCard (mValX, mValY) == 1)
		{
		    dragType = "1t6";
		    tempCard = t [6].getCard (0);
		    t [6].deleteCard (0);
		}
		else
		{
		    tempCard = t [6].getCard (t [6].isPointInsideNthCard (mValX, mValY) - 1);
		    if (tempCard.getFaceUp () == true)
		    {
			dragType = "t6";
			for (int i = 0 ; i < t [6].isPointInsideNthCard (mValX, mValY) ; i++)
			{
			    tempCard = t [6].getCard (i);
			    tempTableau.addCard (tempCard, i);
			}
			for (int i = 1 ; i <= tempTableau.getDeckSize () ; i++)
			{
			    t [6].deleteCard (0);
			}
		    }
		}
	    }
	}

	if (dragType != "") //prevents graphical bugs
	{
	    g.setColor (Color.white);
	    g.fillRect (0, 0, 800, 800);
	}

	refreshStatsRepaint ();
    }


    public boolean checkAndPlaceWTFT ()  //waste card or single tableau card to foundation or tableau
    {
	//to foundation
	if (fh.isPointInside (mValX, mValY) && fh.allowCardPlace (tempCard)) //1 heart; if mouse is inside the first card of the tableau and placement is allowed
	{ //functions used work exactly as their names imply, explained in their respective classes
	    fh.addCard (tempCard, 0);
	    scoreValue += 1;
	}
	else if (fd.isPointInside (mValX, mValY) && fd.allowCardPlace (tempCard)) //2 diamond
	{
	    fd.addCard (tempCard, 0);
	    scoreValue += 1;
	}
	else if (fs.isPointInside (mValX, mValY) && fs.allowCardPlace (tempCard)) //3 spade
	{
	    fs.addCard (tempCard, 0);
	    scoreValue += 1;
	}
	else if (fc.isPointInside (mValX, mValY) && fc.allowCardPlace (tempCard)) //4 club
	{
	    fc.addCard (tempCard, 0);
	    scoreValue += 1;
	} //to tableau
	else if (t [0].isPointInsideNthCard (mValX, mValY) == 1 && t [0].allowCardPlace (tempCard))
	{ //if mouse is inside the first card of the tableau and placement is allowed
	    t [0].addCard (tempCard, 0);
	}
	else if (t [1].isPointInsideNthCard (mValX, mValY) == 1 && t [1].allowCardPlace (tempCard))
	{
	    t [1].addCard (tempCard, 0);
	}
	else if (t [2].isPointInsideNthCard (mValX, mValY) == 1 && t [2].allowCardPlace (tempCard))
	{
	    t [2].addCard (tempCard, 0);
	}
	else if (t [3].isPointInsideNthCard (mValX, mValY) == 1 && t [3].allowCardPlace (tempCard))
	{
	    t [3].addCard (tempCard, 0);
	}
	else if (t [4].isPointInsideNthCard (mValX, mValY) == 1 && t [4].allowCardPlace (tempCard))
	{
	    t [4].addCard (tempCard, 0);
	}
	else if (t [5].isPointInsideNthCard (mValX, mValY) == 1 && t [5].allowCardPlace (tempCard))
	{
	    t [5].addCard (tempCard, 0);
	}
	else if (t [6].isPointInsideNthCard (mValX, mValY) == 1 && t [6].allowCardPlace (tempCard))
	{
	    t [6].addCard (tempCard, 0);
	}
	else
	{
	    return false; //placement unsuccessful
	}
	return true; //placement successful
    }


    public boolean checkAndPlaceTT ()  //multiple tableau to tableau
    {
	if (t [0].isPointInsideNthCard (mValX, mValY) == 1 && t [0].allowCardPlace (tempCard))
	{ //if mouse is over the first tableau card, and placement is allowed
	    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
	    { //add tempTableau to tableau 1
		tempCard = tempTableau.getCard (i);
		t [0].addCard (tempCard, 0);
	    }
	}
	else if (t [1].isPointInsideNthCard (mValX, mValY) == 1 && t [1].allowCardPlace (tempCard))
	{ //works same as above
	    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
	    {
		tempCard = tempTableau.getCard (i);
		t [1].addCard (tempCard, 0);
	    }
	}
	else if (t [2].isPointInsideNthCard (mValX, mValY) == 1 && t [2].allowCardPlace (tempCard))
	{
	    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
	    {
		tempCard = tempTableau.getCard (i);
		t [2].addCard (tempCard, 0);
	    }
	}
	else if (t [3].isPointInsideNthCard (mValX, mValY) == 1 && t [3].allowCardPlace (tempCard))
	{
	    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
	    {
		tempCard = tempTableau.getCard (i);
		t [3].addCard (tempCard, 0);
	    }
	}
	else if (t [4].isPointInsideNthCard (mValX, mValY) == 1 && t [4].allowCardPlace (tempCard))
	{
	    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
	    {
		tempCard = tempTableau.getCard (i);
		t [4].addCard (tempCard, 0);
	    }
	}
	else if (t [5].isPointInsideNthCard (mValX, mValY) == 1 && t [5].allowCardPlace (tempCard))
	{
	    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
	    {
		tempCard = tempTableau.getCard (i);
		t [5].addCard (tempCard, 0);
	    }
	}
	else if (t [6].isPointInsideNthCard (mValX, mValY) == 1 && t [6].allowCardPlace (tempCard))
	{
	    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
	    {
		tempCard = tempTableau.getCard (i);
		t [6].addCard (tempCard, 0);
	    }
	}
	else
	{
	    return false; //failure
	}
	return true; //success
    }


    public void mouseReleased (MouseEvent e)
    {
	tfMouse.setText ("Released");

	if (dragType != "") //if something is being dragged
	{
	    tempCard.erase (g);
	    tempTableau.eraseTableau (g);
	    if (dragType == "waste" && checkAndPlaceWTFT () == false)
	    { //if placement failed, put the card back where it was before
		waste.addCard (tempCard, 0);
	    }
	    else if (dragType == "1t0")
	    { //single tableau dragging
		if (checkAndPlaceWTFT () == false)
		{ //if card placement failed, put it back
		    t [0].addCard (tempCard, 0);
		}
		else if (t [0].getDeckSize () != 0) //if tableau is not empty
		{
		    tempCard = t [0].getCard (0);
		    tempCard.setFaceUp (true); //force card below to be face up
		}
	    }
	    else if (dragType == "1t1")
	    { //same as above
		if (checkAndPlaceWTFT () == false)
		{
		    t [1].addCard (tempCard, 0);
		}
		else if (t [1].getDeckSize () != 0)
		{
		    tempCard = t [1].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "1t2")
	    {
		if (checkAndPlaceWTFT () == false)
		{
		    t [2].addCard (tempCard, 0);
		}
		else if (t [2].getDeckSize () != 0)
		{
		    tempCard = t [2].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "1t3")
	    {
		if (checkAndPlaceWTFT () == false)
		{
		    t [3].addCard (tempCard, 0);
		}
		else if (t [3].getDeckSize () != 0)
		{
		    tempCard = t [3].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "1t4")
	    {
		if (checkAndPlaceWTFT () == false)
		{
		    t [4].addCard (tempCard, 0);
		}
		else if (t [4].getDeckSize () != 0)
		{
		    tempCard = t [4].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "1t5")
	    {
		if (checkAndPlaceWTFT () == false)
		{
		    t [5].addCard (tempCard, 0);
		}
		else if (t [5].getDeckSize () != 0)
		{
		    tempCard = t [5].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "1t6")
	    {
		if (checkAndPlaceWTFT () == false)
		{
		    t [6].addCard (tempCard, 0);
		}
		else if (t [6].getDeckSize () != 0)
		{
		    tempCard = t [6].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t0")
	    { //gets the bottom card of the tableau being dragged, used in placement allowance
		tempCard = tempTableau.getCard (tempTableau.getDeckSize () - 1);
		if (checkAndPlaceTT () == false)
		{ //if placement failed, put the tableau being dragged back
		    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [0].addCard (tempCard, 0);
		    }
		}
		else if (t [0].getDeckSize () != 0) //if tableau is not empty
		{ //force card below to be face up
		    tempCard = t [0].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t1")
	    { //works same as above
		tempCard = tempTableau.getCard (tempTableau.getDeckSize () - 1);
		if (checkAndPlaceTT () == false)
		{
		    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [1].addCard (tempCard, 0);
		    }
		}
		else if (t [1].getDeckSize () != 0)
		{
		    tempCard = t [1].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t2")
	    {
		tempCard = tempTableau.getCard (tempTableau.getDeckSize () - 1);
		if (checkAndPlaceTT () == false)
		{
		    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [2].addCard (tempCard, 0);
		    }
		}
		else if (t [2].getDeckSize () != 0)
		{
		    tempCard = t [2].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t3")
	    {
		tempCard = tempTableau.getCard (tempTableau.getDeckSize () - 1);
		if (checkAndPlaceTT () == false)
		{
		    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [3].addCard (tempCard, 0);
		    }
		}
		else if (t [3].getDeckSize () != 0)
		{
		    tempCard = t [3].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t4")
	    {
		tempCard = tempTableau.getCard (tempTableau.getDeckSize () - 1);
		if (checkAndPlaceTT () == false)
		{
		    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [4].addCard (tempCard, 0);
		    }
		}
		else if (t [4].getDeckSize () != 0)
		{
		    tempCard = t [4].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t5")
	    {
		tempCard = tempTableau.getCard (tempTableau.getDeckSize () - 1);
		if (checkAndPlaceTT () == false)
		{
		    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [5].addCard (tempCard, 0);
		    }
		}
		else if (t [5].getDeckSize () != 0)
		{
		    tempCard = t [5].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    else if (dragType == "t6")
	    {
		tempCard = tempTableau.getCard (tempTableau.getDeckSize () - 1);
		if (checkAndPlaceTT () == false)
		{
		    for (int i = tempTableau.getDeckSize () - 1 ; i >= 0 ; i--)
		    {
			tempCard = tempTableau.getCard (i);
			t [6].addCard (tempCard, 0);
		    }
		}
		else if (t [6].getDeckSize () != 0)
		{
		    tempCard = t [6].getCard (0);
		    tempCard.setFaceUp (true);
		}
	    }
	    dragType = ""; //clears dragType
	    tempTableau = new TableauClass (); //clears tempTableau
	}

	refreshStatsRepaint ();
    }


    public void mouseDragged (MouseEvent e)
    {
	tfMouse.setText ("Dragged");
	mValX = e.getX ();
	mValY = e.getY ();

	if (dragType == "waste" || dragType == "1t0" || dragType == "1t1" || dragType == "1t2" ||
		dragType == "1t3" || dragType == "1t4" || dragType == "1t5" || dragType == "1t6")
	{ //drawing for non >1 tableau dragging
	    tempCard.erase (g);
	    tempCard.setCentre (mValX, mValY);
	    tempCard.draw (g);
	}
	else if (dragType != "" && dragType != "waste")
	{ //drawing for >1 tableau dragging
	    tempTableau.eraseTableau (g);
	    tempTableau.setDeckCentre (mValX, mValY);
	    tempTableau.drawTableau (g);
	}

	refreshStatsRepaint ();
    }


    public void mouseMoved (MouseEvent e)
    {
	tfMouse.setText ("Moved");
	mValX = e.getX ();
	mValY = e.getY ();

	refreshStatsRepaint ();
    }


    /////////////////////////////////////////////////////
    public void actionPerformed (ActionEvent e)
    {
	Object objSource = e.getSource ();

	if (objSource == bNewGame) //if NEW GAME is pressed
	{
	    g.setColor (Color.white); //clear the screen
	    g.fillRect (0, 0, 800, 800);

	    passValue = 0; //set all stats to 0
	    startTime = System.currentTimeMillis ();
	    scoreValue = 0;

	    tempTableau = new TableauClass ();

	    stock = new DeckClass ('s'); //create a standard deck
	    stock.shuffle ();

	    restartDeck = new DeckClass ();
	    for (int i = 0 ; i <= 51 ; i++) //duplicate stock into restartDeck
	    {
		tempCard = stock.getCard (i);
		restartDeck.addCard (tempCard, 51);
	    }

	    waste = new DeckClass ();

	    fd = new FoundationClass ('d');
	    fc = new FoundationClass ('c');
	    fh = new FoundationClass ('h');
	    fs = new FoundationClass ('s');

	    t = new TableauClass [7];
	    for (int x = 0 ; x < 7 ; x++)
	    {
		t [x] = new TableauClass ();
	    }

	    stock.setDeckCentre (100, 150); //set appropriate locations of the decks
	    waste.setDeckCentre (200, 150);

	    fd.setDeckCentre (400, 150);
	    fc.setDeckCentre (500, 150);
	    fh.setDeckCentre (600, 150);
	    fs.setDeckCentre (700, 150);

	    for (int x = 0 ; x < 7 ; x++) //set locations of tableaus
	    {
		t [x].setDeckCentre ((x + 1) * 100, 300);
	    }

	    for (int x = 0 ; x < 7 ; x++) //distribute stock into tableaus
	    {
		for (int y = 0 ; y <= x ; y++)
		{ //first tableau starts with 1 card, seconds tableau starts with 2 cards, etc. goes up to 7th tableau with 7 card
		    tempCard = stock.getCard (0);
		    stock.deleteCard (0);
		    tempCard.erase (g);
		    if (y == x)
		    { //first card in each tableau is face up
			tempCard.setFaceUp (true);
		    }
		    t [x].addCard (tempCard, 0);
		}
	    }
	}


	else if (objSource == bRestart)
	{ //works same as NEW GAME, but copies the restart deck into stock rather than newing and shuffling a standard stock deck
	    g.setColor (Color.white);
	    g.fillRect (0, 0, 800, 800);

	    passValue = 0;
	    startTime = System.currentTimeMillis ();
	    scoreValue = 0;

	    tempTableau = new TableauClass ();
	    stock = new DeckClass ();

	    for (int i = 0 ; i <= 51 ; i++)
	    { //copy restartDeck to stock
		tempCard = restartDeck.getCard (i);
		tempCard.setFaceUp (false);
		stock.addCard (tempCard, 51);
	    }

	    waste = new DeckClass ();

	    fd = new FoundationClass ('d');
	    fc = new FoundationClass ('c');
	    fh = new FoundationClass ('h');
	    fs = new FoundationClass ('s');

	    t = new TableauClass [7];
	    for (int x = 0 ; x < 7 ; x++)
	    {
		t [x] = new TableauClass ();
	    }

	    stock.setDeckCentre (100, 150);
	    waste.setDeckCentre (200, 150);

	    fd.setDeckCentre (400, 150);
	    fc.setDeckCentre (500, 150);
	    fh.setDeckCentre (600, 150);
	    fs.setDeckCentre (700, 150);

	    for (int x = 0 ; x < 7 ; x++)
	    {
		t [x].setDeckCentre ((x + 1) * 100, 300);
	    }

	    for (int x = 0 ; x < 7 ; x++)
	    {
		for (int y = 0 ; y <= x ; y++)
		{
		    tempCard = stock.getCard (0);
		    stock.deleteCard (0);
		    tempCard.erase (g);
		    if (y == x)
		    {
			tempCard.setFaceUp (true);
		    }
		    t [x].addCard (tempCard, 0);
		}
	    }
	}
	else if (objSource == bQuit)
	{ //close the game
	    System.exit (0);
	}

	refreshStatsRepaint ();
    }


    /////////////////////////////////////////////////////
    public void repaint ()
    { //body of the drawing method
	g.setColor (Color.white);

	stock.drawTop (g);
	waste.drawTop (g);
	fd.drawTop (g);
	fc.drawTop (g);
	fh.drawTop (g);
	fs.drawTop (g);
	for (int x = 0 ; x < 7 ; x++)
	{
	    t [x].drawTableau (g);
	}
    }
}



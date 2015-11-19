package Client;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

/**
 * Created by teodor on 08.11.2015.
 */
public class Player {
    private String name;
    private Vector<Card> cards;
    private int points;

    public Player( String name )
    {
        this.name = name;
        cards = new Vector<Card>();
        this.points = 0;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String newName )
    {
        this.name = newName;
    }


    public Vector<Card> getCards()
    {
        return cards;
    }

    public void setCards( Vector<Card> newCards )
    {
        this.cards = newCards;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints( int newPoints )
    {
        points = newPoints;
    }

    public void addCard( Card newCard )
    {
        cards.add( newCard );
        points += newCard.getPoints();
        for( Card card : cards ) {
            if( points > 21 && card.getNumber() == 11 ) {
                card.setNumber(1);
                points -= 10;
            }
        }
    }

    public String toString() {
        return "\n{" + this.name + "\nCards: " + cards + "\nPoints:" + points + "}\n" ;
    }

}

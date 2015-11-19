package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

/**
 * Created by teodor on 08.11.2015.
 */
public class Player {

    private String name;
    private Vector<Card> cards;
    private int points;
    private Socket socket;
    private PrintWriter sendStream;
    private BufferedReader reciveStream;


    public Player( String name, Socket socket ) {
        this.name = name;
        cards = new Vector<Card>();
        this.points = 0;
        this.socket = socket;
        try {
            sendStream = new PrintWriter( socket.getOutputStream(), true );
            reciveStream =  new BufferedReader( new InputStreamReader( socket.getInputStream() ));
        }catch (Exception e ) {
            System.out.println("Couldn't open the socket's streams! Sorry!");
            System.exit(1);
        }
    }

    public String getName()
    {
        return name;
    }

    public void setName( String newName )
    {
        this.name = newName;
    }


    public Vector<Card> getCards() {
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

    public void addCard( Card newCard ) {
        cards.add(newCard);
        points += newCard.getPoints();
        for (Card card : cards) {
            if (points > 21 && card.getNumber() == 1) {
                points -= 10;
            }
        }
    }

    public void sendMessage(String message) {
        sendStream.println( message );
     }

    public String reciveMessage() {
        String response = null;
        try{
            while ( (response = reciveStream.readLine()) != null ) {
                return response;
            }
        }catch(IOException e) {
            System.out.println("Couldn't read from socket");
            System.exit(1);
        }
        return response;
    }

    public String toString() {
        return this.name + "\nCards: " + cards + "\nPoints:" + points + "\n" ;
    }

    public void closePlayer() {
        try {
            reciveStream.close();
            sendStream.close();
            socket.close();
        }catch (IOException e) {
            System.out.println("Eroare inchidere sockets si streamuri");
        }

    }

}

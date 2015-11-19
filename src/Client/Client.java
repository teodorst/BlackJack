package Client;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by teodor on 08.11.2015.
 */

public class Client {


    public static void main (String []args ) {
        Socket playerSocket;
        PrintWriter sendStream = null;
        BufferedReader reciveStream = null;
        String fromServer;
        Vector<Player> otherPlayers = new Vector<Player>();
        Player clientPlayer = null;
        if( args.length > 1 )
            clientPlayer = new Player(args[args.length - 1]);
        else {
            clientPlayer = new Player("DemoPlayer");
        }
        try {
            playerSocket = new Socket("localhost", 3001);
            sendStream = new PrintWriter(playerSocket.getOutputStream(), true);
            reciveStream =  new BufferedReader( new InputStreamReader(playerSocket.getInputStream()));
        }catch (Exception e ) {
            System.out.println("Couldn't open the socket! Sorry!");
            System.exit(1);
        }
        try {
            while( (fromServer = reciveStream.readLine()) != null ) {
                System.out.println("From Server:" + fromServer);
                if ( fromServer.contains("[Details]") ) {
                    StringTokenizer token = new StringTokenizer(fromServer, " ");
                    int index = 0;
                    while ( token.hasMoreTokens() ) {
                        String currentElement = token.nextToken();
                        if( index == 1 || index == 2 ) {
                            int delimiter = currentElement.lastIndexOf('|');
                            Card newCard = new Card(
                                    Integer.parseInt(currentElement.substring(0, delimiter), 10),
                                    currentElement.substring(delimiter + 1)
                            );
                            clientPlayer.addCard(newCard);
                        }
                        index ++;
                    }
                    sendStream.println(clientPlayer.getName());
                }
                else if( fromServer.contains("[Update]") ) {
                    // [Update]-teodor1 5|romb 6|inima-
                    otherPlayers = handleUpdate(fromServer);
                }
                else if( fromServer.contains("[Turn]") ) {
                    System.out.println("STAND/HIT? Points: " + clientPlayer.getPoints() + " " + clientPlayer.getCards());
                    Scanner display = new Scanner(System.in);
                    String command = display.next();
                    sendStream.println(command);
                }
                else if( fromServer.contains("[Card]") ) {
                    StringTokenizer token = new StringTokenizer(fromServer, " ");
                    int index = 0;
                    while( token.hasMoreTokens() ) {
                        String currentElement = token.nextToken();
                        if( index > 0 ) {
                            int delimiter = currentElement.lastIndexOf('|');
                            Card newCard = new Card(
                                    Integer.parseInt( currentElement.substring( 0, delimiter ), 10 ),
                                    currentElement.substring( delimiter + 1 )
                            );
                            clientPlayer.addCard(newCard);
                        }
                        index ++;
                    }
                }
            }
        }catch (IOException e ) {
            System.out.println("Can't read from socket");
        }

    }

    public static Vector<Player> handleUpdate (String fromServer){
        //[Update]-playerX 5|trefa 8|romb-playerX2 ...
        Vector<Player> otherPlayers = new Vector<Player>();
        StringTokenizer token = new StringTokenizer(fromServer, "-");
        int index = 0;
        while ( token.hasMoreTokens() ) {
            String playersToken = token.nextToken();
            if( index > 0 ) {
                StringTokenizer playerToken = new StringTokenizer(playersToken ," ");
                int playerParserIndex = 0;
                Player newPlayer = new Player("defaultName");
                while( playerToken.hasMoreTokens() ) {
                    String currentPlayerParser = playerToken.nextToken();
                    if( playerParserIndex == 0 ) {
                        newPlayer.setName(currentPlayerParser);
                    }
                    else {
                        int delimiter = currentPlayerParser.lastIndexOf('|');
                        Card newCard = new Card(
                                Integer.parseInt(currentPlayerParser.substring(0, delimiter), 10),
                                currentPlayerParser.substring(delimiter + 1)
                        );
                        newPlayer.addCard(newCard);
                    }
                    playerParserIndex ++;
                }
                otherPlayers.add(newPlayer);
            }
            index ++;
        }
        return otherPlayers;
    }
}

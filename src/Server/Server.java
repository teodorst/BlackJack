package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by teodor on 08.11.2015.
 */
public class Server {
    public static ArrayList<BlackJackGame> games = new ArrayList<BlackJackGame>();
    public static void main( String[] args ) {
        ServerSocket serverSocket = null;
        Vector<Player> playersInQueue = new Vector<Player>();

        try {
            serverSocket = new ServerSocket(3001);
        }catch (IOException e) {
            System.out.println("Couldn't open server socket");
            System.exit(1);
        }
        while(true) {
            try{
                Socket newSocket = serverSocket.accept();
                playersInQueue.add(new Player(
                        "Player" + playersInQueue.size(),
                        newSocket
                ));
                if( playersInQueue.size() == 4 ) {
                    BlackJackGame handleConnections = new BlackJackGame(playersInQueue);
                    handleConnections.start();
                    try{
                        handleConnections.join();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }catch (IOException e ) {
                System.out.println("Couldn't open a socket for the new client");
            }
        }

    }

}

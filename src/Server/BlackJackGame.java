package Server;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by teodor on 08.11.2015.
 */

public class BlackJackGame extends Thread {
    private Vector<Player> players;
    private Vector<Card> deck;

    public BlackJackGame(Vector<Player> players)
    {
        this.players = players;
        int i, j;
        // generare pachet de carti
        deck = new Vector<Card>();

        for( i = 2; i < 15; i ++ )
        {
            if( i == 11 )
            {
                continue;
            }
            deck.add(new Card( i, "trefla"));
            deck.add(new Card( i, "romb"));
            deck.add(new Card( i, "inimaRosie"));
            deck.add(new Card( i, "inimaNeagra"));
        }
        Collections.shuffle(deck);
    }

    public void run ()
    {
        System.out.println("Let the game begin");
        String response = "";
        int i = 0;
        // Initialization Step 1 ---- > [Details] firstCard secondCard --------> PlayerName
        for( i = 0; i < players.size(); i ++ ) {
            Card firstCard = deck.remove(0);
            Card secondCard = deck.remove(0);
            players.get(i).sendMessage("[Details] " + firstCard.toSendFormat() + " " + secondCard.toSendFormat());
            response = players.get(i).reciveMessage();
            players.get(i).setName(response);
            players.get(i).addCard(firstCard);
            players.get(i).addCard(secondCard);
        }
        // Initialization Step 2 ---- > [Update]
        for( Player player : players ) {
            String formatText = "[Update]";
            for( Player toSendPlayer : players ) {
                if( !player.equals(toSendPlayer) ) {
                    formatText += "-" + toSendPlayer.getName();
                    for( Card card : toSendPlayer.getCards() ) {
                        formatText += " " + card.getNumber() + "|" + card.getType();
                    }
                }
            }
            player.sendMessage(formatText);
        }

        // begin the game
        for ( Player player : players ) {
            player.sendMessage("[Turn]");
            response = player.reciveMessage();
            System.out.println("[Turn] " + player.getName() );
            System.out.println(response);
            if( response.equals("HIT") ) {
                while ( response.equals("HIT") ) {
                    Card newCard = deck.remove(0);
                    player.addCard(newCard);
                    player.sendMessage("[Card] " + newCard.getNumber() + "|" + newCard.getType() );
                    if( player.getPoints() == 21 ) {
                        player.sendMessage("You Won!");
                        break;
                    }
                    else if ( player.getPoints() > 21 ) {
                        player.sendMessage("You Lost!");
                        player.closePlayer();
                        break;
                    }
                    else {
                        player.sendMessage("[Turn]");
                        response = player.reciveMessage();
                    }
                }
            }
        }

        System.out.println(players);

        int max = 0;
        for ( Player player : players ) {
            if( max < player.getPoints() && player.getPoints() <= 21 )
                max = player.getPoints();
        }

        Vector<String> winners = new Vector<String>();

        for ( Player player : players ) {
            if( player.getPoints() == max )
                winners.add(player.getName());
        }

        for ( Player player : players ) {
            if( player.getPoints() == max ) {
                player.sendMessage("You Won! :)");
            }
            else {
                player.sendMessage("You Lost!:(, " + winners + "  Won");
            }
            player.closePlayer();
        }

    }

}

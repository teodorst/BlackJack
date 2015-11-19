package Client;

/**
 * Created by teodor on 08.11.2015.
 */
public class Card {
    private int number;
    private String type;
    private int points;

    public Card(int number, String type)
    {
        this.number = number;
        this.type = type;
        if( number == 10 || number == 12 || number == 13 || number == 14 ) {
            points = 10;
        }
        else if( number == 11  ){
            points = 10;
        }
        else {
            points = number;
        }
    }

    public int getNumber()
    {
        return this.number;
    }

    public void setNumber(int newNumber)
    {
        this.number = newNumber;
    }

    public String getType()
    {
        return this.type;
    }

    public void setType(String newType)
    {
        this.type = newType;
    }

    public int getPoints()
    {
        return points;
    }

    public void setPoints( int newPoints )
    {
        points = newPoints;
    }

    public String toString() {
        if( number == 1 || number == 1 ) {
            return "A|" + type;
        }
        if( number == 12 ) {
            return "J|" + type;
        }
        if( number == 13 ) {
            return "Q|" + type;
        }
        if( number == 14 ) {
            return "K|" + type;
        }
        return number + "|" + type;
    }

    public String toSendFormat() {
        return number + "|" + type;
    }

}

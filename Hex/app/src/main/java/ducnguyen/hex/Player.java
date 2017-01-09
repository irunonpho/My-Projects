package ducnguyen.hex;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by ducng on 12/15/2016.
 */

public class Player {
    private ArrayList<Tile> tileList = new ArrayList<Tile>();
    private boolean turn;
    private int actionCounter;
    private int playerColor;

    public Player(int color, boolean first){
        playerColor = color;
        if(first){
            turn = true;
            actionCounter = 1;
        }
        else{
            turn = false;
            actionCounter = 2;
        }
    }
    public void setColor(int color){
        playerColor = color;
        if(playerColor == Color.WHITE) {
        }
        else{
        }
    }
    public Player(Player player){
        this.playerColor = player.getPlayerColor();
        this.turn = player.isTurn();
        this.actionCounter = player.actionCounter;
        this.tileList = player.tileList;
    }
    public int getPlayerColor(){
        return playerColor;
    }
    public ArrayList<Tile> getTileList(){
        return tileList;
    }
    public void setTurn(boolean turn){
        this.turn = turn;
    }
    public boolean isTurn(){
        return turn;
    }
    public void endOfTurn(boolean turn){
        resetactionCounter();
        if(turn) {
            this.turn = false;
        }
        else {
            this.turn = true;
        }
    }
    public void setActionCounter(int c){
        actionCounter = c;
    }
    public int getActionCounter(){
        return actionCounter;
    }
    public void resetactionCounter(){
        actionCounter = 2;
    }
    public void usedAction(){
        actionCounter--;
    }
    public void displayPlayer(){
        if(playerColor == Color.WHITE) {
            System.out.println("White Player");
        }
        else{
            System.out.println("Black Player");
        }
        System.out.println("Current Action: " + actionCounter);
        if(turn){
            System.out.println("Active Turn");
        }
        else{
            System.out.println("Inactive Turn");
        }

    }
}

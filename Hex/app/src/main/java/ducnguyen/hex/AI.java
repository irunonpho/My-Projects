package ducnguyen.hex;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ducng on 12/19/2016.
 */

public class AI extends Player{
    private AITree tree;
    private boolean aiw;
    private boolean first;
    private int difficulty;

    public AI(boolean aiw, Board board, Player player, Player enemy, int difficulty){
        super(player);
        this.aiw = aiw;
        this.difficulty = difficulty;
        tree = null;
        super.displayPlayer();
        if(enemy.isTurn() && first){
            first = false;
        }
        else {
            first = true;
        }
    }
    public void setAiw(boolean aiw){
        first = true;
        this.aiw = aiw;
    }
    public boolean getAiw(){
        return aiw;
    }
    public Tile moveDecision(Board board, Player enemy){

        Random rand = new Random();
        Tile tile = null;
        if(!first) {
            int i = 0;
            for(Tile T: board.getTileList()){
                if(T.getColor() == Color.GRAY){
                    i++;
                }
            }
            if(i > 9-difficulty) {
                tree = new AITree(board, this, enemy, this.aiw, this.difficulty);
            }
            else{
                tree = new AITree(board, this, enemy, this.aiw, this.difficulty+1);
            }
            super.displayPlayer();
            int buffer = 2 * AITree.LOSS;
            ArrayList<Integer> tileID = new ArrayList<Integer>();

            for (int R : tree.getRootsTileID()) {
                if (tree.getTileWeight(R) >= buffer + 2  || buffer == 0) {
                    tileID.clear();
                    buffer = tree.getTileWeight(R);
                }
                if (tree.getTileWeight(R) >= buffer - 2  && tree.getTileWeight(R) <= buffer + 2 ) {
                    buffer = tree.getTileWeight(R);
                    tileID.add(R);
                }
            }
            int k = 0;
            if (tileID.size() > 1) {
                k = rand.nextInt(tileID.size());
            } else {
                k = 0;
            }

            for (Tile T : board.getTileList()) {
                if (T.getTileID() == tileID.get(k)) {
                    tile = T;
                }
            }
            System.out.println("TID:" + tileID);
        }
        else{
            while(first) {
                int k = rand.nextInt(board.getTileList().size()) + 1;
                for (Tile T : board.getTileList()) {
                    if (T.getTileID() == k) {
                        tile = T;
                    }
                }
                if(tile.getColor() != enemy.getPlayerColor()) {
                    first = false;
                }
            }
        }
        return tile;
    }
}

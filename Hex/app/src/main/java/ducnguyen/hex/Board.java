package ducnguyen.hex;

import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Created by ducng on 12/13/2016.
 */

public class Board {
    //72 neighbors
    private ArrayList<Tile> tileList = new ArrayList<Tile>();
    private ArrayList<Neighbors> neighborList = new ArrayList<Neighbors>();
    public static float e = 10.0f;
    //generates board for game
    Board(){
        tileList.add(new Tile(GamePanel.WIDTH/2, GamePanel.HEIGHT/2, 1));
        tileGeneration(tileList.get(0));

        for(int i = 1; i < 7; i++){
            tileGeneration(tileList.get(i));
        }
        tileGeneration(tileList.get(8));
        tileGeneration(tileList.get(11));
        tileGeneration(tileList.get(13));
        tileGeneration(tileList.get(15));
        tileGeneration(tileList.get(17));
        tileGeneration(tileList.get(9));
    }
    public ArrayList<Tile> getTileList(){
        return tileList;
    }
    public ArrayList<Neighbors> getNeighborList(){
        return neighborList;
    }

    public void tileGeneration(Tile tile) {
        boolean neighborFlag = false;
        for (int i = 0; i < 6; i++) {
            if (availableSpace(tile,i)) {
                neighborFlag = true;
            }
            if(!neighborFlag) {
                Tile newTile = new Tile(neighborCenterCompute(tile, i), tileList.size() + 1);
                tileList.add(newTile);
                neighborList.add(new Neighbors(tile, newTile));
            }
            neighborFlag = false;
        }
        updateNeighbor();
    }
    public void updateNeighbor(){
        boolean exist = false;
        for(int i = 1; i < tileList.size(); i++){
            for(int j = i+1; j < tileList.size(); j++){
                if( neighborLogic(tileList.get(i),tileList.get(j))){
                    for(Neighbors n: neighborList){
                        if(n.isMember(tileList.get(i)) && n.isMember(tileList.get(j)) ){
                            exist = true;
                        }
                    }
                    if(!exist) {
                        neighborList.add(new Neighbors(tileList.get(i), tileList.get(j)));
                    }
                    exist = false;
                }
            }
        }
    }
    //checks to see if adjacent space is available for building a tile
    public boolean availableSpace(Tile tile, int direction){
        Coordinate newPt = neighborCenterCompute(tile, direction);
        for (Tile t: tileList) {
            if (neighborMetric(newPt, t) < e) {
                return true;
            }
        }
        return false;
    }

    public boolean neighborLogic(Tile tile1, Tile tile2){
        if(neighborMetric(tile1, tile2) < Tile.Apothem + e){
            return true;
        }
        return false;
    }
    public float neighborMetric(Tile x_1, Tile x_2){
        return (float)(Math.sqrt(Math.pow((double)(x_1.getX()-x_2.getX()),2.0)+Math.pow((double)(x_1.getY()-x_2.getY()),2.0)));
    }
    public float neighborMetric(Coordinate x_1, Tile x_2){
        return (float)(Math.sqrt(Math.pow((double)(x_1.getX()-x_2.getX()),2.0)+Math.pow((double)(x_1.getY()-x_2.getY()),2.0)));
    }

    public Coordinate neighborCenterCompute(Tile tile, int i){
        float X = (float)(tile.getX() + Math.cos((30+60*i)*Tile.K)*Tile.Apothem);
        float Y = (float)(tile.getY() + Math.sin((30+60*i)*Tile.K)*Tile.Apothem);
        return new Coordinate(X,Y);
    }
    public String tileNameGenerator(String tileName, int i){
        return "b";
    }
    void draw(Canvas canvas){
        for(Tile t: tileList){
            t.drawGrowing(canvas);
        }
    }
}

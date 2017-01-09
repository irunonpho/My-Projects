package ducnguyen.hex;

import java.util.ArrayList;

/**
 * Created by ducng on 12/17/2016.
 */

public class AINeighbors {
    private AITile neighborTiles[] = new AITile[2];

    public AINeighbors(Neighbors neighbors){
        neighborTiles[0] = new AITile(neighbors.getNeighborTiles()[0]);
        neighborTiles[1] = new AITile(neighbors.getNeighborTiles()[1]);
    }
    public AINeighbors(AINeighbors neighbors){
        neighborTiles[0] = new AITile(neighbors.getNeighborTiles()[0]);
        neighborTiles[1] = new AITile(neighbors.getNeighborTiles()[1]);
    }
    public AINeighbors(AINeighbors neighbors, ArrayList<AITile> tileList){
        int i = 0;
        for(AITile t: tileList){
            if(neighbors.isMember(t)){
                neighborTiles[i] = t;
                i++;
            }
            if(i>2){
                break;
            }
        }
    }
    public AINeighbors(Neighbors neighbors, ArrayList<AITile> tileList){
        int i = 0;
        for(AITile t: tileList){
            if(neighbors.isMember(t)){
                neighborTiles[i] = t;
                i++;
            }
            if(i>2){
                break;
            }
        }
    }
    public AINeighbors(AITile tile1, AITile tile2){
        neighborTiles[0] = tile1;
        neighborTiles[1] = tile2;
    }

    public AITile[] getNeighborTiles(){
        return neighborTiles;
    }
    public void setNeighborTiles(AITile[] neighborTiles) {
        this.neighborTiles = neighborTiles;
    }
    public void setNeighborTiles(AITile tile1, AITile tile2) {
        this.neighborTiles[0] = tile1;
        this.neighborTiles[1] = tile2;
    }
    public AITile getNeighbor(AITile tile){
        if(tile.getTileID() == neighborTiles[0].getTileID()){
            return neighborTiles[1];
        }
        else if(tile.getTileID() == neighborTiles[1].getTileID()){
            return neighborTiles[0];
        }
        else{
            return null;
        }
    }

    public boolean isMember(AITile tile){
        if(neighborTiles[0].getTileID() == tile.getTileID() || neighborTiles[1].getTileID() == tile.getTileID()){
            return true;
        }
        return false;
    }
    public void displayNeighbors(){
        System.out.println("NEIGHBORS");
        System.out.println("N1: " + neighborTiles[0]);
        System.out.println("N2: " + neighborTiles[1]);
    }
}

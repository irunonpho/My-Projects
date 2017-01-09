package ducnguyen.hex;

/**
 * Created by ducng on 12/13/2016.
 */

public class Neighbors {
    private Tile neighborTiles[] = new Tile[2];
    public Neighbors(Tile tile1, Tile tile2){
        neighborTiles[0] = tile1;
        neighborTiles[1] = tile2;
    }
    public Tile[] getNeighborTiles(){
        return neighborTiles;
    }
    public void setNeighborTiles(Tile[] neighborTiles) {
        this.neighborTiles = neighborTiles;
    }
    public void setNeighborTiles(Tile tile1, Tile tile2) {
        neighborTiles[0] = tile1;
        neighborTiles[1] = tile2;
    }
    public Tile getNeighbor(Tile tile){
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

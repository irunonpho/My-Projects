package ducnguyen.hex;

import android.graphics.Color;

/**
 * Created by ducng on 12/16/2016.
 */

public class AITile{
    private int x,y;
    private int tileID;
    int color;
    public AITile(int x, int y, int tileID){
        this.tileID = tileID;
        this.x = x;
        this.y = y;
        color = Color.GRAY;
    }
    public AITile(AITile tile){
        x = tile.getX();
        y = tile.getY();
        color = tile.getColor();
        tileID = tile.getTileID();
    }
    public void setX(int x){
        this.x = x;
    }
    public int getX(){
        return x;
    }
    public void setY(int y){
        this.y = y;
    }
    public int getY(){
        return y;
    }
    public void setTileID(int id){
        tileID = id;
    }
    public int getTileID(){
        return tileID;
    }
    public void setColor(int color){
        this.color = color;
    }
    public int getColor(){
        return color;
    }
}
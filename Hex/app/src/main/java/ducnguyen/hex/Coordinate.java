package ducnguyen.hex;

/**
 * Created by ducng on 12/13/2016.
 */

public class Coordinate{
    private float x,y;
    public Coordinate(){
        this.x = GamePanel.WIDTH/2;
        this.y = GamePanel.HEIGHT/2;
    }
    public Coordinate(float x, float y){
        this.x = x;
        this.y = y;
    }
    public Coordinate(Coordinate coordinate){
        this.x = coordinate.getX();
        this.y = coordinate.getY();
    }
    public float getX(){
        return x;
    }
    public void setX(float x){
        this.x = x;
    }
    public float getY(){
        return y;
    }
    public void setY(float y){
        this.y = y;
    }
    public void setCoordinate(float x, float y){
        this.x = x;
        this.y = y;
    }
}
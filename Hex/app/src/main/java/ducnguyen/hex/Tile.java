package ducnguyen.hex;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Created by ducng on 12/13/2016.
 */

public class Tile extends AITile{
    public static final double K = (Math.PI)/180;
    public static final double Apothem = 75;
    public static final double radius = Apothem*Math.sin(30*K)/Math.sin(120*K);
    private double r;
    private double dr;
    private boolean growing;
    private Coordinate borderPoints[] = new Coordinate[6];
    private Coordinate points[] = new Coordinate[6];

    public Tile(int x, int y, int tileID){
        super( x, y, tileID);
        r = radius;
        for(int i = 0; i < 6; i++){
            borderPoints[i] = new Coordinate((float)(getX() + Math.cos(60*K*i)*r),(float)(getY() + Math.sin(60*K*i)*r));
            points[i] = new Coordinate((float)(getX() + Math.cos(60*K*i)*(r-2)),(float)(getY() + Math.sin(60*K*i)*(r-2)));
        }
        setColor(Color.GRAY);
    }
    public Tile(Coordinate coordinate, int tileID){
        super((int)coordinate.getX(), (int)coordinate.getY(), tileID);
        r = radius;
        for(int i = 0; i < 6; i++){
            borderPoints[i] = new Coordinate((float)(getX() + Math.cos(60*K*i)*r),(float)(getY() + Math.sin(60*K*i)*r));
            points[i] = new Coordinate((float)(getX() + Math.cos(60*K*i)*(r-2)),(float)(getY() + Math.sin(60*K*i)*(r-2)));
        }
        setColor(Color.GRAY);
    }

    public void draw(Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        Path p = new Path();
        p.moveTo(borderPoints[0].getX(),borderPoints[0].getY());
        for(int i = 1; i < 6; i++){
            p.lineTo(borderPoints[i].getX(),borderPoints[i].getY());
        }
        canvas.drawPath(p, paint);

        Paint paint1 = new Paint();
        paint1.setColor(getColor());
        paint1.setStyle(Paint.Style.FILL);
        Path p1 = new Path();
        p1.moveTo(points[0].getX(),points[0].getY());
        for(int i = 1; i < 6; i++){
            p1.lineTo(points[i].getX(),points[i].getY());
        }
        canvas.drawPath(p1, paint1);
    }
    public void drawGrowing(Canvas canvas){
        if(!growing){
            r = radius;
            dr = 0;
            draw(canvas);
        }
        if(growing) {
            for (int i = 0; i < 6; i++) {
                borderPoints[i] = new Coordinate((float) (getX() + Math.cos(60 * K * i) * r), (float) (getY() + Math.sin(60 * K * i) * r));
                points[i] = new Coordinate((float) (getX() + Math.cos(60 * K * i) * (r - 2)), (float) (getY() + Math.sin(60 * K * i) * (r - 2)));
            }
            r += dr;
            dr++;
            draw(canvas);
            if(r >= radius){
                r = radius;
                for (int i = 0; i < 6; i++) {
                    borderPoints[i] = new Coordinate((float) (getX() + Math.cos(60 * K * i) * r), (float) (getY() + Math.sin(60 * K * i) * r));
                    points[i] = new Coordinate((float) (getX() + Math.cos(60 * K * i) * (r - 2)), (float) (getY() + Math.sin(60 * K * i) * (r - 2)));
                }
                growing = false;
            }
        }
    }
    public void setGrowing(boolean growing){
        if(growing = true){
            r = 0;
        }
        this.growing = growing;
    }
    public boolean getGrowing(){
        return growing;
    }
    @Override
    public void setColor(int color){
        setGrowing(true);
        this.color = color;
    }
}

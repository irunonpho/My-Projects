package ducnguyen.hex;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by ducng on 12/21/2016.
 */

public class Drop {
    float radius;
    float dr;
    Coordinate dropPos;
    long startDrop;
    int alpha;

    public Drop(Coordinate coordinate, float scaleFactorX, float scaleFactorY){
        radius = 0;
        dr = 5;
        alpha = 255;
        dropPos = new Coordinate(coordinate.getX()/scaleFactorX, coordinate.getY()/scaleFactorY);
        startDrop = System.nanoTime();
        displayDrop();
    }
    public Drop(Coordinate coordinate){
        radius = 0;
        dr = 5;
        alpha = 255;
        dropPos = new Coordinate(coordinate.getX(), coordinate.getY());
        startDrop = System.nanoTime();
        displayDrop();
    }
    public Drop(float x, float y){
        radius = 0;
        dr = 2;
        alpha = 255;
        dropPos = new Coordinate(x, y);
        startDrop = System.nanoTime();
    }
    public long getStartDrop(){
        return startDrop;
    }

    void draw(Canvas canvas){
        Paint paint = new Paint();
        if(alpha <= 0){
            alpha = 0;
        }
        paint.setColor(Color.argb(alpha, 192,192,192));
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(dropPos.getX(), dropPos.getY(), radius, paint);
        if(alpha > 0) {
            alpha -= 10;
        }
        radius += dr;
        dr++;
    }
    void displayDrop(){
        System.out.println("Drop X: " + dropPos.getX());
        System.out.println("Drop Y: " + dropPos.getY());
    }
}

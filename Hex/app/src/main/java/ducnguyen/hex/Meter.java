package ducnguyen.hex;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by ducng on 12/22/2016.
 */

public class Meter {
    private Rect rect;
    private Paint paint = new Paint();
    public Meter(int left, int top, int right, int bottom, int index){
        rect = new Rect(left,top,right,bottom);
        paint.setColor(Color.rgb(238, 37 + 20*index, 38));
    }
    public void setRect(Rect rect){
        this.rect = rect;
    }
    public Rect getRect(){
        return rect;
    }
    public void setPaint(int color){
        paint.setColor(color);
    }
    public Paint getPaint(){
        return paint;
    }
}

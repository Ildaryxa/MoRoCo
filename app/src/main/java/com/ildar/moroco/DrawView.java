package com.ildar.moroco;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by ildar on 13.06.2016.
 */
public class DrawView extends View {

    private ArrayList<Point> points;
    private Paint p;
    private Path path;
    private boolean canDraw;

    public DrawView(Context context) {
        super(context);
        p = new Paint();
        p.setStrokeWidth(15);
        p.setStyle(Paint.Style.STROKE);

        path = new Path();

        points = new ArrayList<>();
        canDraw = true;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(80, 102, 204, 255);
        if (!points.isEmpty()){
            path.reset();
            path.moveTo(points.get(0).x, points.get(0).y);
            path.addCircle(points.get(0).x, points.get(0).y, 15, Path.Direction.CW);
            for (Point pos: points) {
                path.lineTo(pos.x, pos.y);
            }
            p.setColor(Color.RED);
            canvas.drawPath(path, p);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (canDraw) {
                    points.add(new Point((int) event.getX(), (int) event.getY()));
                    invalidate();
                }
                break;
        }
        return true;
    }

    public void undoTheLastAction(){
        if (!points.isEmpty() && canDraw) {
            points.remove(points.size() - 1);
            invalidate();
        }
    }

    public void allowedDraw(){
        canDraw = true;
    }

    public void forbidden(){
        canDraw = false;
    }
}

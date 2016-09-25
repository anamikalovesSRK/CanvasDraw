package com.example.anamika.canvasdraw;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class TouchView extends ImageView implements View.OnTouchListener {
    private Paint paint,textPaint;
    private Bitmap mBitmapImage;
    private Path path = new Path();
    private ArrayList<Path> paths = new ArrayList<Path>();

    public TouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setOnTouchListener(this);


        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        this.setBackgroundColor(Color.WHITE);
        paint.setDither(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        setDrawingCacheEnabled(true);
    }

    public Bitmap returnBitmap()
    {
        return getDrawingCache();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Path p : paths) {
            canvas.drawPath(p, paint);
        }
//        canvas.drawPath(path, paint);
    }


    public void undo() {

        if (paths.size()>0)
        {
            Log.d("myTag",""+paths.size());
//            paths.remove(paths.size()-1);
            paths.remove(paths.get(paths.size()-1));
            Log.d("myTag",""+paths.size());

        }
        invalidate();

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Point point = new Point();
        float pointX = event.getX();
        float pointY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                 path.reset();
                path.moveTo(pointX, pointY);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                    path.lineTo(pointX,pointY);
                    invalidate();
                break;
            case MotionEvent.ACTION_UP:
                path.lineTo(pointX,pointY);

                paths.add(path);
                invalidate();
               // path = new Path();
                break;

            default:return false;
        }
        return true;
    }

    public void save() {
        String path = Environment.getExternalStorageDirectory().toString();
        File file = new File(path, "saving.jpg");
        OutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            mBitmapImage=returnBitmap();
            mBitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Log.d("myTag","in save "+path);
            //+Environment.getExternalStorageDirectory().toString()
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Save Failed", Toast.LENGTH_SHORT).show();
        }
    }


}


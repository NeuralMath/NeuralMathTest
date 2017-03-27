package com.example.marc4492.neuralmathtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;


/**
 * Author: Created by Mathieu on 2017-02-22.
 * This is a view that allow the user to draw on the screen and that save a png of the drawing
 *
 * Code based on a tutorial by Sue Smith
 * link: https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-essential-functionality--mobile-19328
 */

public class DrawingView extends View {

    private Bitmap  mBitmap;
    private Canvas  mCanvas;
    private Path    mPath;
    private Context context;
    private Paint mPaint;
    private Handler saveHandler;

    /**
     * Constructeur par défaut, création du style de l'écriture
     * @param c     Contexte de l'app
     */
    public DrawingView(Context c) {
        super(c);
        context = c;
        mPath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(ContextCompat.getColor(context, R.color.black));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(20);

        setDrawingCacheEnabled(true);

        saveHandler = new Handler();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Créer le bitmpa dasn lequel le canvas va s'enregistrer
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Save le canvas dasn le bitmap
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        //Actions selon le touvher
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                mPath.lineTo(x, y);
                mCanvas.drawPath(mPath,  mPaint);
                mPath.reset();
                break;
        }
        invalidate();

        //Creation of a saving timer that save only at the end of the drawing
        saveHandler.removeCallbacks(run); //remove the saving timer
        saveHandler.postDelayed(run, 2000); //create a new saving timer

        return true;
    }

    /**
     * Clear the canvas and stop the saving timer
     */
    public void clear()
    {
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    /**
     * The runnable that calls saveCharacter and clear functions after a set delay
     */
    private Runnable run = new Runnable() {
        @Override
        public void run() {
            ((MainActivity)context).setBitmap(getDrawingCache());
            clear();
        }
    };
}
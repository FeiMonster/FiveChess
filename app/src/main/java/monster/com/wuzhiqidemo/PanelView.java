package monster.com.wuzhiqidemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2017/6/15.
 */

public class PanelView extends View {
    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;
    private Bitmap mWhitePiece;
    private Bitmap mBackPiece;
    private float radioPieceOfLineHeight = 3 * 1.0f / 4;
    private Paint mPaint = new Paint();
    private List<Point> mWhiteArrar = new ArrayList<>();
    private List<Point> mBackArray = new ArrayList<>();
    private boolean mIsGameOver;
    private boolean mIsWhiteWinner;
    private int MAX_COUNT_IN_LINE = 5;


    private Boolean mIsWhite = true;//白起先手 或者轮到白棋

    public PanelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

        int pieceWidth = (int) (mLineHeight * radioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBackPiece = Bitmap.createScaledBitmap(mBackPiece, pieceWidth, pieceWidth, false);
        Log.d("TAG", "大小" + pieceWidth);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(wSize, hSize);
        if (wMode == MeasureSpec.UNSPECIFIED) {
            width = hSize;
        } else if (hMode == MeasureSpec.UNSPECIFIED) {
            width = wSize;
        }
        setMeasuredDimension(width, width);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsGameOver) return false;
        if (event.getAction() == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getValidPoint(x, y);
            if (mWhiteArrar.contains(p) || mBackArray.contains(p)) {
                return false;
            }

            if (mIsWhite) {
                mWhiteArrar.add(p);
            } else {
                mBackArray.add(p);
            }
            invalidate();
            mIsWhite = !mIsWhite;
            return true;
        }
        return true;
    }


    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPiece(canvas);
        checkGameOver();
    }

    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhiteArrar);
        boolean backWin = checkFiveInLine(mWhiteArrar);
        if (whiteWin || backWin) {
            mIsGameOver = true;
            mIsWhiteWinner = whiteWin;

            String text = mIsWhiteWinner ? "白棋胜利" : "黑棋胜利";

            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point p : points) {
            int x = p.x;
            int y = p.y;
            boolean win = checkHorizontal(x, y, points);
            if(win) return true;
            win = checkVertical(x,y,points);
            if(win) return  true;
            win = checkLeftDiagonal(x,y,points);
            if(win) return  true;
            win = checkRightDiagnal(x,y,points);
            if(win) return  true;
        }
        return false;
    }
    private boolean checkRightDiagnal(int x, int y, List<Point> points) {
        int count = 1;
        //
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        return false;
    }
    private boolean checkLeftDiagonal(int x, int y, List<Point> points) {
        int count = 1;
        //
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y+ i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        return false;
    }
    private boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;
        //上
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x , y- i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x , y + i))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        return false;
    }
    /**
     * 判断想x，y位置的棋子，是否相邻五个一致
     *
     * @param x
     * @param y
     * @param points
     * @return
     */

    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        //
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count == MAX_COUNT_IN_LINE) return true;
        for (int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }
            if (count == MAX_COUNT_IN_LINE) return true;
            return false;
        }


    private void drawPiece(Canvas canvas) {
        for (int i = 0,n = mWhiteArrar.size();i < n;i++) {
            Point whitePoint = mWhiteArrar.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whitePoint.x + (1 -radioPieceOfLineHeight) / 2) * mLineHeight,
                    (whitePoint.y + (1 -radioPieceOfLineHeight) / 2) * mLineHeight,null);
        }
        for (int i = 0,n = mBackArray.size();i < n;i++) {
            Point blackPoint = mBackArray.get(i);
            canvas.drawBitmap(mBackPiece,
                    (blackPoint.x + (1 -radioPieceOfLineHeight) / 2) * mLineHeight,
                    (blackPoint.y + (1 -radioPieceOfLineHeight) / 2) * mLineHeight,null);
        }
    }

    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeigt = mLineHeight;
        for(int i = 0;i<MAX_LINE;i++){
            int startX = (int) (lineHeigt/2);
            int endX = (int) (w-lineHeigt/2);

            int y = (int) ((0.5 + i)*lineHeigt);
            canvas.drawLine(startX,y,endX,y,mPaint);
            canvas.drawLine(y,startX,y,endX,mPaint);
        }
    }
    /**
     * 当View被销毁时需要保存游戏数据
     */
    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";
    public  void resStart(){
        mWhiteArrar.clear();
        mBackArray.clear();
        mIsGameOver  =false;
        invalidate();
    }
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, (ArrayList<? extends Parcelable>) mWhiteArrar);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, (ArrayList<? extends Parcelable>) mWhiteArrar);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArrar = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }
        super.onRestoreInstanceState(state);
    }
}

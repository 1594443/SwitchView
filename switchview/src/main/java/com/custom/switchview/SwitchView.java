package com.custom.switchview;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

/**
 * @author 1594443
 * Created on 2025/04/21
 * Description: 自定义开关
 */
public class SwitchView extends View {

    private final Context context;
    //开关状态
    private boolean status = false;
    //开-track颜色
    private int trackOnColor = Color.GREEN;
    //开-track文字
    private String trackOnText;
    //开-track文字颜色
    private int trackOnTextColor = Color.WHITE;
    //关-track文字
    private String trackOffText;
    //关-track颜色
    private int trackOffColor = Color.GRAY;
    //关-track文字颜色
    private int trackOffTextColor = Color.WHITE;
    //track文字大小
    private int trackTextSize = 20;
    //thumb颜色
    private int thumbColor = Color.WHITE;
    //thumb文字
    private String thumbText;
    //thumb文字颜色
    private int thumbTextColor = Color.BLACK;
    //thumb文字大小
    private int thumbTextSize = 14;

    //不加 padding 的宽和高
    private int mWidth, mHeight;

    //track胶囊画笔
    private Paint trackPaint;
    //track文字画笔
    private Paint trackTextPaint;
    //thumb胶囊画笔
    private Paint thumbPaint;
    //thumb文字画笔
    private Paint thumbTextPaint;

    //track胶囊
    private final RectF trackRect = new RectF();
    //track胶囊圆角半径
    private float trackRadius;
    //thumb胶囊
    private final RectF thumbRect = new RectF();
    //开-thumb胶囊
    private final RectF thumbOnRect = new RectF();
    //关-thumb胶囊
    private final RectF thumbOffRect = new RectF();
    //thumb胶囊圆角半径
    private float thumbRadius;

    private float thumbOnDx, thumbOffDx, thumbDx, thumbBaseLine;

    private ValueAnimator animator;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public SwitchView(Context context) {
        this(context, null);
    }

    public SwitchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SwitchView);
        //开关状态
        status = array.getBoolean(R.styleable.SwitchView_status, status);
        //开-track颜色
        trackOnColor = array.getColor(R.styleable.SwitchView_track_on_color, trackOnColor);
        //开-track文字
        trackOnText = array.getString(R.styleable.SwitchView_track_on_text);
        //开-track文字颜色
        trackOnTextColor = array.getColor(R.styleable.SwitchView_track_on_text_color, trackOnTextColor);
        //关-track颜色
        trackOffColor = array.getColor(R.styleable.SwitchView_track_off_color, trackOffColor);
        //关-track文字
        trackOffText = array.getString(R.styleable.SwitchView_track_off_text);
        //关-track文字颜色
        trackOffTextColor = array.getColor(R.styleable.SwitchView_track_off_text_color, trackOffTextColor);
        //track文字大小
        trackTextSize = array.getDimensionPixelSize(R.styleable.SwitchView_track_text_size, trackTextSize);
        //thumb颜色
        thumbColor = array.getColor(R.styleable.SwitchView_thumb_color, thumbColor);
        //thumb文字
        thumbText = array.getString(R.styleable.SwitchView_thumb_text);
        //thumb文字颜色
        thumbTextColor = array.getColor(R.styleable.SwitchView_thumb_text_color, thumbTextColor);
        //thumb文字大小
        thumbTextSize = array.getDimensionPixelSize(R.styleable.SwitchView_thumb_text_size, thumbTextSize);
        array.recycle();

        initPaint();
    }

    private void initPaint() {
        //初始化 track胶囊画笔
        trackPaint = new Paint();
        trackPaint.setStyle(Paint.Style.FILL);
        trackPaint.setColor(trackOffColor);
        trackPaint.setStrokeCap(Paint.Cap.BUTT);
        trackPaint.setAntiAlias(true);
        trackPaint.setDither(true);

        //初始化 thumb胶囊画笔
        thumbPaint = new Paint();
        thumbPaint.setStyle(Paint.Style.FILL);
        thumbPaint.setColor(thumbColor);
        thumbPaint.setStrokeCap(Paint.Cap.BUTT);
        thumbPaint.setAntiAlias(true);
        thumbPaint.setDither(true);

        //初始化 track文字画笔
        trackTextPaint = new Paint();
        trackTextPaint.setAntiAlias(true);
        trackTextPaint.setColor(trackOffTextColor);
        trackTextPaint.setTextSize(trackTextSize);

        //初始化 thumb文字画笔
        thumbTextPaint = new Paint();
        thumbTextPaint.setAntiAlias(true);
        thumbTextPaint.setColor(thumbTextColor);
        thumbTextPaint.setTextSize(thumbTextSize);
    }

    @Override
    @SuppressLint("SwitchIntDef")
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //宽高的模式：三种分别是———MeasureSpec.AT_MOST，MeasureSpec.EXACTLY，MeasureSpec.UNSPECIFIED
        // MeasureSpec.AT_MOST 在布局中指定了 wrap_content
        // MeasureSpec.EXACTLY 在布局中指定了确切的值，例如：100dp，match_parent
        // MeasureSpec.UNSPECIFIED 在布局中尽可能的大，很少用到，例如：ListView，Recycler，ScrollView
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                width = PxUtils.dpToPx(context, 100) + getPaddingStart() + getPaddingEnd();
                break;
            case MeasureSpec.EXACTLY:
                width = width + getPaddingStart() + getPaddingEnd();
                break;
        }
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                height = PxUtils.dpToPx(context, 30) + getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.EXACTLY:
                height = height + getPaddingTop() + getPaddingBottom();
                break;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //计算真实宽和高
        mWidth = w - getPaddingStart() - getPaddingEnd();
        mHeight = h - getPaddingTop() - getPaddingBottom();
        //track胶囊
        trackRect.left = getPaddingStart();
        trackRect.top = getPaddingTop();
        trackRect.right = w - getPaddingEnd();
        trackRect.bottom = h - getPaddingBottom();
        //track胶囊圆角半径，取宽和高中最小值的一半作为圆角矩形的半径
        trackRadius = Math.min(trackRect.right, trackRect.bottom) / 2;

        //thumb胶囊的间隔
        float interval = 5f;
        //开-thumb胶囊
        thumbOnRect.left = (w / 2) + interval;
        thumbOnRect.top = getPaddingTop() + interval;
        thumbOnRect.right = w - getPaddingEnd() - interval;
        thumbOnRect.bottom = h - getPaddingBottom() - interval;

        //关-thumb胶囊
        thumbOffRect.left = getPaddingStart() + interval;
        thumbOffRect.top = getPaddingTop() + interval;
        thumbOffRect.right = (w / 2) - interval;
        thumbOffRect.bottom = h - getPaddingBottom() - interval;
        //thumb胶囊圆角半径，取宽和高中最小值的一半作为圆角矩形的半径
        thumbRadius = Math.min(thumbOffRect.right, thumbOffRect.bottom) / 2;

        //计算thumb文字的位置
        if (thumbText != null) {
            Rect rect = new Rect();
            thumbTextPaint.getTextBounds(thumbText, 0, thumbText.length(), rect);
            thumbOffDx = (mWidth / 2 - rect.width()) / 2 + getPaddingStart();
            thumbOnDx = mWidth * 3 / 4 - rect.width() / 2 + getPaddingStart();
            Paint.FontMetricsInt thumbFontMetrics = thumbTextPaint.getFontMetricsInt();
            int thumbDy = (thumbFontMetrics.bottom - thumbFontMetrics.top) / 2 - thumbFontMetrics.bottom;
            thumbBaseLine = mHeight / 2 + thumbDy + getPaddingTop();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (status) {
            //画 track胶囊
            trackPaint.setColor(trackOnColor);
            canvas.drawRoundRect(trackRect, trackRadius, trackRadius, trackPaint);
            //画开-track文字
            if (trackOnText != null) {
                trackTextPaint.setColor(trackOnTextColor);
                Rect trackTextRect = new Rect();
                trackTextPaint.getTextBounds(trackOnText, 0, trackOnText.length(), trackTextRect);
                int trackDx = (mWidth / 2 - trackTextRect.width()) / 2 + getPaddingStart();
                Paint.FontMetricsInt trackFontMetrics = trackTextPaint.getFontMetricsInt();
                int trackDy = (trackFontMetrics.bottom - trackFontMetrics.top) / 2 - trackFontMetrics.bottom;
                int trackBaseLine = mHeight / 2 + trackDy + getPaddingTop();
                canvas.drawText(trackOnText, trackDx, trackBaseLine, trackTextPaint);
            }

            if (animator == null) {
                //画开-thumb胶囊
                canvas.drawRoundRect(thumbOnRect, thumbRadius, thumbRadius, thumbPaint);
                //画开-thumb文字
                if (thumbText != null) {
                    canvas.drawText(thumbText, thumbOnDx, thumbBaseLine, thumbTextPaint);
                }
            } else {
                //画开-thumb胶囊
                canvas.drawRoundRect(thumbRect, thumbRadius, thumbRadius, thumbPaint);
                //画开-thumb文字
                if (thumbText != null) {
                    canvas.drawText(thumbText, thumbDx, thumbBaseLine, thumbTextPaint);
                }
            }
        } else {
            //画 track胶囊
            trackPaint.setColor(trackOffColor);
            canvas.drawRoundRect(trackRect, trackRadius, trackRadius, trackPaint);
            //画关-track文字
            if (trackOffText != null) {
                trackTextPaint.setColor(trackOffTextColor);
                Rect trackTextRect = new Rect();
                trackTextPaint.getTextBounds(trackOffText, 0, trackOffText.length(), trackTextRect);
                int trackDx = mWidth * 3 / 4 - trackTextRect.width() / 2 + getPaddingStart();
                Paint.FontMetricsInt trackFontMetrics = trackTextPaint.getFontMetricsInt();
                int trackDy = (trackFontMetrics.bottom - trackFontMetrics.top) / 2 - trackFontMetrics.bottom;
                int trackBaseLine = mHeight / 2 + trackDy + getPaddingTop();
                canvas.drawText(trackOffText, trackDx, trackBaseLine, trackTextPaint);
            }
            if (animator == null) {
                //画关-thumb胶囊
                canvas.drawRoundRect(thumbOffRect, thumbRadius, thumbRadius, thumbPaint);
                //画关-thumb文字
                if (thumbText != null) {
                    canvas.drawText(thumbText, thumbOffDx, thumbBaseLine, thumbTextPaint);
                }
            } else {
                //画关-thumb胶囊
                canvas.drawRoundRect(thumbRect, thumbRadius, thumbRadius, thumbPaint);
                //画关-thumb文字
                if (thumbText != null) {
                    canvas.drawText(thumbText, thumbDx, thumbBaseLine, thumbTextPaint);
                }
            }
        }
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            status = !status;
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, status);
            }
            animator();
        }
        return true;
    }

    private void animator() {
        if (status) {
            animator = ValueAnimator.ofFloat(0, mWidth / 2);
            animator.setDuration(200);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(animation -> {
                float animatedFraction = animation.getAnimatedFraction();
                thumbRect.left = thumbOffRect.left + (thumbOnRect.left - thumbOffRect.left) * animatedFraction; // 计算新的left位置
                thumbRect.top = thumbOffRect.top;
                thumbRect.right = thumbOffRect.right + (thumbOnRect.right - thumbOffRect.right) * animatedFraction; // 计算新的right位置
                thumbRect.bottom = thumbOffRect.bottom;
                thumbDx = thumbOffDx + (thumbOnDx - thumbOffDx) * animatedFraction;//计算新的X位置
                invalidate();
            });
            animator.start();
        } else {
            animator = ValueAnimator.ofFloat(mWidth / 2, 0);
            animator.setDuration(200);
            animator.setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(animation -> {
                float animatedFraction = animation.getAnimatedFraction(); // 获取动画进度（0到1）
                thumbRect.left = thumbOnRect.left - (thumbOnRect.left - thumbOffRect.left) * animatedFraction; // 计算新的left位置
                thumbRect.top = thumbOnRect.top;
                thumbRect.right = thumbOnRect.right - (thumbOnRect.right - thumbOffRect.right) * animatedFraction; // 计算新的right位置
                thumbRect.bottom = thumbOnRect.bottom;
                thumbDx = thumbOnDx - (thumbOnDx - thumbOffDx) * animatedFraction;//计算新的X位置
                invalidate();
            });
            animator.start();
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(SwitchView switchView, boolean isChecked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
        invalidate();
    }

    public int getTrackOnColor() {
        return trackOnColor;
    }

    public void setTrackOnColor(int trackOnColor) {
        this.trackOnColor = trackOnColor;
    }

    public String getTrackOnText() {
        return trackOnText;
    }

    public void setTrackOnText(String trackOnText) {
        this.trackOnText = trackOnText;
    }

    public int getTrackOnTextColor() {
        return trackOnTextColor;
    }

    public void setTrackOnTextColor(int trackOnTextColor) {
        this.trackOnTextColor = trackOnTextColor;
    }

    public String getTrackOffText() {
        return trackOffText;
    }

    public void setTrackOffText(String trackOffText) {
        this.trackOffText = trackOffText;
    }

    public int getTrackOffColor() {
        return trackOffColor;
    }

    public void setTrackOffColor(int trackOffColor) {
        this.trackOffColor = trackOffColor;
    }

    public int getTrackOffTextColor() {
        return trackOffTextColor;
    }

    public void setTrackOffTextColor(int trackOffTextColor) {
        this.trackOffTextColor = trackOffTextColor;
    }

    public int getTrackTextSize() {
        return trackTextSize;
    }

    public void setTrackTextSize(int trackTextSize) {
        this.trackTextSize = trackTextSize;
    }

    public int getThumbColor() {
        return thumbColor;
    }

    public void setThumbColor(int thumbColor) {
        this.thumbColor = thumbColor;
    }

    public String getThumbText() {
        return thumbText;
    }

    public void setThumbText(String thumbText) {
        this.thumbText = thumbText;
    }

    public int getThumbTextColor() {
        return thumbTextColor;
    }

    public void setThumbTextColor(int thumbTextColor) {
        this.thumbTextColor = thumbTextColor;
    }

    public int getThumbTextSize() {
        return thumbTextSize;
    }

    public void setThumbTextSize(int thumbTextSize) {
        this.thumbTextSize = thumbTextSize;
    }
}

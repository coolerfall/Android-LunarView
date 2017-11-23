package com.coolerfall.widget.lunar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Display the week label in the top of {@link MonthView}.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class WeekLabelView extends View {
  private static final int DAYS_IN_WEEK = 7;
  private static final String[] CHINESE_WEEK = { "日", "一", "二", "三", "四", "五", "六" };
  private final Region[] weekRegion = new Region[DAYS_IN_WEEK];
  private Paint paint;

  /**
   * The constructor of week view.
   *
   * @param context the context to use
   */
  public WeekLabelView(Context context) {
    super(context);
    init();
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    int itemWidth = (int) (w / 7f);
    for (int i = 0; i < DAYS_IN_WEEK; i++) {
      weekRegion[i].set(i * itemWidth, 0, (i + 1) * itemWidth, h);
    }

    paint.setTextSize(w / 25f);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
    Paint.FontMetrics fm = paint.getFontMetrics();
    int heightSize = (int) Math.ceil(fm.descent - fm.ascent) + getPaddingTop() + getPaddingBottom();

    setMeasuredDimension(widthSize, heightSize);
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    Paint.FontMetrics fm = paint.getFontMetrics();
    for (int i = 0; i < DAYS_IN_WEEK; i++) {
      Rect rect = weekRegion[i].getBounds();
      float centerY = rect.height() / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
      canvas.drawText(CHINESE_WEEK[i], rect.centerX(), centerY, paint);
    }
  }

  /* init week view */
  private void init() {
    paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
    paint.setTextAlign(Paint.Align.CENTER);
    paint.setColor(Color.GRAY);
    int padding = dip2px(getContext());
    setPadding(0, padding, 0, padding);

    for (int i = 0; i < DAYS_IN_WEEK; i++) {
      Region region = new Region();
      weekRegion[i] = region;
    }
  }

  private static int dip2px(Context context) {
    int dpValue = 10;
    int dpi = context.getResources().getDisplayMetrics().densityDpi;
    if (dpi >= DisplayMetrics.DENSITY_XXXHIGH) {
      dpValue = 18;
    } else if (dpi >= DisplayMetrics.DENSITY_XXHIGH) {
      dpValue = 16;
    } else if (dpi >= DisplayMetrics.DENSITY_XHIGH) {
      dpValue = 15;
    } else if (dpi >= DisplayMetrics.DENSITY_HIGH) {
      dpValue = 14;
    }

    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }
}

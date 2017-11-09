package com.coolerfall.widget.lunar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import java.util.Calendar;

/**
 * Display one month with solar and lunar date on a calendar.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
@SuppressLint("ViewConstructor") final class MonthView extends View {
  private static final int DAYS_IN_WEEK = 7;

  private int selectedIndex = -1;

  private float solarTextSize;
  private float lunarTextSize;
  private float lunarOffset;
  private float circleRadius;

  private Month month;
  private LunarView lunarView;

  private final Region[][] monthWithFourWeeks = new Region[4][DAYS_IN_WEEK];
  private final Region[][] monthWithFiveWeeks = new Region[5][DAYS_IN_WEEK];
  private final Region[][] monthWithSixWeeks = new Region[6][DAYS_IN_WEEK];
  private Paint paint;

  /**
   * The constructor of month view.
   *
   * @param context context to use
   * @param lunarView {@link LunarView}
   */
  public MonthView(Context context, Month month, LunarView lunarView) {
    super(context);

    this.month = month;
    this.lunarView = lunarView;
    init();
  }

  @Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);

    int dayWidth = (int) (w / 7f);
    int dayHeightInFourWeek = (int) (h / 4f);
    int dayHeightInFiveWeek = (int) (h / 5f);
    int dayHeightInSixWeek = (int) (h / 6f);

    circleRadius = dayWidth / 2.2f;

    solarTextSize = h / 15f;
    paint.setTextSize(solarTextSize);
    float solarHeight = paint.getFontMetrics().bottom - paint.getFontMetrics().top;

    lunarTextSize = solarTextSize / 2;
    paint.setTextSize(lunarTextSize);
    float lunarHeight = paint.getFontMetrics().bottom - paint.getFontMetrics().top;

    lunarOffset = (Math.abs(paint.ascent() + paint.descent()) + solarHeight + lunarHeight) / 3f;

    initMonthRegion(monthWithFourWeeks, dayWidth, dayHeightInFourWeek);
    initMonthRegion(monthWithFiveWeeks, dayWidth, dayHeightInFiveWeek);
    initMonthRegion(monthWithSixWeeks, dayWidth, dayHeightInSixWeek);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
    setMeasuredDimension(measureWidth, (int) (measureWidth * 6f / 7f));
  }

  @Override protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    if (month == null) {
      return;
    }

    canvas.save();
    int weeks = month.getWeeksInMonth();
    Region[][] monthRegion = getMonthRegion();

    for (int i = 0; i < weeks; i++) {
      for (int j = 0; j < DAYS_IN_WEEK; j++) {
        draw(canvas, monthRegion[i][j].getBounds(), i, j);
      }
    }
    canvas.restore();
  }

  @SuppressLint("ClickableViewAccessibility") @Override
  public boolean onTouchEvent(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        return true;

      case MotionEvent.ACTION_UP:
        handleClickEvent((int) event.getX(), (int) event.getY());
        return true;

      default:
        return super.onTouchEvent(event);
    }
  }

  /* init month view */
  private void init() {
    paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
    paint.setTextAlign(Paint.Align.CENTER);

    if (month.isMonthOfToday()) {
      selectedIndex = month.getIndexOfToday();
    }

    setBackgroundColor(lunarView.getMonthBackgroundColor());
  }

  /* init month region with the width and height of day */
  private void initMonthRegion(Region[][] monthRegion, int dayWidth, int dayHeight) {
    for (int i = 0; i < monthRegion.length; i++) {
      for (int j = 0; j < monthRegion[i].length; j++) {
        Region region = new Region();
        region.set(j * dayWidth, i * dayHeight, dayWidth + j * dayWidth, dayWidth + i * dayHeight);
        monthRegion[i][j] = region;
      }
    }
  }

  /* get month region for current month */
  private Region[][] getMonthRegion() {
    int weeks = month.getWeeksInMonth();
    Region[][] monthRegion;
    if (weeks == 4) {
      monthRegion = monthWithFourWeeks;
    } else if (weeks == 5) {
      monthRegion = monthWithFiveWeeks;
    } else {
      monthRegion = monthWithSixWeeks;
    }

    return monthRegion;
  }

  /* draw all the text in month view */
  private void draw(Canvas canvas, Rect rect, int xIndex, int yIndex) {
    MonthDay monthDay = month.getMonthDay(xIndex, yIndex);

    drawBackground(canvas, rect, monthDay, xIndex, yIndex);
    drawSolarText(canvas, rect, monthDay);
    drawLunarText(canvas, rect, monthDay);
  }

  /* draw solar text in month view */
  private void drawSolarText(Canvas canvas, Rect rect, MonthDay monthDay) {
    if (monthDay == null) {
      return;
    }

    if (!monthDay.isCheckable()) {
      paint.setColor(lunarView.getUnCheckableColor());
    } else if (monthDay.isWeekend()) {
      paint.setColor(lunarView.getHightlightColor());
    } else {
      paint.setColor(lunarView.getSolarTextColor());
    }

    paint.setTextSize(solarTextSize);
    canvas.drawText(monthDay.getSolarDay(), rect.centerX(), rect.centerY(), paint);
  }

  /* draw lunar text in month view */
  private void drawLunarText(Canvas canvas, Rect rect, MonthDay monthDay) {
    if (monthDay == null) {
      return;
    }

    if (!monthDay.isCheckable()) {
      paint.setColor(lunarView.getUnCheckableColor());
    } else if (monthDay.isHoliday()) {
      paint.setColor(lunarView.getHightlightColor());
    } else {
      paint.setColor(lunarView.getLunarTextColor());
    }

    paint.setTextSize(lunarTextSize);
    canvas.drawText(monthDay.getLunarDay(), rect.centerX(), rect.centerY() + lunarOffset, paint);
  }

  /* draw circle for selected day */
  private void drawBackground(Canvas canvas, Rect rect, MonthDay day, int xIndex, int yIndex) {
    if (day.isToday()) {
      Drawable background = lunarView.getTodayBackground();
      if (background == null) {
        drawRing(canvas, rect);
      } else {
        background.setBounds(rect);
        background.draw(canvas);
      }

      return;
    }

		/* not today was selected */
    if (selectedIndex == -1 && day.isFirstDay()) {
      selectedIndex = xIndex * DAYS_IN_WEEK + yIndex;
    }

    if (selectedIndex / DAYS_IN_WEEK != xIndex || selectedIndex % DAYS_IN_WEEK != yIndex) {
      return;
    }

    paint.setColor(lunarView.getCheckedDayBackgroundColor());
    canvas.drawCircle(rect.centerX(), rect.centerY(), circleRadius, paint);
  }

  /* draw ring as background of today */
  private void drawRing(Canvas canvas, Rect rect) {
    paint.setColor(Color.RED);
    canvas.drawCircle(rect.centerX(), rect.centerY(), circleRadius, paint);
    paint.setColor(lunarView.getMonthBackgroundColor());
    canvas.drawCircle(rect.centerX(), rect.centerY(), circleRadius - 4, paint);
  }

  /* handle date click event */
  private void handleClickEvent(int x, int y) {
    Region[][] monthRegion = getMonthRegion();
    for (int i = 0; i < monthRegion.length; i++) {
      for (int j = 0; j < DAYS_IN_WEEK; j++) {
        Region region = monthRegion[i][j];
        if (!region.contains(x, y)) {
          continue;
        }

        MonthDay monthDay = month.getMonthDay(i, j);
        if (monthDay == null) {
          return;
        }

        int day = monthDay.getCalendar().get(Calendar.DAY_OF_MONTH);

        if (monthDay.isCheckable()) {
          selectedIndex = i * DAYS_IN_WEEK + j;
          performDayClick();
          invalidate();
        } else {
          if (monthDay.getDayFlag() == MonthDay.PREV_MONTH_DAY) {
            lunarView.showPrevMonth(day);
          } else if (monthDay.getDayFlag() == MonthDay.NEXT_MONTH_DAY) {
            lunarView.showNextMonth(day);
          }
        }
        break;
      }
    }
  }

  /**
   * Perform day click event.
   */
  protected void performDayClick() {
    MonthDay monthDay = month.getMonthDay(selectedIndex);
    lunarView.dispatchDateClickListener(monthDay);
  }

  /**
   * Set selected day, the selected day will draw background.
   *
   * @param day selected day
   */
  protected void setSelectedDay(int day) {
    if (month.isMonthOfToday() && day == 0) {
      selectedIndex = month.getIndexOfToday();
    } else {
      int selectedDay = day == 0 ? 1 : day;
      selectedIndex = month.getIndexOfDayInCurMonth(selectedDay);
    }

    invalidate();

    if ((day == 0 && lunarView.getShouldPickOnMonthChange()) || day != 0) {
      performDayClick();
    }
  }
}

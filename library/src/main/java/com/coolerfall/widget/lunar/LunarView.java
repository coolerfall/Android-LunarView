package com.coolerfall.widget.lunar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import java.util.Calendar;

/**
 * This class is a calendar widget for displaying and selecting dates.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public class LunarView extends LinearLayout {
  private int solarTextColor = 0xff454545;
  private int lunarTextColor = Color.GRAY;
  private int hightlistColor = 0xff03a9f4;
  private int uncheckableColor = 0xffb0b0b0;
  private int monthBackgroundColor = 0xfffafafa;
  private int weekLabelBackgroundColor = 0xfffafafa;
  private int checkedDayBackgroundColor = 0xffeaeaea;
  private Drawable todayBackground;
  private boolean shouldPickOnMonthChange = true;

  private ViewPager viewPager;
  private MonthPagerAdapter monthPagerAdapter;
  private WeekLabelView weekLabelView;
  private OnDatePickListener onDatePickListener;
  private boolean isChangedByUser;

  public LunarView(Context context) {
    this(context, null);
  }

  public LunarView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public LunarView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int measureWidth = MeasureSpec.getSize(widthMeasureSpec);

    for (int i = 0; i < getChildCount(); i++) {
      final View child = getChildAt(i);

      int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY);
      int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY);
      child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    int measureHeight = (int) (measureWidth * 6f / 7f) + weekLabelView.getMeasuredHeight();
    setMeasuredDimension(measureWidth, measureHeight);
  }

  /* init lunar view */
  private void init(AttributeSet attrs) {
    /* get custom attrs */
    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LunarView);
    monthBackgroundColor =
        a.getColor(R.styleable.LunarView_monthBackgroundColor, monthBackgroundColor);
    weekLabelBackgroundColor =
        a.getColor(R.styleable.LunarView_monthBackgroundColor, weekLabelBackgroundColor);
    solarTextColor = a.getColor(R.styleable.LunarView_solarTextColor, solarTextColor);
    lunarTextColor = a.getColor(R.styleable.LunarView_lunarTextColor, lunarTextColor);
    hightlistColor = a.getColor(R.styleable.LunarView_highlightColor, hightlistColor);
    uncheckableColor = a.getColor(R.styleable.LunarView_uncheckableColor, uncheckableColor);
    todayBackground = a.getDrawable(R.styleable.LunarView_todayBackground);
    checkedDayBackgroundColor =
        a.getColor(R.styleable.LunarView_checkedDayBackgroundColor, checkedDayBackgroundColor);
    shouldPickOnMonthChange =
        a.getBoolean(R.styleable.LunarView_shouldPickOnMonthChange, shouldPickOnMonthChange);
    a.recycle();

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      /* if we're on good Android versions, turn off clipping for cool effects */
      setClipToPadding(false);
      setClipChildren(false);
    } else {
			/* old Android does not like _not_ clipping view pagers, we need to clip */
      setClipChildren(true);
      setClipToPadding(true);
    }

		/* set the orientation to vertical */
    setOrientation(VERTICAL);

    weekLabelView = new WeekLabelView(getContext());
    weekLabelView.setBackgroundColor(weekLabelBackgroundColor);
    addView(weekLabelView);

    viewPager = new ViewPager(getContext());
    viewPager.setOffscreenPageLimit(1);
    addView(viewPager);

    monthPagerAdapter = new MonthPagerAdapter(getContext(), this);
    viewPager.setAdapter(monthPagerAdapter);
    viewPager.addOnPageChangeListener(mPageListener);
    viewPager.setCurrentItem(monthPagerAdapter.getIndexOfCurrentMonth());
    viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
      @Override public void transformPage(View page, float position) {
        page.setAlpha(1 - Math.abs(position));
      }
    });
  }

  /* page change listener */
  private ViewPager.OnPageChangeListener mPageListener = new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override public void onPageSelected(int position) {
      if (isChangedByUser) {
        isChangedByUser = false;
        return;
      }

      monthPagerAdapter.resetSelectedDay(position);
    }

    @Override public void onPageScrollStateChanged(int state) {

    }
  };

  /* get color with given color resource id */
  private int getColor(@ColorRes int resId) {
    return ContextCompat.getColor(getContext(), resId);
  }

  /* get color with given drawable resource id */
  private Drawable getDrawable(@DrawableRes int resId) {
    return ContextCompat.getDrawable(getContext(), resId);
  }

  /**
   * Interface definition for a callback to be invoked when date picked.
   */
  public interface OnDatePickListener {
    /**
     * Invoked when date picked.
     *
     * @param view {@link LunarView}
     * @param monthDay {@link MonthDay}
     */
    void onDatePick(LunarView view, MonthDay monthDay);
  }

  /**
   * Get the color of month view background.
   *
   * @return color of month background
   */
  protected int getMonthBackgroundColor() {
    return monthBackgroundColor;
  }

  /**
   * Get the text color of solar day.
   *
   * @return text color of solar day
   */
  protected int getSolarTextColor() {
    return solarTextColor;
  }

  /**
   * Get the text color of lunar day.
   *
   * @return text color of lunar day
   */
  protected int getLunarTextColor() {
    return lunarTextColor;
  }

  /**
   * Get the highlight color.
   *
   * @return thighlight color
   */
  protected int getHightlightColor() {
    return hightlistColor;
  }

  /**
   * Get the color of uncheckable day.
   *
   * @return uncheckable color
   */
  protected int getUnCheckableColor() {
    return uncheckableColor;
  }

  /**
   * Get the background of today.
   *
   * @return background drawable
   */
  protected Drawable getTodayBackground() {
    return todayBackground;
  }

  /**
   * Get the color of checked day.
   *
   * @return color of checked day
   */
  int getCheckedDayBackgroundColor() {
    return checkedDayBackgroundColor;
  }

  /**
   * Auto pick date when month changed or not.
   *
   * @return true or false
   */
  protected boolean getShouldPickOnMonthChange() {
    return shouldPickOnMonthChange;
  }

  /**
   * Set the text color of solar day.
   *
   * @param color color
   */
  public void setSolarTextColor(@ColorInt int color) {
    solarTextColor = color;
  }

  /**
   * Set the text color resource of solar day.
   *
   * @param resId resource id
   */
  public void setSolarTextColorRes(@ColorRes int resId) {
    solarTextColor = getColor(resId);
  }

  /**
   * Set the text color of lunar day.
   *
   * @param color color
   */
  public void setLunarTextColor(@ColorInt int color) {
    lunarTextColor = color;
  }

  /**
   * Set the text color resource of lunar day.
   *
   * @param resId resource id
   */
  public void setLunarTextColorRes(@ColorRes int resId) {
    lunarTextColor = getColor(resId);
  }

  /**
   * Set the highlight color.
   *
   * @param color color
   */
  public void setHighlightColor(@ColorInt int color) {
    hightlistColor = color;
  }

  /**
   * Set the highlight color resource.
   *
   * @param resId resource id
   */
  public void setHighlightColorRes(@ColorRes int resId) {
    hightlistColor = getColor(resId);
  }

  /**
   * Set the text color resource of uncheckable day.
   *
   * @param resId resource id
   */
  public void setUncheckableColorRes(@ColorRes int resId) {
    uncheckableColor = getColor(resId);
  }

  /**
   * Set the text color of uncheckable day.
   *
   * @param color color
   */
  public void setUncheckableColor(@ColorInt int color) {
    uncheckableColor = color;
  }

  /**
   * Set the background drawable of today.
   *
   * @param resId drawable resource id
   */
  public void setTodayBackground(@DrawableRes int resId) {
    todayBackground = getDrawable(resId);
  }

  /**
   * Set on date click listener. This listener will be invoked
   * when a day in month was picked.
   *
   * @param l date pick listner
   */
  public void setOnDatePickListener(OnDatePickListener l) {
    onDatePickListener = l;
  }

  /**
   * Dispatch date pick listener. This will be invoked be {@link MonthView}
   *
   * @param monthDay month day
   */
  protected void dispatchDateClickListener(MonthDay monthDay) {
    if (onDatePickListener != null) {
      onDatePickListener.onDatePick(this, monthDay);
    }
  }

  /* show the month page with specified pager position and selected day */
  private void showMonth(int position, int selectedDay) {
    isChangedByUser = true;
    monthPagerAdapter.setSelectedDay(position, selectedDay);
    viewPager.setCurrentItem(position, true);
  }

  /**
   * Show previous month page with selected day.
   *
   * @param selectedDay selected day
   */
  protected void showPrevMonth(int selectedDay) {
    int position = viewPager.getCurrentItem() - 1;
    showMonth(position, selectedDay);
  }

  /**
   * Show next month page with selected day.
   *
   * @param selectedDay selected day
   */
  protected void showNextMonth(int selectedDay) {
    int position = viewPager.getCurrentItem() + 1;
    showMonth(position, selectedDay);
  }

  /**
   * Show previous month view.
   */
  public void showPrevMonth() {
    showPrevMonth(1);
  }

  /**
   * Show next month view.
   */
  public void showNextMonth() {
    showNextMonth(1);
  }

  /**
   * Go to the month with specified year and month.
   *
   * @param year the specified year
   * @param month the specified month
   */
  public void goToMonth(int year, int month) {
    showMonth(monthPagerAdapter.getIndexOfMonth(year, month), 1);
  }

  /**
   * Go to the month with specified year, month and day.
   *
   * @param year the specified year
   * @param month the specified month
   * @param day the specified day
   */
  public void goToMonthDay(int year, int month, int day) {
    showMonth(monthPagerAdapter.getIndexOfMonth(year, month), day);
  }

  /**
   * Go back to the month of today.
   */
  public void backToToday() {
    Calendar today = Calendar.getInstance();
    today.setTimeInMillis(System.currentTimeMillis());
    showMonth(monthPagerAdapter.getIndexOfCurrentMonth(), today.get(Calendar.DAY_OF_MONTH));
  }

  /**
   * Set the range of date.
   *
   * @param minYear min year
   * @param maxYear max year
   */
  public void setDateRange(int minYear, int maxYear) {
    Month min = new Month(minYear, 0, 1);
    Month max = new Month(maxYear, 11, 1);
    monthPagerAdapter.setDateRange(min, max);
  }
}

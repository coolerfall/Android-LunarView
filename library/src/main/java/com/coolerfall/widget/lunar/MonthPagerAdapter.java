package com.coolerfall.widget.lunar;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.PagerAdapter;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import java.util.Calendar;

/**
 * A pager adapter for month, used in {@link LunarView}.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
final class MonthPagerAdapter extends PagerAdapter {
  private final Context context;
  private final LunarView lunarView;
  private int totalCount;
  private Month mminMonth;
  private Month maxMonth;
  private final SparseIntArray selectedDayCache = new SparseIntArray();
  private final SparseArrayCompat<Month> monthCache = new SparseArrayCompat<>();
  private final SparseArrayCompat<MonthView> viewCache = new SparseArrayCompat<>();

  /**
   * The constructor of month pager adapter.
   *
   * @param context the context to use
   * @param lunarView {@link LunarView}
   */
  protected MonthPagerAdapter(Context context, LunarView lunarView) {
    this.context = context;
    this.lunarView = lunarView;

    mminMonth = new Month(1900, 0, 1);
    maxMonth = new Month(2100, 11, 1);

    calculateRange(mminMonth, maxMonth);
  }

  @Override public int getCount() {
    return totalCount;
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    MonthView monthView = new MonthView(context, getItem(position), lunarView);
    int selectedDay = selectedDayCache.get(position, -1);
    if (selectedDay != -1) {
      monthView.setSelectedDay(selectedDay);
      selectedDayCache.removeAt(selectedDayCache.indexOfKey(position));
    }

    container.addView(monthView);
    viewCache.put(position, monthView);

    return monthView;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    MonthView monthView = (MonthView) object;
    container.removeView(monthView);
    viewCache.remove(position);
  }

  @Override public boolean isViewFromObject(View view, Object object) {
    return view == object;
  }

  /* get month item from cache array */
  private Month getItem(int position) {
    Month monthItem = monthCache.get(position);
    if (monthItem != null) {
      return monthItem;
    }

    int numYear = position / 12;
    int numMonth = position % 12;

    int year = mminMonth.getYear() + numYear;
    int month = mminMonth.getMonth() + numMonth;
    if (month >= 12) {
      year += 1;
      month -= 12;
    }

    monthItem = new Month(year, month, 1);
    monthCache.put(position, monthItem);

    return monthItem;
  }

  /* calculate month range */
  private void calculateRange(Month minDate, Month maxDate) {
    /* calculate total month */
    int minYear = minDate.getYear();
    int minMonth = minDate.getMonth();
    int maxYear = maxDate.getYear();
    int maxMonth = maxDate.getMonth();
    totalCount = (maxYear - minYear) * 12 + maxMonth - minMonth;
  }

  /**
   * Get the index of month for today.
   *
   * @return index of month
   */
  protected int getIndexOfCurrentMonth() {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());

    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);

    return getIndexOfMonth(year, month);
  }

  /**
   * Get the index of given year and month.
   *
   * @param year the specified year
   * @param month the specified month
   * @return the index
   */
  protected int getIndexOfMonth(int year, int month) {
    return (year - mminMonth.getYear()) * 12 + month;
  }

  /**
   * Set selected index for month view.
   *
   * @param pagerPosition position of pager
   * @param selectedDay selected day
   */
  protected void setSelectedDay(int pagerPosition, int selectedDay) {
    MonthView monthView = viewCache.get(pagerPosition);
    if (monthView == null) {
      selectedDayCache.put(pagerPosition, selectedDay);
      return;
    }

    monthView.setSelectedDay(selectedDay);
  }

  /**
   * Reset selected day to the first day of month or today.
   *
   * @param pagerPosition position of pager
   */
  protected void resetSelectedDay(int pagerPosition) {
    setSelectedDay(pagerPosition, 0);
  }

  /**
   * Set date range of lunar view.
   *
   * @param minDate min date month
   * @param maxDate max date month
   */
  protected void setDateRange(Month minDate, Month maxDate) {
    mminMonth = minDate;
    maxMonth = maxDate;
    calculateRange(minDate, maxDate);
    notifyDataSetChanged();
  }
}

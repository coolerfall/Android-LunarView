package com.coolerfall.widget.lunar;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Representation of a month on a calendar.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
class Month implements Parcelable {
  private static final int DAYS_IN_WEEK = 7;

  private final int year;
  private final int month;
  private final int day;
  private int totalWeeks;
  private int delta;
  private int totalDays;
  private boolean isMonthOfToday;
  private List<MonthDay> monthDayList = new ArrayList<>();

  /**
   * Parcelable Stuff.
   */
  private Month(Parcel in) {
    this(in.readInt(), in.readInt(), in.readInt());
  }

  /**
   * The constructor for month.
   *
   * @param year year
   * @param month month
   * @param day day of month
   */
  protected Month(int year, int month, int day) {
    this.year = year;
    this.month = month;
    this.day = day;

    addMonthDay(year, month, day);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(year);
    dest.writeInt(month);
    dest.writeInt(day);
  }

  /**
   * Flatten this object in to a Parcel.
   */
  public static final Creator<Month> CREATOR = new Creator<Month>() {
    public Month createFromParcel(Parcel in) {
      return new Month(in);
    }

    public Month[] newArray(int size) {
      return new Month[size];
    }
  };

  /* generate working calendar */
  private Calendar generateWorkingCalendar(int year, int month, int day) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day);

    isMonthOfToday = isMonthOfToday(calendar);
    totalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    calendar.set(year, month, 1);
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    delta = dayOfWeek - 1;
    calendar.add(Calendar.DATE, -delta);

		/* calculate total weeks to show in current month */
    totalWeeks =
        (totalDays + delta) / DAYS_IN_WEEK + ((totalDays + delta) % DAYS_IN_WEEK != 0 ? 1 : 0);

    return calendar;
  }

  /* add month day into list */
  private void addMonthDay(int year, int month, int day) {
    Calendar calendar = generateWorkingCalendar(year, month, day);

    for (int i = 0; i < totalWeeks; i++) {
      for (int j = 0; j < DAYS_IN_WEEK; j++) {
        MonthDay monthDay = new MonthDay(calendar);
        int currentDays = i * DAYS_IN_WEEK + j;
        monthDay.setCheckable(!(currentDays < delta || currentDays >= totalDays + delta));
        if (currentDays < delta) {
          monthDay.setDayFlag(MonthDay.PREV_MONTH_DAY);
        } else if (currentDays >= totalDays + delta) {
          monthDay.setDayFlag(MonthDay.NEXT_MONTH_DAY);
        }
        monthDayList.add(monthDay);
        calendar.add(Calendar.DATE, 1);
      }
    }
  }

  /* to check if current month is the month of today for given calendar */
  private boolean isMonthOfToday(Calendar calendar) {
    Calendar today = Calendar.getInstance();
    today.setTimeInMillis(System.currentTimeMillis());

    return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
        && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH);
  }

  /**
   * Get total weeks in current month.
   *
   * @return total weeks
   */
  protected int getWeeksInMonth() {
    return totalWeeks;
  }

  /**
   * Get {@link MonthDay} in current month according to index.
   *
   * @param index index in month view
   * @return {@link MonthDay}
   */
  protected MonthDay getMonthDay(int index) {
    return monthDayList.size() <= index ? null : monthDayList.get(index);
  }

  /**
   * Get {@link MonthDay} in current month according to x index and y index in month view.
   *
   * @param xIndex x index in month view
   * @param yIndex y index in month view
   * @return {@link MonthDay}
   */
  protected MonthDay getMonthDay(int xIndex, int yIndex) {
    return getMonthDay(xIndex * DAYS_IN_WEEK + yIndex);
  }

  /**
   * Get the year of current month.
   *
   * @return year
   */
  protected int getYear() {
    return year;
  }

  /**
   * Get current month.
   *
   * @return current month
   */
  protected int getMonth() {
    return month;
  }

  /**
   * To check if current month is the month of today.
   *
   * @return true if was, otherwise return false
   */
  protected boolean isMonthOfToday() {
    return isMonthOfToday;
  }

  /**
   * Get the index of day in current month.
   *
   * @param day the day in current month
   * @return the index of day
   */
  protected int getIndexOfDayInCurMonth(int day) {
    for (int i = 0; i < monthDayList.size(); i++) {
      MonthDay monthDay = monthDayList.get(i);
      if (monthDay.isCheckable() && monthDay.getCalendar().get(Calendar.DAY_OF_MONTH) == day) {
        return i;
      }
    }

    return -1;
  }

  /**
   * Get the index of today if today was in current month.
   *
   * @return the index of today if was in current month, otherwise return -1
   */
  protected int getIndexOfToday() {
    if (!isMonthOfToday) {
      return -1;
    }

    Calendar today = Calendar.getInstance();
    today.setTimeInMillis(System.currentTimeMillis());
    return getIndexOfDayInCurMonth(today.get(Calendar.DAY_OF_MONTH));
  }
}

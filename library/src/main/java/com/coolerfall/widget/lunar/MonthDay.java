package com.coolerfall.widget.lunar;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Calendar;

/**
 * Representation one day in a month.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public final class MonthDay implements Parcelable {
  protected static final int PREV_MONTH_DAY = 1;
  protected static final int NEXT_MONTH_DAY = 2;

  private int day;
  private String lunarDay;
  private boolean isHoliday;
  private boolean isWeekend;
  private boolean isCheckable = true;
  private boolean isToday;
  private int dayFlag;
  private Calendar calendar;
  private Lunar lunar;

  /**
   * The constructor of month day.
   *
   * @param calendar {@link Calendar}
   */
  public MonthDay(Calendar calendar) {
    copy(calendar);

    day = this.calendar.get(Calendar.DAY_OF_MONTH);
    int dayOfWeek = this.calendar.get(Calendar.DAY_OF_WEEK);
    isWeekend = dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY;
    isToday = isToday(this.calendar);

    String lunarDay =
        lunar.getLunarDayNum() == 1 ? lunar.getLunarMonth() + "月" : lunar.getLunarDay();
    String lunarHoliday = lunar.getLunarHoliday();
    String solarHoliday = lunar.getSolarHolidy();
    String holiday = TextUtils.isEmpty(lunarHoliday) ? solarHoliday : lunarHoliday;
    String solarTerm = lunar.getSolarTerm();

		/* if current day is a holiday or solar term, show first */
    this.lunarDay =
        TextUtils.isEmpty(holiday) ? (TextUtils.isEmpty(solarTerm) ? lunarDay : solarTerm)
            : holiday;
    isHoliday = !TextUtils.isEmpty(holiday) || !TextUtils.isEmpty(solarTerm);
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(calendar.getTimeInMillis());
  }

  public static final Creator<MonthDay> CREATOR = new Creator<MonthDay>() {
    public MonthDay createFromParcel(Parcel source) {
      Calendar calendar = Calendar.getInstance();
      calendar.setTimeInMillis(source.readLong());
      return new MonthDay(calendar);
    }

    public MonthDay[] newArray(int size) {
      return new MonthDay[size];
    }
  };

  /* copy calendar to month day */
  private void copy(Calendar calendar) {
    this.calendar = Calendar.getInstance();
    lunar = Lunar.newInstance();

    this.calendar.setTimeInMillis(calendar.getTimeInMillis());
    lunar.setTimeInMillis(calendar.getTimeInMillis());
  }

  /* to check if the given calendar was today */
  private boolean isToday(Calendar calendar) {
    Calendar today = Calendar.getInstance();
    today.setTimeInMillis(System.currentTimeMillis());

    return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)
        && calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH)
        && calendar.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Get the string of solar day of current day.
   *
   * @return solar day string
   */
  protected String getSolarDay() {
    return Integer.toString(day);
  }

  /**
   * Get the string of lunar day of current day.
   *
   * @return lunar day string
   */
  protected String getLunarDay() {
    return lunarDay;
  }

  /**
   * To check if current day was weekend.
   *
   * @return true if was, otherwise return false
   */
  protected boolean isWeekend() {
    return isWeekend;
  }

  /**
   * To check if lunar day is holiday or solar term.
   *
   * @return true if was holiday, otherwise return false
   */
  protected boolean isHoliday() {
    return isHoliday;
  }

  /**
   * To check if current day if the first day in current month.
   *
   * @return true if was first day, otherwise return false
   */
  protected boolean isFirstDay() {
    return calendar.get(Calendar.DAY_OF_MONTH) == 1 && isCheckable;
  }

  /**
   * To set current day checkable or not.
   *
   * @param checkable true or false
   */
  protected void setCheckable(boolean checkable) {
    isCheckable = checkable;
  }

  /**
   * To check if current day was checkable.
   *
   * @return true if checkable, otherwise return false
   */
  protected boolean isCheckable() {
    return isCheckable;
  }

  /**
   * Set the flag of current day.
   *
   * @param flag {@link #PREV_MONTH_DAY}, {@link #NEXT_MONTH_DAY}
   */
  protected void setDayFlag(int flag) {
    dayFlag = flag;
  }

  /**
   * Get the flag of current day.
   *
   * @return the flag {@link #PREV_MONTH_DAY}, {@link #NEXT_MONTH_DAY}
   */
  protected int getDayFlag() {
    return dayFlag;
  }

  /**
   * To check if current day was today.
   *
   * @return true if was today, otherwise return false
   */
  public boolean isToday() {
    return isToday;
  }

  /**
   * Get {@link Calendar} for current day.
   *
   * @return {@link Calendar}
   */
  public Calendar getCalendar() {
    return calendar;
  }

  /**
   * Get {@link Lunar} for current day.
   *
   * @return {@link Lunar}
   */
  public Lunar getLunar() {
    return lunar;
  }
}

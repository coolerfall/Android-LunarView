package com.coolerfall.widget.calendar;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Representation of a month on a calendar.
 *
 * @author Vincent Cheung
 * @since Oct. 12, 2015
 */
public class Month implements Parcelable {
	private static final int DAYS_IN_WEEK = 7;

	private final int mYear;
	private final int mMonth;
	private final int mDay;
	private int mTotalWeeks;
	private int mDelta;
	private int mTotalDays;
	private boolean mIsMonthOfToday;
	private List<MonthDay> mMonthDayList = new ArrayList<>();

	/**
	 * Parcelable Stuff.
	 */
	private Month(Parcel in) {
		this(in.readInt(), in.readInt(), in.readInt());
	}

	/**
	 * The constructor for month.
	 *
	 * @param year  year
	 * @param month month
	 * @param day   day of month
	 */
	protected Month(int year, int month, int day) {
		mYear = year;
		mMonth = month;
		mDay = day;

		addMonthDay(year, month, day);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mYear);
		dest.writeInt(mMonth);
		dest.writeInt(mDay);
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

		mIsMonthOfToday = isMonthOfToday(calendar);
		mTotalDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(year, month, 1);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		mDelta = dayOfWeek - 1;
		calendar.add(Calendar.DATE, -mDelta);

		/* calculate total weeks to show in current month */
		mTotalWeeks = (mTotalDays + mDelta) / DAYS_IN_WEEK +
			((mTotalDays + mDelta) % DAYS_IN_WEEK != 0 ? 1 : 0);

		return calendar;
	}

	/* add month day into list */
	private void addMonthDay(int year, int month, int day) {
		Calendar calendar = generateWorkingCalendar(year, month, day);

		for (int i = 0; i < mTotalWeeks; i++) {
			for (int j = 0; j < DAYS_IN_WEEK; j++) {
				MonthDay monthDay = new MonthDay(calendar);
				int currentDays = i * DAYS_IN_WEEK + j;
				monthDay.setCheckable(!(currentDays < mDelta ||
					currentDays >= mTotalDays + mDelta));
				if (currentDays < mDelta) {
					monthDay.setDayFlag(MonthDay.PREV_MONTH_DAY);
				} else if (currentDays >= mTotalDays + mDelta) {
					monthDay.setDayFlag(MonthDay.NEXT_MONTH_DAY);
				}
				mMonthDayList.add(monthDay);
				calendar.add(Calendar.DATE, 1);
			}
		}
	}

	/* to check if current month is the month of today for given calendar */
	private boolean isMonthOfToday(Calendar calendar) {
		Calendar today = Calendar.getInstance();
		today.setTimeInMillis(System.currentTimeMillis());

		return calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
			calendar.get(Calendar.MONTH) == today.get(Calendar.MONTH);
	}

	/**
	 * Get total weeks in current month.
	 *
	 * @return total weeks
	 */
	protected int getWeeksInMonth() {
		return mTotalWeeks;
	}

	/**
	 * Get {@link MonthDay} in current month according to index.
	 *
	 * @param index index in month view
	 * @return {@link MonthDay}
	 */
	protected MonthDay getMonthDay(int index) {
		return mMonthDayList.size() <= index ? null : mMonthDayList.get(index);
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
		return mYear;
	}

	/**
	 * Get current month.
	 *
	 * @return current month
	 */
	protected int getMonth() {
		return mMonth;
	}

	/**
	 * To check if current month is the month of today.
	 *
	 * @return true if was, otherwise return false
	 */
	protected boolean isMonthOfToday() {
		return mIsMonthOfToday;
	}

	/**
	 * Get the index of day in current month.
	 *
	 * @param day the day in current month
	 * @return the index of day
	 */
	protected int getIndexOfDayInCurMonth(int day) {
		for (int i = 0; i < mMonthDayList.size(); i++) {
			MonthDay monthDay = mMonthDayList.get(i);
			if (monthDay.isCheckable() &&
				monthDay.getCalendar().get(Calendar.DAY_OF_MONTH) == day) {
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
		if (!mIsMonthOfToday) {
			return -1;
		}

		Calendar today = Calendar.getInstance();
		today.setTimeInMillis(System.currentTimeMillis());
		return getIndexOfDayInCurMonth(today.get(Calendar.DAY_OF_MONTH));
	}
}

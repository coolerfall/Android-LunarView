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
	private Context mContext;
	private LunarView mLunarView;
	private int mTotalCount;
	private Month mMinMonth;
	private Month mMaxMonth;
	private SparseIntArray mSelectedDayCache = new SparseIntArray();
	private SparseArrayCompat<Month> mMonthCache = new SparseArrayCompat<>();
	private SparseArrayCompat<MonthView> mViewCache = new SparseArrayCompat<>();

	/**
	 * The constructor of month pager adapter.
	 *
	 * @param context   the context to use
	 * @param lunarView {@link LunarView}
	 */
	protected MonthPagerAdapter(Context context, LunarView lunarView) {
		mContext = context;
		mLunarView = lunarView;

		mMinMonth = new Month(1900, 0, 1);
		mMaxMonth = new Month(2100, 11, 1);

		calculateRange(mMinMonth, mMaxMonth);
	}

	@Override
	public int getCount() {
		return mTotalCount;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		MonthView monthView = new MonthView(mContext, getItem(position), mLunarView);
		int selectedDay = mSelectedDayCache.get(position, -1);
		if (selectedDay != -1) {
			monthView.setSelectedDay(selectedDay);
			mSelectedDayCache.removeAt(mSelectedDayCache.indexOfKey(position));
		}

		container.addView(monthView);
		mViewCache.put(position, monthView);

		return monthView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		MonthView monthView = (MonthView) object;
		container.removeView(monthView);
		mViewCache.remove(position);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	/* get month item from cache array */
	private Month getItem(int position) {
		Month monthItem = mMonthCache.get(position);
		if (monthItem != null) {
			return monthItem;
		}

		int numYear = position / 12;
		int numMonth = position % 12;

		int year = mMinMonth.getYear() + numYear;
		int month = mMinMonth.getMonth() + numMonth;
		if (month >= 12) {
			year += 1;
			month -= 12;
		}

		monthItem = new Month(year, month, 1);
		mMonthCache.put(position, monthItem);

		return monthItem;
	}

	/* calculate month range */
	private void calculateRange(Month minDate, Month maxDate) {
		/* calculate total month */
		int minYear = minDate.getYear();
		int minMonth = minDate.getMonth();
		int maxYear = maxDate.getYear();
		int maxMonth = maxDate.getMonth();
		mTotalCount = (maxYear - minYear) * 12 + maxMonth - minMonth;
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
	 * @param year  the specified year
	 * @param month the specified month
	 * @return the index
	 */
	protected int getIndexOfMonth(int year, int month) {
		return (year - mMinMonth.getYear()) * 12 + month;
	}

	/**
	 * Set selected index for month view.
	 *
	 * @param pagerPosition position of pager
	 * @param selectedDay   selected day
	 */
	protected void setSelectedDay(int pagerPosition, int selectedDay) {
		MonthView monthView = mViewCache.get(pagerPosition);
		if (monthView == null) {
			mSelectedDayCache.put(pagerPosition, selectedDay);
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
		mMinMonth = minDate;
		mMaxMonth = maxDate;
		calculateRange(minDate, maxDate);
		notifyDataSetChanged();
	}
}

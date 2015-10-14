package com.coolerfall.widget.calendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Calendar;

/**
 * This class is a calendar widget for displaying and selecting dates.
 *
 * @author Vincent Cheung
 * @since Oct. 12, 2015
 */
public class LunarView extends LinearLayout {
	private int mSolarTextColor = 0xff454545;
	private int mLunarTextColor = Color.GRAY;
	private int mHightlistColor = 0xff03a9f4;
	private int mUncheckableColor = 0xffb0b0b0;

	private ViewPager mPager;
	private MonthPagerAdapter mAdapter;
	private WeekLabelView mWeekLabelView;
	private OnDatePickListener mOnDatePickListener;
	private boolean mIsChangedByUser;

	public LunarView(Context context) {
		this(context, null);
	}

	public LunarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LunarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		int measureHeight = (int) (measureWidth * 6f / 7f) + mWeekLabelView.getMeasuredHeight();
		setMeasuredDimension(measureWidth, measureHeight);

		for (int i = 0; i < getChildCount(); i++) {
			final View child = getChildAt(i);

			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
				measureWidth, MeasureSpec.EXACTLY);
			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
				measureHeight, MeasureSpec.EXACTLY);
			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
		}
	}

	/* init lunar view */
	private void init() {
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

		mWeekLabelView = new WeekLabelView(getContext());
		addView(mWeekLabelView);

		mPager = new ViewPager(getContext());
		mPager.setOffscreenPageLimit(1);
		addView(mPager);

		mAdapter = new MonthPagerAdapter(getContext(), this);
		mPager.setAdapter(mAdapter);
		mPager.addOnPageChangeListener(mPageListener);
		mPager.setCurrentItem(mAdapter.getIndexOfCurrentMonth());
		mPager.setPageTransformer(false, new ViewPager.PageTransformer() {
			@Override
			public void transformPage(View page, float position) {
				page.setAlpha(1 - Math.abs(position));
			}
		});
	}

	private ViewPager.OnPageChangeListener mPageListener = new ViewPager.OnPageChangeListener() {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

		}

		@Override
		public void onPageSelected(int position) {
			if (mIsChangedByUser) {
				mIsChangedByUser = false;
				return;
			}

			mAdapter.setSelectedDay(position, 1);
		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	};

	@SuppressWarnings("deprecation")
	private int getColor(@ColorRes int resId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return getResources().getColor(resId, null);
		} else {
			return getResources().getColor(resId);
		}
	}

	/**
	 * Interface definition for a callback to be invoked when date picked.
	 */
	public interface OnDatePickListener {
		/**
		 * Invoked when date picked.
		 *
		 * @param view {@link LunarView}
		 * @param day  {@link MonthDay}
		 */
		void onDatePick(LunarView view, MonthDay day);
	}

	/**
	 * Get the text color of solar day.
	 *
	 * @return text color of solar day
	 */
	protected int getSolarTextColor() {
		return mSolarTextColor;
	}

	/**
	 * Get the text color of lunar day.
	 *
	 * @return text color of lunar day
	 */
	protected int getLunarTextColor() {
		return mLunarTextColor;
	}

	/**
	 * Get the highlight color.
	 *
	 * @return thighlight color
	 */
	protected int getHightlightColor() {
		return mHightlistColor;
	}

	/**
	 * Get the color of uncheckable day.
	 *
	 * @return uncheckable color
	 */
	protected int getUnCheckableColor() {
		return mUncheckableColor;
	}

	/**
	 * Set the text color of solar day.
	 *
	 * @param color color
	 */
	public void setSolarTextColor(@ColorInt int color) {
		mSolarTextColor = color;
	}

	/**
	 * Set the text color resource of solar day.
	 *
	 * @param resId resource id
	 */
	public void setSolarTextColorRes(@ColorRes int resId) {
		mSolarTextColor = getColor(resId);
	}

	/**
	 * Set the text color of lunar day.
	 *
	 * @param color color
	 */
	public void setLunarTextColor(@ColorInt int color) {
		mLunarTextColor = color;
	}

	/**
	 * Set the text color resource of lunar day.
	 *
	 * @param resId resource id
	 */
	public void setLunarTextColorRes(@ColorRes int resId) {
		mLunarTextColor = getColor(resId);
	}

	/**
	 * Set the highlight color.
	 *
	 * @param color color
	 */
	public void setHighlightColor(@ColorInt int color) {
		mHightlistColor = color;
	}

	/**
	 * Set the highlight color resource.
	 *
	 * @param resId resource id
	 */
	public void setHighlightColorRes(@ColorRes int resId) {
		mHightlistColor = getColor(resId);
	}

	public void setTodayBackground(@DrawableRes int resId) {

	}

	/**
	 * Set on date click listener. This listener will be invoked
	 * when a day in month was picked.
	 *
	 * @param l date pick listner
	 */
	public void setOnDateClickListener(OnDatePickListener l) {
		mOnDatePickListener = l;
	}

	/**
	 * Dispatch date pick listener. This will be invoked be {@link MonthView}
	 *
	 * @param monthDay month day
	 */
	protected void dispatchDateClickListener(MonthDay monthDay) {
		if (mOnDatePickListener != null) {
			mOnDatePickListener.onDatePick(this, monthDay);
		}
	}

	/* show the month page with specified pager position and selected day */
	private void showMonth(int position, int selectedDay) {
		mIsChangedByUser = true;
		mAdapter.setSelectedDay(position, selectedDay);
		mPager.setCurrentItem(position, true);
	}

	/**
	 * Show previous month page with selected day.
	 *
	 * @param selectedDay selected day
	 */
	protected void showPrevMonth(int selectedDay) {
		int position = mPager.getCurrentItem() - 1;
		showMonth(position, selectedDay);
	}

	/**
	 * Show next month page with selected day.
	 *
	 * @param selectedDay selected day
	 */
	protected void showNextMonth(int selectedDay) {
		int position = mPager.getCurrentItem() + 1;
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
	 * @param year  the specified year
	 * @param month the specified month
	 */
	public void goToMonth(int year, int month) {
		showMonth(mAdapter.getIndexOfMonth(year, month), 1);
	}

	/**
	 * Go to the month with specified year, month and day.
	 *
	 * @param year  the specified year
	 * @param month the specified month
	 * @param day   the specified day
	 */
	public void goToMonthDay(int year, int month, int day) {
		showMonth(mAdapter.getIndexOfMonth(year, month), day);
	}

	/**
	 * Go back to the month of today.
	 */
	public void backToToday() {
		Calendar today = Calendar.getInstance();
		today.setTimeInMillis(System.currentTimeMillis());
		showMonth(mAdapter.getIndexOfCurrentMonth(), today.get(Calendar.DAY_OF_MONTH));
	}
}

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
 * @author Vincent Cheung
 * @since Oct. 12, 2015
 */
public class LunarView extends LinearLayout {
	private int mSolarTextColor = 0xff454545;
	private int mLunarTextColor = Color.GRAY;
	private int mHightlistColor = 0xff03a9f4;
	private int mUncheckableColor = 0xffb0b0b0;
	private int mMonthBackgroundColor = 0xfffafafa;
	private int mWeekLabelBackgroundColor = 0xfffafafa;
	private Drawable mTodayBackground;
	private boolean mShouldPickOnMonthChange = true;

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
		init(attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);

		for (int i = 0; i < getChildCount(); i++) {
			final View child = getChildAt(i);

			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY);
			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY);
			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
		}

		int measureHeight = (int) (measureWidth * 6f / 7f) + mWeekLabelView.getMeasuredHeight();
		setMeasuredDimension(measureWidth, measureHeight);
	}

	/* init lunar view */
	private void init(AttributeSet attrs) {
	/* get custom attrs */
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LunarView);
		mMonthBackgroundColor =
			a.getColor(R.styleable.LunarView_monthBackgroundColor, mMonthBackgroundColor);
		mWeekLabelBackgroundColor =
			a.getColor(R.styleable.LunarView_monthBackgroundColor, mWeekLabelBackgroundColor);
		mSolarTextColor = a.getColor(R.styleable.LunarView_solarTextColor, mSolarTextColor);
		mLunarTextColor = a.getColor(R.styleable.LunarView_lunarTextColor, mLunarTextColor);
		mHightlistColor = a.getColor(R.styleable.LunarView_highlightColor, mHightlistColor);
		mUncheckableColor = a.getColor(R.styleable.LunarView_uncheckableColor, mUncheckableColor);
		mTodayBackground = a.getDrawable(R.styleable.LunarView_todayBackground);
		mShouldPickOnMonthChange =
			a.getBoolean(R.styleable.LunarView_shouldPickOnMonthChange, mShouldPickOnMonthChange);
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

		mWeekLabelView = new WeekLabelView(getContext());
		mWeekLabelView.setBackgroundColor(mWeekLabelBackgroundColor);
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

	/* page change listener */
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

			mAdapter.resetSelectedDay(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {

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
		 * @param view     {@link LunarView}
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
		return mMonthBackgroundColor;
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
	 * Get the background of today.
	 *
	 * @return background drawable
	 */
	protected Drawable getTodayBackground() {
		return mTodayBackground;
	}

	/**
	 * Auto pick date when month changed or not.
	 *
	 * @return true or false
	 */
	protected boolean getShouldPickOnMonthChange() {
		return mShouldPickOnMonthChange;
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

	/**
	 * Set the text color resource of uncheckable day.
	 *
	 * @param resId resource id
	 */
	public void setUncheckableColorRes(@ColorRes int resId) {
		mUncheckableColor = getColor(resId);
	}

	/**
	 * Set the text color of uncheckable day.
	 *
	 * @param color color
	 */
	public void setUncheckableColor(@ColorInt int color) {
		mUncheckableColor = color;
	}

	/**
	 * Set the background drawable of today.
	 *
	 * @param resId drawable resource id
	 */
	public void setTodayBackground(@DrawableRes int resId) {
		mTodayBackground = getDrawable(resId);
	}

	/**
	 * Set on date click listener. This listener will be invoked
	 * when a day in month was picked.
	 *
	 * @param l date pick listner
	 */
	public void setOnDatePickListener(OnDatePickListener l) {
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

	/**
	 * Set the range of date.
	 *
	 * @param minYear min year
	 * @param maxYear max year
	 */
	public void setDateRange(int minYear, int maxYear) {
		Month min = new Month(minYear, 0, 1);
		Month max = new Month(maxYear, 11, 1);
		mAdapter.setDateRange(min, max);
	}
}

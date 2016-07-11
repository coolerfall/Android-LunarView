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
@SuppressLint("ViewConstructor")
final class MonthView extends View {
	private static final int DAYS_IN_WEEK = 7;

	private int mSelectedIndex = -1;

	private float mSolarTextSize;
	private float mLunarTextSize;
	private float mLunarOffset;
	private float mCircleRadius;

	private Month mMonth;
	private LunarView mLunarView;

	private final Region[][] mMonthWithFourWeeks = new Region[4][DAYS_IN_WEEK];
	private final Region[][] mMonthWithFiveWeeks = new Region[5][DAYS_IN_WEEK];
	private final Region[][] mMonthWithSixWeeks = new Region[6][DAYS_IN_WEEK];
	private Paint mPaint;

	/**
	 * The constructor of month view.
	 *
	 * @param context context to use
	 * @param lunarView {@link LunarView}
	 */
	public MonthView(Context context, Month month, LunarView lunarView) {
		super(context);

		mMonth = month;
		mLunarView = lunarView;
		init();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		int dayWidth = (int) (w / 7f);
		int dayHeightInFourWeek = (int) (h / 4f);
		int dayHeightInFiveWeek = (int) (h / 5f);
		int dayHeightInSixWeek = (int) (h / 6f);

		mCircleRadius = dayWidth / 2.2f;

		mSolarTextSize = h / 15f;
		mPaint.setTextSize(mSolarTextSize);
		float solarHeight = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;

		mLunarTextSize = mSolarTextSize / 2;
		mPaint.setTextSize(mLunarTextSize);
		float lunarHeight = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top;

		mLunarOffset = (Math.abs(mPaint.ascent() + mPaint.descent()) +
			solarHeight + lunarHeight) / 3f;

		initMonthRegion(mMonthWithFourWeeks, dayWidth, dayHeightInFourWeek);
		initMonthRegion(mMonthWithFiveWeeks, dayWidth, dayHeightInFiveWeek);
		initMonthRegion(mMonthWithSixWeeks, dayWidth, dayHeightInSixWeek);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(measureWidth, (int) (measureWidth * 6f / 7f));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mMonth == null) {
			return;
		}

		canvas.save();
		int weeks = mMonth.getWeeksInMonth();
		Region[][] monthRegion = getMonthRegion();

		for (int i = 0; i < weeks; i++) {
			for (int j = 0; j < DAYS_IN_WEEK; j++) {
				draw(canvas, monthRegion[i][j].getBounds(), i, j);
			}
		}
		canvas.restore();
	}

	@Override
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
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
		mPaint.setTextAlign(Paint.Align.CENTER);

		if (mMonth.isMonthOfToday()) {
			mSelectedIndex = mMonth.getIndexOfToday();
		}

		setBackgroundColor(mLunarView.getMonthBackgroundColor());
	}

	/* init month region with the width and height of day */
	private void initMonthRegion(Region[][] monthRegion, int dayWidth, int dayHeight) {
		for (int i = 0; i < monthRegion.length; i++) {
			for (int j = 0; j < monthRegion[i].length; j++) {
				Region region = new Region();
				region.set(j * dayWidth, i * dayHeight, dayWidth +
					j * dayWidth, dayWidth + i * dayHeight);
				monthRegion[i][j] = region;
			}
		}
	}

	/* get month region for current month */
	private Region[][] getMonthRegion() {
		int weeks = mMonth.getWeeksInMonth();
		Region[][] monthRegion;
		if (weeks == 4) {
			monthRegion = mMonthWithFourWeeks;
		} else if (weeks == 5) {
			monthRegion = mMonthWithFiveWeeks;
		} else {
			monthRegion = mMonthWithSixWeeks;
		}

		return monthRegion;
	}

	/* draw all the text in month view */
	private void draw(Canvas canvas, Rect rect, int xIndex, int yIndex) {
		MonthDay monthDay = mMonth.getMonthDay(xIndex, yIndex);

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
			mPaint.setColor(mLunarView.getUnCheckableColor());
		} else if (monthDay.isWeekend()) {
			mPaint.setColor(mLunarView.getHightlightColor());
		} else {
			mPaint.setColor(mLunarView.getSolarTextColor());
		}

		mPaint.setTextSize(mSolarTextSize);
		canvas.drawText(monthDay.getSolarDay(), rect.centerX(), rect.centerY(), mPaint);
	}

	/* draw lunar text in month view */
	private void drawLunarText(Canvas canvas, Rect rect, MonthDay monthDay) {
		if (monthDay == null) {
			return;
		}

		if (!monthDay.isCheckable()) {
			mPaint.setColor(mLunarView.getUnCheckableColor());
		} else if (monthDay.isHoliday()) {
			mPaint.setColor(mLunarView.getHightlightColor());
		} else {
			mPaint.setColor(mLunarView.getLunarTextColor());
		}

		mPaint.setTextSize(mLunarTextSize);
		canvas.drawText(monthDay.getLunarDay(), rect.centerX(), rect.centerY() + mLunarOffset,
			mPaint);
	}

	/* draw circle for selected day */
	private void drawBackground(Canvas canvas, Rect rect, MonthDay day, int xIndex, int yIndex) {
		if (day.isToday()) {
			Drawable background = mLunarView.getTodayBackground();
			if (background == null) {
				drawRing(canvas, rect);
			} else {
				background.setBounds(rect);
				background.draw(canvas);
			}

			return;
		}

		/* not today was selected */
		if (mSelectedIndex == -1 && day.isFirstDay()) {
			mSelectedIndex = xIndex * DAYS_IN_WEEK + yIndex;
		}

		if (mSelectedIndex / DAYS_IN_WEEK != xIndex ||
			mSelectedIndex % DAYS_IN_WEEK != yIndex) {
			return;
		}

		mPaint.setColor(mLunarView.getCheckedDayBackgroundColor());
		canvas.drawCircle(rect.centerX(), rect.centerY(), mCircleRadius, mPaint);
	}

	/* draw ring as background of today */
	private void drawRing(Canvas canvas, Rect rect) {
		mPaint.setColor(Color.RED);
		canvas.drawCircle(rect.centerX(), rect.centerY(), mCircleRadius, mPaint);
		mPaint.setColor(mLunarView.getMonthBackgroundColor());
		canvas.drawCircle(rect.centerX(), rect.centerY(), mCircleRadius - 4, mPaint);
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

				MonthDay monthDay = mMonth.getMonthDay(i, j);
				if (monthDay == null) {
					return;
				}

				int day = monthDay.getCalendar().get(Calendar.DAY_OF_MONTH);

				if (monthDay.isCheckable()) {
					mSelectedIndex = i * DAYS_IN_WEEK + j;
					performDayClick();
					invalidate();
				} else {
					if (monthDay.getDayFlag() == MonthDay.PREV_MONTH_DAY) {
						mLunarView.showPrevMonth(day);
					} else if (monthDay.getDayFlag() == MonthDay.NEXT_MONTH_DAY) {
						mLunarView.showNextMonth(day);
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
		MonthDay monthDay = mMonth.getMonthDay(mSelectedIndex);
		mLunarView.dispatchDateClickListener(monthDay);
	}

	/**
	 * Set selected day, the selected day will draw background.
	 *
	 * @param day selected day
	 */
	protected void setSelectedDay(int day) {
		if (mMonth.isMonthOfToday() && day == 0) {
			mSelectedIndex = mMonth.getIndexOfToday();
		} else {
			int selectedDay = day == 0 ? 1 : day;
			mSelectedIndex = mMonth.getIndexOfDayInCurMonth(selectedDay);
		}

		invalidate();

		if ((day == 0 && mLunarView.getShouldPickOnMonthChange()) || day != 0) {
			performDayClick();
		}
	}
}

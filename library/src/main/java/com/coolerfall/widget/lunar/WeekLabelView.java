package com.coolerfall.widget.lunar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.view.View;

/**
 * Display the week label in the top of {@link MonthView}.
 *
 * @author Vincent Cheung
 * @since Oct. 13, 2015
 */
public class WeekLabelView extends View {
	private static final int DAYS_IN_WEEK = 7;
	private final Region[] mWeekRegion = new Region[DAYS_IN_WEEK];
	private static final String[] CHINESE_WEEK = {"日", "一", "二", "三", "四", "五", "六"};

	private Paint mPaint;

	/**
	 * The constructor of week view.
	 *
	 * @param context the context to use
	 */
	public WeekLabelView(Context context) {
		super(context);
		init();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		int itemWidth = (int) (w / 7f);
		for (int i = 0; i < DAYS_IN_WEEK; i++) {
			mWeekRegion[i].set(i * itemWidth, 0, (i + 1) * itemWidth, h);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		Paint.FontMetrics fm = mPaint.getFontMetrics();
		int heightSize = (int) Math.ceil(fm.descent - fm.ascent) +
			getPaddingTop() + getPaddingBottom();

		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint.FontMetrics fm = mPaint.getFontMetrics();
		for (int i = 0; i < DAYS_IN_WEEK; i++) {
			Rect rect = mWeekRegion[i].getBounds();
			float centerY = rect.height() / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
			canvas.drawText(CHINESE_WEEK[i], rect.centerX(), centerY, mPaint);
		}
	}

	/* init week view */
	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
		mPaint.setTextAlign(Paint.Align.CENTER);
		mPaint.setTextSize(40);
		mPaint.setColor(Color.GRAY);
		setPadding(0, 10, 0, 10);

		for (int i = 0; i < DAYS_IN_WEEK; i++) {
			Region region = new Region();
			mWeekRegion[i] = region;
		}
	}
}

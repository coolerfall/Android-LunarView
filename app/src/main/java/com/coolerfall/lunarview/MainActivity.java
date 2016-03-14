package com.coolerfall.lunarview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.coolerfall.widget.lunar.LunarView;
import com.coolerfall.widget.lunar.MonthDay;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements LunarView.OnDatePickListener {
	@Bind(R.id.toolbar) Toolbar mToolBar;
	@Bind(R.id.main_lunar_view) LunarView mLunarView;
	@Bind(R.id.main_tv_date) TextView mTvDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		setSupportActionBar(mToolBar);
		mLunarView.setOnDatePickListener(this);
	}

	@OnClick(R.id.main_btn_today) void onBtnTodayClick() {
		mLunarView.backToToday();
	}

	@Override public void onDatePick(LunarView view, MonthDay monthDay) {
		int year = monthDay.getCalendar().get(Calendar.YEAR);
		int month = monthDay.getCalendar().get(Calendar.MONTH);
		int day = monthDay.getCalendar().get(Calendar.DAY_OF_MONTH);
		String lunarMonth = monthDay.getLunar().getLunarMonth();
		String lunarDay = monthDay.getLunar().getLunarDay();

		mTvDate.setText(String.format("%d-%d-%d  %s月%s",
			year, month + 1, day, lunarMonth, lunarDay));
	}
}

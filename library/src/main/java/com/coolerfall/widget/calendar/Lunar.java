package com.coolerfall.widget.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Lunar calendar in 200 years from 1900.
 *
 * @author Vincent Cheung
 * @since Aug. 25, 2015
 */
public class Lunar {
	/**
	 * Lunar information in 200 years from 1900.
	 * <p>
	 * | 0 - 11(bit) | 12 - 15(bit) |
	 * month      leap month
	 * If last 4bit is 1111 or 0000 means no leap month.
	 * If the last 4bit in next data is 1111, the days of leap month is 30 days,
	 * otherwise, the days of leap month is 29days.
	 */
	private static final int[] LUNR_INFO = {
		0x4bd8, 0x4ae0, 0xa570, 0x54d5, 0xd260, 0xd950, 0x5554, 0x56af,
		0x9ad0, 0x55d2, 0x4ae0, 0xa5b6, 0xa4d0, 0xd250, 0xd295, 0xb54f,
		0xd6a0, 0xada2, 0x95b0, 0x4977, 0x497f, 0xa4b0, 0xb4b5, 0x6a50,
		0x6d40, 0xab54, 0x2b6f, 0x9570, 0x52f2, 0x4970, 0x6566, 0xd4a0,
		0xea50, 0x6a95, 0x5adf, 0x2b60, 0x86e3, 0x92ef, 0xc8d7, 0xc95f,
		0xd4a0, 0xd8a6, 0xb55f, 0x56a0, 0xa5b4, 0x25df, 0x92d0, 0xd2b2,
		0xa950, 0xb557, 0x6ca0, 0xb550, 0x5355, 0x4daf, 0xa5b0, 0x4573,
		0x52bf, 0xa9a8, 0xe950, 0x6aa0, 0xaea6, 0xab50, 0x4b60, 0xaae4,
		0xa570, 0x5260, 0xf263, 0xd950, 0x5b57, 0x56a0, 0x96d0, 0x4dd5,
		0x4ad0, 0xa4d0, 0xd4d4, 0xd250, 0xd558, 0xb540, 0xb6a0, 0x95a6,
		0x95bf, 0x49b0, 0xa974, 0xa4b0, 0xb27a, 0x6a50, 0x6d40, 0xaf46,
		0xab60, 0x9570, 0x4af5, 0x4970, 0x64b0, 0x74a3, 0xea50, 0x6b58,
		0x5ac0, 0xab60, 0x96d5, 0x92e0, 0xc960, 0xd954, 0xd4a0, 0xda50,
		0x7552, 0x56a0, 0xabb7, 0x25d0, 0x92d0, 0xcab5, 0xa950, 0xb4a0,
		0xbaa4, 0xad50, 0x55d9, 0x4ba0, 0xa5b0, 0x5176, 0x52bf, 0xa930,
		0x7954, 0x6aa0, 0xad50, 0x5b52, 0x4b60, 0xa6e6, 0xa4e0, 0xd260,
		0xea65, 0xd530, 0x5aa0, 0x76a3, 0x96d0, 0x4afb, 0x4ad0, 0xa4d0,
		0xd0b6, 0xd25f, 0xd520, 0xdd45, 0xb5a0, 0x56d0, 0x55b2, 0x49b0,
		0xa577, 0xa4b0, 0xaa50, 0xb255, 0x6d2f, 0xada0, 0x4b63, 0x937f,
		0x49f8, 0x4970, 0x64b0, 0x68a6, 0xea5f, 0x6b20, 0xa6c4, 0xaaef,
		0x92e0, 0xd2e3, 0xc960, 0xd557, 0xd4a0, 0xda50, 0x5d55, 0x56a0,
		0xa6d0, 0x55d4, 0x52d0, 0xa9b8, 0xa950, 0xb4a0, 0xb6a6, 0xad50,
		0x55a0, 0xaba4, 0xa5b0, 0x52b0, 0xb273, 0x6930, 0x7337, 0x6aa0,
		0xad50, 0x4b55, 0x4b6f, 0xa570, 0x54e4, 0xd260, 0xe968, 0xd520,
		0xdaa0, 0x6aa6, 0x56df, 0x4ae0, 0xa9d4, 0xa4d0, 0xd150, 0xf252, 0xd520
	};

	/* solar terms information */
	private static final int[] SOLAR_TERM_INFO = {
		0, 21208, 42467, 63836, 85337, 107014, 128867, 150921,
		173149, 195551, 218072, 240693, 263343, 285989, 308563, 331033,
		353350, 375494, 397447, 419210, 440795, 462224, 483532, 504758
	};

	/* Heavenly Stems */
	private static final String[] HEAVENLY_STEMS = {
		"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"
	};

	/* Earthly Branches */
	private static final String[] EARTHLY_BRANCHES = {
		"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"
	};

	/* Chinese zodiac */
	private static final String[] ZODIAC = {
		"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"
	};

	/* solar terms */
	private static final String[] SOLAR_TERM = {
		"小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨",
		"立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑",
		"白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"
	};

	/* lunar string used in month and day */
	private static final String[] LUNAR_STRING = {
		"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"
	};

	/* special lunar string */
	private static final String[] LUNAR_SPEC_STRING = {
		"初", "十", "廿", "卅", "正", "冬", "腊", "闰"
	};

	/* day of week in Chinese */
	private static final String[] DAY_OF_WEEK_IN_CHINESE = {
		"日", "一", "二", "三", "四", "五", "六",
	};

	/* all the lunar holidays */
	private final Holiday[] mLunarHolidays = new Holiday[] {
		new Holiday(1, 1, "春节"),
		new Holiday(1, 15, "元宵节"),
		new Holiday(5, 5, "端午节"),
		new Holiday(7, 7, "七夕节"),
		new Holiday(7, 15, "中元节"),
		new Holiday(8, 15, "中秋节"),
		new Holiday(9, 9, "重阳节"),
		new Holiday(12, 8, "腊八节"),
		new Holiday(12, 23, "北方小年"),
		new Holiday(12, 24, "南方小年")
	};

	/* all the solar days */
	private final Holiday[] mSolarHolidays = new Holiday[] {
		new Holiday(1, 1, "元旦节"),
		new Holiday(2, 14, "情人节"),
		new Holiday(3, 8, "妇女节"),
		new Holiday(3, 12, "植树节"),
		new Holiday(3, 15, "消费者权益日"),
		new Holiday(3, 21, "世界森林日"),
		new Holiday(4, 1, "愚人节"),
		new Holiday(4, 7, "世界卫生日"),
		new Holiday(4, 22, "世界地球日"),
		new Holiday(5, 1, "劳动节"),
		new Holiday(5, 4, "青年节"),
		new Holiday(5, 31, "世界无烟日"),
		new Holiday(6, 1, "儿童节"),
		new Holiday(6, 26, "禁毒日"),
		new Holiday(7, 1, "建党节"),
		new Holiday(8, 1, "建军节"),
		new Holiday(8, 15, "抗战胜利"),
		new Holiday(9, 10, "教师节"),
		new Holiday(9, 28, "孔子诞辰"),
		new Holiday(10, 1, "国庆节"),
		new Holiday(12, 20, "澳门回归"),
		new Holiday(12, 24, "平安夜"),
		new Holiday(12, 25, "圣诞节"),
	};

	/* Pengzu hundred dread Heavenly Stems */
	private static final String[] PENG_ZU_HEAVENLY = {
		"甲不开仓\n财物耗亡", "乙不栽植\n千株不长", "丙不修灶\n必见灾殃", "丁不剃头\n头主生疮",
		"戊不受田\n田主不祥", "己不破券\n二比并亡", "庚不经络\n织机虚张", "辛不合酱\n主人不尝",
		"壬不决水\n更难提防", "癸不词讼\n理弱敌强",
	};

	/* Pengzu hundred dread Earthly Branches */
	private static final String[] PENG_ZU_EARTHLY = {
		"子不问卜\n自惹祸殃", "丑不冠带\n主不还乡", "寅不祭祀\n神鬼不尝", "卯不穿井\n水泉不香",
		"辰不哭泣\n必主重丧", "巳不远行\n财物伏藏", "午不苫盖\n屋主更张", "未不服药\n毒气入肠",
		"申不安床\n鬼祟入房", "酉不宴客\n醉坐颠狂", "戌不吃犬\n作怪上床", "亥不嫁娶\n不利新郎"
	};

	/* direction of evil spirit */
	private static final String[] EVIL_SPIRIT = {
		"南", "东", "北", "西"
	};

	/* fetus god */
	private static final String[] FETUS_GOD_DIRECTION = {
		"外东北", "外正东", "外东南", "外正南", "外西南",
		"外正西", "外西北", "外正北", "房内北", "房内南", "房内东"
	};

	/* fetus god position in Heavenlu Stems */
	private static final String[] FETUS_GOD_HEAVENLY = {
		"门", "碓磨", "厨灶", "仓库", "房床"
	};

	/* fetus god position in Earthly Braches */
	private static final String[] FETUS_GOD_EARTHLY = {
		"碓", "厕", "炉灶", "大门", "栖", "床"
	};

	/* twelve duty */
	private static final String[] TWELVE_DURY = {
		"开", "闭", "建", "除", "满", "平", "定", "执", "破", "危", "成", "收"
	};

	/* five elements */
	private static final String[] FIVE_ELEMENTS = {
		"海中金", "炉中火", "大林木", "路旁土", "剑锋金", "山头火", "涧下水", "城头土", "白蜡金", "杨柳木",
		"泉中水", "屋上土", "霹雳火", "松柏木", "长流水", "砂石金", "山下火", "平地木", "壁上土", "金箔金",
		"灯头火", "天河水", "大驿土", "钗钏金", "桑柘木", "大溪水", "沙中土", "天上火", "石榴木", "大海水"
	};

	/* twenty eight stars with direction and fortune */
	private final Star[][] mTwentyEightStars = new Star[][] {
		new Star[] {
			new Star("房日兔", "吉", "东方"),
			new Star("心月狐", "凶", "东方"),
			new Star("尾火虎", "吉", "东方"),
			new Star("箕水豹", "吉", "东方"),
			new Star("角木蛟", "吉", "东方"),
			new Star("亢金龙", "凶", "东方"),
			new Star("氐土貉", "凶", "东方"),
		},

		new Star[] {
			new Star("虚日鼠", "凶", "北方"),
			new Star("危月燕", "凶", "北方"),
			new Star("室火猪", "吉", "北方"),
			new Star("壁水貐", "吉", "北方"),
			new Star("斗木獬", "吉", "北方"),
			new Star("牛金牛", "凶", "北方"),
			new Star("女士蝠", "凶", "北方"),
		},

		new Star[] {
			new Star("昴日鸡", "凶", "西方"),
			new Star("毕月乌", "吉", "西方"),
			new Star("觜火猴", "凶", "西方"),
			new Star("参水猿", "凶", "西方"),
			new Star("奎水狼", "凶", "西方"),
			new Star("娄金狗", "吉", "西方"),
			new Star("胃土雉", "吉", "西方"),
		},

		new Star[] {
			new Star("星日马", "凶", "南方"),
			new Star("张月鹿", "吉", "南方"),
			new Star("翼火蛇", "凶", "南方"),
			new Star("轸水蚓", "吉", "南方"),
			new Star("井木犴", "吉", "南方"),
			new Star("鬼金羊", "凶", "南方"),
			new Star("柳土獐", "凶", "南方"),
		},
	};

	private Calendar mSolar;
	private GregorianCalendar mUTCCalendar;
	private int mLunarYear;
	private int mLunarMonth;
	private int mLunarDay;
	private int mDaysInLuarMonth;
	private boolean mIsLeap;
	private int mSolarYear;
	private int mSolarMonth;
	private int mSolarDay;
	private int mCyclicalYear = 0;
	private int mCyclicalMonth = 0;
	private int mCyclicalDay = 0;

	/**
	 * Create a new instance of Lunar Calendar.
	 *
	 * @return new instance
	 */
	public static synchronized Lunar newInstance() {
		return new Lunar();
	}

	/**
	 * The default constructor of lunar, create an empty Lunar, don't
	 * forget to use {@link #setDate} or {@link #setTimeInMillis}.
	 */
	public Lunar() {

	}

	/**
	 * The constructor of Lunar calendar.
	 *
	 * @param year  the year
	 * @param month the month
	 * @param day   the day
	 */
	public Lunar(int year, int month, int day) {
		setDate(year, month, day);
	}

	/**
	 * The constructor of Lunar calendar.
	 *
	 * @param millisec millisecond
	 */
	public Lunar(long millisec) {
		init(millisec);
	}

	/* Twenty-eight stars class */
	private class Star {
		private String star;
		private String direction;
		private String fortune;

		public Star(String star, String fortune, String direction) {
			this.star = star;
			this.fortune = fortune;
			this.direction = direction;
		}

		public String getDirection() {
			return direction;
		}

		public String getFortune() {
			return fortune;
		}

		public String getStar() {
			return star;
		}
	}

	/* lunar or solar holiday */
	private class Holiday {
		private int month;
		private int day;
		private String name;

		public Holiday(int month, int day, String name) {
			this.month = month;
			this.day = day;
			this.name = name;
		}

		public int getDay() {
			return day;
		}

		public int getMonth() {
			return month;
		}

		public String getName() {
			return name;
		}
	}

	/* init lunar calendar with millisecond */
	private void init(long millisec) {
		mUTCCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		mSolar = Calendar.getInstance();
		mSolar.setTimeInMillis(millisec);
		mLunarYear = 1900;

		Calendar baseDate = new GregorianCalendar(1900, 0, 31);
		long offset = (millisec - baseDate.getTimeInMillis()) / 86400000;

		int daysInLunarYear = getLunarYearDays(mLunarYear);
		/* get current lunar year */
		while (mLunarYear < 2100 && offset >= daysInLunarYear) {
			offset -= daysInLunarYear;
			daysInLunarYear = getLunarYearDays(++mLunarYear);
		}

		/* get current lunar month */
		int lunarMonth = 1;
		int leapMonth = getLunarLeapMonth(mLunarYear);
		boolean leapDec = false;
		boolean isLeap = false;
		int daysInLunarMonth = 0;

		/* to get lunar year, month and day */
		while (lunarMonth < 13 && offset > 0) {
			if (isLeap && leapDec) {
				daysInLunarMonth = getLunarLeapDays(mLunarYear);
				leapDec = false;
			} else {
				daysInLunarMonth = getLunarMonthDays(mLunarYear, lunarMonth);
			}

			if (offset < daysInLunarMonth) {
				break;
			}
			offset -= daysInLunarMonth;

			if (leapMonth == lunarMonth && !isLeap) {
				leapDec = true;
				isLeap = true;
			} else {
				lunarMonth++;
			}
		}

		mDaysInLuarMonth = daysInLunarMonth;
		mLunarMonth = lunarMonth;
		mIsLeap = (lunarMonth == leapMonth && isLeap);
		mLunarDay = (int) offset + 1;

		getCyclicalData();
	}

	/* init with lunar date */
	private void initLunar(int lunarYear, int lunarMonth, int lunarDay, boolean isLeap) {
		int initYear = lunarYear;
		int initMonth = lunarMonth;
		long offset = lunarDay - 1;

		/* calculate month into days */
		while (--initMonth > 0) {
			offset += getLunarMonthDays(lunarYear, initMonth);
		}

		int leapMonth = getLunarLeapMonth(lunarYear);
		if (leapMonth <= lunarMonth) {
			offset += getLunarLeapDays(lunarYear);
		}

		/* calculate year into days */
		while (initYear > 1900) {
			offset += getLunarYearDays(--initYear);
		}

		/* init the all variable */
		mLunarYear = lunarYear;
		mLunarMonth = lunarMonth;
		mLunarDay = lunarDay;
		mUTCCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
		Calendar baseDate = new GregorianCalendar(1900, 0, 31);
		mSolar = Calendar.getInstance();
		mSolar.setTimeInMillis(offset * 86400000 + baseDate.getTimeInMillis());

		getCyclicalData();
	}

	/**
	 * Get the leap month in lunar year.
	 *
	 * @param lunarYear lunar year
	 * @return the month in specified lunar year, otherwise return 0
	 */
	private int getLunarLeapMonth(int lunarYear) {
		int leapMonth = LUNR_INFO[lunarYear - 1900] & 0xf;
		return leapMonth == 0xf ? 0 : leapMonth;
	}

	/**
	 * Get total days of leap month in lunar year.
	 *
	 * @param lunarYear lunar year
	 * @return total days of leap month, otherwise return 0 if no leap month.
	 */
	private int getLunarLeapDays(int lunarYear) {
		return getLunarLeapMonth(lunarYear) > 0 ?
			((LUNR_INFO[lunarYear - 1899] & 0xf) == 0xf ? 30 : 29) : 0;
	}

	/**
	 * Get total days of lunar year.
	 *
	 * @param lunarYear lunar year
	 * @return total days of lunar year
	 */
	private int getLunarYearDays(int lunarYear) {
		/* lunar year has (12 * 29 =) 348 days at least */
		int totalDays = 348;
		for (int i = 0x8000; i > 0x8; i >>= 1) {
			totalDays += ((LUNR_INFO[lunarYear - 1900] & i) != 0) ? 1 : 0;
		}

		return totalDays + getLunarLeapDays(lunarYear);
	}

	/**
	 * Get total days of lunar month in normal case.
	 *
	 * @param lunarYear  lunar year
	 * @param lunarMonth lunar month
	 * @return total days
	 */
	private int getLunarMonthDays(int lunarYear, int lunarMonth) {
		return ((LUNR_INFO[lunarYear - 1900] & (0x10000 >> lunarMonth)) != 0) ? 30 : 29;
	}

	/**
	 * Get Coordinated Universal Time for given params.
	 *
	 * @param year  the year to set
	 * @param month the month to set
	 * @param day   the day to set
	 * @param hour  the hour to set
	 * @param min   the minute to set
	 * @param sec   the second to set
	 * @return Coordinated Universal Time
	 */
	private synchronized long getUTC(int year, int month, int day, int hour, int min, int sec) {
		synchronized (Lunar.class) {
			mUTCCalendar.clear();
			mUTCCalendar.set(year, month, day, hour, min, sec);
			return mUTCCalendar.getTimeInMillis();
		}
	}

	/**
	 * Convert current date into Coordinated Universal Time.
	 *
	 * @param date current date
	 * @return get day in Coordinated Universal Time
	 */
	private synchronized int getUTCDay(Date date) {
		synchronized (Lunar.class) {
			mUTCCalendar.clear();
			mUTCCalendar.setTimeInMillis(date.getTime());
			return mUTCCalendar.get(Calendar.DAY_OF_MONTH);
		}
	}

	/**
	 * Convert current date into Coordinated Universal Time.
	 *
	 * @param date current date
	 * @return get month in Coordinated Universal Time
	 */
	private synchronized int getUTCMonth(Date date) {
		synchronized (Lunar.class) {
			mUTCCalendar.clear();
			mUTCCalendar.setTimeInMillis(date.getTime());
			return mUTCCalendar.get(Calendar.MONTH);
		}
	}

	/**
	 * Get the day of solar terms.
	 *
	 * @param solarYear the specified solar year
	 * @param index     the index of solar terms in {@link #SOLAR_TERM}
	 * @return the day of solar terms
	 */
	private int getSolarTermDay(int solarYear, int index) {
		long millisec = (long) 31556925974.7 * (solarYear - 1900) +
			SOLAR_TERM_INFO[index] * 60000L;
		return getUTCDay(new Date(millisec + getUTC(1900, 0, 6, 2, 5, 0)));
	}

	/**
	 * Get the month of solar terms.
	 *
	 * @param solarYear the specified solar year
	 * @param index     the index of solar terms in {@link #SOLAR_TERM}
	 * @return the month of solar terms
	 */
	private int getSolarTermMonth(int solarYear, int index) {
		long millisec = (long) 31556925974.7 * (solarYear - 1900) +
			SOLAR_TERM_INFO[index] * 60000L;
		return getUTCMonth(new Date(millisec + getUTC(1900, 0, 6, 2, 5, 0)));
	}

	/* get Heavenly Stems and Earthly Branches data */
	private void getCyclicalData() {
		mSolarYear = mSolar.get(Calendar.YEAR);
		mSolarMonth = mSolar.get(Calendar.MONTH);
		mSolarDay = mSolar.get(Calendar.DAY_OF_MONTH);

		int cyclicalYear;
		int cyclicalMonth;
		int cyclicalDay;

		/* year in Heavenly Stems and Earthly Branches */
		int term = getSolarTermDay(mSolarYear, 2);
		if (mSolarMonth < 1 || (mSolarMonth == 1 && mSolarDay < term)) {
			cyclicalYear = (mSolarYear - 1900 + 36 - 1) % 60;
		} else {
			cyclicalYear = (mSolarYear - 1900 + 36) % 60;
		}

		/* month in Heavenly Stems and Earthly Branches */
		int firstNode = getSolarTermDay(mSolarYear, mSolarMonth * 2);
		if (mSolarDay < firstNode) {
			cyclicalMonth = ((mSolarYear - 1900) * 12 + mSolarMonth + 12) % 60;
		} else {
			cyclicalMonth = ((mSolarYear - 1900) * 12 + mSolarMonth + 13) % 60;
		}

		cyclicalDay = (int) (getUTC(mSolarYear, mSolarMonth,
			mSolarDay, 0, 0, 0) / 86400000 + 25567 + 10) % 60;

		mCyclicalYear = cyclicalYear;
		mCyclicalMonth = cyclicalMonth;
		mCyclicalDay = cyclicalDay;
	}

	/**
	 * Get Heavenly Stems with specified cyclical number.
	 *
	 * @param cyclicalNum cyclical number in Heavenly Stems and Earthly Branches
	 * @return Heavenly stems index
	 */
	private int getHeavenlyStems(int cyclicalNum) {
		return cyclicalNum % 10;
	}

	/**
	 * Get Earthly Branches with specified cyclical number.
	 *
	 * @param cyclicalNum cyclical number in Heavenly Stems and Earthly Branches
	 * @return Earthly Branches index
	 */
	private int getEarthlyBranches(int cyclicalNum) {
		return cyclicalNum % 12;
	}

	/**
	 * Get Heavenly Stems and Earthly Branches.
	 *
	 * @param cyclicalNum cyclical number in Heavenly Stems and Earthly Branches
	 * @return Heavenly Stems and Earthly Branches string
	 */
	private String getCyclical(int cyclicalNum) {
		return HEAVENLY_STEMS[getHeavenlyStems(cyclicalNum)] +
			EARTHLY_BRANCHES[getEarthlyBranches(cyclicalNum)];
	}

	/**
	 * Set date to get lunar data.
	 *
	 * @param year  the year
	 * @param month the month
	 * @param day   the day
	 */
	public void setDate(int year, int month, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, day);
		init(year == 0 ? System.currentTimeMillis() : calendar.getTimeInMillis());
	}

	/**
	 * Set lunar date, so then can get GregorianCalendar date.
	 *
	 * @param year   lunar year
	 * @param month  lunar month
	 * @param day    lunar day
	 * @param isLeap the month is leap month or not
	 */
	public void setLunarDate(int year, int month, int day, boolean isLeap) {
		initLunar(year, month, day, isLeap);
	}

	/**
	 * Set time in millisecond.
	 *
	 * @param millisecond millisecond to set
	 */
	public void setTimeInMillis(long millisecond) {
		init(millisecond);
	}

	/**
	 * Get Chinese zodiac string.
	 *
	 * @return Chinese zodiac
	 */
	public String getZodiac() {
		return ZODIAC[(mLunarYear - 4) % 12];
	}

	/**
	 * Get solar terms for current day.
	 *
	 * @return solar terms, otherwise return null if was not solar term.
	 */
	public String getSolarTerm() {
		if (getSolarTermDay(mSolarYear, mSolarMonth * 2) == mSolarDay) {
			return SOLAR_TERM[mSolarMonth * 2];
		} else if (getSolarTermDay(mSolarYear, mSolarMonth * 2 + 1) == mSolarDay) {
			return SOLAR_TERM[mSolarMonth * 2 + 1];
		}

		return null;
	}

	/**
	 * Get the index of current day in Heavenly Stems and Earthly Branches.
	 *
	 * @return the index
	 */
	public int getHeavenlyAndEarthly() {
		return mCyclicalDay;
	}

	/**
	 * Get year in Heavenly Stems and Earthly Branches.
	 *
	 * @return year in Heavenly Stems and Earthly Branches
	 */
	public String getCyclicalYear() {
		return getCyclical(mCyclicalYear);
	}

	/**
	 * Get month in Heavenly Stems and Earthly Branches.
	 *
	 * @return month in Heavenly Stems and Earthly Branches
	 */
	public String getCyclicalMonth() {
		return getCyclical(mCyclicalMonth);
	}

	/**
	 * Get day in Heavenly Stems and Earthly Branches.
	 *
	 * @return day in Heavenly Stems and Earthly Branches
	 */
	public String getCyclicalDay() {
		return getCyclical(mCyclicalDay);
	}

	/**
	 * Get year in lunar calendar.
	 *
	 * @return year in lunar calendar
	 */
	public String getLunarYear() {
		return getCyclical(mLunarYear - 1900 + 36);
	}

	/**
	 * Get lunar month in Chinese according to lunar month numeric.
	 *
	 * @param lunarMonth lunar month numeric
	 * @param isLeap     is current month leap or not
	 * @return lunar month in Chinese
	 */
	public String getLunarMonth(int lunarMonth, boolean isLeap) {
		String lunarMonthStr = "";
		if (lunarMonth == 1) {
			lunarMonthStr = LUNAR_SPEC_STRING[4];
		} else {
			switch (lunarMonth) {
			case 10:
				lunarMonthStr = LUNAR_SPEC_STRING[1];
				break;

			case 11:
				lunarMonthStr = LUNAR_SPEC_STRING[5];
				break;

			case 12:
				lunarMonthStr = LUNAR_SPEC_STRING[6];
				break;

			default:
				lunarMonthStr += LUNAR_STRING[lunarMonth % 10];
				break;
			}
		}

		return (isLeap ? "闰" : "") + lunarMonthStr;
	}

	/**
	 * Get month in Chinese of lunar calendar.
	 *
	 * @return month in lunar calendar
	 */
	public String getLunarMonth() {
		return getLunarMonth(mLunarMonth, mIsLeap);
	}

	/**
	 * Get lunar day in Chinese according to lunar day numeric.
	 *
	 * @param lunarDay lunar day numeric
	 * @return lunar day in Chinese
	 */
	public String getLunarDay(int lunarDay) {
		if (lunarDay < 1 || lunarDay > 30) {
			return "";
		}

		int decade = lunarDay / 10;
		int unit = lunarDay % 10;
		String decadeStr = LUNAR_SPEC_STRING[decade];
		String unitStr = LUNAR_STRING[unit];

		if (lunarDay < 11) {
			decadeStr = LUNAR_SPEC_STRING[0];
		}

		if (unit == 0) {
			unitStr = LUNAR_SPEC_STRING[1];
		}

		return decadeStr + unitStr;
	}

	/**
	 * Get day in lunar calendar.
	 *
	 * @return day in lunar calendar
	 */
	public String getLunarDay() {
		return getLunarDay(mLunarDay);
	}

	public int getLunarYearNum() {
		return mLunarYear;
	}

	/**
	 * Get lunar month in numeric.
	 *
	 * @return the numeric of lunar month
	 */
	public int getLunarMonthNum() {
		return mLunarMonth;
	}

	public int getLunarDayNum() {
		return mLunarDay;
	}

	/**
	 * Get max days in current lunar month.
	 *
	 * @return max days
	 */
	public int getMaxDaysInLunarMonth() {
		return mDaysInLuarMonth;
	}

	/**
	 * Get solar calendar.
	 *
	 * @return solar calendar
	 */
	public Calendar getCalendar() {
		return mSolar;
	}

	/**
	 * Get year in the Gregorian calendar.
	 *
	 * @return year in the Gregorian calendar
	 */
	public int getSolarYear() {
		return mSolarYear;
	}

	/**
	 * Get month in the Gregorian calendar, based-on 0.
	 *
	 * @return month in the Gregorian calendar
	 */
	public int getSolarMonth() {
		return mSolarMonth;
	}

	/**
	 * Get day in the Gregorian calendar.
	 *
	 * @return day in the Gregorian calendar
	 */
	public int getSolarDay() {
		return mSolarDay;
	}

	/**
	 * Get the day of week.
	 *
	 * @return day of week(1-Sunday, 7-Saturday).
	 */
	public int getDayOfWeek() {
		return mSolar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Get the week of year.
	 *
	 * @return week of year
	 */
	public int getWeekOfYear() {
		return mSolar.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * Get day of week in Chinese.
	 *
	 * @return day of week in Chinese
	 */
	public String getDayOfWeekInChinese() {
		return DAY_OF_WEEK_IN_CHINESE[getDayOfWeek() - 1];
	}

	/**
	 * To check if current date is today.
	 *
	 * @return true if was today, otherwise return false
	 */
	public boolean isToday() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR) == mSolarYear &&
			calendar.get(Calendar.MONTH) == mSolarMonth &&
			calendar.get(Calendar.DAY_OF_MONTH) == mSolarDay;
	}

	/**
	 * Get Pengzu one hundred dread.
	 *
	 * @return Pengzu one hundred dread
	 */
	public String[] getPengzu() {
		String[] pengzu = new String[2];
		pengzu[0] = PENG_ZU_HEAVENLY[getHeavenlyStems(mCyclicalDay)];
		pengzu[1] = PENG_ZU_EARTHLY[getEarthlyBranches(mCyclicalDay)];

		return pengzu;
	}

	/**
	 * Get lunar holidy if existed.
	 *
	 * @return lunar holiday, null if not existed
	 */
	public String getLunarHoliday() {
		if (mIsLeap) {
			return null;
		}

		/* New Year's Eve is special, maybe 29, maybe 30 */
		if (mLunarMonth == 12 && mLunarDay == mDaysInLuarMonth) {
			return "除夕";
		}

		for (Holiday holiday : mLunarHolidays) {
			if (holiday.getMonth() == mLunarMonth && holiday.getDay() == mLunarDay) {
				return holiday.getName();
			}
		}

		return null;
	}

	/**
	 * Get solar holiday if existed.
	 *
	 * @return solar holiday, null if not existed
	 */
	public String getSolarHolidy() {
		for (Holiday holiday : mSolarHolidays) {
			if (holiday.getMonth() == mSolarMonth + 1 && holiday.getDay() == mSolarDay) {
				return holiday.getName();
			}
		}

		return null;
	}

	/**
	 * Get confilict evil spirit for current day.
	 *
	 * @return evil spirit
	 */
	public String getConflictEvilSpirit() {
		int heavenlyIndex = getHeavenlyStems(mCyclicalDay);
		int earthlyIndex = getEarthlyBranches(mCyclicalDay);
		int conflictHeavenlyIndex;
		int conflictEarthlyIndex;

		if (heavenlyIndex < 6) {
			conflictHeavenlyIndex = heavenlyIndex + 4;
		} else {
			conflictHeavenlyIndex = heavenlyIndex - 6;
		}

		if (earthlyIndex < 6) {
			conflictEarthlyIndex = earthlyIndex + 6;
		} else {
			conflictEarthlyIndex = earthlyIndex - 6;
		}

		return "冲" + ZODIAC[conflictEarthlyIndex] + "(" + HEAVENLY_STEMS[conflictHeavenlyIndex] +
			EARTHLY_BRANCHES[conflictEarthlyIndex] + ")" + "煞" + EVIL_SPIRIT[earthlyIndex % 4];
	}

	/**
	 * Get star description for current day.
	 *
	 * @return star description
	 */
	public String getTwentyEightStar() {
		int weekOfYear = (getWeekOfYear() - 1) % 4;
		int dayOfWeek = getDayOfWeek() - 1;
		Star star = mTwentyEightStars[weekOfYear][dayOfWeek];

		return star.getDirection() + star.getStar() + "-" + star.getFortune();
	}

	/**
	 * Get the index of star duty.
	 *
	 * @return the index
	 */
	public int getWielding() {
		int earthlyIndexOfMonth = getEarthlyBranches(mCyclicalMonth);
		int earthlyIndexOfDay = getEarthlyBranches(mCyclicalDay);
		int monthIndex = earthlyIndexOfMonth >= 2 ?
			earthlyIndexOfMonth - 2 : 12 - earthlyIndexOfMonth;
		int offset = 12 - (monthIndex == 0 ? 12 : monthIndex);
		if (earthlyIndexOfDay + offset < 12) {
			return earthlyIndexOfDay + offset;
		} else {
			return earthlyIndexOfDay + offset - 12;
		}
	}

	/**
	 * Get five elements for current day.
	 *
	 * @return five elements description
	 */
	public String getFiveElements() {
		return FIVE_ELEMENTS[mCyclicalDay / 2] + " " + TWELVE_DURY[getWielding()] + "执位";
	}

	/**
	 * Get the index of fetus god direction.
	 *
	 * @return the index
	 */
	private int getFetusGodDirectionIndex() {
		if (mCyclicalDay < 2 || mCyclicalDay > 55) {
			return 2;
		} else if (mCyclicalDay < 6) {
			return 3;
		} else if (mCyclicalDay < 12) {
			return 4;
		} else if (mCyclicalDay < 17) {
			return 5;
		} else if (mCyclicalDay < 23) {
			return 6;
		} else if (mCyclicalDay < 28) {
			return 7;
		} else if (mCyclicalDay < 33) {
			return 8;
		} else if (mCyclicalDay < 39) {
			return 9;
		} else if (mCyclicalDay < 44) {
			return 10;
		} else if (mCyclicalDay < 50) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * Get the fetus god description for current day.
	 *
	 * @return fetus god description
	 */
	public String getFetusGod() {
		int heavenlyIndex = getHeavenlyStems(mCyclicalDay);
		int earthlyIndex = getEarthlyBranches(mCyclicalDay);

		String fetusGod;
		String heavenlyPosition = FETUS_GOD_HEAVENLY[heavenlyIndex % 5];
		String earthlyPosition = FETUS_GOD_EARTHLY[earthlyIndex % 5];
		if (heavenlyPosition.contains(earthlyPosition)) {
			fetusGod = heavenlyPosition;
		} else if (earthlyPosition.contains(heavenlyPosition)) {
			fetusGod = earthlyPosition;
		} else {
			fetusGod = (heavenlyPosition + earthlyPosition);
		}

		fetusGod = (fetusGod.length() <= 2 ? "占" : "") + fetusGod;
		fetusGod += FETUS_GOD_DIRECTION[getFetusGodDirectionIndex()];

		return fetusGod;
	}
}

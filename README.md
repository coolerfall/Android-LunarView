Android-LunarView
=================

A Chinese lunar calendar view.

Screenshot
==========
![](https://raw.githubusercontent.com/Coolerfall/Android-LunarView/master/lunar.gif)

Usage
=====
Use in xml:

	<com.coolerfall.widget.lunar.LunarView
		android:layout_width="wrap_content"
		android:layout_height="wrap_content"/>

and you can add OnDatePickListener:
```java
lunarView.setOnDatePickListener(new LunarView.OnDatePickListener() {
	@Override public void onDatePick(LunarView view, MonthDay monthDay) {
		//do something
	}
});
```

| **LunarView** ||
|:---|:---|
| app:todayBackground | The background of today.
| app:checkedDayBackgroundColor | The background color of checked day by user.
| app:monthBackgroundColor |  The background color of the whole month view.
| app:weekLabelBackgroundColor | The background color of week label.
| app:solarTextColor | The text color of solar day.
| app:lunarTextColor | The text color of lunar day.
| app:highlightColor | The text color of highlight day(such as weekend and holidays).
| app:uncheckableColor | The text color of uncheckable day.
| app:shouldPickOnMonthChange | Shoul invoke callback when month changed(default is true).


You can check [LunarLite][2] to see the detail usage of LunarView.


Gradle
--------
	
	compile 'com.coolerfall:android-lunar-view:1.0.1'

License
=======

    Copyright (C) 2015-2017 Vincent Cheung

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[1]: https://search.maven.org/remote_content?g=com.coolerfall&a=android-lunar-view&v=LATEST
[2]: https://github.com/Tourbillon/LunarLite
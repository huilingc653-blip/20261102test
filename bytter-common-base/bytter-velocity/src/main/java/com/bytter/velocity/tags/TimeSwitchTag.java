package com.bytter.velocity.tags;
import org.apache.velocity.tools.config.DefaultKey;
import org.apache.velocity.tools.generic.LocaleConfig;
@DefaultKey("timeSwitch")
public class TimeSwitchTag extends LocaleConfig {
    private static final String[] chinese = { "天", "小时", "分", "秒" };
    private static final String[] foreign = { "d", "h", "m", "s" };
    public String millisToTime(long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;
        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        // Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;
        boolean chineseFlag = getLocale().toString().contains("zh");
        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day).append(chineseFlag ? chinese[0] : foreign[0]);
        }
        if (hour > 0) {
            sb.append(hour).append(chineseFlag ? chinese[1] : foreign[1]);
        }
        if (minute > 0) {
            sb.append(minute).append(chineseFlag ? chinese[2] : foreign[2]);
        }
        if (second > 0) {
            sb.append(second).append(chineseFlag ? chinese[3] : foreign[3]);
        }
        return sb.toString();
    }
}

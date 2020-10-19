package base.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * created by 李云 on 2019/6/26
 * 本类的作用:整数秒转为时分秒
 */
public class TimeUtil {
    //整数(秒数)转换为时分秒格式(xx:xx:xx)
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:"+unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String converTime(long time) {
        long currentSeconds = System.currentTimeMillis() / 1000;
        long timeGap = currentSeconds - time / 1000;// 与现在时间相差秒数
        String timeStr = null;
        if(timeGap > 3*24 *60*60){
            timeStr = getDayTime(time) + " " + getMinTime(time);
        }
        else if (timeGap > 24 * 2 * 60 * 60) {// 2天以上就返回标准时间
            timeStr = "前天 " + getMinTime(time);
        } else if (timeGap > 24 * 60 * 60) {// 1天-2天
            timeStr = timeGap / (24 * 60 * 60) + "昨天 " + getMinTime(time);
        } else if (timeGap > 60 * 60) {// 1小时-24小时
            timeStr = timeGap / (60 * 60) + "今天 " + getMinTime(time);
        } else if (timeGap > 60) {// 1分钟-59分钟
            timeStr = timeGap / 60 + "今天 " + getMinTime(time);
        } else {// 1秒钟-59秒钟
            timeStr = "今天 " + getMinTime(time);
        }
        return timeStr;
    }

    public static String getChatTime(long time) {
        return getMinTime(time);
    }

    public static String getPrefix(long time) {
        long currentSeconds = System.currentTimeMillis();
        long timeGap = currentSeconds - time;// 与现在时间差
        String timeStr = null;
        if (timeGap > 24 * 3 * 60 * 60 * 1000) {
            timeStr = getDayTime(time) + " " + getMinTime(time);
        } else if (timeGap > 24 * 2 * 60 * 60 * 1000) {
            timeStr = "前天 " + getMinTime(time);
        } else if (timeGap > 24 * 60 * 60 * 1000) {
            timeStr = "昨天 " + getMinTime(time);
        } else {
            timeStr = "今天 " + getMinTime(time);
        }
        return timeStr;
    }

    public static String getDayTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd");
        return format.format(new Date(time));
    }

    public static String getMinTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(time));
    }
}

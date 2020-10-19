package base.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * created by 李云 on 2019/1/5
 * 本类的作用:
 */
public class StringUtils {
    /**
     * 格式化字符串，去空格回车
     */
    public static String replaceBlankEnter(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }
}

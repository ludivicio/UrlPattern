package url.ancode.org.urlpattern;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lu on 1/4/16.
 */
public class UrlUtil {

    protected static final String TAG = UrlUtil.class.getSimpleName();

    public static final String GOOD_IRI_CHAR = "a-zA-Z0-9";
    public static final String GOOD_GTLD_CHAR = "a-zA-Z";
    public static final String GTLD = "[" + GOOD_GTLD_CHAR + "]{2,63}";
    public static final String IRI = "[" + GOOD_IRI_CHAR + "]([" + GOOD_IRI_CHAR + "\\-]{0,61}[" + GOOD_IRI_CHAR + "]){0,1}";
    public static final String HOST_NAME = "(" + IRI + "\\.)+" + GTLD;

    // public static final Pattern WEB_URL = Pattern.compile("([a-zA-z]+://[^\\s]*" + "|" + HOST_NAME + ")");
    // public static final Pattern WEB_URL = Pattern.compile("([a-zA-z]+://[^\\u4e00-\\u9fa5\\s]*" + "|"  + HOST_NAME + ")");
    // public static final Pattern WEB_URL = Pattern.compile("((https?|s?ftp|irc[6s]?|git|afp|telnet|smb)://)?((\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(www.|[a-zA-Z].)?[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,63})(((:[0-9]+)?/($|[a-zA-Z0-9\\./,;\\?'\\\\+&amp;%\\$#=~_\\-]+)*[^\\u4e00-\\u9fa5\\s]*)|([^\\u4e00-\\u9fa5\\s0-9]*))");

    public static final Pattern WEB_URL;

    private static final String[] ext = {
            "top", "com", "net", "org", "edu", "gov", "int", "mil", "cn", "tel", "biz", "cc", "tv",
            "info", "name", "hk", "mobi", "asia", "cd", "travel", "pro", "museum", "link", "coop", "aero", "ad", "ae", "af", "ag", "ai", "al", "am",
            "an", "ao", "aq", "ar", "as", "at", "au", "aw", "az", "ba", "bb", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bm", "bn", "bo", "br",
            "bs", "bt", "bv", "bw", "by", "bz", "ca", "cc", "cf", "cg", "ch", "ci", "ck", "cl", "cm", "cn", "co", "cq", "cr", "cu", "cv", "cx",
            "cy", "cz", "de", "dj", "dk", "dm", "do", "dz", "ec", "ee", "eg", "eh", "es", "et", "ev", "fi", "fj", "fk", "fm", "fo", "fr", "ga",
            "gb", "gd", "ge", "gf", "gh", "gi", "gl", "gm", "gn", "gp", "gr", "gt", "gu", "gw", "gy", "hk", "hm", "hn", "hr", "ht", "hu", "id",
            "ie", "il", "in", "io", "iq", "ir", "is", "it", "jm", "jo", "jp", "ke", "kg", "kh", "ki", "km", "kn", "kp", "kr", "kw", "ky", "kz",
            "la", "lb", "lc", "li", "lk", "lr", "ls", "lt", "lu", "lv", "ly", "ma", "mc", "md", "mg", "mh", "ml", "mm", "mn", "mo", "mp", "mq",
            "mr", "ms", "mt", "mv", "mw", "mx", "my", "mz", "na", "nc", "ne", "nf", "ng", "ni", "nl", "no", "np", "nr", "nt", "nu", "nz", "om",
            "qa", "pa", "pe", "pf", "pg", "ph", "pk", "pl", "pm", "pn", "pr", "pt", "pw", "py", "re", "ro", "ru", "rw", "sa", "sb", "sc", "sd",
            "se", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sr", "st", "su", "sy", "sz", "tc", "td", "tf", "tg", "th", "tj", "tk",
            "tm", "tn", "to", "tp", "tr", "tt", "tv", "tw", "tz", "ua", "ug", "uk", "us", "uy", "va", "vc", "ve", "vg", "vn", "vu", "wf", "ws",
            "ye", "yu", "za", "zm", "zr", "zw"};


    static {

        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < ext.length; i++) {
            sb.append(ext[i]);
            sb.append("|");
        }

        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");

        // final pattern str
//        String pattern = "((https?|s?ftp|irc[6s]?|git|afp|telnet|smb)://)?((\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|((www\\.|[a-zA-Z\\.]+\\.)?[a-zA-Z0-9\\-]+\\.(top|com|net|org|edu|gov|int|mil|cn|ml)(:[0-9]+)?))((/[a-zA-Z0-9\\./,;\\?'\\+&amp;%\\$#=~_\\-]*)|([^\\u4e00-\\u9fa5\\s0-9a-zA-Z]*))";

        String pattern = "((https?|s?ftp|irc[6s]?|git|afp|telnet|smb)://)?((\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|((www\\.|[a-zA-Z0-9\\.]+\\.)?[a-zA-Z0-9\\-]+\\." + sb.toString() + "(:[0-9]+)?))((/[a-zA-Z0-9\\./,;\\?'\\+&amp;%\\$#=~_\\-]*)|([^\\u4e00-\\u9fa5\\s0-9a-zA-Z\\./,;\\?'\\+&amp;%\\$#=~_\\-]*))";

        Log.v(TAG, "pattern = " + pattern);

        WEB_URL = Pattern.compile(pattern);
    }

    /**
     * 提取网址
     *
     * @param source
     * @return
     */
    public static SpannableString retractUrl(String source, String target) {
        SpannableString ss = new SpannableString(source);
        Matcher matcher2 = WEB_URL.matcher(source);

        if (!matcher2.find()) {
            return null;
        }

        final String group = matcher2.group();
        Log.d(TAG, "group = " + group);
        if (group.equals(target)) {
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#19A15F")), matcher2.start(), matcher2.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ss.setSpan(new ForegroundColorSpan(Color.parseColor("#DD5044")), matcher2.start(), matcher2.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            int start = group.indexOf(target);
            if (start != -1) {
                start = matcher2.start() + start;
                int end = start + target.length();
                ss.setSpan(new ForegroundColorSpan(Color.parseColor("#19A15F")), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return ss;
    }

}

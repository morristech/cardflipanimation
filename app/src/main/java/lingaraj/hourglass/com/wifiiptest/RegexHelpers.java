package lingaraj.hourglass.com.wifiiptest;

import android.text.style.TextAppearanceSpan;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by lingaraj on 9/30/16.
 */

public class RegexHelpers {
    private static String TAG = "Regex Helpers";

    public static boolean isProperIpAddress(String input_ip_address)
    {
       // Log.d(TAG,)
       String  ip = input_ip_address.trim();
        if ((ip.length() <=6)  || (ip.length() > 15))
        {
            Log.d(TAG,"Not a proper Ip address"+" "+"Input Ip Lenght"+ip.length());
            return false;
        }
        else
        {
            try {
                Pattern pattern = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
                Matcher matcher = pattern.matcher(ip);
                return matcher.matches();
            } catch (PatternSyntaxException ex) {
                return false;
            }
        }

    }

}


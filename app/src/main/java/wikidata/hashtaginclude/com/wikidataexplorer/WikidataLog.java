package wikidata.hashtaginclude.com.wikidataexplorer;

import android.util.Log;

/**
 * Created by matthewmichaud on 1/18/15.
 */
public class WikidataLog {
    private static String TAG = "Wikidata";

    public static void v(String tag, String message) {
        Log.v(TAG+" ["+tag+"]", message);
    }

    public static void v(String tag, String message, Throwable tr) {
        Log.v(TAG+" ["+tag+"]", message, tr);
    }

    public static void d(String tag, String message) {
        Log.d(TAG+" ["+tag+"]", message);
    }

    public static void d(String tag, String message, Throwable tr) {
        Log.d(TAG+" ["+tag+"]", message, tr);
    }

    public static void i(String tag, String message) {
        Log.i(TAG+" ["+tag+"]", message);
    }

    public static void i(String tag, String message, Throwable tr) {
        Log.i(TAG+" ["+tag+"]", message, tr);
    }

    public static void w(String tag, String message) {
        Log.w(TAG+" ["+tag+"]", message);
    }

    public static void w(String tag, String message, Throwable tr) {
        Log.w(TAG+" ["+tag+"]", message, tr);
    }

    public static void e(String tag, String message) {
        Log.e(TAG+" ["+tag+"]", message);
    }

    public static void e(String tag, String message, Throwable tr) {
        Log.e(TAG+" ["+tag+"]", message, tr);
    }
}

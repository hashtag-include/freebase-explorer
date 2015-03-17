package wikidata.hashtaginclude.com.wikidataexplorer;

import android.app.Activity;
import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * Created by matthewmichaud on 1/21/15.
 */
public class WikidataUtility {

    static Style croutonStyle;

    private static void makeStyle() {
        croutonStyle = new Style.Builder().setBackgroundColor(R.color.app_primary_dark).setTextColor(android.R.color.white).build();
    }

    public static void makeCroutonText(String text, Activity activity) {
        if(croutonStyle == null) {
            makeStyle();
        }
        Crouton.makeText(activity, text, croutonStyle, R.id.crouton_handle).show();
    }

    public static long dateToMilli(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        try {
            return sdf.parse(dateString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static float dpToPx(float dp, Context context) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
    public static float pxToDp(float px, Context context) {
        return px / context.getResources().getDisplayMetrics().density;
    }
}

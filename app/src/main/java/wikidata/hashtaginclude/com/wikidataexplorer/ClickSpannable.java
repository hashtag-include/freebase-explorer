package wikidata.hashtaginclude.com.wikidataexplorer;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by matthewmichaud on 2/11/15.
 */
public class ClickSpannable extends ClickableSpan {
    String Url;
    public ClickSpannable(String Url) {
        this.Url = Url;
    }
    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(Color.BLUE);
        ds.setUnderlineText(true);
    }
    @Override
    public void onClick(View widget) {
    }
    public String getUrl() {
        return Url;
    }
}

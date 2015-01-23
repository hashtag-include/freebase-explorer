package wikidata.hashtaginclude.com.wikidataexplorer.ui.recent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.api.WikidataLookup;
import wikidata.hashtaginclude.com.wikidataexplorer.models.RecentItemModel;

/**
 * Created by matthewmichaud on 1/17/15.
 */
public class RecentAdapter extends ArrayAdapter<RecentItemModel> {

    public RecentAdapter(Context context, ArrayList<RecentItemModel> objects) {
        super(context, R.layout.adapter_recent_item, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        RecentItemModel item = getItem(position);

        final ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.adapter_recent_item, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.recent_item_title);
            viewHolder.arrow = (ImageView) convertView.findViewById(R.id.recent_item_arrow);
            viewHolder.webView = (WebView) convertView.findViewById(R.id.recent_web);
            viewHolder.webView .setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            viewHolder.hiddenContent = (LinearLayout) convertView.findViewById(R.id.recent_content_hidden);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText("");
        viewHolder.hiddenContent.setVisibility(View.GONE);
        viewHolder.arrow.setImageResource(R.drawable.ic_hardware_keyboard_arrow_right);
        // check to see if the title has been looked up yet
        if(item.getTitleText()==null) {
            // we need to lookup the title
            WikidataLookup.getLabel(item.getTitleId(), new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    RecentItemModel item = getItem(position);
                    if(s==null || s.length()==0) {
                        s = item.getTitleId();
                    }
                    item.setTitleText(s);
                    viewHolder.title.setText(item.getTitleText());
                }

                @Override
                public void failure(RetrofitError error) {
                    RecentItemModel item = getItem(position);
                    item.setTitleText(item.getTitleId());
                    viewHolder.title.setText(item.getTitleText());
                }
            });
        } else {
            viewHolder.title.setText(item.getTitleText());
        }
        viewHolder.webView.loadData(item.getParsedComment(), "text/html; charset=UTF-8", null);

        return convertView;
    }

    private static class ViewHolder {
        ImageView arrow;
        TextView title;
        LinearLayout hiddenContent;
        WebView webView;
    }
}

package wikidata.hashtaginclude.com.wikidataexplorer.ui.entity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataLog;
import wikidata.hashtaginclude.com.wikidataexplorer.api.WikidataLookup;
import wikidata.hashtaginclude.com.wikidataexplorer.models.LabelListResponseModel;
import wikidata.hashtaginclude.com.wikidataexplorer.models.SearchEntityResponseModel;

/**
 * Created by matthewmichaud on 2/22/15.
 */
public class EntityListAdapter extends ArrayAdapter<String> {
    private static final String TAG = "[EntityListAdapter]";

    public EntityListAdapter(Context context, List<String> objects) {
        super(context, R.layout.adapter_entity_item, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String item = getItem(position);

        ViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.adapter_entity_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.label.setText(item);
        WikidataLog.d(TAG, "getView: "+position);
        WikidataLookup.getLabel(item, position, viewHolder, new Callback<LabelListResponseModel>() {
            @Override
            public void success(LabelListResponseModel model, Response response) {
                String item = getItem(model.getPosition());
                WikidataLog.d(TAG, "position: "+position);
                ((ViewHolder)model.getViewHolder()).label.setText(item + " (" + model.getLabel() + ")");
            }

            @Override
            public void failure(RetrofitError error) {
                WikidataLog.e(TAG, "Could not get label", error);
            }
        });



        return convertView;
    }

    protected static class ViewHolder {
        @InjectView(R.id.entity_label)
        TextView label;

        public ViewHolder(View root) {
            ButterKnife.inject(this, root);
        }
    }
}
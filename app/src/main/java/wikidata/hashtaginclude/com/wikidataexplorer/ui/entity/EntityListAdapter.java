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
import wikidata.hashtaginclude.com.wikidataexplorer.api.WikidataLookup;
import wikidata.hashtaginclude.com.wikidataexplorer.models.SearchEntityResponseModel;

/**
 * Created by matthewmichaud on 2/22/15.
 */
public class EntityListAdapter extends ArrayAdapter<String> {

    public EntityListAdapter(Context context, List<String> objects) {
        super(context, R.layout.adapter_entity_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String item = getItem(position);

        final ViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.adapter_entity_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        WikidataLookup.getLabel(item, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                viewHolder.label.setText(item + "(" + s + ")");
            }

            @Override
            public void failure(RetrofitError error) {
                viewHolder.label.setText(item);
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
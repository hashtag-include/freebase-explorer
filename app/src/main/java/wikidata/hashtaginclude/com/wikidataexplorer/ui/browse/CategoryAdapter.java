package wikidata.hashtaginclude.com.wikidataexplorer.ui.browse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

/**
 * Created by matthewmichaud on 3/23/15.
 */
public class CategoryAdapter extends ArrayAdapter<String> {
    public CategoryAdapter(Context context, List<String> objects) {
        super(context, R.layout.adapter_category, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String item = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.adapter_category, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.categoryButton.setText(item);
        viewHolder.categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    protected static class ViewHolder {
        @InjectView(R.id.category_list_text)
        Button categoryButton;

        public ViewHolder(View root) {
            ButterKnife.inject(this, root);
        }
    }
}

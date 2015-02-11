package wikidata.hashtaginclude.com.wikidataexplorer.ui.query;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.models.SearchEntityResponseModel;

/**
 * Created by matthewmichaud on 2/10/15.
 */
public class SearchEntitiesAdapter extends ArrayAdapter<SearchEntityResponseModel.SearchModel> {

    public SearchEntitiesAdapter(Context context, List<SearchEntityResponseModel.SearchModel> objects) {
        super(context, R.layout.adapter_search_entity_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchEntityResponseModel.SearchModel model = getItem(position);

        final ViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.adapter_search_entity_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
        SpannableStringBuilder labelText = new SpannableStringBuilder("Label: "+model.getLabel());
        labelText.setSpan(bss, 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        viewHolder.label.setText(labelText);

        SpannableStringBuilder descriptionText = new SpannableStringBuilder("Description: "+model.getDescription());
        descriptionText.setSpan(bss, 0, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        viewHolder.description.setText(descriptionText);

        SpannableStringBuilder linkText = new SpannableStringBuilder("Link: "+model.getUrl());
        linkText.setSpan(bss, 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        viewHolder.link.setText(linkText);

        SpannableStringBuilder idText = new SpannableStringBuilder("ID: "+model.getId());
        idText.setSpan(bss, 0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        viewHolder.id.setText(idText);
        return convertView;
    }

    protected static class ViewHolder {
        @InjectView(R.id.search_entity_label)
        TextView label;

        @InjectView(R.id.search_entity_description)
        TextView description;

        @InjectView(R.id.search_entity_link)
        TextView link;

        @InjectView(R.id.search_entity_id)
        TextView id;

        public ViewHolder(View root) {
            ButterKnife.inject(this, root);
        }
    }
}

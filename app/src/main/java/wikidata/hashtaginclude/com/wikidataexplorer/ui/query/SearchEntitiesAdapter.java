package wikidata.hashtaginclude.com.wikidataexplorer.ui.query;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wikidata.hashtaginclude.com.wikidataexplorer.ClickSpannable;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataLog;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataUtility;
import wikidata.hashtaginclude.com.wikidataexplorer.api.WikidataLookup;
import wikidata.hashtaginclude.com.wikidataexplorer.models.SearchEntityResponseModel;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.entity.EntityActivity;

/**
 * Created by matthewmichaud on 2/10/15.
 */
public class SearchEntitiesAdapter extends ArrayAdapter<SearchEntityResponseModel.SearchModel> {

    private String TAG = "SearchEntitiesAdapter";

    StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

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

        SpannableStringBuilder labelText = new SpannableStringBuilder("Label: "+model.getLabel());
        labelText.setSpan(bss, 0, 7, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        viewHolder.label.setText(labelText);

        SpannableStringBuilder descriptionText = new SpannableStringBuilder("Description: "+model.getDescription());
        descriptionText.setSpan(bss, 0, 13, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        viewHolder.description.setText(descriptionText);

        SpannableStringBuilder linkText = new SpannableStringBuilder("Link: ");
        linkText.setSpan(bss, 0, 6, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        viewHolder.link.setText(linkText);

        SpannableStringBuilder linkBuilder = new SpannableStringBuilder(model.getUrl().substring(2));
        ClickSpannable clickSpannable = new ClickSpannable(model.getUrl().substring(2)) {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://"+this.getUrl()));
                getContext().startActivity(intent);
            }
        };
        linkBuilder.setSpan(clickSpannable, 0, model.getUrl().substring(2).length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        viewHolder.link.setText(TextUtils.concat(linkText, linkBuilder), TextView.BufferType.SPANNABLE);
        viewHolder.link.setMovementMethod(LinkMovementMethod.getInstance());

        viewHolder.goTo.setTag(model.getId());
        viewHolder.goTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WikidataLookup.getEntities(
                        (String)(v.getTag()), "", "", "yes", "info|sitelinks|aliases|descriptions|claims|datatype",
                        "", "", "", "", "",
                        new Callback<JsonElement>() {
                            @Override
                            public void success(JsonElement getEntityResponseModel, Response response) {
                                if (getEntityResponseModel != null) {
                                    JsonObject entities = getEntityResponseModel.getAsJsonObject().getAsJsonObject("entities");
                                    ArrayList<String> responses = new ArrayList<String>();
                                    Iterator<Map.Entry<String, JsonElement>> iterator = entities.entrySet().iterator();
                                    while (iterator.hasNext()) {
                                        Map.Entry<String, JsonElement> entry = iterator.next();
                                        responses.add(entry.getValue().toString());
                                    }

                                    // launch a new activity with the model
                                    Intent intent = new Intent(getContext(), EntityActivity.class);
                                    intent.putExtra("responses", responses);
                                    getContext().startActivity(intent);
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                WikidataLog.e(TAG, "Failed to get entities", error);
                                WikidataUtility.makeCroutonText("Could not complete request", (Activity)(getContext()));
                            }
                        }
                );
            }
        });

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
        @InjectView(R.id.search_entity_go_to)
        ImageView goTo;

        public ViewHolder(View root) {
            ButterKnife.inject(this, root);
        }
    }
}

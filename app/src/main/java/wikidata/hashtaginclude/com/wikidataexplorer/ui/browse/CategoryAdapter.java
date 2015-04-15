package wikidata.hashtaginclude.com.wikidataexplorer.ui.browse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataLog;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataUtility;
import wikidata.hashtaginclude.com.wikidataexplorer.api.WikidataLookup;
import wikidata.hashtaginclude.com.wikidataexplorer.models.ClaimQueryModel;
import wikidata.hashtaginclude.com.wikidataexplorer.models.LabelListResponseModel;
import wikidata.hashtaginclude.com.wikidataexplorer.models.SearchEntityResponseModel;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.entity.EntityActivity;

/**
 * Created by matthewmichaud on 3/23/15.
 */
public class CategoryAdapter extends ArrayAdapter<String> {

    private static final String TAG = "CategoryAdapter";

    public CategoryAdapter(Context context, List<String> objects) {
        super(context, R.layout.adapter_category, objects);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final String item = getItem(position);

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
                WikidataLookup.searchEntities(item, new Callback<SearchEntityResponseModel>() {
                    @Override
                    public void success(SearchEntityResponseModel searchEntityResponseModel, Response response) {
                        BrowseFragment.browseLoading.setVisibility(View.VISIBLE);
                        if(searchEntityResponseModel!=null) {
                            if(searchEntityResponseModel.getSearchModels().size()>0) {
                                final int id = Integer.parseInt(searchEntityResponseModel.getSearchModels().get(0).getId().replace("Q", ""));
                                WikidataLookup.queryClaim(id, 279, new Callback<ClaimQueryModel>() {
                                    @Override
                                    public void success(final ClaimQueryModel claimQueryModel, Response response) {
                                        if(claimQueryModel!=null) {
                                            // Surprisingly enough, this works
                                            if(claimQueryModel.getItems().length > 0) {
                                                final ArrayList<String> titles = new ArrayList<String>();
                                                for (int i = 0; i < claimQueryModel.getItems().length; i++) {
                                                    int id = claimQueryModel.getItems()[i];
                                                    WikidataLookup.getLabel("Q" + id, new Callback<String>() {
                                                        @Override
                                                        public void success(String s, Response response) {
                                                            titles.add(s);
                                                            if (titles.size() == claimQueryModel.getItems().length) {
                                                                BrowseFragment newFragment = BrowseFragment.newInstance(
                                                                        titles
                                                                );
                                                                ((ActionBarActivity)(getContext())).getSupportFragmentManager().beginTransaction().replace(R.id.container,
                                                                        newFragment).addToBackStack(null).commit();
                                                            }
                                                        }

                                                        @Override
                                                        public void failure(RetrofitError error) {
                                                            BrowseFragment.browseLoading.setVisibility(View.GONE);
                                                        }
                                                    });
                                                }
                                            } else {
                                                // we need to do something else here
                                                BrowseFragment.browseLoading.setVisibility(View.GONE);
                                                getTheThingsInstead("Q"+id);
                                            }
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        BrowseFragment.browseLoading.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        BrowseFragment.browseLoading.setVisibility(View.GONE);
                    }
                });
            }
        });
        return convertView;
    }

    private void setLoading(boolean loading) {
        if(loading) {
            BrowseFragment.browseLoading.setVisibility(View.VISIBLE);
        } else {
            BrowseFragment.browseLoading.setVisibility(View.GONE);
        }
    }

    private void getTheThingsInstead(String thing) {
        WikidataLookup.getEntities(thing,
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
                        WikidataUtility.makeCroutonText("Could not complete request", (Activity)getContext());
                    }
                }
        );
    }

    protected static class ViewHolder {
        @InjectView(R.id.category_list_text)
        Button categoryButton;

        public ViewHolder(View root) {
            ButterKnife.inject(this, root);
        }
    }
}

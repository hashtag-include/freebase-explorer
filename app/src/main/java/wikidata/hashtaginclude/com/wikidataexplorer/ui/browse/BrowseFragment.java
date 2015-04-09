package wikidata.hashtaginclude.com.wikidataexplorer.ui.browse;

import android.content.Intent;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataLog;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataUtility;
import wikidata.hashtaginclude.com.wikidataexplorer.api.WikidataLookup;
import wikidata.hashtaginclude.com.wikidataexplorer.models.ClaimQueryModel;
import wikidata.hashtaginclude.com.wikidataexplorer.models.SearchEntityResponseModel;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.entity.EntityActivity;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.query.QueryFragment;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.query.QueryResponseActivity;

public class BrowseFragment extends Fragment {

    private static final String TAG = "BrowseFragment";

    View root;
    GridView gridView;
    ProgressBar browseLoading;
    CategoryAdapter adapter;

    List<String> categories;

    public static BrowseFragment newInstance(ArrayList<String> items) {
        BrowseFragment fragment = new BrowseFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("items", items);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static BrowseFragment newInstance() {
        ArrayList<String> categories = new ArrayList<String>();
        categories.add("Animal");
        categories.add("Music");
        //categories.add("Books");
        categories.add("Media");
        categories.add("People");
        categories.add("Film");
        //categories.add("TV");
        categories.add("Business");
        //categories.add("Location");
        categories.add("Organization");
        categories.add("Biology");
        categories.add("Sports");
        //categories.add("Awards");
        categories.add("Education");
        //categories.add("Time");
        //categories.add("Government");
        //categories.add("Soccer");
        categories.add("Architecture");
        categories.add("Medicine");
        //categories.add("Video Games");
        //categories.add("Projects");
        //categories.add("Physical Geography");
        categories.add("Visual Art");

        BrowseFragment fragment = new BrowseFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("items", categories);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        categories = getArguments().getStringArrayList("items");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_browse, container, false);

        gridView = (GridView) root.findViewById(R.id.browse_gridview);
        browseLoading = (ProgressBar) root.findViewById(R.id.browse_loading);

        adapter = new CategoryAdapter(getActivity(), categories);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                WikidataLookup.searchEntities(categories.get(position), new Callback<SearchEntityResponseModel>() {
                    @Override
                    public void success(SearchEntityResponseModel searchEntityResponseModel, Response response) {
                        browseLoading.setVisibility(View.VISIBLE);
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
                                                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,
                                                                        newFragment).addToBackStack(null).commit();
                                                            }
                                                        }

                                                        @Override
                                                        public void failure(RetrofitError error) {
                                                            browseLoading.setVisibility(View.GONE);
                                                        }
                                                    });
                                                }
                                            } else {
                                                // we need to do something else here
                                                browseLoading.setVisibility(View.GONE);
                                                getTheThingsInstead("Q"+id);
                                            }
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        browseLoading.setVisibility(View.GONE);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        browseLoading.setVisibility(View.GONE);
                    }
                });

            }
        });

        return root;
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
                            Intent intent = new Intent(BrowseFragment.this.getActivity(), EntityActivity.class);
                            intent.putExtra("responses", responses);
                            BrowseFragment.this.getActivity().startActivity(intent);
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        WikidataLog.e(TAG, "Failed to get entities", error);
                        WikidataUtility.makeCroutonText("Could not complete request", BrowseFragment.this.getActivity());
                    }
                }
        );
    }
}
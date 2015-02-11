package wikidata.hashtaginclude.com.wikidataexplorer.ui.query;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataLog;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataUtility;
import wikidata.hashtaginclude.com.wikidataexplorer.api.WikidataLookup;
import wikidata.hashtaginclude.com.wikidataexplorer.models.SearchEntityResponseModel;

public class QueryFragment extends Fragment implements View.OnClickListener {

    private final static String TAG = "QueryFragment";

    View root;
    Spinner actionSpinner;
    LinearLayout actionsExpanded;
    Button submitButton;
    List<View> paramViews;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_query, container, false);

        paramViews = new ArrayList<View>();

        submitButton = (Button) root.findViewById(R.id.query_submit_button);
        submitButton.setOnClickListener(this);

        actionsExpanded = (LinearLayout) root.findViewById(R.id.query_actions_expanded);

        actionSpinner = (Spinner) root.findViewById(R.id.query_actions);
        actionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: // default
                        showDefault();
                        break;
                    case 1: // wbgetentities
                        showGetEntities();
                        break;
                    case 2: // wbsearchentities
                        showSearchEntities();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return root;
    }

    private void showDefault() {
        actionsExpanded.removeAllViews();
        submitButton.setVisibility(View.GONE);
    }

    private void showGetEntities() {
        showParams(R.array.wbgetentities_params, R.array.wbgetentities_defaults);
    }

    private void showSearchEntities() {
        showParams(R.array.wbsearchentities_params, R.array.wbsearchentities_defaults);
    }

    private void showParams(int paramsResource, int defaultsResource) {
        actionsExpanded.removeAllViews();
        submitButton.setVisibility(View.VISIBLE);
        paramViews.clear();

        String[] params = getResources().getStringArray(paramsResource);
        String[] defaults = getResources().getStringArray(defaultsResource);
        for(int i = 0; i < params.length; i++) {
            String param = params[i];
            String dflt = defaults[i];
            TextView paramText = new TextView(this.getActivity());
            paramText.setText(param);
            paramText.setTextSize(14);

            EditText paramEdit = new EditText(this.getActivity());
            paramEdit.setTextSize(12);
            paramEdit.setHint(dflt);
            LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            editParams.setMargins((int)WikidataUtility.dpToPx(16, getActivity()), 0, 0, 0);
            paramEdit.setLayoutParams(editParams);

            LinearLayout linearLayout = new LinearLayout(this.getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, (int)WikidataUtility.dpToPx(4, getActivity()), 0, (int)WikidataUtility.dpToPx(4, getActivity()));
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setBackgroundColor(Color.WHITE);
            linearLayout.setPadding(
                    (int)WikidataUtility.dpToPx(8, getActivity()),
                    (int)WikidataUtility.dpToPx(8, getActivity()),
                    (int)WikidataUtility.dpToPx(8, getActivity()),
                    (int)WikidataUtility.dpToPx(8, getActivity()));

            linearLayout.addView(paramText);
            linearLayout.addView(paramEdit);

            paramViews.add(paramEdit);
            actionsExpanded.addView(linearLayout);
        }
    }

    @Override
    public void onClick(View v) {
        if(submitButton.getVisibility()==View.VISIBLE) {
            switch(actionSpinner.getSelectedItemPosition()) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    String search = ((EditText)paramViews.get(0)).getText().toString();
                    if(search==null || search.length()==0) {
                        search = ((EditText)paramViews.get(0)).getHint().toString();
                    }
                    String language = ((EditText)paramViews.get(1)).getText().toString();
                    if(language==null || language.length()==0) {
                        language = ((EditText)paramViews.get(1)).getHint().toString();
                    }
                    String type = ((EditText)paramViews.get(2)).getText().toString();
                    if(type==null || type.length()==0) {
                        type = ((EditText)paramViews.get(2)).getHint().toString();
                    }
                    String limitString = ((EditText)paramViews.get(3)).getText().toString();
                    if(limitString==null || limitString.length()==0) {
                        limitString = ((EditText)paramViews.get(3)).getHint().toString();
                    }
                    String continueString = ((EditText)paramViews.get(4)).getText().toString();
                    if(continueString==null || continueString.length()==0) {
                        continueString = ((EditText)paramViews.get(4)).getHint().toString();
                    }
                    int limit = Integer.parseInt(limitString);
                    int continueQuery = Integer.parseInt(continueString);
                    WikidataLookup.searchEntities(
                        search, language, type, limit, continueQuery,
                            new Callback<SearchEntityResponseModel>() {
                                @Override
                                public void success(SearchEntityResponseModel searchEntityResponseModel, Response response) {
                                    if(searchEntityResponseModel!=null) {
                                        // launch a new activity with the model
                                        Intent intent = new Intent(QueryFragment.this.getActivity(), SearchEntitiesActivity.class);
                                        intent.putExtra("responseModel", searchEntityResponseModel);
                                        QueryFragment.this.getActivity().startActivity(intent);
                                    }
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    WikidataLog.e(TAG, "Failed to search entities", error);
                                    WikidataUtility.makeCroutonText("Could not complete request", getActivity());
                                }
                            }
                    );
                    break;
            }
        }
    }
}
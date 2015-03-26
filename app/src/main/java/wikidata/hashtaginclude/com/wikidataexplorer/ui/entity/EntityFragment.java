package wikidata.hashtaginclude.com.wikidataexplorer.ui.entity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataLog;
import wikidata.hashtaginclude.com.wikidataexplorer.api.WikidataLookup;
import wikidata.hashtaginclude.com.wikidataexplorer.models.ClaimModel;
import wikidata.hashtaginclude.com.wikidataexplorer.models.EntityModel;

/**
 * Created by matthewmichaud on 2/17/15.
 */
public class EntityFragment extends Fragment {

    private static final String TAG = "EntityFragment";

    View root;
    @InjectView(R.id.entity_claim_list)
    LinearLayout claimsList;

    String content;

    EntityModel entityModel;

    public static interface OnDataChangedListener {
        public void onDataChanged(Object model);
    }

    public static EntityFragment newInstance(String content) {
        EntityFragment fragment = new EntityFragment();
        Bundle args = new Bundle();
        args.putString("content", content);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        content = this.getArguments().getString("content");
        try {
            JSONObject jsonObj = new JSONObject(content);

            entityModel = EntityModel.parse(jsonObj);

            ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(entityModel.getTitle());

            if(entityModel.getLabels().length > 0) {
                String subtitle = entityModel.getLabels()[0].getValue();
                for(int i = 1; i < entityModel.getLabels().length; i++) {
                    subtitle+=", ";
                    subtitle+=entityModel.getLabels()[i].getValue();
                }
                ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(subtitle);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_entity_single, container, false);
        ButterKnife.inject(this, root);

        if(entityModel!=null) {
            for(ClaimModel claimModel : entityModel.getClaimModels()) {
                TextView textView = new TextView(getActivity());
                textView.setTag(claimModel);
                String value = "";
                if(claimModel.getMainsnak().getDatatype().equals(ClaimModel.DataType.QUANTITY)) {
                    value = ((ClaimModel.DataValueQuantity)claimModel.getMainsnak().getDataValue()).getAmount();
                } else if(claimModel.getMainsnak().getDatatype().equals(ClaimModel.DataType.TIME)) {
                    value = ((ClaimModel.DataValueTime)claimModel.getMainsnak().getDataValue()).getTime();
                } else if(claimModel.getMainsnak().getDatatype().equals(ClaimModel.DataType.WIKIITEM)) {
                    value = ((ClaimModel.DataValueWikibaseItem)claimModel.getMainsnak().getDataValue()).getNumericText();
                } else if(claimModel.getMainsnak().getDatatype().equals(ClaimModel.DataType.VALUE)) {
                    value = ((ClaimModel.DataValueValue)claimModel.getMainsnak().getDataValue()).getValue();
                }
                textView.setText(
                        claimModel.getMainsnak().getPropertyText()+": "+value
                );
                claimsList.addView(textView);
            }
        }


        return root;
    }
}

package wikidata.hashtaginclude.com.wikidataexplorer.ui.entity;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

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
import wikidata.hashtaginclude.com.wikidataexplorer.models.ClaimModel;
import wikidata.hashtaginclude.com.wikidataexplorer.models.EntityModel;
import wikidata.hashtaginclude.com.wikidataexplorer.models.ValueLanguageModel;

/**
 * Created by matthewmichaud on 2/17/15.
 */
public class EntityFragment extends Fragment {

    private static final String TAG = "EntityFragment";

    View root;

    @InjectView(R.id.entity_aliases_list)
    LinearLayout aliasesList;
    @InjectView(R.id.entity_aliases)
    LinearLayout aliases;
    @InjectView(R.id.entity_aliases_arrow)
    ImageView aliasesArrow;

    @InjectView(R.id.entity_labels_list)
    LinearLayout labelsList;
    @InjectView(R.id.entity_labels)
    LinearLayout labels;
    @InjectView(R.id.entity_labels_arrow)
    ImageView labelsArrow;

    @InjectView(R.id.entity_desc_list)
    LinearLayout descList;
    @InjectView(R.id.entity_desc)
    LinearLayout desc;
    @InjectView(R.id.entity_desc_arrow)
    ImageView descArrow;

    @InjectView(R.id.entity_claim_list)
    LinearLayout claimsList;
    @InjectView(R.id.entity_claims)
    LinearLayout claims;
    @InjectView(R.id.entity_claim_arrow)
    ImageView claimsArrow;

    String content;

    EntityModel entityModel;

    public static Bus bus;
    {
        bus = new Bus();
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

            ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(entityModel.getTitle());

            if(entityModel.getLabels().length > 0) {
                String subtitle = entityModel.getLabels()[0].getValue();
                for(int i = 1; i < entityModel.getLabels().length; i++) {
                    subtitle+=", ";
                    subtitle+=entityModel.getLabels()[i].getValue();
                }
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(subtitle);
            } else {
                ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(entityModel.getTitle());
                ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle("");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            bus.unregister(this);
        } catch (IllegalArgumentException exception) {
            WikidataLog.e(TAG, "Ugh, who cares?", exception);
        }
    }

    @Override
    public void onStart() {
        super.onStop();
        bus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_entity_single, container, false);
        ButterKnife.inject(this, root);

        redrawUI();

        aliases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(aliasesList.getVisibility()==View.GONE) {
                    aliasesList.setVisibility(View.VISIBLE);
                    aliasesArrow.setImageResource(R.drawable.ic_hardware_keyboard_arrow_down);
                } else{
                    aliasesList.setVisibility(View.GONE);
                    aliasesArrow.setImageResource(R.drawable.ic_hardware_keyboard_arrow_right);
                }
            }
        });

        labels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(labelsList.getVisibility()==View.GONE) {
                    labelsList.setVisibility(View.VISIBLE);
                    labelsArrow.setImageResource(R.drawable.ic_hardware_keyboard_arrow_down);
                } else{
                    labelsList.setVisibility(View.GONE);
                    labelsArrow.setImageResource(R.drawable.ic_hardware_keyboard_arrow_right);
                }
            }
        });

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(descList.getVisibility()==View.GONE) {
                    descList.setVisibility(View.VISIBLE);
                    descArrow.setImageResource(R.drawable.ic_hardware_keyboard_arrow_down);
                } else{
                    descList.setVisibility(View.GONE);
                    descArrow.setImageResource(R.drawable.ic_hardware_keyboard_arrow_right);
                }
            }
        });

        claims.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(claimsList.getVisibility()==View.GONE) {
                    claimsList.setVisibility(View.VISIBLE);
                    claimsArrow.setImageResource(R.drawable.ic_hardware_keyboard_arrow_down);
                } else{
                    claimsList.setVisibility(View.GONE);
                    claimsArrow.setImageResource(R.drawable.ic_hardware_keyboard_arrow_right);
                }
            }
        });

        return root;
    }

    private void redrawUI() {

        redrawAliases();
        if(entityModel.getAliases().length==0) {
            aliasesList.setVisibility(View.GONE);
            aliases.setVisibility(View.GONE);
        }
        redrawLabels();
        if(entityModel.getLabels().length==0) {
            labelsList.setVisibility(View.GONE);
            labels.setVisibility(View.GONE);
        }
        redrawDesc();
        if(entityModel.getDescriptions().length==0) {
            descList.setVisibility(View.GONE);
            desc.setVisibility(View.GONE);
        }
        redrawClaims();
        if(entityModel.getClaimModels().length==0) {
            claimsList.setVisibility(View.GONE);
            claims.setVisibility(View.GONE);
        }
    }

    private void redrawAliases() {
        aliasesList.removeAllViews();

        if(entityModel!=null) {
            for(ValueLanguageModel[] models : entityModel.getAliases()) {
                for(ValueLanguageModel model : models) {
                    TextView textView = new TextView(getActivity());
                    textView.setTag(model);

                    textView.setTextSize(18);
                    textView.setTextColor(Color.DKGRAY);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.setMargins((int)WikidataUtility.dpToPx(16, getActivity()), (int)WikidataUtility.dpToPx(8, getActivity()),
                            (int)WikidataUtility.dpToPx(16, getActivity()), (int)WikidataUtility.dpToPx(8, getActivity()));

                    SpannableString spannedText=new SpannableString(model.getLanguage()+": "+model.getValue());
                    spannedText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                            0, model.getLanguage().length() + 1, 0);

                    textView.setText(spannedText);

                    aliasesList.addView(textView, params);
                    View view = new View(getActivity());
                    view.setBackgroundColor(Color.LTGRAY);
                    aliasesList.addView(view, new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                            (int) WikidataUtility.dpToPx(1, getActivity())));
                }
            }
        }
    }

    private void redrawLabels() {
        labelsList.removeAllViews();

        if(entityModel!=null) {
            for(ValueLanguageModel model : entityModel.getLabels()) {
                TextView textView = new TextView(getActivity());
                textView.setTag(model);

                textView.setTextSize(18);
                textView.setTextColor(Color.DKGRAY);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins((int)WikidataUtility.dpToPx(16, getActivity()), (int)WikidataUtility.dpToPx(8, getActivity()),
                            (int)WikidataUtility.dpToPx(16, getActivity()), (int)WikidataUtility.dpToPx(8, getActivity()));

                SpannableString spannedText=new SpannableString(model.getLanguage()+": "+model.getValue());
                spannedText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                        0, model.getLanguage().length() + 1, 0);
                textView.setText(spannedText);

                labelsList.addView(textView, params);
                View view = new View(getActivity());
                view.setBackgroundColor(Color.LTGRAY);
                labelsList.addView(view, new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                        (int) WikidataUtility.dpToPx(1, getActivity())));
            }
        }
    }

    private void redrawDesc() {
        descList.removeAllViews();

        if(entityModel!=null) {
            for(ValueLanguageModel model : entityModel.getDescriptions()) {
                TextView textView = new TextView(getActivity());
                textView.setTag(model);

                textView.setTextSize(18);
                textView.setTextColor(Color.DKGRAY);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins((int)WikidataUtility.dpToPx(16, getActivity()), (int)WikidataUtility.dpToPx(8, getActivity()),
                        (int)WikidataUtility.dpToPx(16, getActivity()), (int)WikidataUtility.dpToPx(8, getActivity()));

                SpannableString spannedText=new SpannableString(model.getLanguage()+": "+model.getValue());
                spannedText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                        0, model.getLanguage().length() + 1, 0);
                textView.setText(spannedText);

                descList.addView(textView, params);
                View view = new View(getActivity());
                view.setBackgroundColor(Color.LTGRAY);
                descList.addView(view, new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                        (int) WikidataUtility.dpToPx(1, getActivity())));
            }
        }
    }

    private void redrawClaims() {
        claimsList.removeAllViews();

        if(entityModel!=null) {
            for(ClaimModel claimModel : entityModel.getClaimModels()) {
                TextView textView = new TextView(getActivity());
                textView.setTag(claimModel);
                textView.setTag(claimModel.getMainsnak());
                textView.setTag(claimModel.getMainsnak().getDataValue());
                String value = "";
                if(claimModel.getMainsnak().getDatatype().equals(ClaimModel.DataType.QUANTITY)) {
                    value = ((ClaimModel.DataValueQuantity)claimModel.getMainsnak().getDataValue()).getAmount();
                } else if(claimModel.getMainsnak().getDatatype().equals(ClaimModel.DataType.TIME)) {
                    value = ((ClaimModel.DataValueTime)claimModel.getMainsnak().getDataValue()).getTime();
                } else if(claimModel.getMainsnak().getDatatype().equals(ClaimModel.DataType.WIKIITEM)) {
                    value = ((ClaimModel.DataValueWikibaseItem)claimModel.getMainsnak().getDataValue()).getNumericText();
                } else if(claimModel.getMainsnak().getDatatype().equals(ClaimModel.DataType.VALUE) ||
                        claimModel.getMainsnak().getDatatype().equals(ClaimModel.DataType.STRING) ||
                        claimModel.getMainsnak().getDatatype().equals(ClaimModel.DataType.URL)) {
                    value = ((ClaimModel.DataValueValue)claimModel.getMainsnak().getDataValue()).getValue();
                }

                textView.setTextSize(18);
                textView.setTextColor(Color.DKGRAY);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins((int)WikidataUtility.dpToPx(16, getActivity()), (int)WikidataUtility.dpToPx(8, getActivity()),
                        (int)WikidataUtility.dpToPx(16, getActivity()), (int)WikidataUtility.dpToPx(8, getActivity()));

                SpannableString spannedText=new SpannableString(claimModel.getMainsnak().getPropertyText()+": "+value);
                spannedText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                        0, claimModel.getMainsnak().getPropertyText().length() + 1, 0);

                textView.setAutoLinkMask(Linkify.WEB_URLS);
                textView.setText(spannedText);

                claimsList.addView(textView, params);
                View view = new View(getActivity());
                view.setBackgroundColor(Color.LTGRAY);
                claimsList.addView(view, new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
                        (int) WikidataUtility.dpToPx(1, getActivity())));
            }
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onDataChanged(DataChangedEvent event) {
        redrawClaims();
    }

    public static class DataChangedEvent {
        Object changedDataValue;

        public DataChangedEvent(Object changedDataValue) {
            this.changedDataValue = changedDataValue;
        }
    }
}

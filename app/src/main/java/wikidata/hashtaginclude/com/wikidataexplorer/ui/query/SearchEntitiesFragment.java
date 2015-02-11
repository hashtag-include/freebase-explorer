package wikidata.hashtaginclude.com.wikidataexplorer.ui.query;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.models.SearchEntityResponseModel;

/**
 * Created by matthewmichaud on 2/10/15.
 */
public class SearchEntitiesFragment extends Fragment {

    SearchEntityResponseModel model;

    View root;

    @InjectView(R.id.search_entities_layout)
    LinearLayout linearLayout;
    @InjectView(R.id.search_entities_list)
    ListView listView;

    public static SearchEntitiesFragment newInstance(SearchEntityResponseModel model) {
        SearchEntitiesFragment fragment = new SearchEntitiesFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("responseModel", model);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = (SearchEntityResponseModel) getArguments().getSerializable("responseModel");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search_entities, container, false);
        ButterKnife.inject(this, root);

        SearchEntitiesAdapter adapter = new SearchEntitiesAdapter(getActivity(), model.getSearchModels());
        listView.setAdapter(adapter);

        return root;
    }
}

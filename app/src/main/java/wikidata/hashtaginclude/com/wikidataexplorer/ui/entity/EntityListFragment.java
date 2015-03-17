package wikidata.hashtaginclude.com.wikidataexplorer.ui.entity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.query.SearchEntitiesAdapter;

/**
 * Created by matthewmichaud on 2/22/15.
 */
public class EntityListFragment extends Fragment {
    private static final String TAG = "[EntityListFragment]";

    View root;

    @InjectView(R.id.entity_list)
    ListView entityList;

    ArrayList<String> responses;

    public static EntityListFragment newInstance(ArrayList<String> responses) {
        EntityListFragment fragment = new EntityListFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("responses", responses);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        responses = this.getArguments().getStringArrayList("responses");
    }

    @Override
    public void onResume() {
        super.onResume();

        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Showing "+responses.size()
                + (responses.size() == 1 ? " entity" : " entities"));
        ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle("");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_entity_list, container, false);
        ButterKnife.inject(this, root);

        ArrayList<String> idList = new ArrayList<String>();
        for(String response : responses) {
            JsonElement element = new JsonParser().parse(response); // Error line
            idList.add(element.getAsJsonObject().getAsJsonPrimitive("id").getAsString());
        }

        EntityListAdapter adapter = new EntityListAdapter(getActivity(), idList);
        entityList.setAdapter(adapter);

        entityList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, EntityFragment.newInstance(responses.get(position)))
                        .addToBackStack("entity").commit();
            }
        });
        return root;
    }
}

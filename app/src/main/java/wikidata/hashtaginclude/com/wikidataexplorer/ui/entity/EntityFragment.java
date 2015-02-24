package wikidata.hashtaginclude.com.wikidataexplorer.ui.entity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import wikidata.hashtaginclude.com.wikidataexplorer.R;

/**
 * Created by matthewmichaud on 2/17/15.
 */
public class EntityFragment extends Fragment {

    private static final String TAG = "[EntityFragment]";

    View root;

    String content;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_entity_single, container, false);
        ButterKnife.inject(this, root);



        return root;
    }
}

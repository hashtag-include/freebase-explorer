package wikidata.hashtaginclude.com.wikidataexplorer.ui.recent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wikidata.hashtaginclude.com.wikidataexplorer.R;
import wikidata.hashtaginclude.com.wikidataexplorer.api.WikidataLookup;
import wikidata.hashtaginclude.com.wikidataexplorer.models.RecentItemModel;
import wikidata.hashtaginclude.com.wikidataexplorer.models.RecentResponseModel;

/**
 * Created by matthewmichaud on 1/14/15.
 */
public class RecentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        AbsListView.OnScrollListener, AdapterView.OnItemClickListener {

    private static final String TAG = "RecentFragment";

    View root;
    RecentAdapter adapter;
    ArrayList<RecentItemModel> recentItemModels;
    ListView recentList;
    SwipeRefreshLayout refreshLayout;
    TextView noContentText;
    Style croutonStyle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recentItemModels = new ArrayList<RecentItemModel>();
        if(savedInstanceState==null) {
            ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Recently Changed");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_recent, container, false);

        refreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.recent_swipe_refresh);
        refreshLayout.setOnRefreshListener(this);

        noContentText = (TextView) root.findViewById(R.id.recent_no_content);

        recentList = (ListView) root.findViewById(R.id.recent_list);
        recentList.setOnScrollListener(this);
        recentList.setOnItemClickListener(this);

        refreshLayout.setRefreshing(true);

        croutonStyle = new Style.Builder().setBackgroundColor(R.color.app_primary_dark).setTextColor(android.R.color.white).build();

        getLatestUpdates();

        return root;
    }

    private void getLatestUpdates() {
        WikidataLookup.getRecent(new Callback<RecentResponseModel>() {
            @Override
            public void success(RecentResponseModel recentResponseModel, Response response) {
                if(noContentText.getVisibility()==View.VISIBLE) {
                    noContentText.setVisibility(View.GONE);
                }
                if (recentResponseModel != null) {
                    for (RecentItemModel model : (ArrayList<RecentItemModel>) recentResponseModel.getQuery().getRecentChanges()) {
                        if (!recentItemModels.contains(model)) {
                            recentItemModels.add(0, model);
                        }
                    }
                    adapter = new RecentAdapter(RecentFragment.this.getActivity(), recentItemModels);
                    recentList.setAdapter(adapter);

                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                refreshLayout.setRefreshing(false);
                Crouton.makeText(getActivity(), "Could not refresh recent updates", croutonStyle, R.id.crouton_handle).show();
                if(recentItemModels.size()==0) {
                    noContentText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getEarlierUpdates() {
        String time = recentItemModels.get(recentItemModels.size()-1).getTimeStamp();
        WikidataLookup.getRecent(time, new Callback<RecentResponseModel>() {
            @Override
            public void success(RecentResponseModel recentResponseModel, Response response) {
                if (recentResponseModel != null) {
                    for (RecentItemModel model : (ArrayList<RecentItemModel>) recentResponseModel.getQuery().getRecentChanges()) {
                        if (!recentItemModels.contains(model)) {
                            recentItemModels.add(model);
                        }
                    }
                    if(adapter==null) {
                        adapter = new RecentAdapter(RecentFragment.this.getActivity(), recentItemModels);
                        recentList.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        getLatestUpdates();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private int preLast;
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        final int lastItem = firstVisibleItem + visibleItemCount;
        if(lastItem == totalItemCount) {
            if(preLast!=lastItem) { //to avoid multiple calls for last item
                preLast = lastItem;
                getEarlierUpdates();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LinearLayout recentContentHidden = (LinearLayout) view.findViewById(R.id.recent_content_hidden);
        ImageView arrow = (ImageView) view.findViewById(R.id.recent_item_arrow);
        if(recentContentHidden.getVisibility()==View.GONE) {
            expand(recentContentHidden);
            arrow.setImageResource(R.drawable.ic_hardware_keyboard_arrow_down);
        } else {
            collapse(recentContentHidden);
            arrow.setImageResource(R.drawable.ic_hardware_keyboard_arrow_right);
        }
    }

    private void expand(final View view) {
        view.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = view.getMeasuredHeight()/2;
        view.getLayoutParams().height = 0;
        view.setVisibility(View.VISIBLE);

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                view.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration(200);
        animation.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animation);
    }

    private void collapse(final View view) {
        final int initialHeight = view.getMeasuredHeight();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration(200);
        animation.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(animation);
    }
}

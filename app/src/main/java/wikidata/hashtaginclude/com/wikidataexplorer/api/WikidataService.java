package wikidata.hashtaginclude.com.wikidataexplorer.api;

import com.google.gson.JsonElement;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import wikidata.hashtaginclude.com.wikidataexplorer.models.RecentResponseModel;

/**
 * Created by matthewmichaud on 1/17/15.
 */
public interface WikidataService {
    @GET("/w/api.php")
    void getRecent(@Query("action") String action, @Query("list") String list, @Query("format") String format, @Query("rclimit") int rclimit, @Query("rcprop") String rcprop, Callback<RecentResponseModel> callback);

    @GET("/w/api.php")
    void getRecent(@Query("action") String action, @Query("list") String list, @Query("format") String format, @Query("rclimit") int rclimit, @Query("rcstart") String rcstart, @Query("rcprop") String rcprop, Callback<RecentResponseModel> callback);

    @GET("/w/api.php")
    void getEntity(@Query("action") String action, @Query("ids") String id, @Query("uselang") String useLang, @Query("format") String format, Callback<JsonElement> callback);
}

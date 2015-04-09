package wikidata.hashtaginclude.com.wikidataexplorer.api;

import com.google.gson.JsonElement;

import retrofit.Callback;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Query;
import retrofit.mime.TypedString;
import wikidata.hashtaginclude.com.wikidataexplorer.models.ClaimQueryModel;

/**
 * Created by mmichaud on 4/3/15.
 */
public interface WikidataQueryService {
    @GET("/api")
    void queryClaim(@Query(value = "q", encodeName=false, encodeValue = false) String q, Callback<ClaimQueryModel> callback);
}

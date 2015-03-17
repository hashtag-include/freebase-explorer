package wikidata.hashtaginclude.com.wikidataexplorer.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataLog;
import wikidata.hashtaginclude.com.wikidataexplorer.api.WikidataLookup;

/**
 * Created by matthewmichaud on 2/26/15.
 */
public class EntityModel {

    private static final String TAG = "EntityModel";

    int pageid;
    int ns;
    String title;
    int lastrevid;
    String modified;
    String id;
    String type;
    ValueLanguageModel[][] aliases;
    ValueLanguageModel[] labels;
    ValueLanguageModel[] descriptions;
    ClaimModel[] claimModels;

    public static EntityModel parse(JSONObject entityJSON) throws JSONException {
        EntityModel entityModel = new EntityModel();

        entityModel.setPageid(entityJSON.getInt("pageid"));
        entityModel.setNs(entityJSON.getInt("ns"));
        entityModel.setTitle(entityJSON.getString("title"));
        entityModel.setLastrevid(entityJSON.getInt("lastrevid"));
        entityModel.setModified(entityJSON.getString("modified"));
        entityModel.setId(entityJSON.getString("id"));
        entityModel.setType(entityJSON.getString("type")); 

        int i = 0;

        JSONObject aliasesJSON = entityJSON.getJSONObject("aliases");
        ValueLanguageModel[][] aliases = new ValueLanguageModel[aliasesJSON.length()][];
        i = 0;
        Iterator<String> aliasesIterator = aliasesJSON.keys();
        while(aliasesIterator.hasNext()) {
            String name = aliasesIterator.next();
            JSONArray nestedAliasJSON = aliasesJSON.getJSONArray(name);
            ValueLanguageModel[] nestedAliases = new ValueLanguageModel[nestedAliasJSON.length()];
            for(int r = 0; r < nestedAliasJSON.length(); r++) {
                JSONObject alias = nestedAliasJSON.getJSONObject(r);
                ValueLanguageModel valueLanguageModel = new ValueLanguageModel(
                        alias.getString("language"),
                        alias.getString("value")
                );
                nestedAliases[r] = valueLanguageModel;
            }

            aliases[i++] = nestedAliases;
        }
        entityModel.setAliases(aliases);

        JSONObject labelsJSON = entityJSON.getJSONObject("labels");
        ValueLanguageModel[] labels = new ValueLanguageModel[labelsJSON.length()];
        i = 0;
        Iterator<String> labelsIterator = labelsJSON.keys();
        while(labelsIterator.hasNext()) {
            String name = labelsIterator.next();
            ValueLanguageModel valueLanguageModel = new ValueLanguageModel(
                    labelsJSON.getJSONObject(name).getString("language"),
                    labelsJSON.getJSONObject(name).getString("value")
            );
            labels[i++] = valueLanguageModel;
        }
        entityModel.setLabels(labels);

        JSONObject descriptionsJSON = entityJSON.getJSONObject("descriptions");
        ValueLanguageModel[] descriptions = new ValueLanguageModel[descriptionsJSON.length()];
        i = 0;
        Iterator<String> descriptionIterator = descriptionsJSON.keys();
        while(descriptionIterator.hasNext()) {
            String name = descriptionIterator.next();
            ValueLanguageModel valueLanguageModel = new ValueLanguageModel(
                    descriptionsJSON.getJSONObject(name).getString("language"),
                    descriptionsJSON.getJSONObject(name).getString("value")
            );
            descriptions[i++] = valueLanguageModel;
        }
        entityModel.setDescriptions(descriptions);

        JSONArray claims = entityJSON.getJSONArray("claims");
        ClaimModel[] claimModels = new ClaimModel[claims.length()];
        for(int r = 0; r < claims.length(); r++) {
            ClaimModel claim = ClaimModel.parse(claims.getJSONObject(r));
            claimModels[r] = claim;



            if(claim.getMainsnak().getDatatype().equals("wikibase-item")) {
                WikidataLookup.getProperty(claim.getMainsnak().getProperty(), new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        WikidataLog.d(TAG, "property: " + s);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        WikidataLog.e(TAG, "could not get property", error);
                    }
                });

                WikidataLookup.getLabel("Q" + claim.getMainsnak().getDataValue().getValue().getNumericId(), new Callback<String>() {
                    @Override
                    public void success(String label, Response response) {
                        if (label != null) {
                            WikidataLog.d(TAG, "propertyid = " + label);
                        } else {
                            WikidataLog.e(TAG, "response was null");
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        WikidataLog.e(TAG, "Could not get propertyid", error);
                    }
                });
            }
        }
        entityModel.setClaimModels(claimModels);
        return entityModel;
    }

    public int getPageid() {
        return pageid;
    }

    public void setPageid(int pageid) {
        this.pageid = pageid;
    }

    public int getNs() {
        return ns;
    }

    public void setNs(int ns) {
        this.ns = ns;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLastrevid() {
        return lastrevid;
    }

    public void setLastrevid(int lastrevid) {
        this.lastrevid = lastrevid;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ValueLanguageModel[][] getAliases() {
        return aliases;
    }

    public void setAliases(ValueLanguageModel[][] aliases) {
        this.aliases = aliases;
    }

    public ValueLanguageModel[] getLabels() {
        return labels;
    }

    public void setLabels(ValueLanguageModel[] labels) {
        this.labels = labels;
    }

    public ValueLanguageModel[] getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(ValueLanguageModel[] descriptions) {
        this.descriptions = descriptions;
    }

    public ClaimModel[] getClaimModels() {
        return claimModels;
    }

    public void setClaimModels(ClaimModel[] claimModels) {
        this.claimModels = claimModels;
    }
}

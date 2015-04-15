package wikidata.hashtaginclude.com.wikidataexplorer.models;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import wikidata.hashtaginclude.com.wikidataexplorer.WikidataLog;
import wikidata.hashtaginclude.com.wikidataexplorer.api.WikidataLookup;
import wikidata.hashtaginclude.com.wikidataexplorer.ui.entity.EntityFragment;

/**
 * Created by matthewmichaud on 3/3/15.
 */
public class ClaimModel {
    String id;
    Mainsnak mainsnak;
    String type;
    String rank;
    References references;

    public enum DataType {
        EMPTY,
        VALUE,
        URL,
        STRING,
        QUANTITY,
        WIKIITEM,
        TIME
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Mainsnak getMainsnak() {
        return mainsnak;
    }

    public void setMainsnak(Mainsnak mainsnak) {
        this.mainsnak = mainsnak;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public ClaimModel(String id, Mainsnak mainsnak, String type, String rank, References references) {
        this.id = id;
        this.mainsnak = mainsnak;
        this.type = type;
        this.rank = rank;
        this.references = references;
    }

    public static ClaimModel parse(JSONObject claimJSON) throws JSONException {

        String id = claimJSON.getString("id");
        String type = claimJSON.getString("type");
        String rank = claimJSON.getString("rank");

        Mainsnak mainsnak = Mainsnak.parse(claimJSON.getJSONObject("mainsnak"));
        References references = null;
        if(claimJSON.has("references")) {
            references = References.parse(claimJSON.getJSONArray("references"));
        }

        return new ClaimModel(
                id,
                mainsnak,
                type,
                rank,
                references);
    }

    public static class DataValue {
        String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    public static class DataValueValue extends DataValue {
        String value;

        public DataValueValue() {
        }

        public DataValueValue(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public static DataValueValue parse(JSONObject dataValue) throws JSONException {
            String type = dataValue.getString("type");
            if(type.equals("string") ||
                    type.equals("url") ||
                    type.equals("commonsMedia")) {
                String value = dataValue.getString("value");
                return new DataValueValue(
                        value,
                        type
                );
            }
            return null;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

    }
    public static class DataValueTime extends  DataValue {
        String time;
        int timezone;
        int before;
        int after;
        int precision;
        String calendarModel;

        public DataValueTime() {
        }

        public DataValueTime(String time, int timezone, int before, int after, int precision, String calendarModel, String type) {
            this.time = time;
            this.timezone = timezone;
            this.before = before;
            this.after = after;
            this.precision = precision;
            this.calendarModel = calendarModel;
            this.type = type;
        }

        public static DataValueTime parse(JSONObject dataValue) throws JSONException {
            String type = dataValue.getString("type");
            if(type.equals("time")) {
                JSONObject value = dataValue.getJSONObject("value");
                return new DataValueTime(
                        value.getString("time"),
                        value.getInt("timezone"),
                        value.getInt("before"),
                        value.getInt("after"),
                        value.getInt("precision"),
                        value.getString("calendarmodel"),
                        type
                );
            }
            return null;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getTimezone() {
            return timezone;
        }

        public void setTimezone(int timezone) {
            this.timezone = timezone;
        }

        public int getBefore() {
            return before;
        }

        public void setBefore(int before) {
            this.before = before;
        }

        public int getAfter() {
            return after;
        }

        public void setAfter(int after) {
            this.after = after;
        }

        public int getPrecision() {
            return precision;
        }

        public void setPrecision(int precision) {
            this.precision = precision;
        }

        public String getCalendarModel() {
            return calendarModel;
        }

        public void setCalendarModel(String calendarModel) {
            this.calendarModel = calendarModel;
        }
    }
    public static class DataValueQuantity extends DataValue {
        String amount;
        String unit;
        String upperBound;
        String lowerBound;

        public DataValueQuantity() {
        }

        public DataValueQuantity(String amount, String unit, String upperBound, String lowerBound, String type) {
            this.amount = amount;
            this.unit = unit;
            this.upperBound = upperBound;
            this.lowerBound = lowerBound;
            this.type = type;
        }

        public static DataValueQuantity parse(JSONObject dataValue) throws JSONException {
            String type = dataValue.getString("type");
            if(type.equals("quantity")) {
                JSONObject value = dataValue.getJSONObject("value");
                return new DataValueQuantity(
                        value.getString("amount"),
                        value.getString("unit"),
                        value.getString("upperBound"),
                        value.getString("lowerBound"),
                        type
                );
            }
            return null;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getUpperBound() {
            return upperBound;
        }

        public void setUpperBound(String upperBound) {
            this.upperBound = upperBound;
        }

        public String getLowerBound() {
            return lowerBound;
        }

        public void setLowerBound(String lowerBound) {
            this.lowerBound = lowerBound;
        }
    }
    public static class DataValueWikibaseItem extends DataValue {
        String entityType;
        int numericId;
        String numericText = "loading";

        public DataValueWikibaseItem() {
        }

        public DataValueWikibaseItem(String entityType, int numericId, String type) {
            this.entityType = entityType;
            this.numericId = numericId;
            this.type = type;

            WikidataLookup.getLabel("Q"+Integer.toString(numericId), new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    numericText = s;
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }

        public static DataValueWikibaseItem parse(JSONObject dataValue) throws JSONException {
            String type = dataValue.getString("type");
            if(type.equals("wikibase-entityid")) {
                JSONObject value = dataValue.getJSONObject("value");
                return new DataValueWikibaseItem(
                        value.getString("entity-type"),
                        value.getInt("numeric-id"),
                        type
                );
            }
            return null;
        }

        public String getEntityType() {
            return entityType;
        }

        public void setEntityType(String entityType) {
            this.entityType = entityType;
        }

        public int getNumericId() {
            return numericId;
        }

        public void setNumericId(int numericId) {
            this.numericId = numericId;
        }

        public String getNumericText() {
            return numericText;
        }
    }

    public static class Mainsnak {
        String snaktype;
        String property;
        String propertyText = "loading";
        DataType datatype;
        DataValue dataValue;

        public static Mainsnak parse(JSONObject mainsnakJSON) throws JSONException {

            String snakType = "";
            if(mainsnakJSON.has("snaktype")) {
                snakType = mainsnakJSON.getString("snaktype");
            }
            String property = "";
            if(mainsnakJSON.has("property")) {
                property = mainsnakJSON.getString("property");
            }
            String dataTypeJson = "";
            if(mainsnakJSON.has("datatype")) {
                dataTypeJson = mainsnakJSON.getString("datatype");
            }
            DataType dataType = DataType.EMPTY;
            DataValue dataValue = null;
            if(dataTypeJson.equals("time")) {
                dataValue = DataValueTime.parse(mainsnakJSON.getJSONObject("datavalue"));
                dataType = DataType.TIME;
            } else if(dataTypeJson.equals("quantity")) {
                dataValue = DataValueQuantity.parse(mainsnakJSON.getJSONObject("datavalue"));
                dataType = DataType.QUANTITY;
            } else if(dataTypeJson.equals("url")) {
                dataValue = DataValueValue.parse(mainsnakJSON.getJSONObject("datavalue"));
                dataType = DataType.URL;
            } else if(dataTypeJson.equals("string")) {
                dataValue = DataValueValue.parse(mainsnakJSON.getJSONObject("datavalue"));
                dataType = DataType.STRING;
            } else if(dataTypeJson.equals("commonsMedia")) {
                dataValue = DataValueValue.parse(mainsnakJSON.getJSONObject("datavalue"));
                dataType = DataType.VALUE;
            } else if(dataTypeJson.equals("wikibase-item")) {
                dataValue = DataValueWikibaseItem.parse(mainsnakJSON.getJSONObject("datavalue"));
                dataType = DataType.WIKIITEM;
            }

            return new Mainsnak(
                    snakType,
                    property,
                    dataType,
                    dataValue
            );
        }

        public Mainsnak() {
        }

        public Mainsnak(String snaktype, String property, DataType datatype, DataValue dataValue) {
            this.snaktype = snaktype;
            this.property = property;
            this.datatype = datatype;
            this.dataValue = dataValue;

            WikidataLookup.getProperty(property, new Callback<String>() {
                @Override
                public void success(String s, Response response) {
                    propertyText = s;
                    EntityFragment.bus.post(new EntityFragment.DataChangedEvent(this));
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }

        public String getSnaktype() {
            return snaktype;
        }

        public void setSnaktype(String snaktype) {
            this.snaktype = snaktype;
        }

        public String getProperty() {
            return property;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public String getPropertyText() {
            return propertyText;
        }

        public DataType getDatatype() {
            return datatype;
        }

        public void setDatatype(DataType datatype) {
            this.datatype = datatype;
        }

        public DataValue getDataValue() {
            return dataValue;
        }

        public void setDataValue(DataValue dataValue) {
            this.dataValue = dataValue;
        }

    }

    public static class References {
        Reference[] references;

        public static References parse(JSONArray referencesJSON) throws JSONException {
            Reference[] references = new Reference[referencesJSON.length()];

            for(int r = 0; r < referencesJSON.length(); r++) {
                JSONObject jsonObject = referencesJSON.getJSONObject(r);
                String hash = jsonObject.getString("hash");

                JSONArray snaksJSON = jsonObject.getJSONArray("snaks");
                Mainsnak[] snaks = new Mainsnak[snaksJSON.length()];

                for(int i = 0; i < snaksJSON.length(); i++) {
                    Mainsnak mainsnak = Mainsnak.parse(snaksJSON.getJSONObject(i));
                    snaks[i++] = mainsnak;
                }

                JSONArray snaksOrderJSON = jsonObject.getJSONArray("snaks-order");
                String[] snaksOrder = new String[snaksOrderJSON.length()];
                for(int i = 0; i < snaksOrderJSON.length(); i++) {
                    snaksOrder[i] = snaksOrderJSON.getString(i);
                }

                references[r++] = new Reference(hash, snaks, snaksOrder);
            }

            return new References(references);
        }

        public References(Reference[] references) {
            this.references = references;
        }

        public static class Reference {
            String hash;
            Mainsnak[] snaks;
            String[] snaksOrder;

            public Reference(String hash, Mainsnak[] snaks, String[] snaksOrder) {
                this.hash = hash;
                this.snaks = snaks;
                this.snaksOrder = snaksOrder;
            }
        }
    }
}

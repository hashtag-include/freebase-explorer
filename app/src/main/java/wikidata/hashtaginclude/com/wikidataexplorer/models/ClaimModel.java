package wikidata.hashtaginclude.com.wikidataexplorer.models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by matthewmichaud on 3/3/15.
 */
public class ClaimModel {
    String id;
    Mainsnak mainsnak;
    String type;
    String rank;

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

    public ClaimModel(String id, Mainsnak mainsnak, String type, String rank) {
        this.id = id;
        this.mainsnak = mainsnak;
        this.type = type;
        this.rank = rank;
    }

    public static ClaimModel parse(JSONObject claimJSON) throws JSONException {

        String id = claimJSON.getString("id");
        String type = claimJSON.getString("type");
        String rank = claimJSON.getString("rank");

        Mainsnak mainsnak = Mainsnak.parse(claimJSON.getJSONObject("mainsnak"));

        return new ClaimModel(
                id,
                mainsnak,
                type,
                rank);
    }

    public static class Mainsnak {
        String snaktype;
        String property;
        String datatype;
        DataValue dataValue;

        public static Mainsnak parse(JSONObject mainsnakJSON) throws JSONException {

            String snakType = mainsnakJSON.getString("snaktype");
            String property = mainsnakJSON.getString("property");
            String dataType = mainsnakJSON.getString("datatype");
            DataValue dataValue = DataValue.parse(mainsnakJSON.getJSONObject("datavalue"));

            return new Mainsnak(
                    snakType,
                    property,
                    dataType,
                    dataValue
            );
        }

        public Mainsnak() {
        }

        public Mainsnak(String snaktype, String property, String datatype, DataValue dataValue) {
            this.snaktype = snaktype;
            this.property = property;
            this.datatype = datatype;
            this.dataValue = dataValue;
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

        public String getDatatype() {
            return datatype;
        }

        public void setDatatype(String datatype) {
            this.datatype = datatype;
        }

        public DataValue getDataValue() {
            return dataValue;
        }

        public void setDataValue(DataValue dataValue) {
            this.dataValue = dataValue;
        }

        public static class DataValue {
            Value value;
            String valueString;
            String type;

            public static DataValue parse(JSONObject dataValueJSON) throws JSONException {

                String type = dataValueJSON.getString("type");
                if(type.equals("string")) {
                    String value = dataValueJSON.getString("value");
                    return new DataValue(
                            value,
                            type
                    );
                } else if(type.equals("wikibase-entityid")) {
                    Value value = Value.parse(dataValueJSON.getJSONObject("value"));
                    return new DataValue(
                            value,
                            type
                    );
                }
                return null;
            }

            public DataValue() {
            }

            public DataValue(Value value, String type) {
                this.value = value;
                this.type = type;
            }

            public DataValue(String valueString, String type) {
                this.valueString = valueString;
                this.type = type;
            }

            public Value getValue() {
                return value;
            }

            public void setValue(Value value) {
                this.value = value;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getValueString() {
                return valueString;
            }

            public void setValueString(String valueString) {
                this.valueString = valueString;
            }

            public static class Value {
                String entityType;
                int numericId;

                public static Value parse(JSONObject valueJSON) throws JSONException {
                    String entityType = valueJSON.getString("entity-type");
                    int numericId = valueJSON.getInt("numeric-id");

                    return new Value(entityType, numericId);
                }

                public Value() {

                }

                public Value(String entityType, int numericId) {
                    this.entityType = entityType;
                    this.numericId = numericId;
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
            }
        }
    }
}

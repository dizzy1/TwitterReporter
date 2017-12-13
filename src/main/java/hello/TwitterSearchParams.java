package hello;

import twitter4j.JSONException;
import twitter4j.JSONObject;

public class TwitterSearchParams {

    private String beforeParse;

    private String q;
    private String geocode;
    private String lang;
    private String locale;
    private String resultType;
    private String count;
    private String until;
    private String since_id;
    private String max_id;
    private String includeEntities;

    public TwitterSearchParams(String q, String geocode, String lang, String locale, String resultType, String count, String until, String since_id, String max_id, String includeEntities) {
        this.q = q;
        this.geocode = geocode;
        this.lang = lang;
        this.locale = locale;
        this.resultType = resultType;
        this.count = count;
        this.until = until;
        this.since_id = since_id;
        this.max_id = max_id;
        this.includeEntities = includeEntities;
    }

    //parse object from json
    public TwitterSearchParams (String q){
        try {
            beforeParse = q;
            JSONObject j = new JSONObject(q);
            this.q = j.getString("q"); //required by twitter api
            if(j.has("geocode"))
                this.geocode = j.getString("geocode");
            if(j.has("lang"))
                this.lang = j.getString("lang");
            if(j.has("locale"))
                this.locale = j.getString("locale");
            if(j.has("resultType"))
                this.resultType = j.getString("resultType");
            if(j.has("count"))
                this.count = j.getString("count");
            if(j.has("until"))
                this.until = j.getString("until");
            if(j.has("since_id"))
                this.since_id = j.getString("since_id");
            if(j.has("max_id"))
                this.max_id = j.getString("max_id");
            if(j.has("includeEntities"))
                this.includeEntities = j.getString("includeEntities");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //puts into a form the twitter api is expecting
    public String toTwitterForm() {
        StringBuilder sb = new StringBuilder();
        sb.append("q=");
        sb.append(q);
        if(geocode!=null && !geocode.equals("")) {
            sb.append("&geocode=");
            sb.append(geocode);
        }
        if(lang!=null && !lang.equals("")) {
            sb.append("&lang=");
            sb.append(lang);
        }
        if(locale!=null && !locale.equals("")) {
            sb.append("&locale=");
            sb.append(locale);
        }
        if(resultType!=null && !resultType.equals("")) {
            sb.append("&resultType=");
            sb.append(resultType);
        }
        if(count!=null && !count.equals("")) {
            sb.append("&count=");
            sb.append(count);
        }
        if(until!=null && !until.equals("")) {
            sb.append("&until=");
            sb.append(until);
        }
        if(since_id!=null && !since_id.equals("")) {
            sb.append("&since_id=");
            sb.append(since_id);
        }
        if(max_id!=null && !max_id.equals("")) {
            sb.append("&max_id=");
            sb.append(max_id);
        }
        if(includeEntities!=null && !includeEntities.equals("")) {
            sb.append("&includeEntities=");
            sb.append(includeEntities);
        }
        return sb.toString();
    }

    public String getBeforeParse() {
        return beforeParse;
    }

    public void setBeforeParse(String beforeParse) {
        this.beforeParse = beforeParse;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getGeocode() {
        return geocode;
    }

    public void setGeocode(String geocode) {
        this.geocode = geocode;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public String getSince_id() {
        return since_id;
    }

    public void setSince_id(String since_id) {
        this.since_id = since_id;
    }

    public String getMax_id() {
        return max_id;
    }

    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }

    public String getIncludeEntities() {
        return includeEntities;
    }

    public void setIncludeEntities(String includeEntities) {
        this.includeEntities = includeEntities;
    }
}

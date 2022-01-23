package com.example.demo.algorithm.entity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.net.URI;

public class GCPTranslator {

    private String ContextDocument;
    private String DetectedLanguage;
    private String TranslatedDocument;
    private String Targetlanguage;
    private String APIKey;

    public void TranslateText() {
        HttpClient httpclient = HttpClients.createDefault();

        try {
            URIBuilder builder = new URIBuilder("https://translation.googleapis.com/language/translate/v2?key=" + APIKey);

            builder.setParameter("q", ContextDocument);
            builder.setParameter("target",Targetlanguage);

            URI uri = builder.build();
            HttpGet request = new HttpGet(uri);
            
            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            String JsonResponse = EntityUtils.toString(entity);
            JSONObject responseObject = new JSONObject(JsonResponse); 

            DetectedLanguage = responseObject.getJSONObject("data").getJSONArray("translations").getJSONObject(0).get("detectedSourceLanguage").toString();
            TranslatedDocument = responseObject.getJSONObject("data").getJSONArray("translations").getJSONObject(0).get("translatedText").toString();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}

package com.example.pooreum.phonewear;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by B100 on 2017-04-07.
 */

/*
Update Channel Feed - GET
GET https://api.thingspeak.com/update?api_key=Q1PNOIY2LJCGJ28Z&field1=0

POST https://api.thingspeak.com/update.json
     api_key=Q1PNOIY2LJCGJ28Z
     field1=73

Get a Channel Feed
GET https://api.thingspeak.com/channels/254487/feeds.json?results=2

Get a Channel Field Feed
GET https://api.thingspeak.com/channels/254487/fields/1.json?results=2

Get Status Updates
GET https://api.thingspeak.com/channels/254487/status.json

*/

public interface ApiService {
    public static final String API_URL = "https://api.thingspeak.com/channels/254487/fields/1.json";

    @GET("channels/254487/fields/1.json")
    Call<ResponseBody>getCommentStr(@Query("result") String field1);

}

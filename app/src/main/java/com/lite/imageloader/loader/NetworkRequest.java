package com.lite.imageloader.loader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Saket on 02,August,2019
 */
public interface NetworkRequest {

    @Streaming
    @GET
    Call<ResponseBody> load(@Url String url);
}

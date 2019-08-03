package com.lite.imageloader.loader;

import com.lite.imageloader.utils.Constants;

import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saket on 02,August,2019
 */
public class NetworkInstance {

    private static Retrofit _retrofit;
    private static NetworkResponse networkResponse;
    private Call<ResponseBody> call;
    private String urls;
    private String keys;

    public NetworkInstance(NetworkResponse network) {
        networkResponse = (NetworkResponse)network;
    }

    private static Retrofit getInstance() {
        if (_retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.NONE);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            _retrofit = new Retrofit.Builder().baseUrl(Constants.BASE).client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return _retrofit;
    }

    protected void load(String url, String key) {
        this.urls = url;
        this.keys = key;
        NetworkRequest clients = getInstance().create(NetworkRequest.class);
        call = clients.load(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && !call.isCanceled()) {
                    networkResponse.onSuccess(response.body().byteStream(), urls, keys);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                networkResponse.onFailure(call, t);
            }
        });
    }

    protected void cancel() {
        if (call != null)
            call.cancel();
    }

    public interface NetworkResponse {
        void onSuccess(InputStream responseBody, String url, String key);

        void onFailure(Call<ResponseBody> call, Throwable t);
    }
}

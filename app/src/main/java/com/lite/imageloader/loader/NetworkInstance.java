package com.lite.imageloader.loader;

import android.util.Log;

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

    public NetworkInstance(NetworkResponse network){
        networkResponse = (NetworkResponse)network;
    }

    private static Retrofit getInstance(){
        if(_retrofit == null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                                                    .setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            _retrofit = new Retrofit.Builder().baseUrl(Constants.BASE).client(client)
                            .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return _retrofit;
    }

    public void load(String url){
        final String urls = url;
        NetworkRequest clients = getInstance().create(NetworkRequest.class);
        Call<ResponseBody> call = clients.load(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    networkResponse.onSuccess(response.body().byteStream(), urls);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                networkResponse.onFailure();
            }
        });
    }

    /*public class LoadImages implements Runnable{
        String url = null;
        public LoadImages(String url) {
            this.url = url;
        }

        @Override
        public void run() {

            *//*try {
                InputStream in = call.execute().body().byteStream();
                networkResponse.onSuccess(in, url);
            }catch (IOException e){
                Log.d("Saket","Failure!");
                networkResponse.onFailure();
            }*//*
        }
    }*/

    public interface NetworkResponse{
        public void onSuccess(InputStream responseBody, String keys);
        public void onFailure();
    }
}

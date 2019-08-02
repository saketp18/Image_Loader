package com.lite.imageloader.loader;

import com.lite.imageloader.utils.Constants;

import java.io.IOException;
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

    public NetworkInstance() {
    }

    public NetworkInstance(NetworkResponse network){
        networkResponse = (NetworkResponse)network;
    }

    private static Retrofit getInstance(){
        if(_retrofit == null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                                                    .setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            _retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client)
                            .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return _retrofit;
    }

    public LoadImages load(String url, int key){
       return new LoadImages(url);
    }

    public class LoadImages implements Runnable{
        public LoadImages(String url) {}

        @Override
        public void run() {
            NetworkRequest clients = getInstance().create(NetworkRequest.class);
            Call<ResponseBody> call = clients.load(Constants.URL);
            try {
                InputStream in = call.execute().body().byteStream();
                networkResponse.onSuccess(in, Constants.URL, 1);
            }catch (IOException e){
                networkResponse.onFailure();
            }
            /*call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()){
                        networkResponse.onSuccess(response.body().byteStream(), Constants.URL, 1);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    networkResponse.onFailure();
                }
            });*/
        }
    }

    public interface NetworkResponse{
        public void onSuccess(InputStream responseBody, String keys, int key);
        public void onFailure();
    }
}

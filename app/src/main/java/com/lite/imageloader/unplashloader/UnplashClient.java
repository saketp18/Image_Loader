package com.lite.imageloader.unplashloader;

import com.lite.imageloader.utils.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Saket on 02,August,2019
 */
public class UnplashClient {

    private static Retrofit retrofit = null;

    private UnplashClient(){}

    public static Retrofit getUnplashClient(){
        if(retrofit == null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.NONE);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor)
                                        .addInterceptor(new Interceptor() {
                                            @Override
                                            public Response intercept(Chain chain) throws IOException {
                                                Request request = chain.request();
                                                request = request.newBuilder()
                                                                .addHeader("Authorization", "Client-ID "+Constants.ACCESS_KEY)
                                                                .build();
                                                return chain.proceed(request);
                                            }
                                        }).build();
            retrofit = new Retrofit.Builder().baseUrl(Constants.BASE).client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}

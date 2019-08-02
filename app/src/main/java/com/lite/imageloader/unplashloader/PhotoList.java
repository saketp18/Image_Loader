package com.lite.imageloader.unplashloader;

import com.lite.imageloader.models.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Saket on 02,August,2019
 */
public interface PhotoList {

    @GET("photos")
    Call<List<Photo>> getPhotos(@Query("page") Integer page, @Query("per_page") Integer per_page);
}

package com.example.callvideo.Service;
import com.example.callvideo.Model.Entities.News;
import com.example.callvideo.Model.Entities.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
public interface JsonPlaceHolderApi {

        @GET("v2/everything?q=bitcoin&apiKey=f09cf57510de4cedbd979b1fd29e5210")
        Call<List<News>> getPosts();
}

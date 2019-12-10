package com.example.callvideo.Presenter.News;

import android.util.Log;

import com.example.callvideo.Model.Entities.News;
import com.example.callvideo.Model.Entities.Post;
import com.example.callvideo.Service.Client;
import com.example.callvideo.Service.JsonPlaceHolderApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetNews {
    private NewsListener newsListener;
    public GetNews(NewsListener newsListener){
        this.newsListener=newsListener;
    }
    public void getApi(){
        JsonPlaceHolderApi jsonPlaceHolderApi= Client.getClient("https://newsapi.org/").create(JsonPlaceHolderApi.class);

        Call<List<News>> call = jsonPlaceHolderApi.getPosts();

        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {

                if (!response.isSuccessful()) {
                    newsListener.getError("Code: " + response.code());
                    return;
                }

                List<News> news = response.body();
                Log.d("API", String.valueOf(response.body()));
                for (News newItem : news) {
                    String content = "";
                    //content += "ID: " + post.getId() + "\n";
                    //content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + newItem.getTitle() + "\n";
                    content += "Text: " + newItem.getContent() + "\n\n";

                    newsListener.getSuccess(content);
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                newsListener.getError(t.getMessage());
            }
        });
    }

}

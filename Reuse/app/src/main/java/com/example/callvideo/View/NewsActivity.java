package com.example.callvideo.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.callvideo.Model.Entities.Post;
import com.example.callvideo.Presenter.News.NewsPresenter;
import com.example.callvideo.R;
import com.example.callvideo.Service.APIService;
import com.example.callvideo.Service.Client;
import com.example.callvideo.Service.JsonPlaceHolderApi;
import com.example.callvideo.View.News.NewsView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity implements NewsView {
    private TextView textViewResult;
    private NewsPresenter newsPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        textViewResult = findViewById(R.id.text_view_result);
        newsPresenter=new NewsPresenter(this);
        newsPresenter.GetApi();
    }

    @Override
    public void onSuccess(String data) {
        textViewResult.append(data);
    }

    @Override
    public void onError(String msg) {
        textViewResult.setText(msg);
    }
}

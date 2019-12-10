package com.example.callvideo.Presenter.News;

import com.example.callvideo.View.News.NewsView;

public class NewsPresenter implements NewsListener {
    private GetNews mainInterator;
    private NewsView newsView;
    public NewsPresenter(NewsView newsView){
        this.newsView=newsView;
        mainInterator=new GetNews(this);
    }
    public void GetApi(){
        mainInterator.getApi();
    }
    @Override
    public void getError(String msg) {
        newsView.onError(msg);
    }

    @Override
    public void getSuccess(String data) {
        newsView.onSuccess(data);
    }
}

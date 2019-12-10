package com.example.callvideo.Presenter.Translation;

import com.example.callvideo.Translatetion.TranslatedText;
import com.example.callvideo.View.Translate.ITranslateView;

import java.util.HashMap;

import retrofit2.Call;

public class TranslatePresenter implements ITranslateListener{
    private Translate mainInterator;
    private ITranslateView translateView;
    public TranslatePresenter(ITranslateView translateView){
        this.translateView=translateView;
        mainInterator=new Translate(this);
    }
    public void onTranslate(String translateTest, HashMap<String,Object>spinnerMap){
        mainInterator.textChangedListener(translateTest,spinnerMap);
    }
    @Override
    public void onReturnRespone(Call<TranslatedText> call, String textTranslate) {
        translateView.onReturnRespone(call,textTranslate);
    }
}

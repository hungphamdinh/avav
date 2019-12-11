package com.example.callvideo.Presenter.Translation;

import com.example.callvideo.Model.Entities.TranslatedText;

import retrofit2.Call;

public interface ITranslateListener {
    void onReturnRespone( Call<TranslatedText> call,String textTranslate);
    void onSetFavorite(String mssg);
}

package com.example.callvideo.Presenter.Translation;

import android.widget.ArrayAdapter;

import com.example.callvideo.Translatetion.TranslatedText;

import java.util.ArrayList;

import retrofit2.Call;

public interface ITranslateListener {
    void onReturnRespone( Call<TranslatedText> call,String textTranslate);
    void onSetFavorite(String mssg);
}

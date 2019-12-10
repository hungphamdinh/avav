package com.example.callvideo.View.Translate;

import android.widget.ArrayAdapter;

import com.example.callvideo.Translatetion.TranslatedText;

import java.util.ArrayList;

import retrofit2.Call;

public interface ITranslateView {
    void onReturnRespone(Call<TranslatedText> call, String textTranslate);

}

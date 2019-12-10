package com.example.callvideo.Presenter.Translation;

import com.example.callvideo.Service.APIHelper;
import com.example.callvideo.Service.Client;
import com.example.callvideo.Translatetion.Languages;
import com.example.callvideo.Translatetion.TranslatedText;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.functions.Action1;

public class Translate {
    private ITranslateListener iTranslateListener;
    private boolean noTranslate=true;
    public Translate(ITranslateListener iTranslateListener){
        this.iTranslateListener=iTranslateListener;
    }
    public void textChangedListener(String textToTranslate, HashMap<String,Object>spinnerMap) {
         translate(textToTranslate,spinnerMap);
         //checkIfInFavourites();

    }

    private void translate(String text,HashMap<String,Object>spinnerMap){
        if(noTranslate){
            noTranslate = false;
            return;
        }

        String APIKey = "trnsl.1.1.20170314T200256Z.c558a20c3d6824ff.7" +
                "860377e797dffcf9ce4170e3c21266cbc696f08";
        String language1 = spinnerMap.get("spinner1").toString();
        String language2 = spinnerMap.get("spinner2").toString();

        Retrofit query = Client.getClient("https://translate.yandex.net/");

        APIHelper apiHelper = query.create(APIHelper.class);
        Call<TranslatedText> call = apiHelper.getTranslation(APIKey, text,
                langCode(language1) + "-" + langCode(language2));
        iTranslateListener.onReturnRespone(call,text);
//        call.enqueue(new Callback<TranslatedText>() {
//            @Override
//            public void onResponse(Call<TranslatedText> call, Response<TranslatedText> response) {
//                if(response.isSuccessful()){
//                    context.getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            translatedText.setText(response.body().getText().get(0));
//                            //                     checkIfInFavourites();
//                            //                         addToHistory();
//                        }
//                    });
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TranslatedText> call, Throwable t) {}
//        });
    }

    public String langCode(String selectedLang) {
        String code = null;

        if(Locale.getDefault().getLanguage().equals("en")) {
            for (int i = 0; i < Languages.getLangsEN().length; i++) {
                if(selectedLang.equals(Languages.getLangsEN()[i])){
                    code = Languages.getLangCodeEN(i);
                }
            }
        } else{
            for (int i = 0; i < Languages.getLangsEN().length; i++) {
                if(selectedLang.equals(Languages.getLangsEN()[i])){
                    code = Languages.getLangCodeEN(i);
                }
            }
        }
        return code;
    }
}
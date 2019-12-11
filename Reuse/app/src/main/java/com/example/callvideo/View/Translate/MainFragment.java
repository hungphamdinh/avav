package com.example.callvideo.View.Translate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.callvideo.Presenter.Translation.TranslatePresenter;
import com.example.callvideo.R;
import com.example.callvideo.Model.Entities.Languages;
import com.example.callvideo.Model.Entities.TranslatedText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by almaz on 16.04.17.
 */

public class MainFragment extends Fragment implements ITranslateView {
    private Button btnTranslate;
    private View rootView;
    private Spinner spinner1;
    private Spinner spinner2;
    private EditText txtToTranslate;
    private ImageButton btnAddToFavourites;
    private ImageButton btnChangeLanguages;
    private TextView txtTranslated;
    private boolean isFavourite; // if current word is favourite.
    private boolean noTranslate; // do not translate at 1-st text changing. Need when initialize
                                // with some text.

    private TranslatePresenter translatePresenter;
    /**
     * Initialize widget elements and create view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return created view of fragment
     */
    private Context context;
    private String userPhone;
    public MainFragment(Context context,String userPhone){
        this.context=context;
        this.userPhone=userPhone;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.main_fragment, container, false);
        spinner1 = (Spinner) rootView.findViewById(R.id.languages1);
        spinner2 = (Spinner) rootView.findViewById(R.id.languages2);
        txtToTranslate = (EditText) rootView.findViewById(R.id.textToTranslate);
        txtToTranslate.setMovementMethod(new ScrollingMovementMethod());
        txtToTranslate.setVerticalScrollBarEnabled(true);
        btnChangeLanguages = (ImageButton) rootView.findViewById(R.id.changeLanguages);
        btnAddToFavourites = (ImageButton) rootView.findViewById(R.id.addToFavourites1);
        btnTranslate=(Button)rootView.findViewById(R.id.btnTranslate);
        txtTranslated = (TextView) rootView.findViewById(R.id.translatedText);
        txtTranslated.setMovementMethod(new ScrollingMovementMethod());
        txtTranslated.setVerticalScrollBarEnabled(true);
        translatePresenter=new TranslatePresenter(this);
       // setArgs();
        return rootView;
    }

    /**
     * Add listeners and set data.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setSpinners();
        swapLanguage();
        onClickTranslate();
        onAddFavour();
        super.onViewCreated(view, savedInstanceState);
    }

    private void swapLanguage() {
        btnChangeLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sourceLng = spinner1.getSelectedItemPosition();
                int targetLng = spinner2.getSelectedItemPosition();
                spinner1.setSelection(targetLng);
                spinner2.setSelection(sourceLng);
            }
        });
    }

    private void onAddFavour() {
        btnAddToFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> favourMap=new HashMap<>();
                favourMap.put("userPhone",userPhone);
                favourMap.put("beTrans", txtToTranslate.getText().toString());
                favourMap.put("afTrans", txtTranslated.getText().toString());
                translatePresenter.onSetToFavourite(favourMap);
            }
        });
    }

    private void onClickTranslate() {
        btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> spinnerMap=new HashMap<>();
                spinnerMap.put("spinner1",spinner1.getSelectedItem());
                spinnerMap.put("spinner2",spinner2.getSelectedItem());
                translatePresenter.onTranslate(txtToTranslate.getText().toString(),spinnerMap);
            }
        });
    }

    @Override
    public void onDestroyView() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("default", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("selection1", spinner1.getSelectedItemPosition());
        editor.putInt("selection2", spinner2.getSelectedItemPosition());
        editor.putString("txtToTranslate", txtToTranslate.getText().toString());
        editor.putString("txtTranslated", txtTranslated.getText().toString());
        editor.putBoolean("isFavourite", isFavourite);
        editor.apply();
        super.onDestroyView();
    }
    public void setSpinners() {
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();

        if(Locale.getDefault().getLanguage().equals("en")) {
            Collections.addAll(categories, Languages.getLangsEN());
        } else{
            Collections.addAll(categories, Languages.getLangsEN());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String compareValue1="English";
        String compareValue2="Vietnamese";

        // attaching data adapter to spinner
        spinner1.setAdapter(dataAdapter);
        if (compareValue1 != null) {
            int spinnerPosition = dataAdapter.getPosition(compareValue1);
            spinner1.setSelection(spinnerPosition);
        }
        spinner2.setAdapter(dataAdapter);
        if (compareValue2 != null) {
            int spinnerPosition = dataAdapter.getPosition(compareValue2);
            spinner2.setSelection(spinnerPosition);
        }
    }

    @Override
    public void onReturnRespone(Call<TranslatedText> call, String textTranslate) {
        call.enqueue(new Callback<TranslatedText>() {
            @Override
            public void onResponse(Call<TranslatedText> call, Response<TranslatedText> response) {
                if(response.isSuccessful()){
                    if(getActivity() == null)
                        return;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtTranslated.setText(response.body().getText().get(0));
                            //                     checkIfInFavourites();
                            //                         addToHistory();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TranslatedText> call, Throwable t) {}
        });
    }

    @Override
    public void onSetFavorite(String mssg) {
        Toast.makeText(context,mssg,Toast.LENGTH_SHORT).show();
    }
}

package com.example.callvideo.Presenter.Cart;

import com.example.callvideo.View.Cart.ICartView;

import java.util.HashMap;

public class CartPresenter implements ICartListener {
    private ICartView iCartView;
    private Cart mainInterator;
    public CartPresenter(ICartView iCartView){
        this.iCartView=iCartView;
        mainInterator=new Cart(this);
    }
    public void loadListCourse(HashMap<String,Object>map){
        mainInterator.loadListCourse(map);
    }
    public void onPurchased(HashMap<String,Object>map){
        mainInterator.onPurchased(map);
    }
    @Override
    public void onSuccess(String msg) {
        iCartView.onDisplaySuccess(msg);
    }

    @Override
    public void onError(String msg) {
        iCartView.onDisplayError(msg);
    }

    @Override
    public void onDisplayCourse(HashMap<String, Object> map) {
        iCartView.onDisplayCourse(map);
    }
}

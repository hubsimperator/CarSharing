package com.example.carsharing;

import android.graphics.Bitmap;

import java.util.HashMap;

public  class Obiekt_Photo {

    private static HashMap<Integer,Bitmap> photo=new HashMap<>();
    private Integer parametr;
    private Bitmap bitmap;

  /*  public Obiekt_Photo(Integer parametr, Bitmap bitmap) {
        this.parametr = parametr;
        this.bitmap = bitmap;
    }


   */
    public Integer getParametr() {
        return parametr;
    }

    public void setParametr(Integer parametr) {
        this.parametr = parametr;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setPhoto(Bitmap bitmap,Integer parametr) {
        this.bitmap = bitmap;
        this.parametr = parametr;
        photo.put(parametr,bitmap);
    }

    public Bitmap getPhoto(Integer parametr) {
        bitmap=photo.get(parametr);
       return bitmap;
    }
}

package com.example.carsharing;

import android.graphics.Bitmap;

public class Obiekt_Photo {
    private Integer parametr;
    private Bitmap bitmap;

    public Obiekt_Photo(Integer parametr, Bitmap bitmap) {
        this.parametr = parametr;
        this.bitmap = bitmap;
    }

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
}

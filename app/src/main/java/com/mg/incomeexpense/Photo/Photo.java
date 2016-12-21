package com.mg.incomeexpense.Photo;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.Objects;

/**
 * Created by mario on 2016-12-12.
 */

public class Photo {

    private Bitmap mBitmap;
    private String mPath;

    private Photo(Bitmap bitmap, String path) {
        mBitmap = bitmap;
        mPath = path;
    }

    public static Photo create(@NonNull Bitmap bitmap) {

        Objects.requireNonNull(bitmap, "Parameter bitmap of type Bitmap is mandatory");

        return new Photo(bitmap, null);
    }

    public static Photo create(@NonNull String path) {

        Objects.requireNonNull(path, "Parameter path of type String is mandatory");

        return new Photo(null, path);
    }


    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        return mPath.equals(photo.mPath);

    }

    @Override
    public int hashCode() {
        return mPath.hashCode();
    }

}

package com.mg.incomeexpense.Photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.core.Tools;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

/**
 * Created by mario on 2016-12-12.
 */

public class PhotoManager {
    public static Boolean delete(Photo photo) {
        File file = new File(photo.getPath());
        if (file != null) {
            return file.delete();
        }
        return false;
    }

    public static Photo create(@NonNull Context context, @NonNull Photo photo) {

        Objects.requireNonNull(context, "Parameter context of type Context is mandatory.");
        Objects.requireNonNull(photo, "Parameter photo of type Photo is mandatory.");
        Objects.requireNonNull(photo.getBitmap(), "The property bitmap is mandatory.");

        try {
            final File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            final File destinationFile = Tools.createFile(storageDir, ".jpg");

            photo.setPath(destinationFile.getPath());

            FileOutputStream out = new FileOutputStream(destinationFile);
            photo.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();

        } catch (Exception ex) {
            // TODO gerer l'exception
        }

        return photo;
    }

    public static Photo decodeBitmapFromFile(Photo photo, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photo.getPath(), options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(photo.getPath(), options);
        photo.setBitmap(bitmap);

        return photo;
    }


}

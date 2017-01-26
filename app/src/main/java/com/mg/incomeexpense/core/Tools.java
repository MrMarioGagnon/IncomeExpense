package com.mg.incomeexpense.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.mg.incomeexpense.R;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-07-21.
 */
public class Tools {

    public static List<String> split(String stringToSplit, String separator) {

        if (stringToSplit == null)
            return new ArrayList<>();

        if (separator == null)
            separator = ApplicationConstant.storageSeparator;

        List<String> items;

        String[] strings = stringToSplit.split(separator);

        items = Arrays.asList(strings);

        Collections.sort(items);

        return items;
    }

    public static String getDefaultCurrency(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultCurrency = preferences.getString(context.getString(R.string.pref_default_currency_key), context.getString(R.string.pref_CAD_currency));
        return defaultCurrency;
    }

    public static String formatDate(LocalDate date, String stringFormat) {
        return date.format(DateTimeFormatter.ofPattern(stringFormat));
    }

    public static String formatAmount(@NonNull Double amount) {

        Objects.requireNonNull(amount, "Parameter amount of type Double is mandatory");

        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(amount);
    }

    public static <T> T coalesce(T... items) {
        for (T i : items)
            if (i != null)
                return i;
        return null;
    }

    public static void copy(String sourcePath, String destinationPath)
            throws Exception {

        File sd = Environment.getExternalStorageDirectory();

        File data = Environment.getDataDirectory();

        createDirectory("incomeexpense");

        File sdTraveloid = new File(sd, "incomeexpense");

        if (sd.canWrite()) {

            String sourceFilePath = "/data/com.mg.incomeexpense/databases/incexp.db";

            String now = Tools.formatDate(LocalDate.now(), "yyyyMMddkkmmss");

            String destinationFilePath = "incexp" + now + ".db";

            File currentDB = new File(data, sourceFilePath);

            File backupDB = new File(sdTraveloid, destinationFilePath);

            if (currentDB.exists()) {

                FileChannel src = new FileInputStream(currentDB).getChannel();

                FileChannel dst = new FileOutputStream(backupDB).getChannel();

                dst.transferFrom(src, 0, src.size());

                src.close();

                dst.close();
            }
        }

    }

    public static void createDirectory(String directory) {

        File wallpaperDirectory = new File("/sdcard/" + directory + "/");
        wallpaperDirectory.mkdirs();
    }

    public static boolean isExternalStorageAvailable() {

        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

        return mExternalStorageAvailable && mExternalStorageWriteable;
    }

    public static <T> String join(final Collection<T> objs,
                                  final String delimiter) {
        if (objs == null || objs.isEmpty())
            return "";
        Iterator<T> iter = objs.iterator();
        StringBuffer buffer = new StringBuffer(iter.next().toString());
        T o;
        while (iter.hasNext()) {
            o = iter.next();
            if (o != null)
                buffer.append(delimiter).append(o.toString());
        }
        return buffer.toString();
    }

    public static double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }

    public static <T> void setSpinner(final T o, final Spinner s) {
        int position = 0;

        SpinnerAdapter adapter = s.getAdapter();

        if (o == null && adapter.getCount() == 0)
            return;

        if (o == null) {

            // Pour eviter de se positionner sur l'item Add New
            Object object = adapter.getItem(0);
            if(object instanceof ObjectBase){
                if(((ObjectBase) object).getId() == -1 && adapter.getCount() > 1){
                    position = 1;
                }
            }

            s.setSelection(position);
            return;
        }

        T item;
        for (int i = 0; i < adapter.getCount(); i++) {
            item = (T) adapter.getItem(i);
            if (o.equals(item)) {
                s.setSelection(i);
                break;
            }
        }
    }

    public static File createFile(final File storageDir, final String suffix) throws IOException {

        final String prefix = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File file = File.createTempFile(prefix,suffix,storageDir);

        return file;

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewPager.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
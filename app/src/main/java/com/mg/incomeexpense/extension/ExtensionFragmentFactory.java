package com.mg.incomeexpense.extension;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.Objects;

/**
 * Created by mario on 2016-11-13.
 */

public final class ExtensionFragmentFactory {

    public static Fragment create(ExtensionType type) {

        if(null == type)
            type = ExtensionType.Note;

        Fragment fragment;

        switch (type) {
            case Fuel:
                fragment = new ExtensionFragmentFuel();
                break;
            default:
                fragment = new ExtensionFragmentNote();
        }

        return fragment;
    }

    public enum ExtensionType {Note, Fuel}

}

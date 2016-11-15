package com.mg.incomeexpense.extension;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-11-13.
 */

public final class ExtensionFragmentFactory {

    public static Fragment create(ExtensionType type) {

        if(null == type)
            type = ExtensionType.NOTE;

        Fragment fragment;

        switch (type) {
            case FUEL:
                fragment = new ExtensionFragmentFuel();
                break;
            default:
                fragment = new ExtensionFragmentNote();
        }

        return fragment;
    }

    public enum ExtensionType {
        NOTE, FUEL;

        public static List<String> AsList(){

            List<String> extensionTypes = new ArrayList();

            for(ExtensionType extensionType : ExtensionType.values()){

                extensionTypes.add(extensionType.toString());
            }

            return extensionTypes;
        }
    }

}

package com.mg.incomeexpense.core;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.Map;
import java.util.Objects;

/**
 * Created by mario on 2016-11-01.
 */

public abstract class RepositorySynchronizerBase {

    protected final ContentResolver mContentResolver;
    protected final Uri mItemUri;
    protected final Map<Integer, String> mMessages;

    public RepositorySynchronizerBase(@NonNull ContentResolver contentResolver, @NonNull Uri itemUri, @NonNull Map<Integer, String> messages) {

        Objects.requireNonNull(contentResolver, "Parameter contentResolver of type ContentResolver is mandatory");
        Objects.requireNonNull(itemUri, "Parameter itemUri of type Uri is mandatory");
        Objects.requireNonNull(messages, "Parameter messages of type Map<Integer, String>");

        mContentResolver = contentResolver;
        mItemUri = itemUri;
        mMessages = messages;
    }

    public ObjectBase Save(@NonNull ObjectBase item) {

        Objects.requireNonNull(item, "Parameter item of type ObjectBase is mandatory");

        if ((!item.isNew()) && item.getId() == null) {
            throw new NullPointerException("Item is not new, item id is mandatory.");
        }

        if (!item.isDirty()) {
            return item;
        }

        return item;
    }

}

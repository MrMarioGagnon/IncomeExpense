package com.mg.incomeexpense.core;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by mario on 2016-07-23.
 */
public class ItemRepositorySynchronizerMessageBuilder {

    public static Map<Integer, String> build(@NonNull Context context, @NonNull String objectType) {

        Objects.requireNonNull(context, "Parameter context of type Context is mandatory");
        Objects.requireNonNull(objectType, "Parameter objectType of type String is mandatory");

        Map<Integer, String> messages = new HashMap<>();

        switch (objectType) {
            case "AccountRepositorySynchronizer":
                messages.put(R.string.log_info_adding_item, context.getString(R.string.log_info_adding_item));
                messages.put(R.string.log_info_added_item, context.getString(R.string.log_info_added_item));
                messages.put(R.string.log_info_deleting_item, context.getString(R.string.log_info_deleting_item));
                messages.put(R.string.log_info_deleted_item, context.getString(R.string.log_info_deleted_item));
                messages.put(R.string.log_info_updating_item, context.getString(R.string.log_info_updating_item));
                messages.put(R.string.log_info_updated_item, context.getString(R.string.log_info_updated_item));
                messages.put(R.string.log_info_number_deleted_associated_contributor, context.getString(R.string.log_info_number_deleted_associated_contributor));
                messages.put(R.string.log_info_number_deleted_associated_category, context.getString(R.string.log_info_number_deleted_associated_category));
                messages.put(R.string.log_error_adding_item, context.getString(R.string.log_error_adding_item));
                messages.put(R.string.log_error_deleting_item, context.getString(R.string.log_error_deleting_item));
                messages.put(R.string.log_error_updating_item, context.getString(R.string.log_error_updating_item));
                messages.put(R.string.log_info_number_added_associated_contributor, context.getString(R.string.log_info_number_added_associated_contributor));
                messages.put(R.string.log_info_number_added_associated_category, context.getString(R.string.log_info_number_added_associated_category));
                break;
            case "PaymentMethodRepositorySynchronizer":
                messages.put(R.string.log_info_adding_item, context.getString(R.string.log_info_adding_item));
                messages.put(R.string.log_info_added_item, context.getString(R.string.log_info_added_item));
                messages.put(R.string.log_info_deleting_item, context.getString(R.string.log_info_deleting_item));
                messages.put(R.string.log_info_deleted_item, context.getString(R.string.log_info_deleted_item));
                messages.put(R.string.log_info_updating_item, context.getString(R.string.log_info_updating_item));
                messages.put(R.string.log_info_updated_item, context.getString(R.string.log_info_updated_item));
                messages.put(R.string.log_info_number_deleted_associated_contributor, context.getString(R.string.log_info_number_deleted_associated_contributor));
                messages.put(R.string.log_error_adding_item, context.getString(R.string.log_error_adding_item));
                messages.put(R.string.log_error_deleting_item, context.getString(R.string.log_error_deleting_item));
                messages.put(R.string.log_error_updating_item, context.getString(R.string.log_error_updating_item));
                messages.put(R.string.log_info_number_added_associated_contributor, context.getString(R.string.log_info_number_added_associated_contributor));
                break;
            case "ContributorRepositorySynchronizer":
                messages.put(R.string.log_info_adding_item, context.getString(R.string.log_info_adding_item));
                messages.put(R.string.log_info_added_item, context.getString(R.string.log_info_added_item));
                messages.put(R.string.log_info_deleting_item, context.getString(R.string.log_info_deleting_item));
                messages.put(R.string.log_info_deleted_item, context.getString(R.string.log_info_deleted_item));
                messages.put(R.string.log_info_updating_item, context.getString(R.string.log_info_updating_item));
                messages.put(R.string.log_info_updated_item, context.getString(R.string.log_info_updated_item));
                break;
            case "TransactionRepositorySynchronizer":
                messages.put(R.string.log_info_adding_item, context.getString(R.string.log_info_adding_item));
                messages.put(R.string.log_info_added_item, context.getString(R.string.log_info_added_item));
                messages.put(R.string.log_info_deleting_item, context.getString(R.string.log_info_deleting_item));
                messages.put(R.string.log_info_deleted_item, context.getString(R.string.log_info_deleted_item));
                messages.put(R.string.log_info_updating_item, context.getString(R.string.log_info_updating_item));
                messages.put(R.string.log_info_updated_item, context.getString(R.string.log_info_updated_item));
                break;
        }
        return messages;
    }
}
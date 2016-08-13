package com.mg.incomeexpense.transaction;

import android.content.Context;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.core.ObjectBase;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mario on 2016-07-23.
 */
public class TransactionValidator implements ObjectValidator {

    private final Map<Integer, String> mValidationMessages;

    public TransactionValidator(Map<Integer, String> validationMessages) {
        mValidationMessages = validationMessages;
    }

    public static TransactionValidator create(Context context) {

        Map<Integer, String> messages = new HashMap<>();
        messages.put(R.string.validation_account_mandatory, context.getString(R.string.validation_account_mandatory));
        messages.put(R.string.validation_name_already_exists, context.getString(R.string.validation_name_already_exists));
        messages.put(R.string.validation_sub_categories_mandatory, context.getString(R.string.validation_sub_categories_mandatory));


        return new TransactionValidator(messages);
    }

    public ValidationStatus Validate(ObjectBase objectToValidate) {

        List<String> messages = new ArrayList<>();

        if (!(objectToValidate instanceof Transaction)) {
            return ValidationStatus.create("Wrong object type.");
        }

        Transaction transaction = (Transaction) objectToValidate;

        if (transaction.getAccount() == null) {
            messages.add(mValidationMessages.get(R.string.validation_account_mandatory));
        }

        Category category = transaction.getCategory();
        if (category == null) {
            messages.add(mValidationMessages.get(R.string.validation_category_mandatory));
        } else {
            if (category.getSelectedSubCategory() == null || category.getSelectedSubCategory().trim().length() == 0) {
                messages.add(mValidationMessages.get(R.string.validation_sub_category_mandatory));
            }
        }

        if (transaction.getType() == null) {
            messages.add(mValidationMessages.get(R.string.validation_type_mandatory));
        }

        if (transaction.getDate() == null || transaction.getDate().trim().length() == 0) {
            messages.add(mValidationMessages.get(R.string.validation_date_mandatory));
        }

        if (transaction.getAmount() == null || transaction.getAmount() <= 0.0) {
            messages.add(mValidationMessages.get(R.string.validation_amount_mandatory));
        }

        if (transaction.getCurrency() == null || transaction.getCurrency().trim().length() == 0) {
            messages.add(mValidationMessages.get(R.string.validation_currency_mandatory));
        }

        if (transaction.getExchangeRate() == null || transaction.getExchangeRate() <= 0.0) {
            messages.add(mValidationMessages.get(R.string.validation_exchange_rate_mandatory));
        }

        if (transaction.getPaymentMethod() == null) {
            messages.add(mValidationMessages.get(R.string.validation_payment_method_mandatory));
        }

        return ValidationStatus.create(Tools.join(messages, "\n"));
    }

}


package com.mg.incomeexpense.transaction;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.account.AccountValidator;
import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.category.CategoryListActivity;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ItemStateChangeHandler;
import com.mg.incomeexpense.core.ItemStateChangeListener;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.MultipleChoiceEventHandler;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mario on 2016-07-19.
 */
public class TransactionEditorFragment extends Fragment implements ItemStateChangeHandler {

    private static final String LOG_TAG = TransactionEditorFragment.class.getSimpleName();
    private static final int CATEGORY_LIST_ACTIVITY = 1;

    private final List<ItemStateChangeListener> mListeners = new ArrayList<>();
    private Transaction mTransaction = null;
    private TextView mTextViewValidationErrorMessage;
    private ObjectValidator mObjectValidator = null;

    private Spinner mSpinnerAccount;
    private AccountSpinnerAdapter mAccountSpinnerAdapter;

    private Spinner mSpinnerPaymentMethod;
    private PaymentMethodSpinnerAdapter mPaymentMethodSpinnerAdapter;

    private Spinner mSpinnerCurrency;
    private ArrayAdapter<CharSequence> mSpinnerCurrencyAdapter;

    private EditText mEditTextDate;
    private RadioGroup mRadioGroupType;

    private EditText mEditTextAmount;
    private EditText mEditTextExchangeRate;
    private EditText mEditTextNote;

    private ImageView mImageViewCategory;

    private TextView mTextViewCategory;

    private List<Account> mAccounts;
    private List<Category> mCategories;
    private List<PaymentMethod> mPaymentMethods;

    public TransactionEditorFragment() {

//        mSelectedContributors = new ArrayList<>();
//
//        mOnSwitchClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Switch s = (Switch) v;
//
//                s.setText(s.isChecked() ? getString(R.string.account_close) : getString(R.string.account_active));
//
//            }
//        };
//
//        mOnContributorImageButtonClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showContributorSetterDialog();
//            }
//        };
//
//        mContributorMultipleChoiceEventHandler = new MultipleChoiceEventHandler() { // Creating an anonymous Class Object
//            @Override
//            public void execute(boolean[] idSelected) {
//                mSelectedContributors.clear();
//                StringBuilder sb = new StringBuilder();
//                for (int i = 0; i < idSelected.length; i++) {
//                    if (idSelected[i]) {
//                        Contributor contributor = mAvailableContributors.get(i);
//                        mSelectedContributors.add(contributor);
//                        sb.append(String.format("%1$s%2$s", (sb.length() == 0 ? "" : ","), contributor.getName()));
//                    }
//                }
//                mTextViewContributors.setText(sb.toString());
//            }
//        };
    }

    public ObjectValidator getObjectValidator() {

        if (null == mObjectValidator) {
            mObjectValidator = TransactionValidator.create(getActivity());
        }

        return mObjectValidator;
    }

    public void setObjectValidator(ObjectValidator mObjectValidator) {
        this.mObjectValidator = mObjectValidator;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (null == bundle)
            throw new NullPointerException("A bundle is mandatory");

        mTransaction = (Transaction) bundle.getSerializable("item");
        if (null == mTransaction)
            throw new NullPointerException("An transaction is mandatory");

        mAccounts = (ArrayList<Account>)bundle.getSerializable("accounts");
        mCategories = (ArrayList<Category>)bundle.getSerializable("categories");
        mPaymentMethods = (ArrayList<PaymentMethod>) bundle.getSerializable("paymentMethods");

        mAccountSpinnerAdapter = new AccountSpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                mAccounts);

        mPaymentMethodSpinnerAdapter = new PaymentMethodSpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                mPaymentMethods);

        mSpinnerCurrencyAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.pref_currency_values,
                android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int year;
        int month;
        int day;

        View rootView = inflater.inflate(R.layout.transaction_editor_fragment, container, false);

        mSpinnerAccount = (Spinner) rootView.findViewById(R.id.spinner_account);
        mSpinnerAccount.setAdapter(mAccountSpinnerAdapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        mSpinnerAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Account user = mAccountSpinnerAdapter.getItem(position);
                // Here you can do the action you want to...
                Toast.makeText(getActivity(), "ID: " + user.getId() + "\nName: " + user.getName(),
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        mRadioGroupType = (RadioGroup) rootView.findViewById(R.id.radioGroupType);

        mSpinnerPaymentMethod = (Spinner) rootView.findViewById(R.id.spinner_payment_method);
        mSpinnerPaymentMethod.setAdapter(mPaymentMethodSpinnerAdapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        mSpinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Account user = mAccountSpinnerAdapter.getItem(position);
                // Here you can do the action you want to...
                Toast.makeText(getActivity(), "ID: " + user.getId() + "\nName: " + user.getName(),
                        Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        mSpinnerCurrency = (Spinner) rootView.findViewById(R.id.spinner_currency);
        mSpinnerCurrency.setAdapter(mSpinnerCurrencyAdapter);

        mTextViewValidationErrorMessage = (TextView) rootView.findViewById(R.id.textViewValidationErrorMessage);

        mEditTextDate = (EditText) rootView.findViewById(R.id.edit_text_date);

        mEditTextAmount = (EditText) rootView.findViewById(R.id.edit_text_amount);
        mEditTextExchangeRate = (EditText) rootView.findViewById(R.id.edit_text_exchange_rate);
        mEditTextNote = (EditText) rootView.findViewById(R.id.edit_text_note);

        mImageViewCategory = (ImageView) rootView.findViewById(R.id.image_view_category);
        mImageViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCategoryList();
            }
        });
        mTextViewCategory = (TextView) rootView.findViewById(R.id.text_view_category_name);

        if (null == savedInstanceState) {

            if(mTransaction.isNew()){
                // get the current date
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                mEditTextDate.setText(String.format("%d-%02d-%02d", year, month + 1,
                        day));
            }

//            mEditTextName.setText(mTransaction.getName());
//            mSwitchClose.setChecked(mTransaction.getIsClose());
//            mSwitchClose.setText(mTransaction.getIsClose() ? getString(R.string.account_close) : getString(R.string.account_active));
//            mTextViewContributors.setText(mTransaction.getContributorsForDisplay());
//            mSpinnerCurrency.setSelection(((ArrayAdapter<String>) mSpinnerCurrency.getAdapter()).getPosition(mTransaction.getCurrency()), false);
        }

        return rootView;
    }

    private void ShowCategoryList() {

        Intent intent = new Intent(getActivity(), CategoryListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("hideHomeButton", true);
        intent.putExtras(bundle);
        startActivityForResult(intent,CATEGORY_LIST_ACTIVITY);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode){
            case CATEGORY_LIST_ACTIVITY:
                if(data != null){
                    Category category = (Category)data.getSerializableExtra("item");

                    if(category != null){
                        mTextViewCategory.setText(category.getSelectedCategoryToDisplay());
                        mTextViewCategory.setTag(category);
                    }

                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

//        mSwitchClose.setOnClickListener(mOnSwitchClickListener);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor, menu);

        if (mTransaction.isNew()) {
            MenuItem mi = menu.findItem(R.id.action_delete);
            if (null != mi) {
                mi.setVisible(false);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_delete:
                mTransaction.setDead(true);
                notifyListener(new ItemStateChangeEvent(mTransaction));
                break;
            case R.id.action_save:

                Account account = (Account) mSpinnerAccount.getSelectedItem();
                mTransaction.setAccount(account);

                Category category = (Category)mTextViewCategory.getTag();
                if(category != null){
                    mTransaction.setCategory(category);
                }

                int selectedRadioButtonId = mRadioGroupType.getCheckedRadioButtonId();
                mTransaction.setType( selectedRadioButtonId == R.id.radioButtonExpense ? Transaction.TransactionType.Expense : Transaction.TransactionType.Income);

                PaymentMethod paymentMethod = (PaymentMethod) mSpinnerPaymentMethod.getSelectedItem();
                mTransaction.setPaymentMethod(paymentMethod);

                String currency = (String)mSpinnerCurrency.getSelectedItem();
                mTransaction.setCurrency(currency);

                String date = mEditTextDate.getText().toString();
                mTransaction.setDate(date);

                String amount = mEditTextAmount.getText().toString();
                if(amount.trim().length() > 0)
                    mTransaction.setAmount( Double.parseDouble(amount) );

                String exchangeRate = mEditTextExchangeRate.getText().toString();
                if(exchangeRate.trim().length() > 0)
                    mTransaction.setExchangeRate( Double.parseDouble(exchangeRate) );

                String note = mEditTextNote.getText().toString();
                mTransaction.setNote(note);

                ValidationStatus validationStatus = getObjectValidator().Validate(mTransaction);

                if (validationStatus.isValid()) {
                    notifyListener(new ItemStateChangeEvent(mTransaction));
                } else {
                    mTextViewValidationErrorMessage.setText(validationStatus.getMessage());
                    mTextViewValidationErrorMessage.setVisibility(View.VISIBLE);
                }
                break;
            case android.R.id.home:
                notifyListener(new ItemStateChangeEvent());
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }

    @Override
    public void addListener(ItemStateChangeListener listener) {

        if (null == listener)
            return;

        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }

    }

    @Override
    public void notifyListener(ItemStateChangeEvent event) {
        if (null == event)
            return;

        for (Object item : mListeners) {
            ((ItemStateChangeListener) item).onItemStateChange(event);
        }

    }



    private void showContributorSetterDialog() {

//        try {
//
//            CharSequence[] contributorArray = new CharSequence[mAvailableContributors.size()];
//            int i = 0;
//            for (Contributor contributor : mAvailableContributors) {
//                contributorArray[i++] = contributor.getName();
//            }
//
//            Dialog dialog = DialogUtils.childSetterDialog(
//                    this.getContext(),
//                    contributorArray,
//                    mContributorMultipleChoiceEventHandler,
//                    buildContributorsCheckedArray(mAvailableContributors, mSelectedContributors),
//                    getString(R.string.dialog_title_contributor_setter));
//
//            dialog.setOwnerActivity(this.getActivity());
//            dialog.show();
//        } catch (Exception exception) {
//            DialogUtils.messageBox(this.getContext(),
//                    getString(R.string.error_unable_to_fetch_all_contributor),
//                    getString(R.string.dialog_title_contributor_setter)).show();
//
//        }

    }

}

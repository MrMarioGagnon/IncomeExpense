package com.mg.incomeexpense.transaction;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.category.CategoryListActivity;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.DatePickerFragment;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ItemStateChangeHandler;
import com.mg.incomeexpense.core.ItemStateChangeListener;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.MultipleChoiceEventHandler;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by mario on 2016-07-19.
 */
public class TransactionEditorFragment extends Fragment implements ItemStateChangeHandler, DatePickerDialog.OnDateSetListener {

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

    private TextView mTextViewCurrency;

    private TextView mTextViewDate;
    private RadioGroup mRadioGroupType;
    private RadioButton mRadioButtonExpense;
    private RadioButton mRadioButtonIncome;

    private EditText mEditTextAmount;
    private EditText mEditTextExchangeRate;
    private EditText mEditTextNote;

    private ImageView mImageViewCategory;

    private ImageView mImageViewDate;

    private TextView mTextViewCategory;

    private List<Account> mAccounts;
    private List<PaymentMethod> mPaymentMethods;

    private ImageButton mImageButtonContributors;
    private View.OnClickListener mOnContributorImageButtonClickListener;
    private MultipleChoiceEventHandler mContributorMultipleChoiceEventHandler;
    private TextView mTextViewContributors;
    private List<Contributor> mAvailableContributors;
    private List<Contributor> mSelectedContributors;


    public TransactionEditorFragment() {
        mSelectedContributors = new ArrayList<>();
        mOnContributorImageButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContributorSetterDialog();
            }
        };

        mContributorMultipleChoiceEventHandler = new MultipleChoiceEventHandler() { // Creating an anonymous Class Object
            @Override
            public void execute(boolean[] idSelected) {
                mSelectedContributors.clear();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < idSelected.length; i++) {
                    if (idSelected[i]) {
                        Contributor contributor = mAvailableContributors.get(i);
                        mSelectedContributors.add(contributor);
                        sb.append(String.format("%1$s%2$s", (sb.length() == 0 ? "" : ","), contributor.getName()));
                    }
                }
                mTextViewContributors.setText(sb.toString());
            }
        };

    }

    public ObjectValidator getObjectValidator() {

        if (null == mObjectValidator) {
            mObjectValidator = TransactionValidator.create(getActivity());
        }

        return mObjectValidator;
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

        mAccounts = (ArrayList<Account>) bundle.getSerializable("accounts");
        mPaymentMethods = (ArrayList<PaymentMethod>) bundle.getSerializable("paymentMethods");

        mAccountSpinnerAdapter = new AccountSpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                mAccounts);

        mPaymentMethodSpinnerAdapter = new PaymentMethodSpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                mPaymentMethods);

        mAvailableContributors = (List<Contributor>) bundle.getSerializable("contributors");
        if (null == mAvailableContributors)
            throw new NullPointerException("A list of contributors is mandatory");

        mSelectedContributors.addAll(mTransaction.getContributors());

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

        mRadioGroupType = (RadioGroup) rootView.findViewById(R.id.radioGroupType);

        mSpinnerPaymentMethod = (Spinner) rootView.findViewById(R.id.spinner_payment_method);
        mSpinnerPaymentMethod.setAdapter(mPaymentMethodSpinnerAdapter); // Set the custom adapter to the spinner
        // You can create an anonymous listener to handle the event when is selected an spinner item
        mSpinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {

                PaymentMethod paymentMethod = mPaymentMethodSpinnerAdapter.getItem(position);
                Double exchangeRate = paymentMethod.getExchangeRate();
                mEditTextExchangeRate.setEnabled(exchangeRate == 1.0 ? true : false);
                mEditTextExchangeRate.setText(exchangeRate.toString());

                mTextViewCurrency.setText(paymentMethod.getCurrency());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapter) {
            }
        });

        mTextViewCurrency = (TextView) rootView.findViewById(R.id.text_view_currency);

        mTextViewValidationErrorMessage = (TextView) rootView.findViewById(R.id.textViewValidationErrorMessage);

        mTextViewDate = (TextView) rootView.findViewById(R.id.text_view_date);

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

        mImageViewDate = (ImageView) rootView.findViewById(R.id.image_view_date);
        mImageViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCalendar();
            }
        });

        mRadioButtonExpense = (RadioButton) rootView.findViewById(R.id.radioButtonExpense);
        mRadioButtonIncome = (RadioButton) rootView.findViewById(R.id.radioButtonIncome);

        mImageButtonContributors = (ImageButton) rootView.findViewById(R.id.imagebutton_contributors);
        mImageButtonContributors.setOnClickListener(mOnContributorImageButtonClickListener);

        mTextViewContributors = (TextView) rootView.findViewById(R.id.textview_contributors);

        if (null == savedInstanceState) {

            if (mTransaction.isNew()) {
                // get the current date
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                mTransaction.setDate(String.format("%d-%02d-%02d", year, month + 1,
                        day));
                mTransaction.setCurrency(Tools.getDefaultCurrency(getActivity()));
            }

            Tools.setSpinner(mTransaction.getAccount(), mSpinnerAccount);
            if (mTransaction.getCategory() != null)
                mTextViewCategory.setText(mTransaction.getCategory().getSelectedCategoryToDisplay());
            if (mTransaction.getType() == Transaction.TransactionType.Expense) {
                mRadioButtonExpense.setChecked(true);
            } else {
                mRadioButtonIncome.setChecked(true);
            }
            mTextViewDate.setText(mTransaction.getDate());
            mEditTextAmount.setText(mTransaction.getAmount().toString());
            mEditTextExchangeRate.setText(mTransaction.getExchangeRate().toString());
            Tools.setSpinner(mTransaction.getPaymentMethod(), mSpinnerPaymentMethod);
            mEditTextNote.setText(mTransaction.getNote());
            mTextViewContributors.setText(mTransaction.getContributorsForDisplay());
            mSpinnerAccount.setEnabled(false);

        }

        return rootView;
    }

    private void ShowCategoryList() {

        Intent intent = new Intent(getActivity(), CategoryListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("hideHomeButton", true);
        intent.putExtras(bundle);
        startActivityForResult(intent, CATEGORY_LIST_ACTIVITY);

    }

    private void ShowCalendar() {

        DatePickerFragment picker = new DatePickerFragment();
        picker.setListener(this);

        picker.show(getFragmentManager(), "datePicker");


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CATEGORY_LIST_ACTIVITY:
                if (data != null) {
                    Category category = (Category) data.getSerializableExtra("item");

                    if (category != null) {
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

                Category category = (Category) mTextViewCategory.getTag();
                if (category != null) {
                    mTransaction.setCategory(category);
                }

                int selectedRadioButtonId = mRadioGroupType.getCheckedRadioButtonId();
                mTransaction.setType(selectedRadioButtonId == R.id.radioButtonExpense ? Transaction.TransactionType.Expense : Transaction.TransactionType.Income);

                PaymentMethod paymentMethod = (PaymentMethod) mSpinnerPaymentMethod.getSelectedItem();
                mTransaction.setPaymentMethod(paymentMethod);

                String currency = mTextViewCurrency.getText().toString();
                mTransaction.setCurrency(currency);

                String date = mTextViewDate.getText().toString();
                mTransaction.setDate(date);

                String amount = mEditTextAmount.getText().toString();
                if (amount.trim().length() > 0)
                    mTransaction.setAmount(Double.parseDouble(amount));

                String exchangeRate = mEditTextExchangeRate.getText().toString();
                if (exchangeRate.trim().length() > 0)
                    mTransaction.setExchangeRate(Double.parseDouble(exchangeRate));

                String note = mEditTextNote.getText().toString();
                mTransaction.setNote(note);

                mTransaction.setContributors(mSelectedContributors);

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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = sdf.format(c.getTime());

        mTextViewDate.setText(formattedDate);


    }

    private void showContributorSetterDialog() {

        try {

            CharSequence[] contributorArray = new CharSequence[mAvailableContributors.size()];
            int i = 0;
            for (Contributor contributor : mAvailableContributors) {
                contributorArray[i++] = contributor.getName();
            }

            Dialog dialog = DialogUtils.childSetterDialog(
                    this.getContext(),
                    contributorArray,
                    mContributorMultipleChoiceEventHandler,
                    buildContributorsCheckedArray(mAvailableContributors, mSelectedContributors),
                    getString(R.string.dialog_title_contributor_setter));

            dialog.setOwnerActivity(this.getActivity());
            dialog.show();
        } catch (Exception exception) {
            DialogUtils.messageBox(this.getContext(),
                    getString(R.string.error_unable_to_fetch_all_contributor),
                    getString(R.string.dialog_title_contributor_setter)).show();

        }

    }

    private boolean[] buildContributorsCheckedArray(List<Contributor> availableContributors,
                                                    List<Contributor> selectedContributors) {

        boolean[] checked = new boolean[availableContributors.size()];

        for (Contributor contributor : selectedContributors) {
            int index = availableContributors.indexOf(contributor);
            if (index >= 0) {
                checked[index] = true;
            }
        }

        return checked;

    }

}

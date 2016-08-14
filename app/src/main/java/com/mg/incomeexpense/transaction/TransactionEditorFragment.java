package com.mg.incomeexpense.transaction;

import android.app.Dialog;
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
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.account.AccountValidator;
import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ItemStateChangeHandler;
import com.mg.incomeexpense.core.ItemStateChangeListener;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.MultipleChoiceEventHandler;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-07-19.
 */
public class TransactionEditorFragment extends Fragment implements ItemStateChangeHandler {

    private static final String LOG_TAG = TransactionEditorFragment.class.getSimpleName();

    private final List<ItemStateChangeListener> mListeners = new ArrayList<>();
    private Transaction mTransaction = null;
    private TextView mTextViewValidationErrorMessage;
    private ObjectValidator mObjectValidator = null;

    private Spinner mSpinnerAccount;
    private AccountSpinnerAdapter mAccountSpinnerAdapter;

    private Spinner mSpinnerPaymentMethod;
    private PaymentMethodSpinnerAdapter mPaymentMethodSpinnerAdapter;

    private List<Account> mAccounts;
    private List<Category> mCategories;
    private List<PaymentMethod> mPaymentMethods;

//    private EditText mEditTextName;

//    private ArrayList<String> mNames;
//    private Spinner mSpinnerCurrency;
//    private ArrayAdapter<CharSequence> mSpinnerCurrencyAdapter;
//    private Switch mSwitchClose;
//    private View.OnClickListener mOnSwitchClickListener;
//    private ImageButton mImageButtonContributors;
//    private View.OnClickListener mOnContributorImageButtonClickListener;
//    private MultipleChoiceEventHandler mContributorMultipleChoiceEventHandler;
//    private TextView mTextViewContributors;
//    private List<Contributor> mAvailableContributors;
//    private List<Contributor> mSelectedContributors;

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
                android.R.layout.simple_spinner_item,
                mAccounts);

        mPaymentMethodSpinnerAdapter = new PaymentMethodSpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_item,
                mPaymentMethods);


//        mNames = (ArrayList<String>) bundle.getSerializable("names");
//        if (null == mNames)
//            throw new NullPointerException("A list of accounts name is mandatory");
//
//        mAvailableContributors = (List<Contributor>) bundle.getSerializable("contributors");
//        if (null == mAvailableContributors)
//            throw new NullPointerException("A list of contributors is mandatory");
//
//        mSelectedContributors.addAll(mTransaction.getContributors());
//        mSpinnerCurrencyAdapter = ArrayAdapter.createFromResource(
//                getActivity(), R.array.pref_currency_values,
//                android.R.layout.simple_spinner_item);
//        mSpinnerCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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



//        mEditTextName = (EditText) rootView.findViewById(R.id.edittext_account_name);
//        mSpinnerCurrency = (Spinner) rootView.findViewById(R.id.spinner_currency);
//        mSpinnerCurrency.setAdapter(mSpinnerCurrencyAdapter);
//
//        mSwitchClose = (Switch) rootView.findViewById(R.id.switch_close);

        mTextViewValidationErrorMessage = (TextView) rootView.findViewById(R.id.textViewValidationErrorMessage);

//        mImageButtonContributors = (ImageButton) rootView.findViewById(R.id.imagebutton_contributors);
//        mImageButtonContributors.setOnClickListener(mOnContributorImageButtonClickListener);
//
//        mTextViewContributors = (TextView) rootView.findViewById(R.id.textview_contributors);

        if (null == savedInstanceState) {
//            mEditTextName.setText(mTransaction.getName());
//            mSwitchClose.setChecked(mTransaction.getIsClose());
//            mSwitchClose.setText(mTransaction.getIsClose() ? getString(R.string.account_close) : getString(R.string.account_active));
//            mTextViewContributors.setText(mTransaction.getContributorsForDisplay());
//            mSpinnerCurrency.setSelection(((ArrayAdapter<String>) mSpinnerCurrency.getAdapter()).getPosition(mTransaction.getCurrency()), false);
        }

        return rootView;
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
//                mTransaction.setName(mEditTextName.getText().toString());
//                mTransaction.setCurrency((String) mSpinnerCurrency
//                        .getSelectedItem());
//                mTransaction.setIsClose(mSwitchClose.isChecked());
//
//
//                mTransaction.setContributors(mSelectedContributors);

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

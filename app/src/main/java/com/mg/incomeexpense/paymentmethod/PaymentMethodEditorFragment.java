package com.mg.incomeexpense.paymentmethod;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.contributor.ContributorSpinnerAdapter;
import com.mg.incomeexpense.core.FragmentBase;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;
import com.mg.incomeexpense.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-07-19.
 */
public class PaymentMethodEditorFragment extends FragmentBase {

    private static final String LOG_TAG = PaymentMethodEditorFragment.class.getSimpleName();

    private PaymentMethod mPaymentMethod = null;
    private EditText mEditTextName;
    private EditText mEditTextExchangeRate;
    private TextView mTextViewValidationErrorMessage;
    private ObjectValidator mObjectValidator = null;
    private ArrayList<String> mNames;
    private Spinner mSpinnerCurrency;
    private ArrayAdapter<CharSequence> mSpinnerCurrencyAdapter;
    private Switch mSwitchClose;
    private View.OnClickListener mOnSwitchClickListener;

    private List<Contributor> mOwners;
    private Spinner mSpinnerOwner;
    private ContributorSpinnerAdapter mOwnerSpinnerAdapter;

    public PaymentMethodEditorFragment() {

        mOnSwitchClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Switch s = (Switch) v;

                s.setText(s.isChecked() ? getString(R.string.account_close) : getString(R.string.account_active));

            }
        };

    }

    public ObjectValidator getObjectValidator() {

        if (null == mObjectValidator) {
            mObjectValidator = PaymentMethodValidator.create(getActivity(), mNames);
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

        mPaymentMethod = (PaymentMethod) bundle.getSerializable("item");
        if (null == mPaymentMethod)
            throw new NullPointerException("A payment method is mandatory");

        mNames = (ArrayList<String>) bundle.getSerializable("names");
        if (null == mNames)
            throw new NullPointerException("A list of payment methods name is mandatory");

        mOwners = (ArrayList<Contributor>) bundle.getSerializable("contributors");
        if (null == mOwners)
            throw new NullPointerException("A list of contributors is mandatory");


        mOwnerSpinnerAdapter = new ContributorSpinnerAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                mOwners);

        mSpinnerCurrencyAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.pref_currency_values,
                android.R.layout.simple_spinner_item);
        mSpinnerCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.payment_method_editor_fragment, container, false);
        mEditTextName = (EditText) rootView.findViewById(R.id.edittext_payment_method_name);
        mEditTextExchangeRate = (EditText) rootView.findViewById(R.id.edittext_exchange_rate);
        mSpinnerCurrency = (Spinner) rootView.findViewById(R.id.spinner_currency);
        mSpinnerCurrency.setAdapter(mSpinnerCurrencyAdapter);

        mSwitchClose = (Switch) rootView.findViewById(R.id.switch_close);

        mTextViewValidationErrorMessage = (TextView) rootView.findViewById(R.id.textViewValidationErrorMessage);

        mSpinnerOwner = (Spinner) rootView.findViewById(R.id.spinner_owner);
        mSpinnerOwner.setAdapter(mOwnerSpinnerAdapter); // Set the custom adapter to the spinner

        if (null == savedInstanceState) {
            mEditTextName.setText(mPaymentMethod.getName());
            mEditTextExchangeRate.setText(mPaymentMethod.getExchangeRate().toString());
            mSwitchClose.setChecked(mPaymentMethod.getIsClose());
            mSwitchClose.setText(mPaymentMethod.getIsClose() ? getString(R.string.account_close) : getString(R.string.account_active));
            Tools.setSpinner(mPaymentMethod.getOwner(), mSpinnerOwner);
            mSpinnerCurrency.setSelection(((ArrayAdapter<String>) mSpinnerCurrency.getAdapter()).getPosition(mPaymentMethod.getCurrency()), false);

        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mSwitchClose.setOnClickListener(mOnSwitchClickListener);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor, menu);

        if (mPaymentMethod.isNew()) {
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

                List<Transaction> transactions = IncomeExpenseRequestWrapper.getAllTransactionForPaymentMethod(getActivity().getContentResolver(), mPaymentMethod);

                if (((PaymentMethodValidator) getObjectValidator()).canDelete(transactions)) {
                    mPaymentMethod.setDead(true);
                    notifyListener(new ItemStateChangeEvent(mPaymentMethod));
                } else {
                    String message = getString(R.string.error_foreign_key_constraint, getString(R.string.payment_method), mPaymentMethod.getName());
                    mTextViewValidationErrorMessage.setText(message);
                    mTextViewValidationErrorMessage.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.action_save:
                mPaymentMethod.setName(mEditTextName.getText().toString());
                mPaymentMethod.setExchangeRate(Double.parseDouble(mEditTextExchangeRate.getText().toString()));
                mPaymentMethod.setCurrency((String) mSpinnerCurrency
                        .getSelectedItem());
                mPaymentMethod.setIsClose(mSwitchClose.isChecked());


                mPaymentMethod.setOwner((Contributor) mSpinnerOwner.getSelectedItem());

                ValidationStatus validationStatus = getObjectValidator().Validate(mPaymentMethod);

                if (validationStatus.isValid()) {
                    notifyListener(new ItemStateChangeEvent(mPaymentMethod));
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

}

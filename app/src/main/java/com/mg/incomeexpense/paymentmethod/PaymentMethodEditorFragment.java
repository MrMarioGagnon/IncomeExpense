package com.mg.incomeexpense.paymentmethod;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.contributor.ContributorSpinnerAdapter;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ItemStateChangeHandler;
import com.mg.incomeexpense.core.ItemStateChangeListener;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.MultipleChoiceEventHandler;
import com.mg.incomeexpense.transaction.AccountSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-07-19.
 */
public class PaymentMethodEditorFragment extends Fragment implements ItemStateChangeHandler {

    private static final String LOG_TAG = PaymentMethodEditorFragment.class.getSimpleName();

    private final List<ItemStateChangeListener> mListeners = new ArrayList<>();
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
//    private ImageButton mImageButtonContributors;
//    private View.OnClickListener mOnContributorImageButtonClickListener;
//    private MultipleChoiceEventHandler mContributorMultipleChoiceEventHandler;
//    private TextView mTextViewContributors;
//    private List<Contributor> mAvailableContributors;
//    private List<Contributor> mSelectedContributors;

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
                mPaymentMethod.setDead(true);
                notifyListener(new ItemStateChangeEvent(mPaymentMethod));
                break;
            case R.id.action_save:
                mPaymentMethod.setName(mEditTextName.getText().toString());
                mPaymentMethod.setExchangeRate(Double.parseDouble(mEditTextExchangeRate.getText().toString()));
                mPaymentMethod.setCurrency((String) mSpinnerCurrency
                        .getSelectedItem());
                mPaymentMethod.setIsClose(mSwitchClose.isChecked());


                mPaymentMethod.setContributors(mSelectedContributors);

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

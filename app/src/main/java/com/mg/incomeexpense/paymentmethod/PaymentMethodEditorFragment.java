package com.mg.incomeexpense.paymentmethod;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
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

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.AppCompatActivityBase;
import com.mg.incomeexpense.core.FragmentBase;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.SingleChoiceEventHandler;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;
import com.mg.incomeexpense.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-07-19.
 */
public class PaymentMethodEditorFragment extends FragmentBase {

    private PaymentMethod mPaymentMethod = null;
    private EditText mEditTextName;
    private EditText mEditTextExchangeRate;
    private PaymentMethodValidator mObjectValidator;
    private ArrayList<String> mNames;
    private Spinner mSpinnerCurrency;
    private ArrayAdapter<CharSequence> mSpinnerCurrencyAdapter;
    private Switch mSwitchClose;
    private View.OnClickListener mOnSwitchClickListener;

    private List<Contributor> mOwners;
    private Spinner mSpinnerOwner;
    private ArrayAdapter<Contributor> mOwnerSpinnerAdapter;

    private View mRootEditorView;

    public PaymentMethodEditorFragment() {

        mOnSwitchClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Switch s = (Switch) v;

                s.setText(s.isChecked() ? getString(R.string.account_close) : getString(R.string.account_active));

            }
        };

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        Objects.requireNonNull(bundle, "A bundle is mandatory");

        mPaymentMethod = (PaymentMethod) bundle.getSerializable("item");
        Objects.requireNonNull(mPaymentMethod, "A payment method is mandatory");

        mNames = (ArrayList<String>) bundle.getSerializable("names");
        Objects.requireNonNull(mNames, "A list of payment methods name is mandatory");

        mOwners = (ArrayList<Contributor>) bundle.getSerializable("contributors");
        Objects.requireNonNull(mOwners, "A list of contributors is mandatory");

        mObjectValidator = PaymentMethodValidator.create(getActivity(), mNames);

        mOwnerSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, mOwners);
        mOwnerSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        mSpinnerCurrencyAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.pref_currency_values,
                android.R.layout.simple_spinner_item);
        mSpinnerCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivityBase) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(mPaymentMethod.isNew() ? R.string.title_payment_method_editor_add : R.string.title_payment_method_editor_update));
        }

        View rootView = inflater.inflate(R.layout.payment_method_editor_fragment, container, false);
        mEditTextName = (EditText) rootView.findViewById(R.id.edit_text_payment_method_name);
        mEditTextExchangeRate = (EditText) rootView.findViewById(R.id.edit_text_exchange_rate);
        mSpinnerCurrency = (Spinner) rootView.findViewById(R.id.spinner_currency);
        mSpinnerCurrency.setAdapter(mSpinnerCurrencyAdapter);

        mSwitchClose = (Switch) rootView.findViewById(R.id.switch_close);

        mSpinnerOwner = (Spinner) rootView.findViewById(R.id.spinner_owner);
        mSpinnerOwner.setAdapter(mOwnerSpinnerAdapter); // Set the custom adapter to the spinner

        mEditTextName.setText(mPaymentMethod.getName());
        mEditTextExchangeRate.setText(mPaymentMethod.getExchangeRate().toString());
        mSwitchClose.setChecked(mPaymentMethod.getIsClose());
        mSwitchClose.setText(mPaymentMethod.getIsClose() ? getString(R.string.account_close) : getString(R.string.account_active));
        Tools.setSpinner(mPaymentMethod.getOwner(), mSpinnerOwner);
        mSpinnerCurrency.setSelection(((ArrayAdapter<String>) mSpinnerCurrency.getAdapter()).getPosition(mPaymentMethod.getCurrency()), false);

        mRootEditorView = rootView.findViewById(R.id.root_editor_view);

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

                DialogUtils.twoButtonMessageBox(getContext(), getString(R.string.ask_delete_payment_method), getString(R.string.dialog_title_deleting_payment_method), new SingleChoiceEventHandler() {
                    @Override
                    public void execute(int idSelected) {

                        List<Transaction> transactions = IncomeExpenseRequestWrapper.getAllTransactionForPaymentMethod(getActivity(), mPaymentMethod);

                        if (mObjectValidator.canDelete(transactions)) {
                            mPaymentMethod.setDead(true);
                            notifyListener(new ItemStateChangeEvent(mPaymentMethod, false));
                        } else {
                            String message = getString(R.string.error_foreign_key_constraint, getString(R.string.payment_method), mPaymentMethod.getName());
                            displayMessage(mRootEditorView, message);
                        }

                    }
                }, new SingleChoiceEventHandler() {
                    @Override
                    public void execute(int idSelected) {
                        // Do nothing
                    }
                }).show();

                break;
            case R.id.action_save:
                mPaymentMethod.setName(mEditTextName.getText().toString());
                mPaymentMethod.setExchangeRate(Double.parseDouble(mEditTextExchangeRate.getText().toString()));
                mPaymentMethod.setCurrency((String) mSpinnerCurrency
                        .getSelectedItem());
                mPaymentMethod.setIsClose(mSwitchClose.isChecked());


                mPaymentMethod.setOwner((Contributor) mSpinnerOwner.getSelectedItem());

                ValidationStatus validationStatus = mObjectValidator.Validate(mPaymentMethod);

                if (validationStatus.isValid()) {
                    notifyListener(new ItemStateChangeEvent(mPaymentMethod, false));
                } else {

                    displayMessage(mRootEditorView, validationStatus.getMessage());

                }
                break;
            case android.R.id.home:
                notifyListener(new ItemStateChangeEvent(mPaymentMethod, true));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }

}

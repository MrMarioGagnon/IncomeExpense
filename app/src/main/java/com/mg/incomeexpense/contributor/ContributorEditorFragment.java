package com.mg.incomeexpense.contributor;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.core.AppCompatActivityBase;
import com.mg.incomeexpense.core.FragmentBase;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.SingleChoiceEventHandler;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;
import com.mg.incomeexpense.paymentmethod.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-07-19.
 */
public class ContributorEditorFragment extends FragmentBase {

    private Contributor mContributor = null;
    private EditText mEditTextName;
    private TextView mTextViewValidationErrorMessage;
    private ContributorValidator mObjectValidator = null;
    private ArrayList<String> mNames;

    public ContributorEditorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (null == bundle)
            throw new NullPointerException("A bundle is mandatory");

        mContributor = (Contributor) bundle.getSerializable("item");
        if (null == mContributor)
            throw new NullPointerException("A contributor is mandatory");

        mNames = (ArrayList<String>) bundle.getSerializable("names");
        if (null == mNames)
            throw new NullPointerException("A list of contributors name is mandatory");

        mObjectValidator = ContributorValidator.create(getActivity(), mNames);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivityBase) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(mContributor.isNew() ? R.string.title_contributor_editor_add : R.string.title_contributor_editor_update));
        }

        View rootView = inflater.inflate(R.layout.contributor_editor_fragment, container, false);
        mEditTextName = (EditText) rootView.findViewById(R.id.edittext_contributor_name);
        mTextViewValidationErrorMessage = (TextView) rootView.findViewById(R.id.textViewValidationErrorMessage);

        mEditTextName.setText(mContributor.getName());

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor, menu);

        if (mContributor.isNew()) {
            MenuItem mi = menu.findItem(R.id.action_delete);
            if (null != mi) {
                mi.setVisible(false);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        ValidationStatus validationStatus;

        switch (id) {
            case R.id.action_delete:

                DialogUtils.twoButtonMessageBox(getContext(), getString(R.string.ask_delete_contributor), getString(R.string.dialog_title_deleting_contributor), new SingleChoiceEventHandler() {
                    @Override
                    public void execute(int idSelected) {

                        List<Account> accounts = IncomeExpenseRequestWrapper.getAvailableAccounts(getActivity().getContentResolver());
                        List<PaymentMethod> paymentMethods = IncomeExpenseRequestWrapper.getAvailablePaymentMethods(getActivity().getContentResolver(), null);

                        ValidationStatus validationStatus = mObjectValidator.canDelete(mContributor, accounts, paymentMethods);

                        if (validationStatus.isValid()) {
                            mContributor.setDead(true);
                            notifyListener(new ItemStateChangeEvent(mContributor, false));
                        } else {
                            mTextViewValidationErrorMessage.setText(validationStatus.getMessage());
                            mTextViewValidationErrorMessage.setVisibility(View.VISIBLE);
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
                mContributor.setName(mEditTextName.getText().toString());
                validationStatus = mObjectValidator.Validate(mContributor);

                if (validationStatus.isValid()) {
                    notifyListener(new ItemStateChangeEvent(mContributor, false));
                } else {
                    mTextViewValidationErrorMessage.setText(validationStatus.getMessage());
                    mTextViewValidationErrorMessage.setVisibility(View.VISIBLE);
                }
                break;
            case android.R.id.home:
                notifyListener(new ItemStateChangeEvent(mContributor, true));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }

}

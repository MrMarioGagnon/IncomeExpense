package com.mg.incomeexpense.account;

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
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.contributor.ContributorValidator;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ItemStateChangeHandler;
import com.mg.incomeexpense.core.ItemStateChangeListener;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.MultipleChoiceEventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by mario on 2016-07-19.
 */
public class AccountEditorFragment extends Fragment implements ItemStateChangeHandler {

    private static final String LOG_TAG = AccountEditorFragment.class.getSimpleName();

    private final List<ItemStateChangeListener> mListeners = new ArrayList<>();
    private Account mAccount = null;
    private EditText mEditTextName;
    private TextView mTextViewValidationErrorMessage;
    private ObjectValidator mObjectValidator = null;
    private ArrayList<String> mNames;
    private Spinner mSpinnerCurrency;
    private ArrayAdapter<CharSequence> mSpinnerCurrencyAdapter;
    private Switch mSwitchClose;
    private View.OnClickListener mOnSwitchClickListener;
    private ImageButton mImageButtonContributors;
    private View.OnClickListener mOnContributorImageButtonClickListener;
    private MultipleChoiceEventHandler mContributorMultipleChoiceEventHandler;
    private TextView mTextViewContributors;
    private SortedSet<Contributor> mContributors;
    private boolean[] mSelectedContributor;

    public ObjectValidator getObjectValidator() {

        if (null == mObjectValidator) {
            mObjectValidator = AccountValidator.create(getActivity(), mNames);
        }

        return mObjectValidator;
    }

    public void setObjectValidator(ObjectValidator mObjectValidator) {
        this.mObjectValidator = mObjectValidator;
    }

    public AccountEditorFragment() {
        mOnSwitchClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Switch s = (Switch)v;

                s.setText(s.isChecked() ? getString(R.string.account_close) : getString(R.string.account_active));

            }
        };

        mOnContributorImageButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContributorSetterDialog();
            }
        };

        mContributorMultipleChoiceEventHandler = new MultipleChoiceEventHandler() { // Creating an anonymous Class Object
            @Override
            public void execute(boolean[] idSelected) {
                Contributor[] a = new Contributor[mContributors.size()];
                mContributors.toArray(a);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < idSelected.length; i++) {
                    if (idSelected[i]) {
                        sb.append(String.format("%1$s%2$s", (sb.length() == 0 ? "" : ","), a[i].getName()));
                    }
                }
                mTextViewContributors.setText(sb.toString());
                mSelectedContributor = idSelected;
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        if (null == bundle)
            throw new NullPointerException("A bundle is mandatory");

        mAccount = (Account) bundle.getSerializable("item");
        if (null == mAccount)
            throw new NullPointerException("An account is mandatory");

        mNames = (ArrayList<String>) bundle.getSerializable("names");
        if(null == mNames)
            throw new NullPointerException("A list of accounts name is mandatory");

        mContributors = (SortedSet<Contributor>) bundle.getSerializable("contributors");
        if(null == mContributors)
            throw new NullPointerException("A list of contributors is mandatory");

        mSpinnerCurrencyAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.pref_currency_values,
                android.R.layout.simple_spinner_item);
        mSpinnerCurrencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.account_editor_fragment, container, false);
        mEditTextName = (EditText) rootView.findViewById(R.id.edittext_account_name);
        mSpinnerCurrency = (Spinner) rootView.findViewById(R.id.spinner_currency);
        mSpinnerCurrency.setAdapter(mSpinnerCurrencyAdapter);

        mSwitchClose = (Switch) rootView.findViewById(R.id.switch_close);

        mTextViewValidationErrorMessage = (TextView) rootView.findViewById(R.id.textViewValidationErrorMessage);

        mImageButtonContributors = (ImageButton) rootView.findViewById(R.id.imagebutton_contributors);
        mImageButtonContributors.setOnClickListener(mOnContributorImageButtonClickListener);

        mTextViewContributors = (TextView) rootView.findViewById(R.id.textview_contributors);

        if (null == savedInstanceState) {
            mEditTextName.setText(mAccount.getName());
            mSwitchClose.setChecked(mAccount.getIsClose());
            mSwitchClose.setText(mAccount.getIsClose() ? getString(R.string.account_close) : getString(R.string.account_active));
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    //    mSpinnerCurrency.setOnItemSelectedListener(mOnItemSelectedListener);
        mSwitchClose.setOnClickListener(mOnSwitchClickListener);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor, menu);

        if (mAccount.isNew()) {
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
                mAccount.setDead(true);
                notifyListener(new ItemStateChangeEvent(mAccount));
                break;
            case R.id.action_save:
                mAccount.setName(mEditTextName.getText().toString());
                mAccount.setCurrency((String) mSpinnerCurrency
                        .getSelectedItem());
                mAccount.setIsClose( mSwitchClose.isChecked()  );

                // if not null, Contributors Dialog Box was call
                if (mSelectedContributor != null) {
                    Contributor[] a = new Contributor[mContributors.size()];
                    mContributors.toArray(a);

                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < mSelectedContributor.length; i++) {
                        if (mSelectedContributor[i]) {
                            if(sb.length() == 0)
                                sb.append(a[i].getId());
                            else
                                sb.append(String.format(";%d",a[i].getId()));
                        }
                    }
                }

                ValidationStatus validationStatus = getObjectValidator().Validate(mAccount);

                if (validationStatus.isValid()) {
                    notifyListener(new ItemStateChangeEvent(mAccount));
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

            CharSequence[] contributorArray = new CharSequence[mContributors.size()];
            int i = 0;
            for (Contributor contributor : mContributors) {
                contributorArray[i++] = contributor.getName();
            }

            Dialog dialog = DialogUtils.childSetterDialog(
                    this.getContext(),
                    contributorArray,
                    mContributorMultipleChoiceEventHandler,
                    buildContributorsCheckedArray(mContributors, mAccount.getContributorsForDisplay()),
                    getString(R.string.dialog_title_contributor_setter));

            dialog.setOwnerActivity(this.getActivity());
            dialog.show();
        } catch (Exception exception) {
            DialogUtils.messageBox(this.getContext(),
                    getString(R.string.error_unable_to_fetch_all_contributor),
                    getString(R.string.dialog_title_contributor_setter)).show();

        }

    }

    private boolean[] buildContributorsCheckedArray(SortedSet<Contributor> contributors,
                                                    String contributorsName) {

        boolean[] checked = new boolean[contributors.size()];

        int i = 0;
        for (Contributor contributor : contributors) {

            checked[i] = contributorsName.contains(contributor.getName());
            i++;
        }

        return checked;

    }
}

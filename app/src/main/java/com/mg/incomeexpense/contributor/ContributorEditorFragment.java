package com.mg.incomeexpense.contributor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.FragmentBase;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.ValidationStatus;

import java.util.ArrayList;

/**
 * Created by mario on 2016-07-19.
 */
public class ContributorEditorFragment extends FragmentBase {

    private static final String LOG_TAG = ContributorEditorFragment.class.getSimpleName();

    private Contributor mContributor = null;
    private EditText mEditTextName;
    private TextView mTextViewValidationErrorMessage;
    private ObjectValidator mObjectValidator = null;
    private ArrayList<String> mNames;

    public ContributorEditorFragment() {
        // Required empty public constructor
    }

    public ObjectValidator getObjectValidator() {

        if (null == mObjectValidator) {
            mObjectValidator = ContributorValidator.create(getActivity(), mNames);
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

        mContributor = (Contributor) bundle.getSerializable("item");
        if (null == mContributor)
            throw new NullPointerException("A contributor is mandatory");

        mNames = (ArrayList<String>) bundle.getSerializable("names");
        if (null == mNames)
            throw new NullPointerException("A list of contributors name is mandatory");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.contributor_editor_fragment, container, false);
        mEditTextName = (EditText) rootView.findViewById(R.id.edittext_contributor_name);
        mTextViewValidationErrorMessage = (TextView) rootView.findViewById(R.id.textViewValidationErrorMessage);

        if (null == savedInstanceState) {
            mEditTextName.setText(mContributor.getName());
        }

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

        switch (id) {
            case R.id.action_delete:
                mContributor.setDead(true);
                notifyListener(new ItemStateChangeEvent(mContributor));
                break;
            case R.id.action_save:
                mContributor.setName(mEditTextName.getText().toString());
                ValidationStatus validationStatus = getObjectValidator().Validate(mContributor);

                if (validationStatus.isValid()) {
                    notifyListener(new ItemStateChangeEvent(mContributor));
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

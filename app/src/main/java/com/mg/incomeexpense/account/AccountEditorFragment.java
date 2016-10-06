package com.mg.incomeexpense.account;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.category.CategoryEditorActivity;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ItemStateChangeHandler;
import com.mg.incomeexpense.core.ItemStateChangeListener;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.MultipleChoiceEventHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-07-19.
 */
public class AccountEditorFragment extends Fragment implements ItemStateChangeHandler {

    private static final String LOG_TAG = AccountEditorFragment.class.getSimpleName();
    private static final int CATEGORY_EDITOR_ACTIVITY = 1;

    private final List<ItemStateChangeListener> mListeners = new ArrayList<>();
    private Account mAccount = null;
    private EditText mEditTextName;
    private EditText mEditTextBudget;
    private TextView mTextViewValidationErrorMessage;
    private ObjectValidator mObjectValidator = null;
    private ArrayList<String> mNames;
    private Switch mSwitchClose;
    private View.OnClickListener mOnSwitchClickListener;
    private ImageButton mImageButtonContributors;
    private View.OnClickListener mOnContributorImageButtonClickListener;
    private MultipleChoiceEventHandler mContributorMultipleChoiceEventHandler;
    private TextView mTextViewContributors;
    private List<Contributor> mAvailableContributors;
    private List<Contributor> mSelectedContributors;
    private ImageView mImageViewCategory;
    private TextView mTextViewCategory;

    public AccountEditorFragment() {

        mSelectedContributors = new ArrayList<>();

        mOnSwitchClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Switch s = (Switch) v;

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
            mObjectValidator = AccountValidator.create(getActivity(), mNames);
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

        mAccount = (Account) bundle.getSerializable("item");
        if (null == mAccount)
            throw new NullPointerException("An account is mandatory");

        mNames = (ArrayList<String>) bundle.getSerializable("names");
        if (null == mNames)
            throw new NullPointerException("A list of accounts name is mandatory");

        mAvailableContributors = (List<Contributor>) bundle.getSerializable("contributors");
        if (null == mAvailableContributors)
            throw new NullPointerException("A list of contributors is mandatory");

        mSelectedContributors.addAll(mAccount.getContributors());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.account_editor_fragment, container, false);
        mEditTextName = (EditText) rootView.findViewById(R.id.edittext_account_name);
        mEditTextBudget = (EditText) rootView.findViewById(R.id.edit_text_budget);

        mSwitchClose = (Switch) rootView.findViewById(R.id.switch_close);

        mTextViewValidationErrorMessage = (TextView) rootView.findViewById(R.id.textViewValidationErrorMessage);

        mImageButtonContributors = (ImageButton) rootView.findViewById(R.id.imagebutton_contributors);
        mImageButtonContributors.setOnClickListener(mOnContributorImageButtonClickListener);

        mTextViewContributors = (TextView) rootView.findViewById(R.id.textview_contributors);

        mImageViewCategory = (ImageView) rootView.findViewById(R.id.image_view_category);
        mImageViewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCategoryEditor();
            }
        });
        mTextViewCategory = (TextView) rootView.findViewById(R.id.text_view_category_name);


        if (null == savedInstanceState) {
            mEditTextName.setText(mAccount.getName());
            if (null != mAccount.getBudget()) {
                DecimalFormat df = new DecimalFormat("#.00");
                mEditTextBudget.setText(df.format(mAccount.getBudget()));
            }
            mSwitchClose.setChecked(mAccount.getIsClose());
            mSwitchClose.setText(mAccount.getIsClose() ? getString(R.string.account_close) : getString(R.string.account_active));
            mTextViewContributors.setText(mAccount.getContributorsForDisplay());
            if (mAccount.getCategoriesAsString() != null)
                mTextViewCategory.setText(mAccount.getCategoriesAsString());
        }

        return rootView;
    }

    private void ShowCategoryEditor() {

        Intent intent = new Intent(getActivity(), CategoryEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", new ArrayList<>()); // TODO Passer les bonnes categories
        bundle.putBoolean("hideHomeButton", true);
        intent.putExtras(bundle);
        startActivityForResult(intent, CATEGORY_EDITOR_ACTIVITY);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CATEGORY_EDITOR_ACTIVITY:
                if (data != null) {
                    List<String> categories = (List<String>) data.getSerializableExtra("item");

                    if (categories != null) {
                        mTextViewCategory.setText(Tools.join(categories,","));
                        mTextViewCategory.setTag(categories);
                    }

                }
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

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

                Category category = (Category) mTextViewCategory.getTag();
                if (category != null) {
                    mAccount.setCategories(null);
                }

                String budget = mEditTextBudget.getText().toString();
                if (budget.trim().length() > 0)
                    mAccount.setBudget(Double.parseDouble(budget));

                mAccount.setIsClose(mSwitchClose.isChecked());

                mAccount.setContributors(mSelectedContributors);

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

package com.mg.incomeexpense.account;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.category.CategorySpinnerAdapter;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.AppCompatActivityBase;
import com.mg.incomeexpense.core.FragmentBase;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.MultipleChoiceEventHandler;
import com.mg.incomeexpense.core.dialog.SingleChoiceEventHandler;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;
import com.mg.incomeexpense.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-07-19.
 */
public class AccountEditorFragment extends FragmentBase {

    private static final int CATEGORY_EDITOR_ACTIVITY = 1;

    private Account mAccount = null;
    private EditText mEditTextName;
    private EditText mEditTextBudget;
    private TextView mTextViewValidationErrorMessage;
    private AccountValidator mObjectValidator;
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
    private ListView mListViewCategory;

    private List<Category> mAvailableCategories;
    private List<Category> mSelectedCategories;
    private View.OnClickListener mOnCategoryImageViewClickListener;
    private MultipleChoiceEventHandler mCategoryMultipleChoiceEventHandler;

    private CategorySpinnerAdapter mCategoryAdapter;

    public AccountEditorFragment() {

        mSelectedContributors = new ArrayList<>();
        mSelectedCategories = new ArrayList<>();

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

        mOnCategoryImageViewClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCategorySetterDialog();
            }
        };

        mContributorMultipleChoiceEventHandler = new MultipleChoiceEventHandler() { // Creating an anonymous Class Object
            @Override
            public void execute(boolean[] idSelected) {

                // Ajouter validation si un contributeur est enleve
                List<Transaction> transactions = IncomeExpenseRequestWrapper.getAllTransactionForAccount(getActivity().getContentResolver(), mAccount);
                List<Contributor> selectedContributors = new ArrayList<>();
                for (int i = 0; i < idSelected.length; i++) {
                    if (idSelected[i]) {
                        Contributor contributor = mAvailableContributors.get(i);
                        selectedContributors.add(contributor);
                    }
                }

                ValidationStatus vs = mObjectValidator.canRemoveContributor(mAccount, selectedContributors, transactions);
                if (vs.isValid()) {
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

                } else {
                    mTextViewValidationErrorMessage.setText(vs.getMessage());
                    mTextViewValidationErrorMessage.setVisibility(View.VISIBLE);
                }

            }
        };

        mCategoryMultipleChoiceEventHandler = new MultipleChoiceEventHandler() { // Creating an anonymous Class Object
            @Override
            public void execute(boolean[] idSelected) {

                // Ajouter validation si un category est enleve
                List<Transaction> transactions = IncomeExpenseRequestWrapper.getAllTransactionForAccount(getActivity().getContentResolver(), mAccount);
                List<Category> selectedCategories = new ArrayList<>();
                for (int i = 0; i < idSelected.length; i++) {
                    if (idSelected[i]) {
                        Category category = mAvailableCategories.get(i);
                        selectedCategories.add(category);
                    }
                }

                ValidationStatus vs = mObjectValidator.canRemoveCategory(mAccount, selectedCategories, transactions);
                if (vs.isValid()) {
                    mSelectedCategories.clear();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < idSelected.length; i++) {
                        if (idSelected[i]) {
                            Category category = mAvailableCategories.get(i);
                            mSelectedCategories.add(category);
                            sb.append(String.format("%1$s%2$s", (sb.length() == 0 ? "" : ","), category.getName()));
                        }
                    }
                    mTextViewContributors.setText(sb.toString());

                } else {
                    mTextViewValidationErrorMessage.setText(vs.getMessage());
                    mTextViewValidationErrorMessage.setVisibility(View.VISIBLE);
                }

            }
        };


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        Objects.requireNonNull(bundle, "A bundle is mandatory");

        mAccount = (Account) bundle.getSerializable("item");
        Objects.requireNonNull(mAccount, "An account is mandatory");

        mNames = (ArrayList<String>) bundle.getSerializable("names");
        Objects.requireNonNull(mNames, "A list of accounts name is mandatory");

        mAvailableContributors = (List<Contributor>) bundle.getSerializable("contributors");
        Objects.requireNonNull(mAvailableContributors, "A list of contributor is mandatory");

        mAvailableCategories = (List<Category>) bundle.getSerializable("categories");
        Objects.requireNonNull(mAvailableCategories, "A list of category is mandatory");

        mObjectValidator = AccountValidator.create(getActivity(), mNames);
        mSelectedContributors.addAll(mAccount.getContributors());
        mSelectedCategories.addAll(mAccount.getCategories());

        mCategoryAdapter = new CategorySpinnerAdapter(this.getActivity(), android.R.layout.simple_list_item_1, mSelectedCategories);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivityBase) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(mAccount.isNew() ? R.string.title_account_editor_add : R.string.title_account_editor_update));
        }

        View rootView = inflater.inflate(R.layout.account_editor_fragment, container, false);
        mEditTextName = (EditText) rootView.findViewById(R.id.edittext_account_name);
        mEditTextBudget = (EditText) rootView.findViewById(R.id.edit_text_budget);

        mSwitchClose = (Switch) rootView.findViewById(R.id.switch_close);

        mTextViewValidationErrorMessage = (TextView) rootView.findViewById(R.id.textViewValidationErrorMessage);

        mImageButtonContributors = (ImageButton) rootView.findViewById(R.id.imagebutton_contributors);
        mImageButtonContributors.setOnClickListener(mOnContributorImageButtonClickListener);

        mTextViewContributors = (TextView) rootView.findViewById(R.id.textview_contributors);

        mImageViewCategory = (ImageView) rootView.findViewById(R.id.image_view_category);
        mImageViewCategory.setOnClickListener(mOnCategoryImageViewClickListener);

        mListViewCategory = (ListView) rootView.findViewById(R.id.list_view_category);

        mEditTextName.setText(mAccount.getName());
        if (null != mAccount.getBudget()) {
            mEditTextBudget.setText(mAccount.getBudgetAsString());
        }
        mSwitchClose.setChecked(mAccount.getIsClose());
        mSwitchClose.setText(mAccount.getIsClose() ? getString(R.string.account_close) : getString(R.string.account_active));
        mTextViewContributors.setText(mAccount.getContributorsForDisplay());

        mListViewCategory.setAdapter(mCategoryAdapter);

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
        ValidationStatus validationStatus;
        switch (id) {
            case R.id.action_delete:

                DialogUtils.twoButtonMessageBox(getContext(), getString(R.string.ask_delete_account), getString(R.string.dialog_title_deleting_account), new SingleChoiceEventHandler() {
                    @Override
                    public void execute(int idSelected) {

                        List<Transaction> transactions = IncomeExpenseRequestWrapper.getAllTransactionForAccount(getActivity().getContentResolver(), mAccount);

                        ValidationStatus validationStatus = mObjectValidator.canDelete(mAccount, transactions);

                        if (validationStatus.isValid()) {
                            mAccount.setDead(true);
                            notifyListener(new ItemStateChangeEvent(mAccount, false));
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
                mAccount.setName(mEditTextName.getText().toString());

                mAccount.setCategories(mSelectedCategories);

                String budget = mEditTextBudget.getText().toString();
                if (budget.trim().length() > 0)
                    mAccount.setBudget(Double.parseDouble(budget));

                mAccount.setIsClose(mSwitchClose.isChecked());

                mAccount.setContributors(mSelectedContributors);

                validationStatus = mObjectValidator.Validate(mAccount);

                if (validationStatus.isValid()) {
                    notifyListener(new ItemStateChangeEvent(mAccount, false));
                } else {

                    mTextViewValidationErrorMessage.setText(validationStatus.getMessage());
                    mTextViewValidationErrorMessage.setVisibility(View.VISIBLE);
                }
                break;
            case android.R.id.home:
                notifyListener(new ItemStateChangeEvent(mAccount, true));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

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

    private void showCategorySetterDialog() {

        try {

            CharSequence[] categoryArray = new CharSequence[mAvailableCategories.size()];
            int i = 0;
            for (Category category : mAvailableCategories) {
                categoryArray[i++] = category.getName();
            }

            Dialog dialog = DialogUtils.childSetterDialog(
                    this.getContext(),
                    categoryArray,
                    mCategoryMultipleChoiceEventHandler,
                    buildCategoriesCheckedArray(mAvailableCategories, mSelectedCategories),
                    getString(R.string.dialog_title_category_setter));

            dialog.setOwnerActivity(this.getActivity());
            dialog.show();
        } catch (Exception exception) {
            DialogUtils.messageBox(this.getContext(),
                    getString(R.string.error_unable_to_fetch_all_category),
                    getString(R.string.dialog_title_category_setter)).show();

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

    private boolean[] buildCategoriesCheckedArray(List<Category> availableCategories,
                                                  List<Category> selectedCategories) {

        boolean[] checked = new boolean[availableCategories.size()];

        for (Category category : selectedCategories) {
            int index = availableCategories.indexOf(category);
            if (index >= 0) {
                checked[index] = true;
            }
        }

        return checked;

    }

}

package com.mg.incomeexpense.account;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.category.Category;
import com.mg.incomeexpense.category.CategoryEditorActivity;
import com.mg.incomeexpense.category.CategorySpinnerAdapter;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.contributor.ContributorEditorActivity;
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

//import android.app.AlertDialog;

/**
 * Created by mario on 2016-07-19.
 */
public class AccountEditorFragment extends FragmentBase {

    private static final int ADD_CONTRIBUTOR_ACTIVITY = 1;
    private static final int ADD_CATEGORY_ACTIVITY = 2;

    private Account mAccount = null;
    private EditText mEditTextName;
    private EditText mEditTextBudget;
    private AccountValidator mObjectValidator;
    private ArrayList<String> mNames;
    private Switch mSwitchClose;
    private Switch mSwitchDisplayLastYearData;
    private View.OnClickListener mOnSwitchClickListener;
    private ImageView mImageViewContributors;
    private View.OnClickListener mOnContributorImageViewClickListener;
    private MultipleChoiceEventHandler mContributorMultipleChoiceEventHandler;
    private DialogInterface.OnClickListener mNewContributorEventHandler;
    private TextView mTextViewContributors;
    private List<Contributor> mAvailableContributors;
    private List<Contributor> mSelectedContributors;

    private ImageView mImageViewCategory;
    private ListView mListViewCategory;

    private List<Category> mAvailableCategories;
    private List<Category> mSelectedCategories;
    private View.OnClickListener mOnCategoryImageViewClickListener;
    private MultipleChoiceEventHandler mCategoryMultipleChoiceEventHandler;
    private DialogInterface.OnClickListener mNewCategoryEventHandler;

    private CategorySpinnerAdapter mCategoryAdapter;

    private View mRootEditorView;
    private EditText mEditTextPosition;

    private boolean[] mCheckedCategory;
    private boolean[] mCheckedContributor;

    public AccountEditorFragment() {

        mSelectedContributors = new ArrayList<>();
        mSelectedCategories = new ArrayList<>();

        mOnSwitchClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Switch s = (Switch) v;

                switch (s.getId()) {
                    case R.id.switch_close:
                        s.setText(s.isChecked() ? getString(R.string.account_close) : getString(R.string.account_active));
                        break;
                    case R.id.switch_display_last_year_data:
                        s.setText(s.isChecked() ? getString(R.string.account_display_last_year_data) : getString(R.string.account_not_display_last_year_data));
                        break;
                }

            }
        };

        mOnContributorImageViewClickListener = new View.OnClickListener() {
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

        mNewContributorEventHandler = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Contributor contributor = Contributor.createNew();

                Intent intent = new Intent(getActivity(), ContributorEditorActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", contributor);
                intent.putExtras(bundle);

                startActivityForResult(intent, ADD_CONTRIBUTOR_ACTIVITY);

            }
        };

        mNewCategoryEventHandler = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Category category = Category.createNew();

                Intent intent = new Intent(getActivity(), CategoryEditorActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", category);
                intent.putExtras(bundle);

                startActivityForResult(intent, ADD_CATEGORY_ACTIVITY);

            }
        };

        mContributorMultipleChoiceEventHandler = new MultipleChoiceEventHandler() { // Creating an anonymous Class Object
            @Override
            public void execute(boolean[] idSelected) {

                // Ajouter validation si un contributeur est enleve
                List<Transaction> transactions = IncomeExpenseRequestWrapper.getAllTransactionForAccount(getActivity(), mAccount);
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
                    displayMessage(mRootEditorView, vs.getMessage());
                }

            }
        };

        mCategoryMultipleChoiceEventHandler = new MultipleChoiceEventHandler() { // Creating an anonymous Class Object
            @Override
            public void execute(boolean[] idSelected) {

                // Ajouter validation si un category est enleve
                List<Transaction> transactions = IncomeExpenseRequestWrapper.getAllTransactionForAccount(getActivity(), mAccount);
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
                    mSelectedCategories.addAll(selectedCategories);
                    mCategoryAdapter.notifyDataSetChanged();
                } else {
                    displayMessage(mRootEditorView, vs.getMessage());
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
        mEditTextName = (EditText) rootView.findViewById(R.id.edit_text_account_name);
        mEditTextBudget = (EditText) rootView.findViewById(R.id.edit_text_budget);

        mSwitchClose = (Switch) rootView.findViewById(R.id.switch_close);
        mSwitchDisplayLastYearData = (Switch) rootView.findViewById(R.id.switch_display_last_year_data);

        mImageViewContributors = (ImageView) rootView.findViewById(R.id.image_view_contributors);
        mImageViewContributors.setOnClickListener(mOnContributorImageViewClickListener);

        mTextViewContributors = (TextView) rootView.findViewById(R.id.text_view_contributors);

        mImageViewCategory = (ImageView) rootView.findViewById(R.id.image_view_category);
        mImageViewCategory.setOnClickListener(mOnCategoryImageViewClickListener);

        mListViewCategory = (ListView) rootView.findViewById(R.id.list_view_category);

        mEditTextPosition = (EditText) rootView.findViewById(R.id.edit_text_position);

        mEditTextName.setText(mAccount.getName());
        if (null != mAccount.getBudget()) {
            mEditTextBudget.setText(mAccount.getBudgetAsString());
        }
        mSwitchClose.setChecked(mAccount.getIsClose());
        mSwitchClose.setText(mAccount.getIsClose() ? getString(R.string.account_close) : getString(R.string.account_active));
        mSwitchDisplayLastYearData.setChecked(mAccount.getDisplayLastYearData());
        mSwitchDisplayLastYearData.setText(mAccount.getDisplayLastYearData() ? getString(R.string.account_display_last_year_data) : getString(R.string.account_not_display_last_year_data));

        mTextViewContributors.setText(mAccount.getContributorsForDisplay());
        mEditTextPosition.setText(mAccount.getPosition().toString());

        mListViewCategory.setAdapter(mCategoryAdapter);

        mRootEditorView = rootView.findViewById(R.id.root_editor_view);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mSwitchClose.setOnClickListener(mOnSwitchClickListener);
        mSwitchDisplayLastYearData.setOnClickListener(mOnSwitchClickListener);

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

                        List<Transaction> transactions = IncomeExpenseRequestWrapper.getAllTransactionForAccount(getActivity(), mAccount);

                        ValidationStatus validationStatus = mObjectValidator.canDelete(mAccount, transactions);

                        if (validationStatus.isValid()) {
                            mAccount.setDead(true);
                            notifyListener(new ItemStateChangeEvent(mAccount, false));
                        } else {
                            displayMessage(mRootEditorView, validationStatus.getMessage());
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
                mAccount.setDisplayLastYearData(mSwitchDisplayLastYearData.isChecked());

                mAccount.setContributors(mSelectedContributors);

                mAccount.setPosition(Integer.parseInt(mEditTextPosition.getText().toString()));

                validationStatus = mObjectValidator.Validate(mAccount);

                if (validationStatus.isValid()) {
                    notifyListener(new ItemStateChangeEvent(mAccount, false));
                } else {
                    displayMessage(mRootEditorView, validationStatus.getMessage());
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
                    getString(R.string.dialog_title_contributor_setter),
                    mNewContributorEventHandler);

            mCheckedContributor = buildContributorsCheckedArray(mAvailableContributors, mSelectedContributors);
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    ListView lv = ((AlertDialog) dialog).getListView();
                    for (int i = 0; i < mCheckedContributor.length; i++) {
                        lv.setItemChecked(i, mCheckedContributor[i]);
                    }
                }
            });

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
                    getString(R.string.dialog_title_category_setter), mNewCategoryEventHandler);

            mCheckedCategory = buildCategoriesCheckedArray(mAvailableCategories, mSelectedCategories);
            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    ListView lv = ((AlertDialog) dialog).getListView();
                    for (int i = 0; i < mCheckedCategory.length; i++) {
                        lv.setItemChecked(i, mCheckedCategory[i]);
                    }
                }
            });

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_CONTRIBUTOR_ACTIVITY:
                mAvailableContributors = IncomeExpenseRequestWrapper.getAvailableContributors(getActivity().getContentResolver());
                break;
            case ADD_CATEGORY_ACTIVITY:
                if(resultCode== Activity.RESULT_OK)
                    mAvailableCategories = IncomeExpenseRequestWrapper.getAvailableCategories(getActivity().getContentResolver());
        }
    }
}

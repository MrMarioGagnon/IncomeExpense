package com.mg.incomeexpense.category;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.core.AppCompatActivityBase;
import com.mg.incomeexpense.core.FragmentBase;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.SingleChoiceEventHandler;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;
import com.mg.incomeexpense.extension.ExtensionFragmentFactory;
import com.mg.incomeexpense.extension.ExtensionSpinnerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-07-19.
 */
public class CategoryEditorFragment extends FragmentBase {

    private Category mCategory = null;
    private EditText mEditTextName;

    private Spinner mSpinnerExtensionType;
    private ExtensionSpinnerAdapter mExtensionTypeSpinnerAdapter;

    private CategoryValidator mObjectValidator;
    private ArrayList<String> mNames;
    private View mRootEditorView;

    public CategoryEditorFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        Objects.requireNonNull(bundle, "A bundle is mandatory");

        mCategory = (Category) bundle.getSerializable("item");
        Objects.requireNonNull(mCategory, "An category is mandatory");

        mNames = (ArrayList<String>) bundle.getSerializable("names");
        Objects.requireNonNull(mNames, "A list of category name is mandatory");

        mObjectValidator = CategoryValidator.create(getActivity(), mNames);

        mExtensionTypeSpinnerAdapter = new ExtensionSpinnerAdapter(getActivity(),
                R.layout.spinner_item,
                ExtensionFragmentFactory.ExtensionType.AsList());
        mExtensionTypeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivityBase) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(mCategory.isNew() ? R.string.title_category_editor_add : R.string.title_category_editor_update));
        }

        View rootView = inflater.inflate(R.layout.category_editor_fragment, container, false);
        mEditTextName = (EditText) rootView.findViewById(R.id.edit_text_category_name);
        mSpinnerExtensionType = (Spinner) rootView.findViewById(R.id.spinner_extension_type);
        mSpinnerExtensionType.setAdapter(mExtensionTypeSpinnerAdapter);

        mEditTextName.setText(mCategory.getName());
        Tools.setSpinner(mCategory.getType(), mSpinnerExtensionType);

        mRootEditorView = rootView.findViewById(R.id.root_editor_view);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor, menu);

        if (mCategory.isNew()) {
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

                DialogUtils.twoButtonMessageBox(getContext(), getString(R.string.ask_delete_category), getString(R.string.dialog_title_deleting_category), new SingleChoiceEventHandler() {
                    @Override
                    public void execute(int idSelected) {

                        List<Account> accounts = IncomeExpenseRequestWrapper.getAvailableAccounts(getActivity().getContentResolver());

                        ValidationStatus validationStatus = mObjectValidator.canDelete(mCategory, accounts);

                        if (validationStatus.isValid()) {
                            mCategory.setDead(true);
                            notifyListener(new ItemStateChangeEvent(mCategory, false));
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
                mCategory.setName(mEditTextName.getText().toString());
                ExtensionFragmentFactory.ExtensionType extensionType = ExtensionFragmentFactory.ExtensionType.valueOf((String) mSpinnerExtensionType.getSelectedItem());
                mCategory.setType(extensionType);

                validationStatus = mObjectValidator.Validate(mCategory);

                if (validationStatus.isValid()) {
                    notifyListener(new ItemStateChangeEvent(mCategory, false));
                } else {
                    displayMessage(mRootEditorView, validationStatus.getMessage());
                }
                break;
            case android.R.id.home:
                notifyListener(new ItemStateChangeEvent(mCategory, true));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }

}

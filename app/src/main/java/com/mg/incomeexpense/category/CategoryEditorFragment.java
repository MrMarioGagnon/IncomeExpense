package com.mg.incomeexpense.category;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.account.AccountValidator;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ItemStateChangeHandler;
import com.mg.incomeexpense.core.ItemStateChangeListener;
import com.mg.incomeexpense.core.LetterDigitFilter;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.ValidationStatus;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.MultipleChoiceEventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-07-19.
 */
public class CategoryEditorFragment extends Fragment implements ItemStateChangeHandler {

    private static final String LOG_TAG = CategoryEditorFragment.class.getSimpleName();

    private final List<ItemStateChangeListener> mListeners = new ArrayList<>();
    private Category mCategory = null;
    private EditText mEditTextName;
    private TextView mTextViewValidationErrorMessage;
    private ObjectValidator mObjectValidator = null;
    private ArrayList<String> mNames;
    private int mLayoutPosition = 1;
    private LinearLayout mLinearLayoutSubCategory;

    public CategoryEditorFragment() {

    }

    private View.OnClickListener mRemoveSubCategoryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLinearLayoutSubCategory.removeView((ViewGroup) v.getParent());
            mLayoutPosition--;
        }
    };

    private View.OnClickListener mAddSubCategoryClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            View view = createSubCategoryView(null);

            mLinearLayoutSubCategory.addView(view, mLayoutPosition);
            mLayoutPosition++;

        }
    };

    public ObjectValidator getObjectValidator() {

        if (null == mObjectValidator) {
            mObjectValidator = CategoryValidator.create(getActivity(), mNames);
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

        mCategory = (Category) bundle.getSerializable("item");
        if (null == mCategory)
            throw new NullPointerException("An category is mandatory");

        mNames = (ArrayList<String>) bundle.getSerializable("names");
        if (null == mNames)
            throw new NullPointerException("A list of categories name is mandatory");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.category_editor_fragment, container, false);
        mEditTextName = (EditText) rootView.findViewById(R.id.edittext_category_name);
        mTextViewValidationErrorMessage = (TextView) rootView.findViewById(R.id.textViewValidationErrorMessage);
        mLinearLayoutSubCategory = (LinearLayout) rootView.findViewById(R.id.linearLayoutMain);
        Button buttonAddSubCategory = (Button)rootView.findViewById(R.id.button_add_sub_category);
        buttonAddSubCategory.setOnClickListener(mAddSubCategoryClickListener);

        if (null == savedInstanceState) {
            if(mCategory.isNew()){
                buttonAddSubCategory.performClick();
            }else {
                mEditTextName.setText(mCategory.getName());
                AddSubCategories(mCategory.getSubCategories());
            }
        }

        return rootView;
    }

    private void AddSubCategories(String[] subCategories) {

        if (null == subCategories)
            return;

        View view;
        for (String subCategory : subCategories) {

            view = createSubCategoryView(subCategory);

            mLinearLayoutSubCategory.addView(view, mLayoutPosition);
            mLayoutPosition++;

        }

    }

    private View createSubCategoryView(String name) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        EditText editText;
        View view = inflater.inflate(R.layout.category_row_editor_fragment, null);

        editText = (EditText) view.findViewById(R.id.editTextName);
        editText.setText(name);
        editText.setFilters(new InputFilter[] { new LetterDigitFilter() });

        ImageButton imageButtonEdit = (ImageButton) view
                .findViewById(R.id.imageButtonEdit);
        imageButtonEdit.setOnClickListener(mRemoveSubCategoryClickListener);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
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

        switch (id) {
            case R.id.action_delete:
                mCategory.setDead(true);
                notifyListener(new ItemStateChangeEvent(mCategory));
                break;
            case R.id.action_save:
                mCategory.setName(mEditTextName.getText().toString());

                ValidationStatus validationStatus = getObjectValidator().Validate(mCategory);

                if (validationStatus.isValid()) {
                    notifyListener(new ItemStateChangeEvent(mCategory));
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


}

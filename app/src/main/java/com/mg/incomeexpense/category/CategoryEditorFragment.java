package com.mg.incomeexpense.category;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ItemStateChangeHandler;
import com.mg.incomeexpense.core.ItemStateChangeListener;
import com.mg.incomeexpense.core.ObjectValidator;
import com.mg.incomeexpense.core.ValidationStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-07-19.
 */
public class CategoryEditorFragment extends Fragment implements ItemStateChangeHandler {

    private static final String LOG_TAG = CategoryEditorFragment.class.getSimpleName();
    private final List<ItemStateChangeListener> mListeners = new ArrayList<>();
    private List<String> mCategories;
    private TextView mTextViewValidationErrorMessage;
    private ObjectValidator mObjectValidator = null;
    private ArrayList<String> mNames;
    private int mLayoutPosition = 0;
    private LinearLayout mLinearLayoutCategory;
    private View.OnClickListener mRemoveCategoryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLinearLayoutCategory.removeView((ViewGroup) v.getParent());
            mLayoutPosition--;
        }
    };
    private View.OnClickListener mAddCategoryClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            View view = createCategoryView(null);
            mLinearLayoutCategory.addView(view, mLayoutPosition);
            mLayoutPosition++;

        }
    };

    public CategoryEditorFragment() {

    }

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

        mCategories = ((Category) bundle.getSerializable("item")).getCategories();
        if (null == mCategories)
            throw new NullPointerException("A list of categories is mandatory");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.category_editor_fragment, container, false);
        mTextViewValidationErrorMessage = (TextView) rootView.findViewById(R.id.textViewValidationErrorMessage);
        mLinearLayoutCategory = (LinearLayout) rootView.findViewById(R.id.linear_layout_category);
        Button buttonAddCategory = (Button) rootView.findViewById(R.id.button_add_category);
        buttonAddCategory.setOnClickListener(mAddCategoryClickListener);

        if (null == savedInstanceState) {
            if (mCategories.size() == 0) {
                buttonAddCategory.performClick();
            } else {
                AddCategories(mCategories);
            }
        }

        return rootView;
    }

    private void AddCategories(List<String> categories) {

        if (null == categories)
            return;

        View view;
        for (String category : categories) {

            view = createCategoryView(category);

            mLinearLayoutCategory.addView(view, mLayoutPosition);
            mLayoutPosition++;

        }

    }

    private View createCategoryView(String name) {

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        EditText editText;
        View view = inflater.inflate(R.layout.category_row_editor_fragment, null);

        editText = (EditText) view.findViewById(R.id.editTextName);
        editText.setText(name);
        editText.requestFocus();

        ImageButton imageButtonEdit = (ImageButton) view
                .findViewById(R.id.imageButtonEdit);
        imageButtonEdit.setOnClickListener(mRemoveCategoryClickListener);

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor, menu);

        MenuItem mi = menu.findItem(R.id.action_delete);
        if (null != mi) {
            mi.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_save:
                // Get sub items from layout and update mCategory
                ViewGroup categoryView = mLinearLayoutCategory;
                EditText editTextCategory;
                String categoryName;
                List<String> categories = new ArrayList();
                for (int i = 0; i < categoryView.getChildCount(); i++) {
                    View view = categoryView.getChildAt(i);
                    editTextCategory = (EditText) view
                            .findViewById(R.id.editTextName);
                    categoryName = editTextCategory.getText().toString();

                    if (categoryName.trim().length() != 0) {
                        categories.add(categoryName);
                    }
                }

                Category category = Category.create(categories);
                ValidationStatus validationStatus = getObjectValidator().Validate(category);

                if (validationStatus.isValid()) {
                    notifyListener(new ItemStateChangeEvent(category));
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

package com.mg.incomeexpense.category;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
import com.mg.incomeexpense.core.AppCompatActivityBase;
import com.mg.incomeexpense.core.FragmentBase;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ValidationStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-07-19.
 */
public class CategoryEditorFragment extends FragmentBase {

    private List<String> mCategories;
    private TextView mTextViewValidationErrorMessage;
    private CategoryValidator mObjectValidator = null;
    private int mLayoutPosition = 0;
    private LinearLayout mLinearLayoutCategory;
    private View.OnClickListener mRemoveCategoryClickListener;
    private View.OnClickListener mAddCategoryClickListener;

    public CategoryEditorFragment() {

        mRemoveCategoryClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLinearLayoutCategory.removeView((ViewGroup) v.getParent());
                mLayoutPosition--;
            }
        };

        mAddCategoryClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                View view = createCategoryView(null);
                mLinearLayoutCategory.addView(view, mLayoutPosition);
                mLayoutPosition++;

            }
        };

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        Bundle bundle = getArguments();
        Objects.requireNonNull(bundle, "A bundle is mandatory");

        mCategories = (ArrayList<String>) bundle.getSerializable("item");
        Objects.requireNonNull(mCategories, "A list of categories is mandatory");

        mObjectValidator = CategoryValidator.create(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        ActionBar actionBar = ((AppCompatActivityBase) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.title_category_editor_update);
        }

        View rootView = inflater.inflate(R.layout.category_editor_fragment, container, false);
        mTextViewValidationErrorMessage = (TextView) rootView.findViewById(R.id.textViewValidationErrorMessage);
        mLinearLayoutCategory = (LinearLayout) rootView.findViewById(R.id.linear_layout_category);
        Button buttonAddCategory = (Button) rootView.findViewById(R.id.button_add_category);
        buttonAddCategory.setOnClickListener(mAddCategoryClickListener);

//        if(null == savedInstanceState) {
        mLayoutPosition = 0;
        if (mCategories.size() == 0) {
            buttonAddCategory.performClick();
        } else {
            AddCategories(mCategories);
        }
        //      }
        return rootView;
    }

    private void AddCategories(final List<String> categories) {

        if (null == categories)
            return;

        View view;
        for (String category : categories) {

            view = createCategoryView(category);

            mLinearLayoutCategory.addView(view, mLayoutPosition);
            mLayoutPosition++;

        }

    }

    private View createCategoryView(final String name) {

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
        List<String> categories = new ArrayList();
        switch (id) {
            case R.id.action_save:
                // Get sub items from layout and update mCategory
                ViewGroup categoryView = mLinearLayoutCategory;
                EditText editTextCategory;
                String categoryName;
                for (int i = 0; i < categoryView.getChildCount(); i++) {
                    View view = categoryView.getChildAt(i);
                    editTextCategory = (EditText) view
                            .findViewById(R.id.editTextName);
                    categoryName = editTextCategory.getText().toString();

                    if (categoryName.trim().length() != 0) {
                        categories.add(categoryName);
                    }
                }
                Collections.sort(categories, new Comparator<String>() {
                    @Override
                    public int compare(String lhs, String rhs) {
                        return lhs.compareToIgnoreCase(rhs);
                    }
                });

                ValidationStatus validationStatus = mObjectValidator.Validate(categories);

                if (validationStatus.isValid()) {
                    notifyListener(new ItemStateChangeEvent(categories, false));
                } else {
                    mTextViewValidationErrorMessage.setText(validationStatus.getMessage());
                    mTextViewValidationErrorMessage.setVisibility(View.VISIBLE);
                }

                break;
            case android.R.id.home:
                notifyListener(new ItemStateChangeEvent(categories, true));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }

}

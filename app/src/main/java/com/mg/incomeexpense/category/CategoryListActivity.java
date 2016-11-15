package com.mg.incomeexpense.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.account.AccountEditorActivity;
import com.mg.incomeexpense.core.ItemSelectedEvent;
import com.mg.incomeexpense.core.ItemSelectedHandler;
import com.mg.incomeexpense.core.ItemSelectedListener;

import java.util.Objects;

/**
 * Created by mario on 2016-07-19.
 */
public class CategoryListActivity extends AppCompatActivity implements ItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list_activity);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {

            fab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    Category category = Category.createNew();

                    Intent intent = new Intent(CategoryListActivity.this, CategoryEditorActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("item", category);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });

        }

        ItemSelectedHandler f = (ItemSelectedHandler) getSupportFragmentManager().findFragmentById(R.id.category_list_fragment_container);
        if (null != f) {
            f.addListener(this);
        }
    }

    @Override
    public void onItemSelected(@NonNull ItemSelectedEvent event) {

        Objects.requireNonNull(event, "Parameter event of the ItemSelectedEvent is mandatory");

        Intent intent = new Intent(this, CategoryEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", event.getItem());
        intent.putExtras(bundle);

        startActivity(intent);

    }

}

package com.mg.incomeexpense.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ItemSelectedEvent;
import com.mg.incomeexpense.core.ItemSelectedHandler;
import com.mg.incomeexpense.core.ItemSelectedListener;

import java.util.Objects;

/**
 * Created by mario on 2016-07-19.
 */
public class AccountListActivity extends AppCompatActivity implements ItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_list_activity);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {

            fab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    Account account = Account.createNew();

                    Intent intent = new Intent(AccountListActivity.this, AccountEditorActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("item", account);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });

        }

        ItemSelectedHandler f = (ItemSelectedHandler) getSupportFragmentManager().findFragmentById(R.id.account_list_fragment_container);
        if (null != f) {
            f.addListener(this);
        }
    }

    @Override
    public void onItemSelected(@NonNull ItemSelectedEvent event) {

        Objects.requireNonNull(event, "Parameter event of the ItemSelectedEvent is mandatory");

        Intent intent = new Intent(this, AccountEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", event.getItem());
        intent.putExtras(bundle);

        startActivity(intent);

    }

}

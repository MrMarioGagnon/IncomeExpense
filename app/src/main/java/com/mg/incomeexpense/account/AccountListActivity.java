package com.mg.incomeexpense.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.contributor.Contributor;
import com.mg.incomeexpense.contributor.ContributorEditorActivity;
import com.mg.incomeexpense.core.ItemSelectedEvent;
import com.mg.incomeexpense.core.ItemSelectedHandler;
import com.mg.incomeexpense.core.ItemSelectedListener;

/**
 * Created by mario on 2016-07-19.
 */
public class AccountListActivity extends AppCompatActivity implements ItemSelectedListener {

    private static final String LOG_TAG = AccountListActivity.class.getSimpleName();
    private static final int EDITOR_ACTIVITY = 1;
    private final OnClickListener mFabOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            Account account = Account.createNew();

//            Intent intent = new Intent(AccountListActivity.this, AccountEditorActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("item", account);
//            intent.putExtras(bundle);
//
//            AccountListActivity.this.startActivityForResult(intent,EDITOR_ACTIVITY);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_list_activity);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(mFabOnClickListener);
        }

        ItemSelectedHandler f = (ItemSelectedHandler) getSupportFragmentManager().findFragmentById(R.id.account_list_fragment_container);
        if(null != f){
            f.addListener(this);
        }
    }

    @Override
    public void onItemSelected(ItemSelectedEvent event) {

//        Intent intent = new Intent(this, AccountEditorActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("item", event.getItem());
//        intent.putExtras(bundle);
//
//        startActivityForResult(intent,EDITOR_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle extras = null;
        if(null != data)
            extras = data.getExtras();

        switch (requestCode){
            case EDITOR_ACTIVITY:
                if(resultCode == RESULT_OK){
                    if(null != data){
                        Account item =(Account) data.getSerializableExtra("item");
                        Log.i(LOG_TAG, item.toString());
                    }
                }
                break;
            default:
                break;
        }
    }
}

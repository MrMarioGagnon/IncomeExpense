package com.mg.incomeexpense.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ItemSelectedEvent;
import com.mg.incomeexpense.core.ItemSelectedListener;

/**
 * Created by mario on 2016-07-19.
 */
public class TransactionListActivity extends AppCompatActivity implements ItemSelectedListener {

    private static final String LOG_TAG = TransactionListActivity.class.getSimpleName();
    private static final int EDITOR_ACTIVITY_ADD = 1;
    private static final int EDITOR_ACTIVITY_UPDATE = 2;

    private final OnClickListener mFabOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            Transaction transaction = Transaction.createNew();

            Intent intent = new Intent(TransactionListActivity.this, TransactionEditorActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("item", transaction);
            intent.putExtras(bundle);

            TransactionListActivity.this.startActivityForResult(intent, EDITOR_ACTIVITY_ADD);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_list_activity);

        Bundle bundle = getIntent().getExtras();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(mFabOnClickListener);
        }

        TransactionListFragment fragment = new TransactionListFragment();
        fragment.addListener(this);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.transaction_list_fragment_container, fragment)
                .commit();

    }

    @Override
    public void onItemSelected(ItemSelectedEvent event) {

        Intent intent = new Intent(this, TransactionEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", event.getItem());
        intent.putExtras(bundle);

        startActivityForResult(intent, EDITOR_ACTIVITY_UPDATE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle extras = null;
        if (null != data)
            extras = data.getExtras();

        switch (requestCode) {
            case EDITOR_ACTIVITY_UPDATE:
                if (resultCode == RESULT_OK) {
                    if (null != data) {
                        Transaction item = (Transaction) data.getSerializableExtra("item");
                        Log.i(LOG_TAG, item.toString());
                    }
                }
                break;
            default:
                break;
        }
    }
}

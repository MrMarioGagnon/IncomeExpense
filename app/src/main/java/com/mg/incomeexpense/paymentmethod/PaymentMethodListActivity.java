package com.mg.incomeexpense.paymentmethod;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.account.AccountEditorActivity;
import com.mg.incomeexpense.core.ItemSelectedEvent;
import com.mg.incomeexpense.core.ItemSelectedHandler;
import com.mg.incomeexpense.core.ItemSelectedListener;

/**
 * Created by mario on 2016-07-19.
 */
public class PaymentMethodListActivity extends AppCompatActivity implements ItemSelectedListener {

    private static final String LOG_TAG = PaymentMethodListActivity.class.getSimpleName();
    private static final int EDITOR_ACTIVITY_ADD = 1;
    private static final int EDITOR_ACTIVITY_UPDATE = 2;
    private final OnClickListener mFabOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            PaymentMethod paymentMethod = PaymentMethod.createNew();

            Intent intent = new Intent(PaymentMethodListActivity.this, PaymentMethodEditorActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("item", paymentMethod);
            intent.putExtras(bundle);

            PaymentMethodListActivity.this.startActivityForResult(intent,EDITOR_ACTIVITY_ADD);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method_list_activity);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(mFabOnClickListener);
        }

        ItemSelectedHandler f = (ItemSelectedHandler) getSupportFragmentManager().findFragmentById(R.id.payment_method_list_fragment_container);
        if(null != f){
            f.addListener(this);
        }
    }

    @Override
    public void onItemSelected(ItemSelectedEvent event) {

        Intent intent = new Intent(this, PaymentMethodEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", event.getItem());
        intent.putExtras(bundle);

        startActivityForResult(intent,EDITOR_ACTIVITY_UPDATE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle extras = null;
        if(null != data)
            extras = data.getExtras();

        switch (requestCode){
            case EDITOR_ACTIVITY_UPDATE:
                if(resultCode == RESULT_OK){
                    if(null != data){
                        PaymentMethod item =(PaymentMethod) data.getSerializableExtra("item");
                        Log.i(LOG_TAG, item.toString());
                    }
                }
                break;
            default:
                break;
        }
    }
}

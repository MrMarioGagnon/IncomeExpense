package com.mg.incomeexpense.contributor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ItemSelectedEvent;
import com.mg.incomeexpense.core.ItemSelectedHandler;
import com.mg.incomeexpense.core.ItemSelectedListener;

/**
 * Created by mario on 2016-07-19.
 */
public class ContributorListActivity extends AppCompatActivity implements ItemSelectedListener {

    private static final String LOG_TAG = ContributorListActivity.class.getSimpleName();
    private static final int EDITOR_ACTIVITY = 1;
    private final View.OnClickListener mFabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Contributor contributor = Contributor.createNew();

            Intent intent = new Intent(ContributorListActivity.this, ContributorEditorActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("item", contributor);
            intent.putExtras(bundle);

            ContributorListActivity.this.startActivityForResult(intent, EDITOR_ACTIVITY);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contributor_list_activity);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(mFabOnClickListener);
        }

        ItemSelectedHandler f = (ItemSelectedHandler) getSupportFragmentManager().findFragmentById(R.id.contributor_list_fragment_container);
        if (null != f) {
            f.addListener(this);
        }
    }

    @Override
    public void onItemSelected(ItemSelectedEvent event) {

        Intent intent = new Intent(this, ContributorEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", event.getItem());
        intent.putExtras(bundle);

        startActivityForResult(intent, EDITOR_ACTIVITY);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bundle extras = null;
        if (null != data)
            extras = data.getExtras();

        switch (requestCode) {
            case EDITOR_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    if (null != data) {
                        Contributor item = (Contributor) data.getSerializableExtra("item");
                        Log.i(LOG_TAG, item.toString());
                    }
                }
                break;
            default:
                break;
        }
    }
}

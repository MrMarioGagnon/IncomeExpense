package com.mg.incomeexpense.contributor;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mg.incomeexpense.R;

/**
 * Created by mario on 2016-07-19.
 */
public class ContributorEditorActivity extends AppCompatActivity {

    private static final String LOG_TAG = ContributorEditorActivity.class.getSimpleName();
    private Contributor mContributor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contributor_editor_activity);

        if(null == savedInstanceState){

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                mContributor = (Contributor) bundle.getSerializable("item");
            }
            assert mContributor != null;
            ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setTitle( getString(mContributor.isNew() ? R.string.title_contributor_editor_add : R.string.title_contributor_editor_update));
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contributor_editor, menu);

        if(mContributor.isNew()) {
            MenuItem mi = menu.findItem(R.id.action_delete);
            if(null != mi){
                mi.setVisible(false);
            }
        }

        return true;
    }
}

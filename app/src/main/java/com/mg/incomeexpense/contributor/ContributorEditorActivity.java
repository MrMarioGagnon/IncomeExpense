package com.mg.incomeexpense.contributor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ItemStateChangeListener;

/**
 * Created by mario on 2016-07-19.
 */
public class ContributorEditorActivity extends AppCompatActivity implements ItemStateChangeListener{

    private static final String LOG_TAG = ContributorEditorActivity.class.getSimpleName();
    private Contributor mContributor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contributor_editor_activity);

        if (null == savedInstanceState) {

            Bundle bundle = getIntent().getExtras();
            assert bundle != null;

            mContributor = (Contributor) bundle.getSerializable("item");
            assert mContributor != null;

            ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setTitle(getString(mContributor.isNew() ? R.string.title_contributor_editor_add : R.string.title_contributor_editor_update));
            }

        }

    }

    @Override
    public void onItemStateChange(ItemStateChangeEvent event) {

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_contributor_editor, menu);
//
//        if (mContributor.isNew()) {
//            MenuItem mi = menu.findItem(R.id.action_delete);
//            if (null != mi) {
//                mi.setVisible(false);
//            }
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        switch (id) {
//            case R.id.action_save:
//            case R.id.action_delete:
//                Intent intent = new Intent();
//                intent.putExtra("item", mContributor);
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
//            case android.R.id.home:
//                setResult(RESULT_CANCELED);
//                break;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//        return true;
//    }

}

package com.mg.incomeexpense.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.AppCompatActivityBase;
import com.mg.incomeexpense.core.ItemStateChangeEvent;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by mario on 2016-07-19.
 */
public class CategoryEditorActivity extends AppCompatActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_editor_activity);

        if (null == savedInstanceState) {

            Bundle bundle = getIntent().getExtras();
            Objects.requireNonNull(bundle, "A bundle with and Category item is mandatory");

            ArrayList<String> categories = (ArrayList) bundle.getSerializable("item");
            Objects.requireNonNull(categories, "An Category object is mandatory");


            CategoryEditorFragment fragment = new CategoryEditorFragment();
            fragment.addListener(this);
            fragment.setArguments(bundle);
            fragment.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.category_editor_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onItemStateChange(@NonNull ItemStateChangeEvent event) {

        Objects.requireNonNull(event, "Parameter event of type ItemStateChangeEvent is mandatory");

        if (event.isCancelled() || !(event.getItem() instanceof ArrayList)) {
            setResult(RESULT_CANCELED);
        } else {
            Intent intent = new Intent();
            intent.putExtra("item", (ArrayList<String>) event.getItem());
            setResult(RESULT_OK, intent);
        }

        finish();
    }

}

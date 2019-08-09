package be.pxl.project.cookaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * search_item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(RecipeDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(RecipeDetailFragment.ARG_ITEM_ID));
            arguments.putString(RecipeDetailFragment.ARG_ITEM_NAME,
                    getIntent().getStringExtra(RecipeDetailFragment.ARG_ITEM_NAME));
            arguments.putString(RecipeDetailFragment.ARG_ITEM_CATEGORY,
                    getIntent().getStringExtra(RecipeDetailFragment.ARG_ITEM_CATEGORY));
            arguments.putString(RecipeDetailFragment.ARG_ITEM_URI,
                    getIntent().getStringExtra(RecipeDetailFragment.ARG_ITEM_URI));
            arguments.putString(RecipeDetailFragment.ARG_ITEM_RECIPE,
                    getIntent().getStringExtra(RecipeDetailFragment.ARG_ITEM_RECIPE));
            arguments.putString(RecipeDetailFragment.ARG_ITEM_LEVEL,
                    getIntent().getStringExtra(RecipeDetailFragment.ARG_ITEM_LEVEL));
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, RecipeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

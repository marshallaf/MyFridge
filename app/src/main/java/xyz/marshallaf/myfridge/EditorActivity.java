package xyz.marshallaf.myfridge;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import xyz.marshallaf.myfridge.data.FoodContract;

/**
 * Activity to view and edit food entries.
 *
 * Created by Andrew Marshall on 1/26/2017.
 */

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    
    // member variables to easily access text fields
    EditText mNameTextView;
    EditText mAmountTextView;
    EditText mStoreTextView;
    EditText mPriceTextView;
    EditText mExpTextView;
    Spinner mUnitSpinner;
    int mUnit;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        
        // get references to textviews to read data from
        mNameTextView = (EditText) findViewById(R.id.edit_item_name);
        mAmountTextView = (EditText) findViewById(R.id.edit_item_amount);
        mStoreTextView = (EditText) findViewById(R.id.edit_item_store);
        mPriceTextView = (EditText) findViewById(R.id.edit_item_price);
        mExpTextView = (EditText) findViewById(R.id.edit_item_expiration);
        mUnitSpinner = (Spinner) findViewById(R.id.edit_item_unit);
        
        setupSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.editor_save:
                saveItem();
                finish();
                return true;
            case R.id.editor_delete:
                // call for delete dialog here
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveItem() {
        // get values from editor fields
        String name = mNameTextView.getText().toString();
        String store = mStoreTextView.getText().toString();
        String priceString = mPriceTextView.getText().toString();
        String amountString = mAmountTextView.getText().toString();
        String expiration = mExpTextView.getText().toString();

        // TODO: don't let user save unless required fields are filled in

        // add values to object
        ContentValues values = new ContentValues();
        values.put(FoodContract.FoodEntry.COLUMN_NAME, name);
        values.put(FoodContract.FoodEntry.COLUMN_UNIT, mUnit);

        if (!TextUtils.isEmpty(expiration)) {
            // TODO: change this to a date entry field and parse to integer
            values.put(FoodContract.FoodEntry.COLUMN_EXPIRATION, 55556);
        }

        if (!TextUtils.isEmpty(store)) {
            values.put(FoodContract.FoodEntry.COLUMN_STORE, store);
        }

        // convert numeric values and add to object
        float amount = Float.parseFloat(amountString);
        values.put(FoodContract.FoodEntry.COLUMN_AMOUNT, amount);

        if (!TextUtils.isEmpty(priceString)) {
            float price = Float.parseFloat(priceString);
            values.put(FoodContract.FoodEntry.COLUMN_PRICE_PER, price);
        }

        // insert using contentresolver
        getContentResolver().insert(FoodContract.FoodEntry.CONTENT_URI, values);
    }

    private void setupSpinner() {
        // create array adapter for spinner
        ArrayAdapter unitSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.unitSpinner, android.R.layout.simple_spinner_item);

        // set dropdown style
        unitSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // bind the adapter to the spinner
        mUnitSpinner.setAdapter(unitSpinnerAdapter);

        // set the on item click listener
        mUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    switch (selection) {
                        case "item":
                            mUnit = FoodContract.FoodEntry.UNIT_ITEM;
                            break;
                        case "volume":
                            mUnit = FoodContract.FoodEntry.UNIT_VOL;
                            break;
                        case "mass":
                            mUnit = FoodContract.FoodEntry.UNIT_MASS;
                            break;
                        default:
                            mUnit = FoodContract.FoodEntry.UNIT_ITEM;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mUnit = FoodContract.FoodEntry.UNIT_ITEM;
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

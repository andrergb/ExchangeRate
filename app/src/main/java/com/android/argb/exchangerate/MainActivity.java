package com.android.argb.exchangerate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements InterfaceOnTaskCompleted {
    private ListView currencyList;
    private TextView currencyValue;
    private TextView currencyName;
    private TextView bottomInfo;
    private SharedPreferences sharedPreferences;
    private String currentBase;

    @Override
    public void onTaskCompleted() {
        updateLayout();
    }

    @Override
    public void onTaskFailure() {
        Log.e("MainActivity", "Failure");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(Preferences.PREF_NAME_EXCHANGE_RATE, 0);

        currentBase = Utils.getLastBase(MainActivity.this);

        createViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Utils.shouldBeUpdate(MainActivity.this, currentBase))
            new AsyncTaskFixerIO(MainActivity.this, currentBase, MainActivity.this);
        else
            updateLayout();
    }

    private void createViews() {
        currencyValue = (TextView) findViewById(R.id.currencyValue);

        currencyName = (TextView) findViewById(R.id.currencyName);
        currencyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupCurrencyList(v);
            }
        });

        currencyList = (ListView) findViewById(R.id.currencyList);

        bottomInfo = (TextView) findViewById(R.id.bottomInfo);
    }

    private void showPopupCurrencyList(View v) {
        String[] items = new String[Preferences.CURRENCY_NAME_ALL_RATES.length];
        for (int i = 0; i < items.length; i++)
            items[i] = Preferences.CURRENCY_NAME_ALL_RATES[i] + " (" + Preferences.API_ALL_RATES[i] + ")";

        new AlertDialog.Builder(this)
                .setSingleChoiceItems(items, 0, null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        currentBase = Preferences.API_ALL_RATES[((AlertDialog) dialog).getListView().getCheckedItemPosition()];
                        if (Utils.shouldBeUpdate(MainActivity.this, currentBase))
                            new AsyncTaskFixerIO(MainActivity.this, currentBase, MainActivity.this);
                    }
                })
                .show();
    }

    private void updateLayout() {
        currencyName.setText(Utils.convertApiNameToCurrencyName(currentBase));

        currencyList.setAdapter(new AdapterCurrencyList(this, Utils.getCurrencyList(this)));

        bottomInfo.setText(String.format(getString(R.string.updated_on), sharedPreferences.getString(Preferences.PREF_DATE, Preferences.DEFAULT_DATE)));

        Utils.saveLastBase(currentBase, MainActivity.this);
    }
}

package com.android.argb.exchangerate;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    private static String TAG = "ExchangeRate";

    static public void logShared(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME_EXCHANGE_RATE, Context.MODE_PRIVATE);
        Log.i(TAG, "Base: " + sharedPreferences.getString(Constants.PREF_BASE, Constants.DEFAULT_BASE));
        Log.i(TAG, "Date: " + sharedPreferences.getString(Constants.PREF_DATE, Constants.DEFAULT_DATE));
        for (int i = 0; i < Constants.API_ALL_RATES.length; i++)
            Log.i(TAG, Constants.API_ALL_RATES[i] + ": " + sharedPreferences.getFloat(Constants.PREF_ALL_RATES[i], Constants.DEFAULT_RATE));
    }

    static public List<Currency> getCurrencyList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME_EXCHANGE_RATE, Context.MODE_PRIVATE);

        List<Currency> cry = new ArrayList<>();
        for (int i = 0; i < Constants.API_ALL_RATES.length; i++)
            cry.add(new Currency(sharedPreferences.getFloat(Constants.PREF_ALL_RATES[i], Constants.DEFAULT_RATE), Constants.CURRENCY_NAME_ALL_RATES[i], Constants.API_ALL_RATES[i]));

        return cry;
    }

    static public String convertApiNameToCurrencyName(String APIName) {
        for (int i = 0; i < Constants.API_ALL_RATES.length; i++) {
            if (APIName.equalsIgnoreCase(Constants.API_ALL_RATES[i]))
                return Constants.CURRENCY_NAME_ALL_RATES[i];
        }
        return "";
    }

    public static void saveLastBase(String currentBase, Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Constants.PREF_NAME_EXCHANGE_RATE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.PREF_CURRENT_BASE, currentBase);
        editor.apply();
    }

    public static String getLastBase(Context ctx) {
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Constants.PREF_NAME_EXCHANGE_RATE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.PREF_CURRENT_BASE, Constants.DEFAULT_BASE);
    }

    public static boolean shouldBeUpdate(Context ctx, String currentBase) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).format(new Date());
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(Constants.PREF_NAME_EXCHANGE_RATE, Context.MODE_PRIVATE);

        return !(currentDate.equalsIgnoreCase(sharedPreferences.getString(Constants.PREF_DATE, Constants.DEFAULT_DATE))
                && currentBase.equalsIgnoreCase(sharedPreferences.getString(Constants.PREF_BASE, Constants.DEFAULT_BASE)
        ));
    }
}

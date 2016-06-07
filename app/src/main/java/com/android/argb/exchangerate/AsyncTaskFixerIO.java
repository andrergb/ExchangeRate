package com.android.argb.exchangerate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AsyncTaskFixerIO {

    private String TAG = "AsyncTaskFixerIO";

    private Context ctx;
    private String currentBase;
    private InterfaceOnTaskCompleted listener;

    public AsyncTaskFixerIO(Context context, String currentBase, InterfaceOnTaskCompleted listener) {
        this.ctx = context;
        this.currentBase = currentBase;
        this.listener = listener;
        new GetRates().execute();
    }

    private class GetRates extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;

        @Override
        protected JSONObject doInBackground(String... args) {
            return new JSONParser().getJSONFromUrl(Constants.API_URL_LATEST_BASE + currentBase);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                SharedPreferences sharedPreferences = ctx.getSharedPreferences(Constants.PREF_NAME_EXCHANGE_RATE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(Constants.PREF_BASE, json.get(Constants.API_BASE).toString());
                editor.putString(Constants.PREF_DATE, json.get(Constants.API_DATE).toString());

                //rates
                for (int i = 0; i < Constants.API_ALL_RATES.length; i++)
                    if (!Constants.API_ALL_RATES[i].equalsIgnoreCase(currentBase))
                        editor.putFloat(Constants.PREF_ALL_RATES[i], (float) json.getJSONObject(Constants.API_RATES).getDouble(Constants.API_ALL_RATES[i]));
                    else
                        editor.putFloat(Constants.PREF_ALL_RATES[i], Constants.DEFAULT_RATE);

                editor.apply();

                listener.onTaskCompleted();

            } catch (JSONException e) {
                listener.onTaskFailure();

                Log.e(TAG, e.getMessage());
            }
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ctx);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
    }
}

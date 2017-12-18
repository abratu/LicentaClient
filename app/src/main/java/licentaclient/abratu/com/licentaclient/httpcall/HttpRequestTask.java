package licentaclient.abratu.com.licentaclient.httpcall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import licentaclient.abratu.com.licentaclient.R;
import licentaclient.abratu.com.licentaclient.constants.Constants;
import licentaclient.abratu.com.licentaclient.utils.InputStreamUtil;
import licentaclient.abratu.com.licentaclient.utils.StringUtils;

/**
 * Created by apetho on 12/18/2017.
 */

public class HttpRequestTask extends AsyncTask<Void, Void, String> {

    ProgressDialog asyncDialog;
    private String url;
    private Context context;
    private String message;
    private String method;
    private boolean showDialog = true;

    public HttpRequestTask( String url, Context context, String message, String method, boolean showDialog ) {
        this.url = url;
        this.context = context;
        this.message = message;
        this.method = method;
        this.asyncDialog = new ProgressDialog(context);
        this.showDialog = showDialog;
    }

    @Override
    protected void onPreExecute() {
        if (showDialog) {
            asyncDialog.setMessage("Loading...");
            asyncDialog.show();
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute( String s ) {
        if (showDialog) {
            asyncDialog.dismiss();
        }
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground( Void... params ) {
        HttpURLConnection urlConnection = null;
        String retVal = null;
        int retryCount = 0;
        boolean isSuccess = false;
        do {
            retryCount++;
            try {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                String token = sharedPreferences.getString(context.getString(R.string.token), "[]");
                urlConnection = buildUrlConnection();
                if (! "[]".equals(token)) {
                    urlConnection.setRequestProperty(Constants.AUTHORIZATION, token);
                }
                if (StringUtils.isNotEmpty(message) && method.equalsIgnoreCase("POST")) {
                    urlConnection.setDoOutput(true);
                    OutputStream os = urlConnection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    writer.write(message);
                    writer.flush();
                    writer.close();
                    os.close();
                }

                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    token = urlConnection.getHeaderField(Constants.AUTHORIZATION);
                    if (StringUtils.isNotEmpty(token) && ! "[]".equals(token)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(context.getString(R.string.token), token);
                        editor.commit();
                    }
                    InputStream is = urlConnection.getInputStream();
                    if (is != null) {
                        retVal = InputStreamUtil.getString(urlConnection.getInputStream());
                    }
                    isSuccess = true;
                } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    retVal = null;
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        } while (retryCount < 4 && ! isSuccess);

        return retVal;
    }

    private HttpURLConnection buildUrlConnection() throws IOException {
        URL url = new URL(this.url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(method);
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
        urlConnection.setReadTimeout(100000 /* milliseconds */);
        urlConnection.setConnectTimeout(150000 /* milliseconds */);
        return urlConnection;
    }
}

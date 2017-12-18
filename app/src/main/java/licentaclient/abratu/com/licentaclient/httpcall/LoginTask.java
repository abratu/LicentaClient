package licentaclient.abratu.com.licentaclient.httpcall;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

import licentaclient.abratu.com.licentaclient.R;
import licentaclient.abratu.com.licentaclient.constants.Constants;
import licentaclient.abratu.com.licentaclient.entity.User;

/**
 * Created by apetho on 12/18/2017.
 */

public class LoginTask extends AsyncTask<User, Void, String> {

    private Context context;
    private ProgressDialog asyncDialog;

    public LoginTask(Context context) {
        this.context = context;
        this.asyncDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        asyncDialog.setMessage("Loading...");
        asyncDialog.show();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        asyncDialog.dismiss();
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(User... params) {

        HttpHeaders requestHeaders = new HttpHeaders();
        String url = context.getString(R.string.url) + "/" + Constants.LOGIN;

        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        User user = new User(params[0].getUsername(), params[0].getPassword());

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

        try {
            // Make the network request
            ResponseEntity<String> response = restTemplate.postForEntity(url, user, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getHeaders().getAuthorization();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}


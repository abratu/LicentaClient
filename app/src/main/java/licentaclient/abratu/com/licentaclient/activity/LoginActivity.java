package licentaclient.abratu.com.licentaclient.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import licentaclient.abratu.com.licentaclient.MainActivity;
import licentaclient.abratu.com.licentaclient.R;
import licentaclient.abratu.com.licentaclient.constants.Constants;
import licentaclient.abratu.com.licentaclient.entity.Response;
import licentaclient.abratu.com.licentaclient.entity.User;
import licentaclient.abratu.com.licentaclient.httpcall.HttpRequestTask;
import licentaclient.abratu.com.licentaclient.httpcall.LoginTask;
import licentaclient.abratu.com.licentaclient.utils.FontManager;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUserName;
    private EditText editTextPassword;
    private TextView textViewInvalidPassword;
    private TextView textViewUserIcon;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boolean logout = false;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            logout = bundle.getBoolean(Constants.LOGOUT);
        }

        editTextUserName = (EditText) this.findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) this.findViewById(R.id.editTextPassword);
        textViewInvalidPassword = (TextView) this.findViewById(R.id.textViewInvalidPassword);
        textViewUserIcon = (TextView) this.findViewById(R.id.textViewUserIcon);
        textViewUserIcon.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME));
        textViewUserIcon.setText(getString(R.string.fa_icon_user_circle));

        editTextUserName.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME));
        editTextUserName.setHint(getString(R.string.fa_icon_user) + "  " + getString(R.string.user_name));

        editTextPassword.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME));
        editTextPassword.setHint(getString(R.string.fa_icon_key) + "  " + getString(R.string.password));

        final Context context = this;

        loginButton = (Button) this.findViewById(R.id.buttonLogin);
        loginButton.setTypeface(FontManager.getTypeface(this, FontManager.FONTAWESOME));
        loginButton.setText(getString(R.string.fa_unlock) + "  " + getString(R.string.sing_in));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin(context, editTextUserName.getText().toString(), editTextPassword.getText().toString());
            }
        });

        if (!logout) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String userName = sharedPreferences.getString(Constants.USER, "[]");
            String password = sharedPreferences.getString(Constants.PASSWORD, "[]");
            if ((!"[]".equals(userName) && !"[]".equals(password)) && (!"".equals(userName) && !"".equals(password))) {
                doLogin(context, userName, password);
            }
        } else {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.token), "");
            editor.putString(Constants.USER, "");
            editor.putString(Constants.PASSWORD, "");
            editor.putInt(Constants.CLIENT_ID, -1);
            editor.commit();
        }

    }

    private void doLogin(final Context context, final String userName, String password) {
        new LoginTask(context) {
            @Override
            protected void onPostExecute(String token) {
                super.onPostExecute(token);
                if (token != null) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.token), token);
                    editor.putString(Constants.USER, editTextUserName.getText().toString());
                    editor.putString(Constants.PASSWORD, editTextPassword.getText().toString());
                    editor.commit();

                    new HttpRequestTask(getString(R.string.url) + "/" + Constants.PERSONAL + "/" + Constants.FIND_BY_USER_NAME + "/" + userName, context, "", Constants.GET, true) {
                        @Override
                        protected void onPostExecute(String message) {
                            super.onPostExecute(message);
                            if (message == null) {
                                return;
                            }
                            User personal = Response.getResponseData(message, User.class);
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt(Constants.CLIENT_ID, personal.getId());
                            editor.commit();
                            Intent mapsIntent = new Intent(getApplicationContext(), MainActivity.class);
                            context.startActivity(mapsIntent);
                            finish();
                        }
                    }.execute();
                } else {
                    textViewInvalidPassword.setText(R.string.invalidUserNamePassword);
                }
            }
        }.execute(new User(userName, password));
    }
}

package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.SharedPreferenceContract;
import com.unilorin.vividmotion.pre_cbtapp.models.LoginResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.network.services.ServiceFactory;
import com.unilorin.vividmotion.pre_cbtapp.network.services.UserAccountService;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailAddressEditText;
    private EditText passwordEditText;
    private Button signInButton;

    private TextView signUpTextVIew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        instantiateViewObjects();
    }

    private void instantiateViewObjects() {
        signUpTextVIew = (TextView) findViewById(R.id.registerTextView);
        emailAddressEditText = (EditText) findViewById(R.id.emailAddressEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signInButton = (Button) findViewById(R.id.signInButton);

        signInButton.setOnClickListener(this);
        signUpTextVIew.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == signUpTextVIew) {
            Intent signUpIntent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(signUpIntent);
        }
        else if (v == signInButton) {
            new LoginTask().execute();
        }
    }

    private class LoginTask extends AsyncTask<Void, Void, LoginResponseStatus> {

        private final String emailAddress = emailAddressEditText.getText().toString();
        private final String password = passwordEditText.getText().toString();

        @Override
        protected LoginResponseStatus doInBackground(Void... params) {

            UserAccountService registrationService = ServiceFactory.getInstance().getUserAccountService();
            return registrationService.loginUser(emailAddress, password);
        }

        @Override
        protected void onPostExecute(LoginResponseStatus result) {
            super.onPostExecute(result);
            switch (result) {
                case ACCEPTED:
                    SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);
                    sharedPreferences.edit().putBoolean(SharedPreferenceContract.IS_LOGGED_IN, true).apply();
                    if (!sharedPreferences.contains(SharedPreferenceContract.FACULTY)) {
                        startActivity(new Intent(LoginActivity.this, SetupActivity.class));
                    }
                    else {
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    }
                    finish();
                    break;
                case INCORRECT_PASSWORD:
                    //TODO: do appropriate stuff
                    break;
                case NO_ACCOUNT_FOR_THIS_EMAIL:
                    //TODO: do appropriate stuff
                    break;
                case UNKNOWN_ERROR:
                    //TODO: do appropriate stuff
                    break;
            }
        }
    }
}

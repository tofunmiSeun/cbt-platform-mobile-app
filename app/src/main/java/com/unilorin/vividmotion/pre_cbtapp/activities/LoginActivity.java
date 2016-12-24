package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.entities.User;
import com.unilorin.vividmotion.pre_cbtapp.entities.UserLoginResponseObject;
import com.unilorin.vividmotion.pre_cbtapp.services.UserAccountService;

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

    private void instantiateViewObjects(){
        signUpTextVIew = (TextView) findViewById(R.id.registerTextView);
        emailAddressEditText = (EditText) findViewById(R.id.emailAddressEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signInButton = (Button) findViewById(R.id.signInButton);

        signInButton.setOnClickListener(this);
        signUpTextVIew.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == signUpTextVIew){
            Intent signUpIntent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(signUpIntent);
        }
        else if(v == signInButton){
            new LoginTask().execute();
        }
    }

    private class LoginTask extends AsyncTask<Void, Void, UserLoginResponseObject> {

        private final String emailAddress  = emailAddressEditText.getText().toString();
        private final String password  = passwordEditText.getText().toString();

        @Override
        protected UserLoginResponseObject doInBackground(Void... params) {

            UserAccountService registrationService = new UserAccountService();
            return registrationService.loginUser(emailAddress, password);
        }

        @Override
        protected void onPostExecute(UserLoginResponseObject result) {
            super.onPostExecute(result);
            switch (result.getStatus()){
                case UserLoginResponseObject.ACCEPTED:
                    // move on
                    User user = result.getUser();
                    break;
                case UserLoginResponseObject.INCORRECT_PASSWORD:
                    // show appropriate error message
                    break;
                case UserLoginResponseObject.NO_ACCOUNT_FOR_THIS_EMAIL: default:
                    // show appropriate error message
                    break;
            }
        }
    }
}

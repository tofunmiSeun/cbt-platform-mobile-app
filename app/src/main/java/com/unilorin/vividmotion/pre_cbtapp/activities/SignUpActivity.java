package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.models.SignUpResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.models.User;
import com.unilorin.vividmotion.pre_cbtapp.network.services.ServiceFactory;
import com.unilorin.vividmotion.pre_cbtapp.network.services.UserAccountService;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText;
    private EditText emailAddressEditText;
    private EditText phoneNumberEditText;
    private EditText passwordEditText;
    private TextView signUpButton;
    private TextView signInTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        instantiateViewObjects();
    }

    private void instantiateViewObjects() {
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailAddressEditText = (EditText) findViewById(R.id.emailAddressEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText);
        signUpButton = (TextView) findViewById(R.id.signUpButton);
        signInTextView = (TextView) findViewById(R.id.signInTextView);

        signUpButton.setOnClickListener(this);
        signInTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == signUpButton) {
            new RegisterUserTask().execute();
        }
        else if (v == signInTextView) {
            Intent signInIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(signInIntent);
        }
    }

    private class RegisterUserTask extends AsyncTask<Void, Void, SignUpResponseStatus> {

        private final String name = nameEditText.getText().toString();
        private final String emailAddress = emailAddressEditText.getText().toString();
        private final String password = passwordEditText.getText().toString();

        @Override
        protected SignUpResponseStatus doInBackground(Void... params) {
            User user = new User();
            user.setName(name);
            user.setEmailAddress(emailAddress);
            user.setPassword(password);

            UserAccountService registrationService = ServiceFactory.getInstance().getUserAccountService();
            return registrationService.registerNewUser(user);
        }

        @Override
        protected void onPostExecute(SignUpResponseStatus result) {
            super.onPostExecute(result);
            switch (result) {
                case ACCEPTED:
                    //TODO: do appropriate stuff
                    break;
                case EMAIL_ALREADY_IN_USE:
                    //TODO: do appropriate stuff
                    break;
                case UNKNOWN_ERROR:
                    //TODO: do appropriate stuff
                    break;
            }
        }
    }
}

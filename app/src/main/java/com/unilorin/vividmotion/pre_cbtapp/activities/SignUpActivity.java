package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.models.SignUpResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.models.User;
import com.unilorin.vividmotion.pre_cbtapp.network.services.ServiceFactory;
import com.unilorin.vividmotion.pre_cbtapp.network.services.UserAccountService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEditText;
    private EditText emailAddressEditText;
    private EditText phoneNumberEditText;
    private EditText passwordEditText;
    private Button signUpButton;
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
        signUpButton = (Button) findViewById(R.id.signUpButton);
        signInTextView = (TextView) findViewById(R.id.signInTextView);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Pattern pattern = Pattern.compile(".+@.+");
                Matcher matcher = pattern.matcher(emailAddressEditText.getText().toString());

                if (!passwordEditText.getText().toString().isEmpty() && matcher.matches()
                && !phoneNumberEditText.getText().toString().isEmpty() && !nameEditText.getText().toString().isEmpty()){
                    signUpButton.setEnabled(true);
                }else{
                    signUpButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        nameEditText.addTextChangedListener(watcher);
        emailAddressEditText.addTextChangedListener(watcher);
        passwordEditText.addTextChangedListener(watcher);
        phoneNumberEditText.addTextChangedListener(watcher);

        signUpButton.setEnabled(false);
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

    private void showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(SignUpActivity.this, R.style.alertDialog).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private class RegisterUserTask extends AsyncTask<Void, Void, SignUpResponseStatus> {

        private final String name = nameEditText.getText().toString();
        private final String emailAddress = emailAddressEditText.getText().toString();
        private final String password = passwordEditText.getText().toString();
        ProgressDialog prog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog = new ProgressDialog(SignUpActivity.this, R.style.alertDialog);
            prog.setMessage("Logging in...");
            prog.setCancelable(false);
            prog.setIndeterminate(true);
            prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prog.show();
        }

        @Override
        protected SignUpResponseStatus doInBackground(Void... params) {
            User user = new User();
            user.setName(name);
            user.setEmailAddress(emailAddress);
            user.setPassword(password);

            UserAccountService registrationService = ServiceFactory.getInstance().getUserAccountService(getApplicationContext());
            return registrationService.registerNewUser(user);
        }

        @Override
        protected void onPostExecute(SignUpResponseStatus result) {
            super.onPostExecute(result);
            prog.dismiss();
            switch (result) {
                case ACCEPTED:
                    startActivity(new Intent(SignUpActivity.this, SetupActivity.class));
                    finish();
                    break;
                case EMAIL_ALREADY_IN_USE:
                    emailAddressEditText.setText("");
                    showAlertDialog("", "This email address is already in use. Please use another email address.");
                    break;
                case UNKNOWN_ERROR:
                    showAlertDialog("", "An unknown error has occurred. Please try again.");
                    break;
            }
        }
    }
}

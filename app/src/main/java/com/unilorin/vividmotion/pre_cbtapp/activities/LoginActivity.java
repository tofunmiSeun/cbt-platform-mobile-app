package com.unilorin.vividmotion.pre_cbtapp.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.unilorin.vividmotion.pre_cbtapp.R;
import com.unilorin.vividmotion.pre_cbtapp.managers.data.SharedPreferenceContract;
import com.unilorin.vividmotion.pre_cbtapp.models.LoginResponseStatus;
import com.unilorin.vividmotion.pre_cbtapp.models.User;
import com.unilorin.vividmotion.pre_cbtapp.network.services.ServiceFactory;
import com.unilorin.vividmotion.pre_cbtapp.network.services.UserAccountService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Pattern pattern = Pattern.compile(".+@.+");
                Matcher matcher = pattern.matcher(emailAddressEditText.getText().toString());
                if (!passwordEditText.getText().toString().isEmpty() && matcher.matches()) {
                    signInButton.setEnabled(true);
                } else {
                    signInButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        emailAddressEditText.addTextChangedListener(watcher);
        passwordEditText.addTextChangedListener(watcher);

        signInButton.setEnabled(false);
        signInButton.setOnClickListener(this);
        signUpTextVIew.setOnClickListener(this);
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this, R.style.alertDialog).create();
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

    @Override
    public void onClick(View v) {
        if (v == signUpTextVIew) {
            Intent signUpIntent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(signUpIntent);
        } else if (v == signInButton) {
            new LoginTask().execute();
        }
    }

    private class LoginTask extends AsyncTask<Void, Void, LoginResponseStatus> {

        private final String emailAddress = emailAddressEditText.getText().toString();
        private final String password = passwordEditText.getText().toString();
        ProgressDialog prog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prog = new ProgressDialog(LoginActivity.this, R.style.alertDialog);
            prog.setMessage("Logging in...");
            prog.setCancelable(false);
            prog.setIndeterminate(true);
            prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            prog.show();
        }

        @Override
        protected LoginResponseStatus doInBackground(Void... params) {

            UserAccountService registrationService = ServiceFactory.getInstance().getUserAccountService(getApplicationContext());
            return registrationService.loginUser(emailAddress, password);
        }

        @Override
        protected void onPostExecute(LoginResponseStatus result) {
            super.onPostExecute(result);
            prog.dismiss();
            switch (result) {
                case ACCEPTED:
                    SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceContract.FILE_NAME, MODE_PRIVATE);

                    sharedPreferences.edit().putBoolean(SharedPreferenceContract.IS_LOGGED_IN, true).apply();

                    User currentUser = new Gson().fromJson(sharedPreferences.getString(SharedPreferenceContract.USER_ACCOUNT_JSON_STRING, null), User.class);

                    if (currentUser != null) {
                        if (currentUser.getStudentProfile() != null) {
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        } else {
                            startActivity(new Intent(LoginActivity.this, SetupActivity.class));
                        }
                        finish();
                        //overridePendingTransition(R.anim.slide_out_right, android.R.anim.slide_in_left);
                    }
                    break;
                case INCORRECT_PASSWORD:
                    //TODO: do appropriate stuff
                    passwordEditText.setText("");
                    showAlertDialog("", "The password you entered is incorrect. Please try again.");
                    break;
                case NO_ACCOUNT_FOR_THIS_EMAIL:
                    //TODO: do appropriate stuff
                    emailAddressEditText.setText("");
                    showAlertDialog("", "There is no account for this email address.");
                    break;
                case UNKNOWN_ERROR:
                    //TODO: do appropriate stuff
                    showAlertDialog("", "An unknown error has occurred. Please try again.");
                    break;
            }
        }
    }
}

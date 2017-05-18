package com.tbgosalvez.southhills;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    UserDB db = new UserDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final AutoCompleteTextView etEmail = (AutoCompleteTextView)findViewById(R.id.register_et_email);
        final EditText etPasswd = (EditText)findViewById(R.id.register_et_password);
        final TextView tvError = (TextView)findViewById(R.id.tv_register_errmsg);
        final Button btnReg = (Button)findViewById(R.id.register_btn_register);
        final Button btnCancel = (Button)findViewById(R.id.register_btn_cancel);

        final String strEmail = getIntent().getStringExtra(CONSTANTS.strEmail);

        // email already validated from previous activity
        etEmail.setText(strEmail);



        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String strPasswd;
                String strNewEmail;

                // field is not empty
                if(etPasswd.getText().toString().length() > 0)
                {
                    // in case they change it
                    strNewEmail = etEmail.getText().toString();

                    if(!LoginActivity.validateEmail(strNewEmail, RegisterActivity.this))
                    {
                        return;
                    }

                    strPasswd = etPasswd.getText().toString();

                    // validate passwd
                    if(validatePassword(strPasswd,tvError))
                    {
                        db.insertUser(strNewEmail, strPasswd);
                    }
                    else
                    {
                        return;
                    }

                    // feedback
                    Toast.makeText(RegisterActivity.this,
                                    "you're now in the db, "+ strNewEmail + " :-) .",
                                    Toast.LENGTH_LONG).show();

                    Intent iMain = new Intent(RegisterActivity.this, MainActivity.class);
                    iMain.putExtra(CONSTANTS.strEmail, strNewEmail);
                    startActivity(iMain);
                }
            }
        });



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    boolean validatePassword(String passwd, TextView tvErr)
    {
        String strErr;
        // TODO: fix regex. this works but not like supposed to
        Pattern pAlphaNumeric = Pattern.compile("[a-zA-Z][0-9]");
        Pattern pDebug = Pattern.compile("[a-z]");
        Pattern pSymbols = Pattern.compile("[!@#$%^&*()-_/?+]");
        Matcher m;

        // min length
        if(passwd.length() < 6)
        {
            tvErr.setText("password must contain at least 6 characters.");
            return false;
        }

        // check alphanumeric
        m = pAlphaNumeric.matcher(passwd);
//        m = pDebug.matcher(passwd);

        if(!m.find())
        {
            strErr = "password must contain at least one capital letter, "+
                    "at least one lowercase letter, and " +
                    "at least one number.";
            tvErr.setText(strErr);

            return false;
        }

        m = pSymbols.matcher(passwd);
        if(!m.find())
        {
            strErr = "password must contain at least one of the following symbols: " +
                    "!@#$%^&*()-_/?+";
            tvErr.setText(strErr);

            return false;
        }


        return true;
    }
}

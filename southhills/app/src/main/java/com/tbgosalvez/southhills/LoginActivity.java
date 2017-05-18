package com.tbgosalvez.southhills;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.PatternMatcher;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    UserDB db = new UserDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final AutoCompleteTextView etEmail = (AutoCompleteTextView)findViewById(R.id.login_et_email);
        final EditText etPasswd = (EditText)findViewById(R.id.login_et_password);
        final Button btnLogin = (Button)findViewById(R.id.login_btn_login);



        //
        // db stuff
        //
        btnLogin.setOnClickListener(new View.OnClickListener()
        {
            private String strEmail, strPasswd;

            @Override
            public void onClick(View v)
            {
                Cursor cur;

                // if email is blank, do nothing.
                // else, do everything.
                if(etEmail.getText().toString().length()>0)
                {
                    // TODO: email validation
                    strEmail = etEmail.getText().toString();
                    if(!validateEmail(strEmail,LoginActivity.this))
                        return;

                    // TODO: password rules & validation
                    if(etPasswd.getText().toString().length() >0)
                        strPasswd = etPasswd.getText().toString();
                    else
                        strPasswd = "temp";


                    // select from users;
                    // store record(s) into "cur"
                    cur  = db.getUser(strEmail);


                    // not in db
                    if(cur.getCount() == 0)
                    {
                        Toast.makeText(LoginActivity.this, "please register :-) .", Toast.LENGTH_LONG)
                                .show();

                        // take to registration activity, bring email
                        Intent iReg = new Intent(LoginActivity.this, RegisterActivity.class);
                        iReg.putExtra(CONSTANTS.strEmail, strEmail);
                        startActivity(iReg);
                    }
                    else // in db
                    {
                        cur.moveToFirst();
                        String strRecordPass = cur.getString(cur.getColumnIndex(CONSTANTS.strPasswd));
                        if(!cur.isClosed())
                            cur.close();

                        // validate pass
                        // TODO: md5 or sha256
                        if (strRecordPass.equals(strPasswd))
                        {
                            //success, move on
                            Intent iMain = new Intent(LoginActivity.this, MainActivity.class);
                            iMain.putExtra(CONSTANTS.strEmail, strEmail);
                            startActivity(iMain);
                        }
                        else // wrong pass, or left blank
                            // (will be same thing after rules are implemented)
                        {
                            Toast.makeText(LoginActivity.this, "incorrect password.", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Please enter email address.", Toast.LENGTH_SHORT).show();
                }
            }

        });

    }

    @Override
    protected void onDestroy()
    {
        db.close();
        super.onDestroy();
    }

    static boolean validateEmail(String email, Context context)
    {
        Pattern p = Pattern.compile("[,!#$%^&*()]");
        Matcher m = p.matcher(email);

        String[] strParts = email.split("[@]");

        // check for illegal characters
        if(m.find())
        {
            Toast.makeText(context, "illegal characters.", Toast.LENGTH_LONG).show();
            return false;
        }

        // check for @
        if(strParts.length != 2)
        {
            Toast.makeText(context, "put only one @ sign.", Toast.LENGTH_LONG).show();
            return false;
        }
        else
        {
            // check for .
            //String strUser = strParts[0];
            String strDomain = strParts[1];

            if(!strDomain.contains("."))
            {
                Toast.makeText(context, "domains have dots.", Toast.LENGTH_LONG).show();
                return false;
            }

            if(strDomain.endsWith("."))
            {
                Toast.makeText(context, "missing top-level domain.", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }
}

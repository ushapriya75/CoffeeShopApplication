package com.org.coffeeshop;

import android.content.Intent;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.org.coffeeshop.Utils.Constant;
import com.org.coffeeshop.Utils.Preferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterationActivity extends AppCompatActivity {

    TextInputEditText mName, mEmailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Preferences.getStringPreferences(Constant.KEY_SAVED_Email).equals("")) {
            openHomeScreen();
        }

        setContentView(R.layout.activity_registeration);

        mName = findViewById(R.id.name_text_input_edittext);
        mEmailId = findViewById(R.id.email_text_input_edittext);
    }


    private void openHomeScreen() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    public void onRegisterClick(View view) {
        String name, email;
        name = mName.getText().toString().trim();
        email = mEmailId.getText().toString().trim();

        if (!name.equals("") && isEmailValid(email)) {

            Preferences.saveStringPreferencessync(Constant.KEY_SAVED_Email, email);
            Preferences.saveStringPreferencessync(Constant.KEY_SAVED_Name, name);
            Toast.makeText(this, R.string.reg_suc_msg, Toast.LENGTH_LONG).show();
            openHomeScreen();
        } else {
            Toast.makeText(this, R.string.email_name_invalid, Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

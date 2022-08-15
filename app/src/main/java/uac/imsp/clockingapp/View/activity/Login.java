package uac.imsp.clockingapp.View.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import uac.imsp.clockingapp.BuildConfig;
import uac.imsp.clockingapp.Controller.control.LoginController;
import uac.imsp.clockingapp.Controller.util.ILoginController;
import uac.imsp.clockingapp.R;
import uac.imsp.clockingapp.View.util.ILoginView;
import uac.imsp.clockingapp.View.util.ToastMessage;

public class Login extends AppCompatActivity
                   implements View.OnClickListener , TextWatcher,
        ILoginView {
    private ImageView eye;
    private  transient EditText editText1, editText2;

    private  EditText Username,Password;
    private  SharedPreferences preferences;
    int currentVersionCode,savedVersionCode;
    final String PREFS_NAME="MyPrefsFile",
            PREF_VERSION_CODE_KEY="version_code";
    final int DOESNT_EXIST=-1;


    ILoginController loginPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPresenter = new LoginController(this);
        // Get current version code
        preferences=getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        savedVersionCode=preferences.getInt(PREF_VERSION_CODE_KEY,DOESNT_EXIST);
        currentVersionCode= BuildConfig.VERSION_CODE;
        loginPresenter.onLoad(savedVersionCode,currentVersionCode);
        //update the shares preferences with the current version code

        preferences.edit().putInt(PREF_VERSION_CODE_KEY,currentVersionCode).apply();


    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.login_button)
            loginPresenter.onLogin(Username.getText().toString(),
                Password.getText().toString());
        else if(v.getId()==R.id.login_show_password)
            loginPresenter.onShowHidePassword();


    }

    @Override
    public void onLoginSuccess(String message,int number) {
        Username.setText("");
        Password.setText("");
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("CURRENT_USER",number);
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();

        startActivity(intent);

    }

    @Override
    public void onLoginError(String message) {

        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFirstRun() {
        Intent intent=new Intent(Login.this, SetUp.class);
        //stop Login activity
        Login.this.finish();
        // start SetUp activity
        startActivity(intent);

    }

    @Override
    public void onNormalRun() {

        setContentView(R.layout.activity_login);
        initView();

    }

    @Override
    public void onUpgrade() {

    }

    @Override
    public void onClocking() {
    Intent intent=new Intent(Login.this,ClockInOut.class);
    startActivity(intent);

    }

    @Override
    public void onPasswordError(String message) {
        Password.setError(message);

    }

    @Override
    public void onUsernameError(String message) {
        Username.setError(message);

    }

    @Override
    public void onShowHidePassword() {
            if(Password.getTransformationMethod().
                    equals(PasswordTransformationMethod.getInstance()))
            {
                eye.setImageResource(R.drawable.ic_baseline_visibility_off_18);


                //show password
                Password.setTransformationMethod(HideReturnsTransformationMethod.
                        getInstance());
            }
            else{
                eye.setImageResource(R.drawable.baseline_visibility_black_18);


                //hide password
                Password.setTransformationMethod(PasswordTransformationMethod.
                        getInstance());
            }


        }

    @Override
    public void onMaxAttempsReached(String message) {
        Intent intent;
        intent = new Intent(Login.this,StartScreen.class);

        ToastMessage Toast=new ToastMessage(this,message);
        Toast.show();
        finish();
        startActivity(intent);
    }


    public void initView(){

        Username=findViewById(R.id.login_username);
        Password=findViewById((R.id.login_password));
        eye=findViewById(R.id.login_show_password);
        eye.setOnClickListener(this);

        Button login = findViewById(R.id.login_button);
        login.setOnClickListener(this);
        Username.addTextChangedListener(this);
        Password.addTextChangedListener(this);

        Password.setOnClickListener(this);



    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {


    }

    @Override
    public void afterTextChanged(Editable s) {
        if(Username.getText().toString().equals(s.toString()))
            loginPresenter.onUsernameEdit(s.toString());
        else if(Password.getText().toString().equals(s.toString()))

            loginPresenter.onPasswordEdit(s.toString());


    }
}
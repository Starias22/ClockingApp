package uac.imsp.clockingapp.View.activity.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import uac.imsp.clockingapp.Controller.control.ChangePasswordController;
import uac.imsp.clockingapp.Controller.util.IChangePasswordController;
import uac.imsp.clockingapp.R;
import uac.imsp.clockingapp.View.util.IAccountView;
import uac.imsp.clockingapp.View.util.ToastMessage;

public class Account extends AppCompatActivity implements View.OnClickListener, IAccountView {
	LinearLayout password;
	private IChangePasswordController changePasswordPresenter;
	EditText oldPassword,newPassword,newPasswordConfirm;
	TextView username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);
		changePasswordPresenter=new ChangePasswordController(this) ;

		changePasswordPresenter.onStart(getIntent().getIntExtra("CURRENT_USER",1));
		initView();
	}

	public void initView(){
		final Button changePassword=findViewById(R.id.change_password);
		 username=findViewById(R.id.username);
		  password=findViewById(R.id.password);
		changePassword.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.password) {
			password.setVisibility(View.VISIBLE);
			newPassword = findViewById(R.id.change_password_new_password);
			oldPassword = findViewById(R.id.change_password_old_password);
			newPasswordConfirm = findViewById(R.id.change_password_new_password_confirm);
		}
		else {

			changePasswordPresenter.onSubmit(oldPassword.getText().toString().trim(),
					newPassword.getText().toString().trim(),
					newPasswordConfirm.getText().toString().trim()
			);
		}

	}

	@Override
	public void onSuccess() {
		String message=getString(R.string.password_changed_successfully);
		new ToastMessage(this,message);


	}

	@Override
	public void onWrongPassword() {
		String message=getString(R.string.password_invalid);
		new ToastMessage(this,message);

	}

	@Override
	public void onStart(String username) {
		this.username.setText(username);

	}
}
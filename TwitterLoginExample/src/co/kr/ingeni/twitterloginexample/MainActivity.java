package co.kr.ingeni.twitterloginexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button twitterLoginBtn, twitterLogoutBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		twitterLoginBtn = (Button) findViewById(R.id.twitter_login_btn);
		twitterLogoutBtn = (Button) findViewById(R.id.twitter_logout_btn);
		
		twitterLoginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.e("TwitterLogin.twitterLoginCheck", ""+ TwitterLogin.getTwitterLoginCheck());
				if (TwitterLogin.getTwitterLoginCheck() != true) {
					TwitterLogin twitterLogin = new TwitterLogin(MainActivity.this);
					twitterLogin.setTwitterLogin();
				} else {
					startActivity(new Intent(MainActivity.this, TwitterWriteActivity.class));
				}
			}
		});
		
		twitterLogoutBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				TwitterLogin twitterLogin = new TwitterLogin(MainActivity.this);
				twitterLogin.removeAccessToken();
				twitterLoginBtn.setText("Twitter Login");
				v.setVisibility(View.GONE);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if (TwitterLogin.getTwitterLoginCheck() != false) {
			twitterLoginBtn.setText("Twitter Write");
			twitterLogoutBtn.setVisibility(View.VISIBLE);
			} 
	}
	
}

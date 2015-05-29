package co.kr.ingeni.twitterloginexample;

import java.util.Timer;
import java.util.TimerTask;

import twitter4j.User;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class TwitterWriteActivity extends FragmentActivity {

	private Button twitterWriteBtn;
	private EditText twitterEditTxt;
	private TextView twitterUserName, twitterScreenName, countTxt;
	private ImageView twitterProfilePhoto;
	
	protected ImageLoader imageLoader;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	private DisplayImageOptions options;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter_write);
		
		ImageView backBtn = (ImageView) findViewById(R.id.backBtn);
		twitterWriteBtn = (Button) findViewById(R.id.twitter_write_btn);
		twitterUserName = (TextView) findViewById(R.id.twitter_name);
		twitterScreenName = (TextView) findViewById(R.id.twitter_screen_name);
		twitterProfilePhoto = (ImageView) findViewById(R.id.twitter_profile_image);
		twitterEditTxt = (EditText) findViewById(R.id.twitter_edit_txt);
		countTxt = (TextView) findViewById(R.id.text_count);
		
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(ImageLoaderConfiguration.createDefault(TwitterWriteActivity.this));
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new RoundedBitmapDisplayer(5))
		.build();
		
		if(twitterEditTxt.getText().toString().length()==0){
			twitterWriteBtn.setEnabled(false);
			}
		
		new TwitterLogin.getTwitterUserData().doRunExecute(TwitterWriteActivity.this, new TwitterUserTaskFunction() {
			
			@Override
			public void doFinish(User result) {
				try {
				Log.e("userData", result.toString());
				
				TwitterLogin twitterLogin = new TwitterLogin(TwitterWriteActivity.this);
				twitterLogin.saveAccessToken();
				
				twitterUserName.setText(result.getName());
				twitterScreenName.setText("@"+ result.getScreenName());
				ImageLoader.getInstance().displayImage(result.getProfileImageURL(), twitterProfilePhoto, options, animateFirstListener);
				
				} catch (NullPointerException e) {
					
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
						
						@Override
						public void run() {
							finish();		
						}
					}, 500);
				}
			}
		});
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		twitterWriteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new TwitterLogin.updateTwitterStatus()
				.doRunExecute(TwitterWriteActivity.this, twitterEditTxt.getText().toString(), new TwitterWriteTaskFunction() {
					@Override
					public void doFinish(Boolean result) {
						Log.e("result", ""+result);
						if(result != true){
							Toast.makeText(TwitterWriteActivity.this, "Successful Tweet", Toast.LENGTH_SHORT).show();
							finish();	
						} else {
							Toast.makeText(TwitterWriteActivity.this, "Error Tweet or Duplication Tweet", Toast.LENGTH_SHORT).show();
						}
						
					}
				});
			}
		});
		
		
		twitterEditTxt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
					countTxt.setText(""+(140-s.length()));
					
					if(140-s.length()<0){
					  countTxt.setTextColor(TwitterWriteActivity.this.getResources().getColor(R.color.red));
					  twitterWriteBtn.setEnabled(false);
					} else if(s.length() == 0){
						twitterWriteBtn.setEnabled(false);
					} else {
					  countTxt.setTextColor(TwitterWriteActivity.this.getResources().getColor(R.color.hint_font));
					  twitterWriteBtn.setEnabled(true);
					} 
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
}
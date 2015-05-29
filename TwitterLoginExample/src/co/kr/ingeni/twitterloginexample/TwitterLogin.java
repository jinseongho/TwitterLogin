package co.kr.ingeni.twitterloginexample;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class TwitterLogin {

	public static String TWITTER_CONSUMER_KEY = "TWITTER_CONSUMER_KEY";
	public static String TWITTER_CONSUMER_SECRET = "TWITTER_CONSUMER_SECRET";

	static final String TWITTER_CALLBACK_URL = "oauth://twitter";
	static final String URL_TWITTER_AUTH = "auth_url";
	static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";

	public static Context mContext;
	public static Twitter twitter;
	public static RequestToken requestToken;
	public static User twitterUserData;
	public static Boolean twitterLoginCheck = false;
	public static ProgressDialog pDialog;
	public static AccessToken accessToken;

	TwitterWebView twitterWebView;

	public TwitterLogin(Context context) {
		super();
		this.mContext = context;
	}

	public void setTwitterLogin() {

		if (TWITTER_CONSUMER_KEY.trim().length() == 0
				|| TWITTER_CONSUMER_SECRET.trim().length() == 0) {
			Toast.makeText(mContext,
					"TWITTER_CONSUMER_KEY/TWITTER_CONSUMER_SECRET error",
					Toast.LENGTH_SHORT).show();
		} else {
			loginToTwitter();
		}
	}

	private void loginToTwitter() {

		ConfigurationBuilder builder = new ConfigurationBuilder();
		builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
		builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
		Configuration configuration = builder.build();

		TwitterFactory factory = new TwitterFactory(configuration);
		twitter = factory.getInstance();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
					
					/**custom webView **/
					Intent intent = new Intent(mContext, TwitterWebView.class);
					intent.putExtra("twitter_url", Uri.parse(requestToken.getAuthorizationURL().toString()));
					mContext.startActivity(intent);

					/** no custom webView **/
//					 Intent intent = new Intent(Intent.ACTION_VIEW,
//					 Uri.parse(requestToken.getAuthorizationURL()));
//					 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
//					 mContext.startActivity(intent);

				} catch (TwitterException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	public static class getTwitterUserData extends AsyncTask<User, User, User> {
		private Context mContext;
		private TwitterUserTaskFunction taskFunction;
		User user = null;

		public void doRunExecute(Context context,
				TwitterUserTaskFunction taskRun) {
			this.mContext = context;
			this.taskFunction = taskRun;
			execute(user);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Get to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected User doInBackground(User... args) {

			SharedPreferences mSharedPreferences = mContext
					.getApplicationContext().getSharedPreferences(
							"twitter_login", Activity.MODE_PRIVATE);

			String access_token = mSharedPreferences.getString(
					PREF_KEY_OAUTH_TOKEN, "");
			String access_token_secret = mSharedPreferences.getString(
					PREF_KEY_OAUTH_SECRET, "");

			Uri uri = ((Activity) mContext).getIntent().getData();

			if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
				String verifier = uri
						.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

				try {
					accessToken = twitter.getOAuthAccessToken(requestToken,
							verifier);
					Log.e("Twitter OAuth getToken",
							"> " + accessToken.getToken());
					Log.e("Twitter OAuth getTokenSecret",
							"> " + accessToken.getTokenSecret());

					long userID = accessToken.getUserId();
					twitterUserData = twitter.showUser(userID);
				} catch (Exception e) {
					Log.e("Twitter Login Error", "> " + e.getMessage());
					twitterUserData = null;
				}
			} else if (access_token.length() > 0) {

				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
				builder.setOAuthAccessToken(access_token);
				builder.setOAuthAccessTokenSecret(access_token_secret);
				Configuration configuration = builder.build();
				TwitterFactory factory = new TwitterFactory(configuration);
				Twitter twitter = factory.getInstance();
				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				long userID = accessToken.getUserId();
				try {
					twitterUserData = twitter.showUser(userID);
				} catch (TwitterException e) {
					e.printStackTrace();
				}
			}
			return twitterUserData;
		}

		@Override
		protected void onPostExecute(User result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			taskFunction.doFinish(result);
		}
	}

	public static class updateTwitterStatus extends
			AsyncTask<String, String, String> {

		private Context mContext;
		private String mTwitterText = "";
		private TwitterWriteTaskFunction twitterWriteTaskFunction;
		private Boolean errorCheck = false;

		public void doRunExecute(Context context, String twitterText,
				TwitterWriteTaskFunction taskFunction) {
			this.mContext = context;
			this.twitterWriteTaskFunction = taskFunction;
			this.mTwitterText = twitterText;
			execute("");
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			String status = mTwitterText;

			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
				builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

				SharedPreferences mSharedPreferences = mContext
						.getApplicationContext().getSharedPreferences(
								"twitter_login", Activity.MODE_PRIVATE);

				// Access Token
				String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token,
						access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build())
						.getInstance(accessToken);

				// Update status
				twitter4j.Status response = twitter.updateStatus(status);
				 Log.e("Status", "> " + response.getText());
			} catch (TwitterException e) {
				 Log.e("Twitter Update Error", e.getMessage());
				errorCheck = true;
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			twitterWriteTaskFunction.doFinish(errorCheck);
		}
	}

	public static boolean getTwitterLoginCheck() {

		boolean currentCheck = false;
		String prefKey = "";
		
		try {
		SharedPreferences mSharedPreferences = mContext.getApplicationContext()
				.getSharedPreferences("twitter_login", Activity.MODE_PRIVATE);
		prefKey = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
		
		} catch (NullPointerException e) {
			Log.e("NullPointerException", ""+ e.toString());
			currentCheck = false;
		}

		if (prefKey.length() > 0) {
			currentCheck = true;
		} else {
			currentCheck = false;
		}

		return currentCheck;
	}

	public void saveAccessToken() {
		SharedPreferences mSharedPreferences = mContext.getApplicationContext()
				.getSharedPreferences("twitter_login", Activity.MODE_PRIVATE);
		Editor e = mSharedPreferences.edit();
		e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
		e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
		e.commit();
	}

	public void removeAccessToken() {
		SharedPreferences mSharedPreferences = mContext.getApplicationContext()
				.getSharedPreferences("twitter_login", Activity.MODE_PRIVATE);
		Editor e = mSharedPreferences.edit();
		e.remove(PREF_KEY_OAUTH_TOKEN);
		e.remove(PREF_KEY_OAUTH_SECRET);
		e.clear();
		e.commit();
	}
}

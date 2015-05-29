# TwitterLogin
Twitter Login Example

# How Use
setTwitterLogin();
-> Twitter Login oAuth

getTwitterLoginCheck();
-> Twitter Login State Check

getTwitterUserData();
-> Twitter User Data
<BR>
example : 


			new TwitterLogin.getTwitterUserData().doRunExecute(TwitterWriteActivity.this, 
			new TwitterUserTaskFunction() {
			@Override
			public void doFinish(User result) {
  			Log.e("userData", result.toString());
			}
		});

saveAccessToken();
-> Twitter Auto Login SharedPreferences Save

removeAccessToken();
-> Twitter Logout SharedPreferences Remove

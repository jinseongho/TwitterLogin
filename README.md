# TwitterLogin
Twitter Login Example

# How Use
setTwitterLogin();
<BR>
-> Twitter Login oAuth

getTwitterLoginCheck();
<BR>
-> Twitter Login State Check

getTwitterUserData();
<BR>
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
<BR>
-> Twitter Auto Login SharedPreferences Save

removeAccessToken();
<BR>
-> Twitter Logout SharedPreferences Remove

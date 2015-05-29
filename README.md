# TwitterLogin
Twitter Login Example

# How Use
setTwitterLogin();
<BR>
-> Twitter Login oAuth
<BR>
exmple:
``` java	
	TwitterLogin twitterLogin = new TwitterLogin(MainActivity.this);
	twitterLogin.setTwitterLogin();
```	

getTwitterLoginCheck();
<BR>
-> Twitter Login State Check
<BR>
example: 
``` java
		if (TwitterLogin.getTwitterLoginCheck() != false) {
			twitterLoginBtn.setText("Twitter Write");
			twitterLogoutBtn.setVisibility(View.VISIBLE);
		} 
```

getTwitterUserData();
<BR>
-> Twitter User Data
<BR>
example: 
``` java
		new TwitterLogin.getTwitterUserData().doRunExecute(TwitterWriteActivity.this, 
			new TwitterUserTaskFunction() {
			@Override
			public void doFinish(User result) {
  			Log.e("userData", result.toString());
			}
		});
```

updateTwitterStatus();
<BR>
-> Post Tweet
<BR>
example:
``` java	
		twitterWriteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new TwitterLogin.updateTwitterStatus()
				.doRunExecute(TwitterWriteActivity.this, twitterEditTxt.getText().toString()
				, new TwitterWriteTaskFunction() {
					@Override
					public void doFinish(Boolean result) {
						Log.e("result", ""+result);
						if(result != true){
							Toast.makeText(TwitterWriteActivity.this, "Successful Tweet"
							, Toast.LENGTH_SHORT).show();
							finish();	
						} else {
							Toast.makeText(TwitterWriteActivity.this
							, "Error Tweet or Duplication Tweet", Toast.LENGTH_SHORT).show();
						}
						
					}
				});
			}
		});
```	

saveAccessToken();
<BR>
-> Twitter Auto Login SharedPreferences Save
<BR>
example:
``` java	
		TwitterLogin twitterLogin = new TwitterLogin(TwitterWriteActivity.this);
		twitterLogin.saveAccessToken();
```	
removeAccessToken();
<BR>
-> Twitter Logout SharedPreferences Remove
<BR>
example:
``` java	
		TwitterLogin twitterLogin = new TwitterLogin(MainActivity.this);
		twitterLogin.removeAccessToken();
```	

# Preview
![ScreenShot](https://s3-ap-northeast-1.amazonaws.com/smart-jundanji/smart_jundanji_images/1432884662360.png)


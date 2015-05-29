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
	

updateTwitterStatus();
<BR>
-> Post Tweet
<BR>
example :

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

saveAccessToken();
<BR>
-> Twitter Auto Login SharedPreferences Save

removeAccessToken();
<BR>
-> Twitter Logout SharedPreferences Remove

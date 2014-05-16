package uni.projecte.dataLayer.CitationManager.Synchro;

public class User {

	private String username;
	private String password;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isLogged() {
		return !username.equals("") && !password.equals("");
	}

}

/*
 * 
 * 
 *  String seedValue = "This Is MySecure";
      
       @Override
       protected void onCreate(Bundle savedInstanceState) {
              super.onCreate(savedInstanceState);
                 String normalText = "VIJAY";
              String normalTextEnc;
                     try {
                               normalTextEnc = AESHelper.encrypt(seedValue, normalText);
                               String normalTextDec = AESHelper.decrypt(seedValue, normalTextEnc);
                             TextView txe = new TextView(this);
                             txe.setTextSize(14);
                             txe.setText("Normal Text ::"+normalText +" \n Encrypted Value :: "+normalTextEnc +" \n Decrypted value :: "+normalTextDec);
                             setContentView(txe);

 * 
 */
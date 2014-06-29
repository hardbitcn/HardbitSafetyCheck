package com.hardbit.hbsc;

import com.hardbit.hbsc.R;
import com.hardbit.hbsc.Constants;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class InputPasswordActivity extends Activity {
	 EditText inputpwd;
	 EditText address;
	 TextView title = null;
	 float rate = 0.0f;
	 TextView label1;	 
	 TextView label_address;	
	 Button btn_1;
	 HbscApplication hbsc;
	 String walletString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inputpassword);
		hbsc=(HbscApplication)this.getApplication();
		title = (TextView)this.findViewById(R.id.title);
		label_address=(TextView)this.findViewById(R.id.label_address);
		inputpwd = (EditText)this.findViewById(R.id.password);
		address=(EditText)this.findViewById(R.id.address);
		label1 = (TextView)this.findViewById(R.id.label_inputpassword);		

		Button btn_1 = (Button)this.findViewById(R.id.btn_1);
 
		 DisplayMetrics displayMetrics = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         Constant.displayWidth = displayMetrics.widthPixels;
         Constant.displayHeight = displayMetrics.heightPixels;
         if(Constant.displayWidth / 480 > 1)
         {
        	  rate = (float)Constant.displayWidth/480;        	 
         }else {
			rate = 1;
		 }
         LinearLayout.LayoutParams params0 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.7f + 0.5f),
                 (int) (Constant.displayHeight * 0.08f + 0.5f));
         double jushang =  Constant.displayHeight * 0.03 * 2;
         double juzuo = Constant.displayWidth * 0.3 * 2;
         params0.setMargins((int)juzuo,  (int)jushang, 0, 0);
         title.setTextSize((int)(title.getTextSize()*rate));
         title.setLayoutParams(params0);         
         
         LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.9f + 0.5f),
                 (int) (Constant.displayHeight * 0.06f + 0.5f));         
         
         double jushang2 =  Constant.displayHeight * 0.04 * 2;
         double juzuo2 = Constant.displayWidth * 0.02 * 2;
         
         params1.setMargins((int)juzuo2,   2, 0, 0);
         label1.setTextSize((int)(label1.getTextSize()*rate));
         label1.setLayoutParams(params1);
         label_address.setTextSize((int)(label_address.getTextSize()*rate));
         label_address.setLayoutParams(params1);
         
         LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.9f + 0.5f),
                LayoutParams.WRAP_CONTENT);
                	         
         params2.setMargins((int)juzuo2,   2, 0, 0);
         inputpwd.setTextSize((int)(inputpwd.getTextSize()*rate));
         inputpwd.setLayoutParams(params2);
         address.setTextSize((int)(address.getTextSize()*rate));
         address.setLayoutParams(params2);         
         
         LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.7f + 0.5f),
                 (int) (Constant.displayHeight * 0.09f + 0.5f));
                	         
         params5.setMargins((int)juzuo2,   2, 0, 0);
         btn_1.setTextSize((int)(btn_1.getTextSize()*rate));
         btn_1.setLayoutParams(params5);
		btn_1.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(inputpwd.length()<10){
					Toast.makeText(InputPasswordActivity.this, getText(R.string.info_lessdigits), Toast.LENGTH_SHORT).show();
					inputpwd.requestFocus();
					return;
				}
				if(hbsc.wallet.checkPassword(inputpwd.getText().toString())){
					dialog(InputPasswordActivity.this.getString(R.string.text_passwordok),true);
				}else{
					dialog(InputPasswordActivity.this.getString(R.string.text_passwordfail),false);
				}
			}
		});
		String addressString=hbsc.wallet.getKeys().get(0).toAddress(Constants.NETWORK_PARAMETERS).toString();	
		address.setText(addressString);
	}	
	
	protected void dialog(String message,final boolean finish) {    	
		AlertDialog.Builder builder = new Builder(InputPasswordActivity.this);    	 
    	builder.setTitle(this.getString(R.string.btn_checkbackup));       	
    	builder.setMessage(message);
    	builder.setPositiveButton(this.getString(R.string.queding), new  OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();		
				if (finish) finish();
			}
		});    	 
    	builder.create().show();
    }
	
	
}

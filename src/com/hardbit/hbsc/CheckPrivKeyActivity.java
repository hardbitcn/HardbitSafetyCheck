package com.hardbit.hbsc;


import com.hardbit.hbsc.R;


import android.os.Bundle;
import android.app.Activity;

import android.util.DisplayMetrics;
import android.util.Log;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class CheckPrivKeyActivity extends Activity {

	 EditText privkey;
	 EditText address;
	 TextView title = null;
	 float rate = 0.0f;
	 TextView label_inputdigits;
	 TextView label_derivedaddress;
	 Button btn_1;
	 HbscApplication hbsc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkprivkey);
		hbsc=(HbscApplication)this.getApplication();
		title = (TextView)this.findViewById(R.id.title);
		privkey = (EditText)this.findViewById(R.id.privkey);
		address = (EditText)this.findViewById(R.id.address);
		label_inputdigits = (TextView)this.findViewById(R.id.label_inputdigits);
		label_derivedaddress = (TextView)this.findViewById(R.id.label_derivedaddress);

		Button btn_1 = (Button)this.findViewById(R.id.btn_1);
		//Button btn_2 = (Button)this.findViewById(R.id.btn_2);
		//jine_ed.setInputType(EditorInfo.); 
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
         
         Log.i("aaa", "gao" +  Constant.displayHeight + "kuan" + Constant.displayWidth);
         double jushang2 =  Constant.displayHeight * 0.04 * 2;
         double juzuo2 = Constant.displayWidth * 0.02 * 2;
         
         params1.setMargins((int)juzuo2,   2, 0, 0);
         label_inputdigits.setTextSize((int)(label_inputdigits.getTextSize()*rate));
         label_inputdigits.setLayoutParams(params1);
         
         LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.9f + 0.5f),
                LayoutParams.WRAP_CONTENT);
                	         
         params2.setMargins((int)juzuo2,   2, 0, 0);
         privkey.setTextSize((int)(privkey.getTextSize()*rate));
         privkey.setLayoutParams(params2);
         
         LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.9f + 0.5f),
                 (int) (Constant.displayHeight * 0.058f + 0.5f));
                	         
         params3.setMargins((int)juzuo2,   2, 0, 0);
         label_derivedaddress.setTextSize((int)(label_derivedaddress.getTextSize()*rate));
         label_derivedaddress.setLayoutParams(params3);
         
         LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.9f + 0.5f),
                  LayoutParams.WRAP_CONTENT);
                	         
         params4.setMargins((int)juzuo2,   2, 0, 0);
         address.setTextSize((int)(address.getTextSize()*rate));
         address.setLayoutParams(params4);
         
         LinearLayout.LayoutParams params5 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.7f + 0.5f),
                 (int) (Constant.displayHeight * 0.09f + 0.5f));
                	         
         params5.setMargins((int)juzuo2,   2, 0, 0);
         btn_1.setTextSize((int)(btn_1.getTextSize()*rate));
         btn_1.setLayoutParams(params5);
         
         
		
		
		btn_1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(privkey.length()<77){
					Toast.makeText(CheckPrivKeyActivity.this, getText(R.string.info_lessdigits), Toast.LENGTH_SHORT).show();
					privkey.requestFocus();
					return;
				}
				address.setText(hbsc.createAddressFrom77(privkey.getText().toString()));	

			}
		});
		
	}
	
	
}

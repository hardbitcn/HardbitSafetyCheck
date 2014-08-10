package com.hardbit.hbsc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.hardbit.hbsc.R;
import com.google.bitcoin.core.NetworkParameters;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.script.Script;
import com.hardbit.hbsc.CaptureActivity;
import com.hardbit.hbsc.MainActivity;
import com.hardbit.hbsc.Constants;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;


@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class MainActivity extends Activity{
	Button btn_checkQr = null;
	Button btn_intro = null;
	Button btn_checkPriv = null;
	Button btn_checkbackup = null;
	ImageView lalian = null;
	TextView title = null;
	float rate = 0.0f;	
	Intent intent;
	 Bundle bundle;
	 byte[] hash;
	 String qrcontent="";
	 HbscApplication hbsc;	 
	 String[] updateresult;
	 UpdateRequestOld updaterequest;
	 UpdateRequest updateRequest1;
	 byte[] qrdata; 
	 Transaction tx;
	 String coinType="";
	 String walletType="";
	 int walletLength=0;
	 byte[] scandatastream=new byte[0];
	 byte[] tempbytearray=new byte[0];
	 int currentpage=0;
	 int totalpage=0;
	 byte qrCommand=0x00;
	 final String path= Environment.getExternalStorageDirectory()+"/hbsc/cbd";
	 final String website="http://www.hardbit.cn/images/coindata/";
	 private ProgressDialog mSaveDialog = null;  
	byte[] fileContent=new byte[1];
	final int Download_OK = 0;
	protected static final int Download_fail = 1;
	int downloadQueue=-1;
	String[] coinTypes;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hbsc=(HbscApplication)this.getApplication();
		title = (TextView)this.findViewById(R.id.title);		
		btn_checkQr = (Button)this.findViewById(R.id.checkqr);
		btn_intro = (Button)this.findViewById(R.id.intro);
		btn_checkPriv = (Button)this.findViewById(R.id.checkpriv);
		btn_checkbackup = (Button)this.findViewById(R.id.checkbackup);		
		lalian = (ImageView)this.findViewById(R.id.lalian);
		
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
                 (int) (Constant.displayHeight * 0.12f + 0.5f));
         double jushang =  Constant.displayHeight * 0.015 * 2;
         double juzuo = Constant.displayWidth * 0.22 * 2;
         params0.setMargins((int)juzuo,  (int)jushang, 0, 0);
         title.setTextSize((int)(title.getTextSize()*rate));
         title.setLayoutParams(params0);
        
         LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.7f + 0.5f),
                 (int) (Constant.displayHeight * 0.12f + 0.5f));
         
         Log.i("aaa", "gao" +  Constant.displayHeight + "kuan" + Constant.displayWidth);
         double jushang2 =  Constant.displayHeight * 0.021 * 2;
         double juyou = Constant.displayWidth * 0.05 * 2;
         
         params1.setMargins(0,   (int)jushang2, (int)juyou, 0);
         btn_intro.setTextSize((int)(btn_intro.getTextSize()*rate));
         btn_intro.setLayoutParams(params1);
		
		 LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.7f + 0.5f),
                 (int) (Constant.displayHeight * 0.12f + 0.5f));
         params2.setMargins(0,2, (int)juyou, 0);
         btn_checkQr.setTextSize((int)(btn_checkQr.getTextSize()*rate));
         btn_checkQr.setLayoutParams(params2);
         LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.7f + 0.5f),
                 (int) (Constant.displayHeight * 0.12f + 0.5f));
         params3.setMargins(0, 2, (int)juyou, 0);
         btn_checkPriv.setTextSize((int)(btn_checkPriv.getTextSize()*rate));
         btn_checkPriv.setLayoutParams(params3);
         LinearLayout.LayoutParams params4 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.7f + 0.5f),
                 (int) (Constant.displayHeight * 0.12f + 0.5f));
         params4.setMargins(0, 2, (int)juyou, 0);
         btn_checkbackup.setTextSize((int)(btn_checkbackup.getTextSize()*rate));
         btn_checkbackup.setLayoutParams(params4);
        
         
         LinearLayout.LayoutParams params6 = new LinearLayout.LayoutParams(
                 (int) (Constant.displayWidth * 0.4f + 0.5f),
                 (int) (Constant.displayHeight * 0.4f + 0.5f));
         double jushang3 =  Constant.displayHeight * 0.090 * 2;
         double juzuo2 = Constant.displayWidth * 0.35 * 2;
        // Log.i("aaa", "juzuo2" + juzuo2);
         params6.setMargins((int)juzuo2, (int)jushang3, 0, 0);
         lalian.setLayoutParams(params6);
		
         btn_checkQr.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				currentpage=0;
				intent = new Intent(MainActivity.this,CaptureActivity.class);
				bundle = new Bundle();				 
				bundle.putInt("title", R.string.title_scanqr);				
				intent.putExtras(bundle);
				startActivityForResult(intent,1);			
			}
		});
         btn_checkPriv.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {	
				Intent intent = new Intent(MainActivity.this, CheckPrivKeyActivity.class);
				startActivity(intent);	
			}
		});
         btn_checkbackup.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDialog(0);
			}
		});
         btn_intro.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialogIntro();
			}
		});	
         checkCoinData();
	}
	public void  checkCoinData(){
		if (!hbsc.loadCoinData()){
			String fileName="coinlist.cbd";
			String url=website+fileName;
			Log.println(3,"hbsc","url:"+url);				
			 mSaveDialog = ProgressDialog.show(MainActivity.this, getString(R.string.title_downloading),  getString(R.string.text_downloadcoininfo), true);
			 
			downLoad(url, path, fileName);
		}
			
	}
	void downLoad(final String url, final String path, final String name)
	{
		final HttpDownloader hDownloader = new HttpDownloader();
		new Thread(){
			public void run() {
				int result = hDownloader.downFile(url, path, name);
				if(result == 0)
				{
					fileContent=hDownloader.fileContent;
					Log.println(3,"hbsc","downloaderOK:"+fileContent.toString());
					handler.sendEmptyMessage(Download_OK);
				}
				else
					handler.sendEmptyMessage(Download_fail);				
			};
		}.start();
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Download_OK:
				Log.println(3,"hbsc","downloadOK");
				continueDownload();
				break;
			case Download_fail:
				Log.println(3,"hbsc","downloadfail");
				downloadQueue=-1;
				 mSaveDialog.dismiss(); 
				Toast.makeText(MainActivity.this, R.string.badweb, Toast.LENGTH_SHORT).show();
				break;
				
			default:
				break;
			}
			
		};
	};
	void continueDownload(){
		Log.println(3,"alan","continuedownload:"+downloadQueue);
		if (downloadQueue==-1){//means downloaded coinlist
			if (processCoinList()){//process coin list first

				Log.println(3,"hbsc","coinlistdownloaded");
				continueDownload();
				return;
			}
			Toast.makeText(this, R.string.badweb, Toast.LENGTH_SHORT).show();
			return;
		}
		if (downloadQueue==0){//finished
			Log.println(3,"hbsc","downloadfinished");			
			downloadQueue=-1;
			 mSaveDialog.dismiss();			 
			 Toast.makeText(this,  getString(R.string.text_coininfoupdated), Toast.LENGTH_SHORT).show();
			 hbsc.updateCoinData();
			return;
		}
		Log.println(3,"hbsc","downloading:"+downloadQueue);		
		if (downloadQueue%2==0){
			String fileName=coinTypes[(int)downloadQueue/2-1]+".cbd";
			String url=website+fileName;
			downloadQueue--;
			downLoad(url, path, fileName);
			return;
		}else{
			String fileName=coinTypes[(int)Math.ceil((double)downloadQueue/2)-1]+".png";
			String url=website+fileName;
			downloadQueue--;
			downLoad(url, path, fileName);
			return;
		}
				
	}
	public boolean processCoinList(){		
		String httpResult="";
		try {
			httpResult = new String(fileContent,"UTF-8");
		} catch (UnsupportedEncodingException e1) {	
			Log.println(3,"hbsc","fileformaterror:"+e1);
		}

		Log.println(3,"hbsc","downloadcontent:"+httpResult);		
		try{
			JSONTokener jsonParser = new JSONTokener(httpResult);   		
			JSONObject person = (JSONObject) jsonParser.nextValue();
			String txs1 = person.getString("coinlist");	
			
			JSONArray arr = new JSONArray(txs1);
			downloadQueue=arr.length()*2;
			coinTypes=new String[arr.length()];
			for (int i=arr.length()-1;i>=0;i--){
				JSONObject temp = (JSONObject) arr.get(i);  
				coinTypes[i] = temp.getString("cointype");				
			}
		}
		catch (Exception e){
			Log.println(3, "hbsc","download fail"+e);
			return false;
		}
		return true;
	}
	@Override
	protected Dialog onCreateDialog(int id) {//choose backup file callback
		if(id==0){
			Map<String, Integer> images = new HashMap<String, Integer>();			
			images.put(OpenFileDialog.sRoot, R.drawable.filedialog_root);
			images.put(OpenFileDialog.sParent, R.drawable.filedialog_folder_up);
			images.put(OpenFileDialog.sFolder, R.drawable.filedialog_folder);
			images.put("bat", R.drawable.ic_launcher);
			images.put(OpenFileDialog.sEmpty, R.drawable.filedialog_root);
			Dialog dialog = OpenFileDialog.createDialog(id, this, getString(R.string.selectfile), new CallbackBundle() {
				
				@Override
				public void callback(Bundle bundle) {				
					String path=bundle.getString("path", "");					
					if (path.equals("")){
						Log.println(3,"alan","empty path");
						Toast.makeText(MainActivity.this, R.string.wrongfile, Toast.LENGTH_SHORT).show();
						return;
					}		
					byte[] walletfile;
					try {						
						walletfile=FileUtils.readSDFile(path);		
						
					} catch (IOException e) {
						Log.println(3,"alan","bad path:");						
						Toast.makeText(MainActivity.this, R.string.wrongfile, Toast.LENGTH_SHORT).show();
						return;
					}					
					if(!hbsc.verifywallet(walletfile)){
						Toast.makeText(MainActivity.this, R.string.badbackup, Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent = new Intent(MainActivity.this, InputPasswordActivity.class);					
					startActivity(intent);
				}				
			}, 
			".dat;.HB1;",
			images);
			return dialog;
		}		
		return null;
	}

	
	protected void dialogTrx(final String fromaddress,final String toaddress,final long amount) {
    	
    	AlertDialog.Builder builder = new Builder(MainActivity.this);
    	 
    	 builder.setTitle(this.getString(R.string.title_checkresult)); 
    	 String msg=new String();
    	 msg+=getString(R.string.text_trx)+"\n";
    	 msg+=getString(R.string.fromaddress)+"\n";
    	 msg+=fromaddress+"\n";
    	 msg+=getString(R.string.toaddress)+"\n";
    	 msg+=toaddress+"\n";
    	 msg+=getString(R.string.amount);
    	 msg+=btcToString(amount)+"BTC"+"\n";    	 
    	 builder.setMessage(msg);
    	 builder.setPositiveButton(this.getString(R.string.queding), new  OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//broadcast				
				
				dialog.dismiss();				
			}
		});
    	
    	builder.create().show();
    }
	protected void dialogUpdateRequest(UpdateRequest request,boolean totalupdate) {
    	
    	AlertDialog.Builder builder = new Builder(MainActivity.this);
    	 
    	 builder.setTitle(this.getString(R.string.title_checkresult)); 
    	 String msg=new String();
    	 msg+=getString(R.string.text_updaterequest)+"\n";
    	 msg+=getString(R.string.text_requesttime);
    	 msg+=timestamp2String(HbscApplication.byteArrayToInt(request.requestID,0))+"\n";
    	 for (int i=0; i<request.requestList.size();i++){
    		 msg+=getString(R.string.text_cointype);
    		 msg+=request.requestList.get(i).coinType+"\n";
    		 msg+=getString(R.string.text_address)+"\n";
    		 msg+=request.requestList.get(i).address+"\n"; 
    		 if (!totalupdate){
    			 msg+=getString(R.string.text_timestamp);  
    			 msg+=request.requestList.get(i).timestamp+"\n";    		 
    			 msg+=getString(R.string.text_txid)+"\n";
    			 msg+=request.requestList.get(i).transactionID+"\n";
    		 }
    	 }    	 
    	 builder.setMessage(msg);
    	 builder.setPositiveButton(this.getString(R.string.queding), new  OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();				
			}
		});    	
    	builder.create().show();
    }
protected void dialogAllCoinBalance(String address) {    	
    	AlertDialog.Builder builder = new Builder(MainActivity.this);
    	 
    	 builder.setTitle(this.getString(R.string.title_all_coin_balance_result)); 
    	 String msg=new String();
    	 msg+=getString(R.string.text_all_coin_balance_result)+"\n";
    	 msg+=address;    	     	 
    	 builder.setMessage(msg);
    	 builder.setPositiveButton(this.getString(R.string.queding), new  OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();				
			}
		});    	
    	builder.create().show();
    }
	protected void dialogUpdateRequestOld(UpdateRequestOld request) {    	
    	AlertDialog.Builder builder = new Builder(MainActivity.this);
    	 
    	 builder.setTitle(this.getString(R.string.title_checkresult)); 
    	 String msg=new String();
    	 msg+=getString(R.string.text_updaterequest)+"\n";	
    	 msg+=getString(R.string.text_address)+"\n";
    	 msg+=request.address+"\n";
    	 if(request.timestamp==8888){
    		 msg+=getString(R.string.text_totalupdate)+"\n";    			 
    	 }else{
    		 msg+=getString(R.string.text_timestamp)+"\n";  
    		 msg+=request.timestamp+"\n";
    	 }
    	 msg+=getString(R.string.text_txid)+"\n";
    	 msg+=request.transactionID+"\n";    	    	 
    	 builder.setMessage(msg);
    	 builder.setPositiveButton(this.getString(R.string.queding), new  OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();				
			}
		});    	
    	builder.create().show();
    }
	protected void dialogIntro() {
    	
    	AlertDialog.Builder builder = new Builder(MainActivity.this);
    	 
    	 builder.setTitle(this.getString(R.string.btn_intro));    	  	 
    	 builder.setMessage(this.getString(R.string.text_intro));
    	 builder.setPositiveButton(this.getString(R.string.queding), new  OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {			//broadcast				
				
				dialog.dismiss();				
			}
		});    	 
    	builder.create().show();
    }
	protected void dialogPaymentRequest(String scanString) {    	
    	AlertDialog.Builder builder = new Builder(MainActivity.this);
    	 
    	 builder.setTitle(this.getString(R.string.title_checkresult));    	  	 
    	 builder.setMessage(this.getString(R.string.text_paymentrequest)+"\n"+scanString);
    	 builder.setPositiveButton(this.getString(R.string.queding), new  OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {			
				
				dialog.dismiss();				
			}
		});    	 
    	builder.create().show();
    }
	protected void dialogLeak(String leaktext) {    	
    	AlertDialog.Builder builder = new Builder(MainActivity.this);    	 
    	 builder.setTitle(this.getString(R.string.title_leakalert));    	  	 
    	 builder.setMessage(this.getString(R.string.text_leakage)+leaktext);
    	 builder.setPositiveButton(this.getString(R.string.queding), new  OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {		
				
				dialog.dismiss();				
			}
		});    	 
    	builder.create().show();
    }
	public boolean receiveMultipage(byte[] fulldata){
		byte[] tempint={0,0,fulldata[3],fulldata[4]};		
		int pagelength=HbscApplication.byteArrayToInt(tempint,0)-4;//minus the hash 4 bytes
		if((pagelength>fulldata.length-9)&&(fulldata[0]==(byte)0xfa)){
			pagelength-=40;			
		}
				if(fulldata[1]==1){//only one page	
					scandatastream= new byte[pagelength];					
					System.arraycopy(fulldata,5,scandatastream,0, pagelength);		
					totalpage=0;
					currentpage=0;
					return true;
				}
				if(fulldata[2]==currentpage+1){//normal							
					if (totalpage==0){// first received page
						totalpage=fulldata[1];
						currentpage=1;
						qrCommand=fulldata[0];						
						scandatastream= new byte[pagelength];
						System.arraycopy(fulldata,5,scandatastream,0, pagelength);						
					}else{//normal
						currentpage++;				
					//array append						
						tempbytearray=new byte[scandatastream.length];
						System.arraycopy(scandatastream,0, tempbytearray,0,scandatastream.length);
						scandatastream= new byte[tempbytearray.length+pagelength];
						System.arraycopy(tempbytearray,0, scandatastream,0,tempbytearray.length);
						System.arraycopy(fulldata,5, scandatastream,tempbytearray.length,pagelength);
					}
					if (fulldata[2]==totalpage){// last page��finish
						totalpage=0;
						currentpage=0;
						return true;													
					}
				//normal, scan next page
					Toast.makeText(MainActivity.this, getString(R.string.continuescan), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MainActivity.this, CaptureActivity.class);						
					intent.putExtra("info", getString(R.string.info_scanpage)+(currentpage+1));	
					intent.putExtra("title", R.string.title_scanqr);	
					startActivityForResult(intent, 1);		
					return false;
				}
				else if(fulldata[2]<=currentpage) {// duplicate scan
					Toast.makeText(MainActivity.this, getString(R.string.duplicatescan), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MainActivity.this, CaptureActivity.class);					
					intent.putExtra("info", getString(R.string.info_scanpage)+(currentpage+1));
					intent.putExtra("title", R.string.title_scanqr);
					startActivityForResult(intent, 1);		
					return false;
				}
				else if(fulldata[2]>currentpage+1) {// jumped scan
					Toast.makeText(MainActivity.this, getString(R.string.jumpedscan), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MainActivity.this, CaptureActivity.class);					
					intent.putExtra("info", getString(R.string.info_scanpage)+(currentpage+1));
					intent.putExtra("title", R.string.title_scanqr);
					startActivityForResult(intent, 1);		
					return false;
				}	
				return false;
	}
	public boolean verifyQr(byte[]fulldata){
		byte[] tempint={0,0,fulldata[3],fulldata[4]};
		int pagelength=HbscApplication.byteArrayToInt(tempint,0);
		Log.println(3, "hbsc", "full data:"+Utils.bytesToHexString(fulldata));
		Log.println(3, "hbsc", "main.verifyqr:pagelength"+pagelength);
		if((pagelength>fulldata.length-5)&&(fulldata[0]==(byte)0xfa)){
			pagelength-=40;			
		}
		if ((pagelength>fulldata.length-5)){
			dialogLeak(getString(R.string.text_wrongpagelength));
			return false;
		}
		byte[] trimedbytes=new byte[pagelength+5-4];		
		System.arraycopy(fulldata,0,trimedbytes,0,trimedbytes.length);
		byte[] hash=Utils.sha256hash160(trimedbytes);
		byte[] shorthash1=new byte[4];
		byte[] shorthash2=new byte[4];
		System.arraycopy(hash,0,shorthash1,0,4);
		System.arraycopy(fulldata,pagelength+5-4,shorthash2,0,4);

		//Log.println(3, "hbsc", "calculated hash:"+Utils.bytesToHexString(shorthash1));
		//Log.println(3, "hbsc", "original hash:"+Utils.bytesToHexString(shorthash2));
		if (!Arrays.equals(shorthash2,shorthash1)){			
			dialogLeak(getString(R.string.text_wronghash));
			return false;
		}
		if (checkTail(fulldata,pagelength+5))
			return true;
		return false;
	}
	public boolean checkTail(byte[] fulldata,int offset){
		if (offset==fulldata.length) return true;
		String tail=Utils.bytesToHexString(fulldata).substring(offset*2,fulldata.length*2);
		Log.println(3,"alan","tail:"+tail);
		String taildefault="ec11";
		if (!tail.substring(0,1).equals("0")){
			dialogLeak(getString(R.string.text_wrongtail));
			return false;
		}
		int i=1;
		int j=0;
		while (i<tail.length()){
			if (!tail.substring(i,i+1).equals(taildefault.substring(j,j+1))){
				dialogLeak(getString(R.string.text_wrongtail));
				return false;
			}
			j=(j+1)%4;
			i++;
		}
		return true;
	}
	public byte[] trimQr(byte[] resultbytes){
		int trim=3;
		if (resultbytes.length>250) 
			trim=5;
		String hexbytes=Utils.bytesToHexString(resultbytes);
		return Utils.hexStringToBytes(hexbytes.substring(trim, hexbytes.length()));
	}
	public static String timestamp2String(long time) {
		  String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
		  if (time > 0l) {		   
		   String format = TIME_FORMAT;
		   SimpleDateFormat sf = new SimpleDateFormat(format,Locale.US);
		   Date date = new Date(time*1000);
		   return sf.format(date);
		  }
		  return "";
	}
	public String btcToString(long value){
		if (Math.abs(value)>100000){
			return String.valueOf((double)value/100000000);
		}
		 DecimalFormat df =  new DecimalFormat("0.00000000");
			String temp  = df.format((double)value/100000000); 
			return temp;
	}
	public void checkBackup(byte[] fulldata){
		if (!verifyQr(fulldata)) return;
		if(receiveMultipage(fulldata)){
			byte[] tempbyte=new byte[3];
			System.arraycopy(scandatastream,0,tempbyte,0,3);
			try {
				walletType=new String(tempbyte,"ISO-8859-1");
			} catch (UnsupportedEncodingException e) {}	
			//temporary solution of single coin version
			if (!walletType.equals("HB1")){
				dialogLeak(getString(R.string.text_wrongwallettype));
				return;
			}
			byte[]tempint1={0,0,scandatastream[3],scandatastream[4]};
			walletLength=Utils.byteArrayToInt(tempint1,0);	
			if (walletLength!=scandatastream.length-5){
				dialogLeak(getString(R.string.text_wrongwalletlength));
				return;
			}				
			byte[] walletfile=new byte[scandatastream.length-5];//minus the wallet type 3bytes, wallet length 2 bytes
			System.arraycopy(scandatastream,5,walletfile,0,walletfile.length);
			if (!hbsc.verifywallet(walletfile)){
				dialogLeak(getString(R.string.text_backupleakage)+getString(R.string.text_backupwrong));
				return;
			}
			else{
				Intent intent = new Intent(MainActivity.this, InputPasswordActivity.class);					
				startActivity(intent);	
				return;		
			}
		}
	}
	public void checkBackupOld(byte[] fulldata){
		if (fulldata[1]!=0){
			dialogLeak(getString(R.string.text_backupleakage)+1);
			return;
		}
		byte[] tempbyte={0,0,fulldata[2],fulldata[3]};
		int walletLength=Utils.byteArrayToInt(tempbyte,0);		
		walletLength-=4;
		Log.println(3,"alan","reportlength:"+walletLength);
		if (walletLength>fulldata.length-8){
			dialogLeak(getString(R.string.text_wrongpagelength));			
			return;
		}	
		byte[] walletfile=new byte[walletLength];
		System.arraycopy(fulldata,4,walletfile,0,walletLength);
		Log.println(3,"alan","wallet:"+Utils.bytesToHexString(walletfile));
		hash=Utils.sha256hash160(walletfile);
		byte[] shorthash1=new byte[4];
		byte[] shorthash2=new byte[4];
		System.arraycopy(hash,0,shorthash1,0,4);
		System.arraycopy(fulldata,walletLength+4,shorthash2,0,4);		
		if (!Arrays.equals(shorthash2,shorthash1)){
			dialogLeak(getString(R.string.text_wronghash));
			return;
		}		
		if (!checkTail(fulldata,walletLength+8)) return;
		if (!hbsc.verifywallet(walletfile)){
			dialogLeak(getString(R.string.text_backupleakage)+getString(R.string.text_backupwrong));
		}
		else{
			Intent intent = new Intent(MainActivity.this, InputPasswordActivity.class);					
			startActivity(intent);	
		}
	}
	public void checkUpdate(byte[] fulldata){
		if (!verifyQr(fulldata)) return;
		if(receiveMultipage(fulldata)){
			if(scandatastream.length<88-5){
				dialogLeak(getString(R.string.text_wrongpagelength));
				return;
			}			
			updateRequest1=new UpdateRequest(scandatastream,false);
			if (updateRequest1.cointypes==0){
				dialogLeak(getString(R.string.text_wrongupdate));			
				return;
			}
			dialogUpdateRequest(updateRequest1,false);
		}
	}
	public void checkAllCoinBalance(byte[] fulldata){
		String address="";
		if (!verifyQr(fulldata)) return;
		if(receiveMultipage(fulldata)){
			if(scandatastream.length<34){
				dialogLeak(getString(R.string.text_wrongpagelength));
				return;
			}
			try {
				address=new String(scandatastream,"ISO-8859-1");
			} catch (UnsupportedEncodingException e) {			}
			if(!hbsc.verifyTargetAddress("BTC", address)){
				dialogLeak(getString(R.string.text_wrong_address));
				return; 
			}
			dialogAllCoinBalance(address);
		}
	}
	public void checkTotalUpdate(byte[] fulldata){
		if (!verifyQr(fulldata)) return;
		if(receiveMultipage(fulldata)){
			
			if(scandatastream.length<4+3+1+35){
				dialogLeak(getString(R.string.text_wrongpagelength));
				return;
			}
			updateRequest1=new UpdateRequest(scandatastream,true );
			if (updateRequest1.cointypes==0){
				dialogLeak(getString(R.string.text_wrongupdate));
			return;
			}
			dialogUpdateRequest(updateRequest1,true);
		}
	}
	public void checkUpdateOld(byte[] fulldata){
		if(fulldata.length<77){
			dialogLeak(getString(R.string.text_wrongpagelength));
			return;
		}
		dialogUpdateRequestOld(new UpdateRequestOld(fulldata));	
	}
	public void checkTrxOld(byte[] fulldata){
		byte[] tempint={0,0,fulldata[4],fulldata[5]};
		byte[] header=new byte[24];
		int pagelength=Utils.byteArrayToInt(tempint,0);
		if(!checkTail(fulldata,pagelength+6)) return;
		if (fulldata[2]==currentpage){//right scan
			//add fulldata to qrdata
			if (currentpage==1){
				System.arraycopy(fulldata,6,header,0,24);
				qrdata=new byte[pagelength-24];//minus the length of header-- 24bytes
				System.arraycopy(fulldata,30,qrdata,0,qrdata.length);
			}else{
				byte[] qrdata1=qrdata;
				qrdata=new byte[qrdata1.length+pagelength];
				System.arraycopy(qrdata1,0,qrdata,0,qrdata1.length);
				System.arraycopy(fulldata,6,qrdata,qrdata1.length,pagelength);
			}
			if ((int)fulldata[1]>(int)fulldata[2]){	//more than 1 pages			
				currentpage++;
				intent = new Intent(MainActivity.this,CaptureActivity.class);
				bundle = new Bundle();				 
				bundle.putInt("title", R.string.saomiao);
				bundle.putString("info",getString(R.string.jixusaomiao)+(currentpage+1));
				intent.putExtras(bundle);
				startActivityForResult(intent,1);
			}else{//full transaction collected 				
				currentpage=1;	
				tx=new Transaction(Constants.NETWORK_PARAMETERS,qrdata);
				if(hbsc.verifyTrx(Constants.NETWORK_PARAMETERS,tx)&&hbsc.verifyTrxHeader(Constants.NETWORK_PARAMETERS,tx,header)){
					@SuppressWarnings("deprecation")
					String originaddress=tx.getInput(0).getScriptSig().getFromAddress(Constants.NETWORK_PARAMETERS).toString();
					String toaddress=(new Script(tx.getOutput(0).getScriptBytes())).getToAddress(Constants.NETWORK_PARAMETERS).toString();
					long payvalue=tx.getOutput(0).getValue().longValue();
					dialogTrx(originaddress,toaddress,payvalue);
					return;
				}
				dialogLeak(getString(R.string.text_wrongtrx));
			}
		}else if (fulldata[2]<currentpage){//please go next page
			intent = new Intent(MainActivity.this,CaptureActivity.class);
			 bundle = new Bundle();				 
				bundle.putInt("title", R.string.saomiao);
				bundle.putInt("info", R.string.jixusaomiao);
				intent.putExtras(bundle);
						startActivityForResult(intent,1);
		}else if (fulldata[2]>currentpage){//please go previous page
			intent = new Intent(MainActivity.this,CaptureActivity.class);
			 bundle = new Bundle();				 
				bundle.putInt("title", R.string.saomiao);
				bundle.putInt("info", R.string.scanprevious);
				intent.putExtras(bundle);
						startActivityForResult(intent,1);
		}
	}
	public void checkTrx(byte[] fulldata){
		if (!verifyQr(fulldata)) return;
		if(receiveMultipage(fulldata)){			
			if(scandatastream.length<24+148+34+10){
				dialogLeak(getString(R.string.text_wrongpagelength));
				return;
			}			
			byte[] tempbyte=new byte[3];
			System.arraycopy(scandatastream,0,tempbyte,0,3);
			try {
				coinType=new String(tempbyte,"ISO-8859-1");
			} catch (UnsupportedEncodingException e) {}	
					
			if (hbsc.cs.find(coinType)==null){
				Log.println(3, "hbsc", "wrong cointype:"+coinType);
				dialogLeak(getString(R.string.text_wrongcointype));
				return;
			}			
			NetworkParameters coinParams=hbsc.getCoinParams(coinType);
			byte[] header=new byte[24];
			System.arraycopy(scandatastream,3,header,0,24);
			qrdata=new byte[scandatastream.length-3-24];//minus the length of cointype 3bytes,header-- 24bytes
			System.arraycopy(scandatastream,3+24,qrdata,0,qrdata.length);			
			try{
				tx=new Transaction(coinParams,qrdata);
			}
			catch (Exception e){
				Log.println(3, "hbsc", "can't serialize tx:"+e.toString()+Utils.bytesToHexString(qrdata));
				dialogLeak(getString(R.string.text_wrongtrx));
				return;
			}
			if(hbsc.verifyTrx(coinParams,tx)&&hbsc.verifyTrxHeader(coinParams,tx,header)){
				@SuppressWarnings("deprecation")
				String originaddress=tx.getInput(0).getScriptSig().getFromAddress(Constants.NETWORK_PARAMETERS).toString();
				String toaddress=(new Script(tx.getOutput(0).getScriptBytes())).getToAddress(Constants.NETWORK_PARAMETERS).toString();
				long payvalue=tx.getOutput(0).getValue().longValue();
				dialogTrx(originaddress,toaddress,payvalue);
				return;
			}
			dialogLeak(getString(R.string.text_wrongtrx));
		}		
	}
	
	public boolean checkHash(byte[]fulldata,int pagelength){
		byte[] trimedbytes=new byte[pagelength+5-4];		
		System.arraycopy(fulldata,0,trimedbytes,0,trimedbytes.length);
		hash=Utils.sha256hash160(trimedbytes);
		byte[] shorthash1=new byte[4];
		byte[] shorthash2=new byte[4];
		System.arraycopy(hash,0,shorthash1,0,4);
		System.arraycopy(fulldata,pagelength+5-4,shorthash2,0,4);
		Log.println(3,"alan","incoming hash:"+Utils.bytesToHexString(shorthash2));
		Log.println(3,"alan","calculated hash:"+Utils.bytesToHexString(shorthash1));		
		Log.println(3,"alan"," hash string compare:"+Arrays.equals(shorthash2,shorthash1));
		if (!Arrays.equals(shorthash2,shorthash1)){			
			Log.println(3,"alan","hasherror:");
			return false;
		}
		return true;
	}
	public void checkPaymentRequest(String scanString){
		PaymentRequest request=new PaymentRequest(scanString,this);
		if(!request.isvalid)	{
			dialogLeak(getString(R.string.text_wrongpaymentrequest));
		return;	
		}
		dialogPaymentRequest(scanString);
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode==1)//scanqr
		{
			if (resultCode==RESULT_OK)
			{
				Bundle bundle = data.getExtras();
				byte[] resultbytes= bundle.getByteArray("resultbytes");
				if (resultbytes==null){
					Toast.makeText(MainActivity.this, R.string.badqr, Toast.LENGTH_SHORT).show();
					return;
				}								
				byte[] fulldata= trimQr(resultbytes);
				if (fulldata[0]==(byte)0xbc){//backup old
					checkBackupOld(fulldata);
					return;
				}
				if (fulldata[0]==(byte)0xba){//backup 
					checkBackup(fulldata);
					return;
				}
				if (fulldata[0]==(byte)0XFF){//update old
					checkUpdateOld(fulldata);
					return;
				}
				if (fulldata[0]==(byte)0XFE){//update
					checkUpdate(fulldata);
					return;
				}
				if (fulldata[0]==(byte)0XFA){//total update
					checkTotalUpdate(fulldata);
					return;
				}
				if (fulldata[0]==(byte)0X88){//transaction
					checkTrxOld(fulldata);
					return;	
				}
				if (fulldata[0]==(byte)0X98){//transaction
					checkTrx(fulldata);
					return;	
				}
				if (fulldata[0]==(byte)0Xcb){//transaction
					checkAllCoinBalance(fulldata);
					return;	
				}
				String scanString = bundle.getString("result");
				for(int i=0;i<hbsc.coinTypes.length;i++){
					if (scanString.indexOf(hbsc.cs.find(hbsc.coinTypes[i]).coinName[0].toLowerCase())>-1){
						checkPaymentRequest(scanString);					
						return;
					}	
				}
				dialogLeak(getString(R.string.text_unknownformat));				
			}		        			
		}	
	}
}

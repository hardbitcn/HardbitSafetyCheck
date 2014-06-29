package com.hardbit.hbsc;

import android.util.Log;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.Base58;

public class PaymentRequest {
	public String address="";
	public byte[] addressbytes=new byte[25];
	public long amount=0;
	public String amountString="";
	public String label="";
	public boolean isvalid=false;
	public PaymentRequest(String request){
		if( (request.indexOf("bitcoin:")!=-1)|(request.indexOf("Bitcoin:")!=-1)){
		request=request.substring(8,request.length());
		}		
		if (request.length()>35){
			if (request.indexOf(" ")>-1){
				address=request.substring(0, request.indexOf(" "));
			}else if (request.indexOf("?")>-1){
				address=request.substring(0, request.indexOf("?"));
				request=request.substring( request.indexOf("?")+1,request.length());
				if (request.indexOf("amount=")>-1){
					request=request.substring( request.indexOf("=")+1,request.length());
					String num="1234567890.";
					int i=0;
					for (i=0;i<request.length();i++){					
						if (num.indexOf(request.substring(i,i+1))==-1){
							label=request.substring(request.indexOf("="),request.length());
							break;	
						}
					}
					amountString=request.substring(0,i);
					Log.println(3, "alan","request amount:"+amountString);
					amount=HbscApplication.parseBTCAmount(amountString);

				}
			}
		}else{
			address=request;			
		}
		isvalid=HbscApplication.verifyTargetAddress(address);
		if(isvalid){
			try {
				addressbytes=Base58.decode(address);
			} catch (AddressFormatException e) {		}
		}		
	}

}

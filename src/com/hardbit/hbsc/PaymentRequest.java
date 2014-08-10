package com.hardbit.hbsc;

import android.content.Context;
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
	public String coinType="BTC";
	public PaymentRequest(String request,Context context){
		//find out cointype
		HbscApplication ini=(HbscApplication)context.getApplicationContext();
		if (request.indexOf(":")!=-1){
			boolean foundCoinType=false;
			for (int i=0;i<ini.coinTypes.length;i++){
				String coinName=ini.cs.find(ini.coinTypes[i]).coinName[0].toLowerCase();		
				if( (request.toLowerCase().indexOf(coinName+":")!=-1)){
					coinType=ini.coinTypes[i];
					request=request.substring(1+coinName.length(),request.length());
					foundCoinType=true;
					break;
				}
			}
			if (!foundCoinType)
			{
				Log.println(3, "alan","payment request:can't find cointype");
				isvalid=false;
				return;
			}
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
					amount=((HbscApplication)context.getApplicationContext()).parseCoinAmount(coinType,amountString);
				}
			}
		}
		else{
			address=request;	
		}
		isvalid=ini.verifyTargetAddress(coinType,address);
		if(isvalid){
			try {
				addressbytes=Base58.decode(address);
			} catch (AddressFormatException e) {		}
		}		
	}

}

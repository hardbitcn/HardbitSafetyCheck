package com.hardbit.hbsc;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import com.google.bitcoin.core.Utils;

public class UpdateRequest{
	public int cointypes=0;
	public byte[] requestID=new byte[4];	
	public List<CoinTypeRequest> requestList=new ArrayList<CoinTypeRequest>();
	private int position=0;
	CoinTypeRequest singleCoinRequest;
	public class CoinTypeRequest{
		public String coinType="";
		public String address="";
		public byte[] addressbytes=new byte[25];//for xor use
		public String transactionID="";
		public long timestamp=(long)0;
		public CoinTypeRequest(){}		
	}

	public UpdateRequest(byte[] data,boolean totalupdate){		
		System.arraycopy(data,0,requestID,0,4);
		cointypes=data[4];
		if (!totalupdate){
			if (data.length<cointypes*75){
				//wrong data				
				cointypes=0;
				return;
			}
			position=5;
			for (int i=0; i<cointypes;i++){
				if (data.length<position+75){
					//wrong data
					cointypes=0;
					return;
				}
				try {
					singleCoinRequest=new CoinTypeRequest();
					byte[] tempbyte=new byte[3];
					System.arraycopy(data,position,tempbyte,0,3);
					singleCoinRequest.coinType=new String(tempbyte,"ISO-8859-1");					
					position+=3;
					int addresslength=data[position];
					position++;					
					if (data.length<position+addresslength+40){
						//wrong data
						cointypes=0;
						return;
					}
					singleCoinRequest.addressbytes=new byte[addresslength];
					System.arraycopy(data, position,singleCoinRequest.addressbytes,0,addresslength);
					position+=addresslength;
					singleCoinRequest.address=new String(singleCoinRequest.addressbytes,"ISO-8859-1");
					byte[] b=new byte[8];
					System.arraycopy(data,position, b,0, 8);
					position+=8;
					singleCoinRequest.timestamp= Utils.ByteArrayToLong(b);
					byte[] id= new byte[32];
					System.arraycopy(data,position, id,0, 32);	
					position+=32;
					singleCoinRequest.transactionID=Utils.bytesToHexString(id);
					requestList.add(singleCoinRequest);
				} catch (UnsupportedEncodingException e) {		}			
			}
		}
		else{			
			if (data.length<cointypes*35){
				//wrong data				
				cointypes=0;
				return;
			}
			position=5;
			for (int i=0; i<cointypes;i++){
				if (data.length<position+35){					//wrong data					
					cointypes=0;
					return;
				}
				try {
					singleCoinRequest=new CoinTypeRequest();
					byte[] tempbyte=new byte[3];
					System.arraycopy(data,position,tempbyte,0,3);
					singleCoinRequest.coinType=new String(tempbyte,"ISO-8859-1");					
					position+=3;
					int addresslength=data[position];
					position++;					
					if (data.length<position+addresslength){	//wrong data
						cointypes=0;
						return;
					}
					singleCoinRequest.addressbytes=new byte[addresslength];
					System.arraycopy(data, position,singleCoinRequest.addressbytes,0,addresslength);
					position+=addresslength;
					singleCoinRequest.address=new String(singleCoinRequest.addressbytes,"ISO-8859-1");									
					requestList.add(singleCoinRequest);
				} catch (UnsupportedEncodingException e) {		}			
			}
		}
	}	
}
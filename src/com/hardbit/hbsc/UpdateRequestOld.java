package com.hardbit.hbsc;

import java.io.UnsupportedEncodingException;
import com.google.bitcoin.core.Utils;


public class UpdateRequestOld{
	public byte coinType;
	public String address="";
	public byte[] addressbytes=new byte[25];//for xor use
	public String transactionID="";
	public long timestamp=(long)0;
	public UpdateRequestOld(byte[] data){			
		try {
			coinType=data[1];
			int addresslength=data[2];			
			addressbytes=new byte[addresslength];
			System.arraycopy(data, 3,addressbytes,0,addresslength);
			address=new String(addressbytes,"ISO-8859-1");			
			byte[] id= new byte[32];
			System.arraycopy(data,11+addresslength, id,0, 32);	
			transactionID=Utils.bytesToHexString(id);;			
			byte[] b=new byte[8];
			System.arraycopy(data,3+addresslength, b,0, 8);
			timestamp= Utils.ByteArrayToLong(b);			
			} catch (UnsupportedEncodingException e) {				
			}	
	}	
}
package com.hardbit.hbsc;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;

import java.security.SecureRandom;
import java.util.Arrays;
import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.ECKey.ECDSASignature;
import com.google.bitcoin.core.AddressFormatException;
import com.google.bitcoin.core.Base58;
import com.google.bitcoin.core.BitcoinSerializer;
import com.google.bitcoin.core.Sha256Hash;
import com.google.bitcoin.core.Transaction;
import com.google.bitcoin.core.TransactionInput;
import com.google.bitcoin.core.Utils;
import com.google.bitcoin.core.Wallet;
import com.google.bitcoin.core.Transaction.SigHash;
import com.google.bitcoin.script.Script;
import com.google.bitcoin.script.ScriptBuilder;
import com.google.bitcoin.store.UnreadableWalletException;
import com.google.bitcoin.store.WalletProtobufSerializer;
import com.hardbit.hbsc.ManualCreateKey;

import android.app.Application;
import android.content.Context;
import android.util.Log;


public class HbscApplication extends Application{	
	private String address;
	private  File walletFile;
	private ECKey PvtKey;
	public Wallet wallet;
	public int pagelength;
	Context context=HbscApplication.this;
	public boolean webstatus=false;
	public int serverTimeout=3;
	public int serverRetry=0;
	public static final BigInteger HALF_CURVE_ORDER;
	private static final SecureRandom secureRandom;
	public static final ECDomainParameters CURVE;
	static {        
	        X9ECParameters params = SECNamedCurves.getByName("secp256k1");
	        CURVE = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
	        HALF_CURVE_ORDER = params.getN().shiftRight(1);
	        secureRandom = new SecureRandom();
	}
	public void onCreate()
	{
		super.onCreate();		 
		 wallet = new Wallet(Constants.NETWORK_PARAMETERS);	
	}
	public boolean verifywallet(byte[]walletfile){
		ByteArrayInputStream is = new ByteArrayInputStream(walletfile);
		try {	
			wallet=new Wallet(Constants.NETWORK_PARAMETERS);
			WalletProtobufSerializer wpf=new WalletProtobufSerializer();
			wallet = wpf.readWallet(is);
			if (wallet.getKeys().isEmpty())
			{
				Log.println(3, "hbsc","check backup error:no keys");
				return false;
			}
		} catch (UnreadableWalletException e1) {
			Log.println(3, "hbsc","check backup error:unreadable");
			return false;
		}		
		return true;
	}
	public String createAddressFrom77(String keyString){
		 ManualCreateKey keyGen=new ManualCreateKey();
		 ECKeyGenerationParameters keygenParams = new ECKeyGenerationParameters(CURVE, secureRandom);
	     keyGen.init(keygenParams);
		 PvtKey=new ECKey(keyGen.generatePriv(keyString));
		 Log.println(3, "hbsc", Utils.bytesToHexString(PvtKey.getPubKey()));
		 return PvtKey.toAddress(Constants.NETWORK_PARAMETERS).toString();
	}
	public static int byteArrayToInt(byte[] b, int offset) {
	       int value= 0;
	       for (int i = 0; i < 4; i++) {
	           int shift= (4 - 1 - i) * 8;
	           value +=(b[i + offset] & 0x000000FF) << shift;
	       }
	       return value;
	 }
	
	public String getaddress(Context context){		
		address="";
		try{
		 walletFile = context.getFileStreamPath(Constants.WALLET_FILENAME_PROTOBUF);		 
			if (checkaccount())			{
				address=PvtKey.toAddress(Constants.NETWORK_PARAMETERS).toString();
			}
		}catch (Exception e){
			Log.println(3, "hbsc","getaddress error:"+e);
		}
		return address;
	}	
	

	private Boolean checkaccount()
	{
		if (walletFile.exists())
		{

			FileInputStream walletStream = null;
			try
			{
				walletStream = new FileInputStream(walletFile);
				Wallet wallet = new WalletProtobufSerializer().readWallet(walletStream);
				Log.println(3, "hbsc","readwalletfileok");
				if (wallet.getKeys().isEmpty())
				{
					return false;
				}
				PvtKey=wallet.getKeys().get(0);
				Log.println(3, "hbsc","getkeysok");
				return true;
			}
			catch (final FileNotFoundException x)
			{
				return false;
			}
			catch (final UnreadableWalletException x)
			{
				return false;
			}
			finally
			{
				if (walletStream != null)
				{
					try
					{
						walletStream.close();
					}
					catch (final IOException x)					{					}
				}				
			}			
		}
		return false;			
	}	
	public boolean verifyTrx(Transaction trx){	
		for (int i=0;i<trx.getInputs().size();i++){
			TransactionInput input=trx.getInput(i);
			Script inputScript=new Script(input.getScriptBytes());
			byte[] pubkey= inputScript.getPubKey();
			ECKey key=new ECKey(null,pubkey);
			byte[] connectedPubKeyScript = ScriptBuilder.createOutputScript(key.toAddress(Constants.NETWORK_PARAMETERS)).getProgram();
			Sha256Hash hashForSig=trx.hashForSignature(i, connectedPubKeyScript, SigHash.ALL,false);		 
			ECDSASignature signature= ECDSASignature.decodeFromDER(inputScript.getChunks().get(0).data);
			if (!ECKey.verify(hashForSig.getBytes(),signature,pubkey)){
				Log.println(3, "hbsc","checktrx signature error");
				return false;
			}
		}
		return true;	
	}
	public boolean verifyTrxHeader(Transaction trx,byte[] header){
		ByteArrayOutputStream out = new ByteArrayOutputStream();       
	   	BitcoinSerializer serializer = new BitcoinSerializer(Constants.NETWORK_PARAMETERS);
	   	try {
			serializer.serialize(trx, out);
		} catch (IOException e1) {
			Log.println(3, "hbsc","toQrerror:"+e1);
			return false;
		}
	   	byte[] rebuiltheader=new byte[24];
	   	System.arraycopy(out.toByteArray(),0,rebuiltheader,0,24);
	   	if (Arrays.equals(header, rebuiltheader)) 	return true;
	   	Log.println(3, "hbsc","check trx header error");
	   	return false;
	}
	public static long parseBTCAmount(String stringvalue){
		if(stringvalue.length() < 1)//empty
			return 0;
		if(stringvalue.indexOf("0") == 0 && (!(stringvalue.indexOf(".") == 1)))//wrong 0 before dot
			return 0;	
		char[] charvalue=stringvalue.toCharArray();		
		String parsestring="1234567890.";
		for (int i=0;i<charvalue.length;i++)
		{
			if (parsestring.indexOf(charvalue[i])<0)// invalid letter				
				return 0;			
		}
		if (stringvalue.indexOf(".")!=stringvalue.lastIndexOf("."))//double dots			
			return 0;		
		int dotposition=stringvalue.indexOf(".");
		if (dotposition==-1){
			if (stringvalue.length()<5)//max 9999 coins per transaction
				stringvalue+="00000000";				
			else	return 0;
								
		}
		else if (dotposition==0)//first letter is dot
		{
			if (stringvalue.length()>8)//max 8 digits after dot
				stringvalue=stringvalue.substring(1,9);//trim to 8 digits
			else{//add to 8 digits
				String b=stringvalue.substring(1,stringvalue.length())+"00000000".substring(0, 8-(stringvalue.length()-1));
				stringvalue=b;	
			}		
		}
		else if (dotposition<5){//max 9999 coins per transaction
			if( dotposition==stringvalue.length()-1)//last letter is dot				
				stringvalue=stringvalue.substring(0, stringvalue.length()-1)+"00000000";
			else if(stringvalue.length()>(dotposition+8)){//max 8 digits after dot
				//get rid of dot and trim to 8 digits
				String b=stringvalue.substring(0,dotposition)+stringvalue.substring(dotposition+1,dotposition+9);
				stringvalue=b;
			}
			else{
				// 	remove dot and add to 8 digits after dot
				String b=stringvalue.substring(0,dotposition)+stringvalue.substring(dotposition+1,stringvalue.length())+"00000000".substring(0, 8-(stringvalue.length()-dotposition-1));
				stringvalue=b;				 
				}
		}else
			return 0;
		long amount= Long.parseLong(stringvalue);		
		return amount;		
	}

	public static boolean verifyTargetAddress(String address){
		byte tmp[];
		try {
			tmp = Base58.decode(address);
		} catch (AddressFormatException e) {
			Log.println(3, "hbsc","address format error");
			return false;
		}
		if (tmp.length ==25){
	    	byte[] bytes = Base58.copyOfRange(tmp, 0, tmp.length - 4);
	      	byte[] checksum =  Base58.copyOfRange(tmp, tmp.length - 4, tmp.length);		        
	       	tmp = Utils.doubleDigest(bytes);
	       	byte[] hash =  Base58.copyOfRange(tmp, 0, 4);
	       	if (Arrays.equals(checksum, hash)){		       		
	       		return true;
        	}
		}else{
	        	Log.println(3, "hbsc","address length error");
		}		
		return false;
	}
}

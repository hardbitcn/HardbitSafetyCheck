package com.hardbit.hbsc;


import java.math.BigInteger;
import java.security.SecureRandom;

import org.spongycastle.asn1.sec.SECNamedCurves;
import org.spongycastle.asn1.x9.X9ECParameters;
import org.spongycastle.crypto.AsymmetricCipherKeyPair;
import org.spongycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.spongycastle.crypto.KeyGenerationParameters;
import org.spongycastle.crypto.generators.ECKeyPairGenerator;
import org.spongycastle.crypto.params.ECDomainParameters;
import org.spongycastle.crypto.params.ECKeyGenerationParameters;
import org.spongycastle.crypto.params.ECPrivateKeyParameters;
import org.spongycastle.crypto.params.ECPublicKeyParameters;
import org.spongycastle.math.ec.ECConstants;
import org.spongycastle.math.ec.ECPoint;

import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.Base58;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.Utils;
import android.util.Log;

public class ManualCreateKey extends ECKeyPairGenerator
    
{
    ECDomainParameters  params;
    SecureRandom        random=new SecureRandom();

    public void init(
            KeyGenerationParameters param)
        {
            ECKeyGenerationParameters  ecP = (ECKeyGenerationParameters)param;

            this.random = ecP.getRandom();
            this.params = ecP.getDomainParameters();

            if (this.random == null)
            {
                this.random = new SecureRandom();
            }
        }

    /**
     * Given the domain parameters this routine generates an EC key
     * pair in accordance with X9.62 section 5.2.1 pages 26, 27.
     */
   
    public AsymmetricCipherKeyPair getKeyPair(BigInteger d)
    {
        BigInteger n = params.getN();
        int        nBitLength = n.bitLength();    

        ECPoint Q = params.getG().multiply(d);
        return new AsymmetricCipherKeyPair(
            new ECPublicKeyParameters(Q, params),
            new ECPrivateKeyParameters(d, params));
    }
    
   
    public BigInteger generatePriv(String keyString){    	
    	BigInteger d=new BigInteger(keyString);
    	ECPrivateKeyParameters privParams = (ECPrivateKeyParameters) getKeyPair(d).getPrivate();
    	BigInteger priv=privParams.getD();
    	return priv;
    }
   
   
}

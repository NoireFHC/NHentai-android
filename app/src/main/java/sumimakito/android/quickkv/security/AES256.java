/**      
 * QucikKV
 * Copyright (c) 2014-2015 Sumi Makito
 * Licensed under Apache License 2.0.
 * @author sumimakito<sumimakito@hotmail.com>
 * @version 0.8.2
 */
 
package sumimakito.android.quickkv.security;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import moe.feng.nhentai.BuildConfig;
import sumimakito.android.quickkv.QKVConfig;

public class AES256
{
	public static String encode(String ePasK, String eConT){
		if (ePasK.length() == 0 || ePasK == null) {
			return "";
		}

		if (eConT.length() == 0 || eConT == null) {
			return "";
		}

		try {
			SecretKeySpec skeySpec = getKey(ePasK);
			byte[] clearText = eConT.getBytes("UTF8");
			
			final byte[] iv = new byte[16];
			Arrays.fill(iv, (byte) 0x00);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivParameterSpec);

			String encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
			return encrypedValue;

		} catch (Exception e) {
			if (QKVConfig.DEBUG){
				e.printStackTrace();
			}
		}
		return "";
	}

	public static String decode(String ePasK, String eConT){
		if (ePasK.length() == 0 || ePasK == null) {
			return "";
		}
		if (eConT.length() == 0 || eConT == null) {
			return "";
		}
		try {
			SecretKey key = getKey(ePasK);
			final byte[] iv = new byte[16];
			Arrays.fill(iv, (byte) 0x00);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
			byte[] encrypedPwdBytes = Base64.decode(eConT, Base64.DEFAULT);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
			byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

			String decrypedValue = new String(decrypedValueBytes);
			return decrypedValue;

		} catch (Exception e) {
			if (BuildConfig.DEBUG){
				e.printStackTrace();
			}
		}
		return "";
	}
	private static SecretKeySpec getKey(String password) throws UnsupportedEncodingException {
		int keyLength = 256;
		byte[] keyBytes = new byte[keyLength / 8];
		Arrays.fill(keyBytes, (byte) 0x0);
		byte[] passwordBytes = password.getBytes("UTF-8");
		int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
		System.arraycopy(passwordBytes, 0, keyBytes, 0, length);
		SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
		return key;
	}
}

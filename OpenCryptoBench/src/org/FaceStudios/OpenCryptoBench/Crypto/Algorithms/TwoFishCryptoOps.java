package org.FaceStudios.OpenCryptoBench.Crypto.Algorithms;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.FaceStudios.OpenCryptoBench.OpenCryptoBench;
import org.FaceStudios.OpenCryptoBench.Crypto.CryptoObject;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.google.common.base.Stopwatch;

public class TwoFishCryptoOps {
	//This is the redirect implementation for TwoFish
	//This will allow data to be logged and processed
	//Logger Implementaion
	private static final BouncyCastleProvider PROVIDER = new BouncyCastleProvider();
	@SuppressWarnings("static-access")
	public static final Logger LOGGER =  OpenCryptoBench.GLOBALLOG.getLogger(TwoFishCryptoOps.class.getName());
	private static Stopwatch stopwatch;
	private static Stopwatch s2;
	private static long keygenTime;
	private static long encryptTime;
	private static long encryptAgTime;
	private static long decryptAgTime;
	private static long decryptTime;
	private static long cryptoTime;
	private static long totalTime;
	private static Cipher c;
	private static Cipher c1;
	private static SecretKey secret;
	protected static KeyGenerator gen;
	
	public static void performTwoFish(int bitlen,CryptoObject thing, int n){
		LOGGER.setUseParentHandlers(true);
		encryptTime = 0;
		encryptAgTime = 0;
		decryptTime = 0;
		decryptAgTime = 0;
		keygenTime = 0;
		cryptoTime=  0;
		totalTime=  0;
		/*try {
			LOGGER.addHandler(new FileHandler(file));
		} catch (SecurityException | IOException e2) {
			e2.printStackTrace();
		}*/
		LOGGER.info("##############################################################");
		LOGGER.info("BEGIN TwoFish PROCEDURE");
		LOGGER.info("##############################################################");
		LOGGER.info("Starting Stopwatch");
		stopwatch = Stopwatch.createStarted();
		LOGGER.info("Starting Encryption procedures for TwoFish");
		LOGGER.config("Creating a SecretKey Generator");
		try {
			gen = KeyGenerator.getInstance("TwoFish",PROVIDER);
		} catch (NoSuchAlgorithmException e1) {
			LOGGER.severe("ERROR: Could not find Algorithm TwoFish");
		}

		gen.init(bitlen);

		LOGGER.config("Generating Key");
		s2 = Stopwatch.createStarted();
		secret = gen.generateKey();
		s2.stop();
		keygenTime = s2.elapsed(TimeUnit.NANOSECONDS);
		cryptoTime = cryptoTime+keygenTime;
		LOGGER.config("Key Generation took "+keygenTime+" ns");
		s2.reset();
		LOGGER.config("CryptoObject's input string is "+thing.getInput());
		LOGGER.config("CryptoObject's SecretKey Object is "+Hex.encodeHexString(secret.getEncoded()));
		LOGGER.info("Initializing Cipher as TwoFish");
		try {
			c = Cipher.getInstance("TwoFish",PROVIDER);
			c.init(Cipher.ENCRYPT_MODE, secret);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			LOGGER.severe("ERROR: Cipher object could not initialize with given algorithm and parameter");

		}
		LOGGER.config("Success in initializing Cipher with given params");
		LOGGER.info("Creating an output String");
		byte[] outBytes = null;
		String out = "";
		LOGGER.config("Starting Encryption");
		s2.start();
		try {
			outBytes = c.doFinal(thing.getInput().getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.severe("ERROR: Cipher could not execute encryption");
			e.printStackTrace();
		}
		s2.stop();
		encryptTime = s2.elapsed(TimeUnit.NANOSECONDS);
		cryptoTime = cryptoTime+encryptTime;
		LOGGER.info("Success");
		LOGGER.info("Encryption Operation took "+encryptTime+" ns");
		s2.reset();
		LOGGER.info("Output string is " +Hex.encodeHexString(outBytes));
		LOGGER.info("Stopping Stopwatch for Encryption");
		stopwatch.stop();
		encryptAgTime = stopwatch.elapsed(TimeUnit.NANOSECONDS);
		totalTime = totalTime+encryptAgTime;
		LOGGER.info("Time Elapsed for Encryption is "+encryptAgTime+" ns" );
		LOGGER.info("Resetting Stopwatch");
		stopwatch.reset();
		LOGGER.config("Success in resetting stopwatch");
		LOGGER.info("Restarting Stopwatch");
		stopwatch.start();
		LOGGER.info("Using Output string "+out+" for decryption");
		LOGGER.info("Using SecretKey "+Hex.encodeHexString(secret.getEncoded())+" as SecretKey for decryption");
		LOGGER.info("Starting Decryption process for TwoFish");
		try {
			c1 = Cipher.getInstance("TwoFish",PROVIDER);
			c1.init(Cipher.DECRYPT_MODE, secret);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			LOGGER.severe("ERROR: Could not initialize the cipher object with given parameters");

		}
		LOGGER.config("Success in initializing a Cipher");
		LOGGER.config("Creating an output String");
		byte[] out1Bytes = null;
		LOGGER.info("Starting Decryption");
		s2.start();
		try {
			out1Bytes = c1.doFinal(outBytes);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.severe("ERROR: Could Not Decrypt Data");
			e.printStackTrace();
		}
		s2.stop();
		decryptTime = s2.elapsed(TimeUnit.NANOSECONDS);
		cryptoTime = cryptoTime+decryptTime;
		LOGGER.info("Success");
		LOGGER.info("Output string is"+out1Bytes.toString());
		LOGGER.info("Stopping Stopwatch");
		stopwatch.stop();
		decryptAgTime = stopwatch.elapsed(TimeUnit.NANOSECONDS);
		totalTime = totalTime+decryptAgTime;
		LOGGER.info("Time elapsed is "+ decryptAgTime+" ns");
		LOGGER.info("//////////////////////////////////////////");
		LOGGER.info("RESULTS");
		LOGGER.info("//////////////////////////////////////////");
		LOGGER.info("Key Generation Time: "+keygenTime+" ns");
		LOGGER.info("Encryption Time: "+encryptTime+" ns");
		LOGGER.info("Encryption Aggregate Time: "+encryptAgTime+" ns");
		LOGGER.info("Decryption Time: "+decryptTime+" ns");
		LOGGER.info("Decryption Aggregate Time: "+decryptAgTime+" ns");
		LOGGER.info("Cryptography Operation Time: "+cryptoTime+" ns");
		LOGGER.info("Total Operation Time: "+totalTime+" ns");
		LOGGER.info("Input String: "+thing.getInput());
		LOGGER.info("Key: "+Hex.encodeHexString(secret.getEncoded()));
		LOGGER.info("Encrypted Output: "+outBytes.toString());
		LOGGER.info("#################################################################");
		LOGGER.info("END TwoFish PROCEDURE");
		LOGGER.info("#################################################################");

		BufferedWriter print = null;
		try {
			print = new BufferedWriter(new FileWriter(new File("OpenCryptoBench.csv"),true));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			print.newLine();
			print.write(n+","+keygenTime+","+encryptTime+","+decryptTime+","+totalTime+","+bitlen+","+"TwoFish");
			print.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void performThreeFish(int bitlen,CryptoObject thing, int n){
		LOGGER.setUseParentHandlers(true);
		encryptTime = 0;
		encryptAgTime = 0;
		decryptTime = 0;
		decryptAgTime = 0;
		keygenTime = 0;
		cryptoTime=  0;
		totalTime=  0;
		/*try {
			LOGGER.addHandler(new FileHandler(file));
		} catch (SecurityException | IOException e2) {
			e2.printStackTrace();
		}*/
		LOGGER.info("##############################################################");
		LOGGER.info("BEGIN ThreeFish PROCEDURE");
		LOGGER.info("##############################################################");
		LOGGER.info("Starting Stopwatch");
		stopwatch = Stopwatch.createStarted();
		LOGGER.info("Starting Encryption procedures for ThreeFish");
		LOGGER.config("Creating a SecretKey Generator");
		try {
			gen = KeyGenerator.getInstance("ThreeFish",PROVIDER);
		} catch (NoSuchAlgorithmException e1) {
			LOGGER.severe("ERROR: Could not find Algorithm ThreeFish");
		}

		gen.init(bitlen);

		LOGGER.config("Generating Key");
		s2 = Stopwatch.createStarted();
		secret = gen.generateKey();
		s2.stop();
		keygenTime = s2.elapsed(TimeUnit.NANOSECONDS);
		cryptoTime = cryptoTime+keygenTime;
		LOGGER.config("Key Generation took "+keygenTime+" ns");
		s2.reset();
		LOGGER.config("CryptoObject's input string is "+thing.getInput());
		LOGGER.config("CryptoObject's SecretKey Object is "+Hex.encodeHexString(secret.getEncoded()));
		LOGGER.info("Initializing Cipher as ThreeFish");
		try {
			c = Cipher.getInstance("ThreeFish",PROVIDER);
			c.init(Cipher.ENCRYPT_MODE, secret);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			LOGGER.severe("ERROR: Cipher object could not initialize with given algorithm and parameter");

		}
		LOGGER.config("Success in initializing Cipher with given params");
		LOGGER.info("Creating an output String");
		byte[] outBytes = null;
		String out = "";
		LOGGER.config("Starting Encryption");
		s2.start();
		try {
			outBytes = c.doFinal(thing.getInput().getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.severe("ERROR: Cipher could not execute encryption");
			e.printStackTrace();
		}
		s2.stop();
		encryptTime = s2.elapsed(TimeUnit.NANOSECONDS);
		cryptoTime = cryptoTime+encryptTime;
		LOGGER.info("Success");
		LOGGER.info("Encryption Operation took "+encryptTime+" ns");
		s2.reset();
		LOGGER.info("Output string is " +Hex.encodeHexString(outBytes));
		LOGGER.info("Stopping Stopwatch for Encryption");
		stopwatch.stop();
		encryptAgTime = stopwatch.elapsed(TimeUnit.NANOSECONDS);
		totalTime = totalTime+encryptAgTime;
		LOGGER.info("Time Elapsed for Encryption is "+encryptAgTime+" ns" );
		LOGGER.info("Resetting Stopwatch");
		stopwatch.reset();
		LOGGER.config("Success in resetting stopwatch");
		LOGGER.info("Restarting Stopwatch");
		stopwatch.start();
		LOGGER.info("Using Output string "+out+" for decryption");
		LOGGER.info("Using SecretKey "+Hex.encodeHexString(secret.getEncoded())+" as SecretKey for decryption");
		LOGGER.info("Starting Decryption process for ThreeFish");
		try {
			c1 = Cipher.getInstance("ThreeFish",PROVIDER);
			c1.init(Cipher.DECRYPT_MODE, secret);

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			LOGGER.severe("ERROR: Could not initialize the cipher object with given parameters");

		}
		LOGGER.config("Success in initializing a Cipher");
		LOGGER.config("Creating an output String");
		byte[] out1Bytes = null;
		LOGGER.info("Starting Decryption");
		s2.start();
		try {
			out1Bytes = c1.doFinal(outBytes);
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			LOGGER.severe("ERROR: Could Not Decrypt Data");
			e.printStackTrace();
		}
		s2.stop();
		decryptTime = s2.elapsed(TimeUnit.NANOSECONDS);
		cryptoTime = cryptoTime+decryptTime;
		LOGGER.info("Success");
		LOGGER.info("Output string is"+out1Bytes.toString());
		LOGGER.info("Stopping Stopwatch");
		stopwatch.stop();
		decryptAgTime = stopwatch.elapsed(TimeUnit.NANOSECONDS);
		totalTime = totalTime+decryptAgTime;
		LOGGER.info("Time elapsed is "+ decryptAgTime+" ns");
		LOGGER.info("//////////////////////////////////////////");
		LOGGER.info("RESULTS");
		LOGGER.info("//////////////////////////////////////////");
		LOGGER.info("Key Generation Time: "+keygenTime+" ns");
		LOGGER.info("Encryption Time: "+encryptTime+" ns");
		LOGGER.info("Encryption Aggregate Time: "+encryptAgTime+" ns");
		LOGGER.info("Decryption Time: "+decryptTime+" ns");
		LOGGER.info("Decryption Aggregate Time: "+decryptAgTime+" ns");
		LOGGER.info("Cryptography Operation Time: "+cryptoTime+" ns");
		LOGGER.info("Total Operation Time: "+totalTime+" ns");
		LOGGER.info("Input String: "+thing.getInput());
		LOGGER.info("Key: "+Hex.encodeHexString(secret.getEncoded()));
		LOGGER.info("Encrypted Output: "+outBytes.toString());
		LOGGER.info("#################################################################");
		LOGGER.info("END ThreeFish PROCEDURE");
		LOGGER.info("#################################################################");

		BufferedWriter print = null;
		try {
			print = new BufferedWriter(new FileWriter(new File("OpenCryptoBench.csv"),true));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			print.newLine();
			print.write(n+","+keygenTime+","+encryptTime+","+decryptTime+","+totalTime+","+bitlen+","+"ThreeFish");
			print.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
	
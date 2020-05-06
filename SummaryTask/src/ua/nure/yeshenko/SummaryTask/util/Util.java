package ua.nure.yeshenko.SummaryTask.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
	public static String hash(String input, String algorithm){
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		digest.update(input.getBytes());
		byte[] hash = digest.digest();
		StringBuilder returned = new StringBuilder(10);
		for (byte i : hash) {
				returned.append(String.format("%02x", i));
		}
		return returned.toString();
	}
}

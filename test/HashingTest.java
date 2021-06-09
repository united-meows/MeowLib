package test;

import java.nio.charset.StandardCharsets;

import pisi.unitedmeows.meowlib.encryption.hashing.HashingHelper;
import pisi.unitedmeows.meowlib.encryption.hashing.MeowHash;

public class HashingTest {
	public static void main(final String args[]) {
		final byte[] data = "kedileradamdir".getBytes(StandardCharsets.UTF_8);
		System.out.println(HashingHelper.convertBytesToString(MeowHash.GET.getHash(data)));
	}
}

package crypto;

import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;
import static crypto.Helper.bytesToString;

public class checkxor {
	//---------------------------MAIN---------------------------
	public static void main(String args[]) {
		
		
		String inputMessage = Helper.readStringFromFile("text_one.txt");
		String key = "M";
		
		String messageClean = cleanString(inputMessage);
		
		
		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);
		
		
		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();
		
		System.out.println("------Xor------");
		testXor(messageBytes, keyBytes[0]);
		
		// TODO: TO BE COMPLETED
		
		
	}
	
	
	//Run the Encoding and Decoding using the caesar pattern 
	public static void testXor(byte[] string , byte key) {
		//Encoding
		byte[] result = Encrypt.xor(string, key, true);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);
		
		//Decoding with key
		String sD = bytesToString(Encrypt.xor(result, (byte) (key), true));
		System.out.println("Decoded knowing the key : " + sD);
		
		//Decoding without key
//		byte[][] bruteForceResult = Decrypt.xorBruteForce(result);
//		String sDA = Decrypt.arrayToString(bruteForceResult);
//		System.out.println(sDA);
		
		//Decoding wihtout key, ez version
		
		byte[] plainText = {105, 32, 119, 97, 110, 116};
		byte keyy = 50;
		byte[] cipherText = Encrypt.xor(plainText, keyy);
		System.out.println("Original input sanitized : " + bytesToString(plainText));
		System.out.println("Encoded : " + bytesToString(cipherText));
		byte[][] bruteForceResult = Decrypt.xorBruteForce(cipherText);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		System.out.println(sDA);
		
		
//		Helper.writeStringToFile(sDA, "bruteForceCaesar.txt");
//		
//		byte decodingKey = Decrypt.caesarWithFrequencies(result);
//		String sFD = bytesToString(Encrypt.caesar(result, decodingKey));
//		System.out.println("Decoded without knowing the key : " + sFD);
	}
}

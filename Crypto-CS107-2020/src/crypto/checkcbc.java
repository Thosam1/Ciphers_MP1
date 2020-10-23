package crypto;

import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;
import static crypto.Helper.bytesToString;

public class checkcbc {
	public static void main(String args[]) {
			
			
			String inputMessage = Helper.readStringFromFile("text_one.txt");
			String key = "onapasdevacances";
			
			String messageClean = cleanString(inputMessage);
			
			
			byte[] messageBytes = stringToBytes(messageClean);
			byte[] keyBytes = stringToBytes(key);
			
			
			System.out.println("Original input sanitized : " + messageClean);
			System.out.println();
			
			System.out.println("------CBC------");
			testCbc(messageBytes, keyBytes);
			
			// TODO: TO BE COMPLETED
			
			
		}
		
		
		//Run the Encoding and Decoding using the caesar pattern 
		public static void testCbc(byte[] string , byte[] key) {
			//Encoding
			byte[] result = Encrypt.cbc(string, key);
			String s = bytesToString(result);
			System.out.println("Encoded : " + s);
			
			//Decoding with key
			String sD = bytesToString(Decrypt.decryptCBC(result, (byte[]) (key)));
			System.out.println("Decoded knowing the key : " + sD);
			
			//Decoding without key
	//		byte[][] bruteForceResult = Decrypt.xorBruteForce(result);
	//		String sDA = Decrypt.arrayToString(bruteForceResult);
	//		System.out.println(sDA);
			
			//Decoding wihtout key, ez version
			
//			byte[] plainText = {105, 32, 119, 97, 110, 116};
//			byte keyy = 50;
//			byte[] cipherText = Encrypt.xor(plainText, keyy);
//			System.out.println("Original input sanitized : " + bytesToString(plainText));
//			System.out.println("Encoded : " + bytesToString(cipherText));
//			byte[][] bruteForceResult = Decrypt.xorBruteForce(cipherText);
//			String sDA = Decrypt.arrayToString(bruteForceResult);
//			System.out.println(sDA);
//	
	}
}

package crypto;

import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;
import static crypto.Helper.bytesToString;

import static crypto.Encrypt.generatePad;

public class checkotp {
	public static void main(String args[]) {
			
			
			String inputMessage = Helper.readStringFromFile("text_one.txt");
			
			String messageClean = cleanString(inputMessage);
			
			
			byte[] messageBytes = stringToBytes(messageClean);
			byte[] keyBytes = generatePad(messageBytes.length);
			
			
			System.out.println("Original input sanitized : " + messageClean);
			System.out.println();
			
			System.out.println("------OTP------");
			testOtp(messageBytes, keyBytes);
			
			// TODO: TO BE COMPLETED
			
			
		}
		
		
		//Run the Encoding and Decoding using the caesar pattern 
		public static void testOtp(byte[] string , byte[] key) {
			//Encoding
			byte[] result = Encrypt.oneTimePad(string, key);
			String s = bytesToString(result);
			System.out.println("Encoded : " + s);
			
			//Decoding with key
			String sD = bytesToString(Encrypt.oneTimePad(result, (byte[]) (key)));
			System.out.println("Decoded knowing the key : " + sD);
		}

}

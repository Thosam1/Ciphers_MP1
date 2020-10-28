package crypto;

import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;

import java.util.List;

import static crypto.Encrypt.generatePad;
import static crypto.Helper.bytesToString;

/*
 * Part 1: Encode (with note that one can reuse the functions to decode)
 * Part 2: bruteForceDecode (caesar, xor) and CBCDecode
 * Part 3: frequency analysis and key-length search
 * Bonus: CBC with encryption, shell
 */
public class Main {
	
	
	//---------------------------MAIN---------------------------
	public static void main(String args[]) {
		
	
		checkVigenere();
		// TODO: TO BE COMPLETED
		
		
	}
	
	
	public static void checkVigenere() {
		String inputMessage = Helper.readStringFromFile("text_two.txt");
		
		String messageClean = cleanString(inputMessage);
		
		
		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = generatePad(4);
		
		
		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();
		
		System.out.println("------Vigenere------");
		testVigenere(messageBytes, keyBytes);
	}
	
	public static void testVigenere(byte[] string , byte[] key) {
		//Encoding
		byte[] result = Encrypt.vigenere(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);
		
		System.out.println("Key used to encode : ");
		for(int z = 0; z < key.length; z++) { //printing key in bytes		//Debugging
			System.out.print(key[z]);
			System.out.println();
		}
		
		//Decoding with key
		byte[] inverseKey = new byte[key.length]; //inverse the key	
		for(int i = 0; i < key.length; i ++) {
			inverseKey[i] = (byte) -key[i];
		}
		
		System.out.println("Key used to decode : ");
		for(int z = 0; z < key.length; z++) {//printing inverse key			//Debugging
			System.out.print(inverseKey[z]);
			System.out.println();
		}
			
		String sD = bytesToString(Encrypt.vigenere(result, (byte[]) (inverseKey)));
		System.out.println("Decoded knowing the key : " + sD);
		
		//Decoding without the key
//		String BD = bytesToString(Decrypt.vigenereWithFrequencies(result));
//		System.out.println("Decoded without knowing the key : " + BD);
		
		//printing the key found in decrypt
		List<Byte> spaceRemoved = Decrypt.removeSpaces(string);				//Debugging
		int keyLength = Decrypt.vigenereFindKeyLength(spaceRemoved);
		byte[] keyDecrypt = Decrypt.vigenereFindKey(spaceRemoved, keyLength);
		System.out.println("Key length : " + keyLength);
		System.out.println("Key decrypted : ");
		for(int z = 0; z < keyLength; z++) { //printing key in bytes
			System.out.print(keyDecrypt[z]);
			System.out.println();
		}
	}
	
	//-----------------------------------------------------------------------------------------------
	
	public static void checkCaesar() {
		String inputMessage = Helper.readStringFromFile("text_one.txt");
		String key = "2cF%5";
		
		String messageClean = cleanString(inputMessage);
		
		
		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);
		
		
		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();
		
		System.out.println("------Caesar------");
		testCaesar(messageBytes, keyBytes[0]);
	}
	
	//Run the Encoding and Decoding using the caesar pattern 
	public static void testCaesar(byte[] string , byte key) {
		//Encoding
		byte[] result = Encrypt.caesar(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);
		
		//Decoding with key
		String sD = bytesToString(Encrypt.caesar(result, (byte) (-key)));
		System.out.println("Decoded knowing the key : " + sD);
		
		//Decoding without key
		byte[][] bruteForceResult = Decrypt.caesarBruteForce(result);
		String sDA = Decrypt.arrayToString(bruteForceResult);
		Helper.writeStringToFile(sDA, "bruteForceCaesar.txt");
		
		byte decodingKey = Decrypt.caesarWithFrequencies(result);
		String sFD = bytesToString(Encrypt.caesar(result, decodingKey));
		System.out.println("Decoded without knowing the key : " + sFD);
	}
	
	//----------------------------------------------------------------------------------------------------------
	
}

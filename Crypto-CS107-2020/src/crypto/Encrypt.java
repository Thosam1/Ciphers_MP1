package crypto;

import java.util.Random;
import static crypto.Helper.*;

public class Encrypt {
	
	public static final int CAESAR = 0;
	public static final int VIGENERE = 1;
	public static final int XOR = 2;
	public static final int ONETIME = 3;
	public static final int CBC = 4; 
	
	public static final byte SPACE = 32;
	
	final static Random rand = new Random();
	
	//-----------------------General-------------------------
	
	/**
	 * General method to encode a message using a key, you can choose the method you want to use to encode.
	 * @param message the message to encode already cleaned
	 * @param key the key used to encode
	 * @param type the method used to encode : 0 = Caesar, 1 = Vigenere, 2 = XOR, 3 = One time pad, 4 = CBC
	 * 
	 * @return an encoded String
	 * if the method is called with an unknown type of algorithm, it returns the original message
	 */
	public static String encrypt(String message, String key, int type) {
		byte[] plainText = stringToBytes(cleanString(message));
		byte[] cipherKey = stringToBytes(key);
		byte[] encodedArray;
		switch (type) {
		case 0:
			encodedArray = caesar(plainText,cipherKey[0]);
			break;
		case 1:
			encodedArray = vigenere(plainText, cipherKey);
			break;
		case 2:
			encodedArray = xor(plainText, cipherKey[0]);
			break;
		case 3:
			encodedArray = oneTimePad(plainText, cipherKey);
			break;
		case 4:
			encodedArray = cbc(plainText, cipherKey);
			break;
		default:
			encodedArray = stringToBytes(message);
			
		}
		
		return bytesToString(encodedArray);

	}
	
	
	//-----------------------Caesar-------------------------
	
	/**
	 * Method to encode a byte array message using a single character key
	 * the key is simply added to each byte of the original message
	 * @param plainText The byte array representing the string to encode
	 * @param key the byte corresponding to the char we use to shift
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array
	 */
	public static byte[] caesar(byte[] plainText, byte key, boolean spaceEncoding) {
		assert(plainText != null);
		// TODO: COMPLETE THIS METHOD
		byte[] newkey = new byte[] {key};
		return byteArrayPlusKeys(plainText, newkey, spaceEncoding);
		
	}
	
	public static byte[] byteArrayPlusKeys(byte[] plainText, byte[] keyArray, boolean spaceEncoding) {	
		byte[] encodedText = new byte[plainText.length];
		
		for (int i=0; i<plainText.length; i++) {
			if (plainText[i] == 32) {
				if(spaceEncoding){
					encodedText[i] = bytePlusKey(i, plainText, keyArray);
				}
				else {
					encodedText[i] = plainText[i];
				}
			}
			else {
				encodedText[i] = bytePlusKey(i, plainText, keyArray);
			}
		}
		
		return encodedText;
	}
	
	public static byte bytePlusKey(int i, byte[] plainText, byte[] keyArray) {
		int keyIndex = i%(keyArray.length);
		byte byteValue = plainText[i];
		
		int encryptedByteValue = byteValue + keyArray[keyIndex];
		if (encryptedByteValue < -128) {
			encryptedByteValue = 256 + encryptedByteValue;
		}
		else if (encryptedByteValue>127){
			encryptedByteValue = encryptedByteValue - 256;
		}
		return (byte) encryptedByteValue;
	}


	
	/**
	 * Method to encode a byte array message  using a single character key
	 * the key is simply added  to each byte of the original message
	 * spaces are not encoded
	 * @param plainText The byte array representing the string to encode
	 * @param key the byte corresponding to the char we use to shift
	 * @return an encoded byte array
	 */
	public static byte[] caesar(byte[] plainText, byte key) {
		// TODO: COMPLETE THIS METHOD
		return caesar(plainText, key, false);

	}
	
	//-----------------------XOR-------------------------
	
	/**
	 * Method to encode a byte array using a XOR with a single byte long key
	 * @param plaintext the byte array representing the string to encode
	 * @param key the byte we will use to XOR
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array
	 */
	public static byte[] xor(byte[] plainText, byte key, boolean spaceEncoding) {
		// TODO: COMPLETE THIS METHOD
		byte[] array1 = new byte[plainText.length]; // we initialize a byte array of same length as plaintext, we will return it at the end
		for(int i = 0; i < plainText.length; i++){
			if(spaceEncoding == true){ //spaces are encoded if true
				array1[i] = (byte) (plainText[i] ^ key);
			}else{
				if(plainText[i] != 32){ //spaces are not encoded if false
					array1[i] = (byte) (plainText[i] ^ key);
				}else{
					array1[i] = (byte) 32;
				}
			}			
		}
		
		return array1; // return the array of byte array1

	}
	/**
	 * Method to encode a byte array using a XOR with a single byte long key
	 * spaces are not encoded
	 * @param key the byte we will use to XOR
	 * @return an encoded byte array
	 */
	public static byte[] xor(byte[] plainText, byte key) {
		// TODO: COMPLETE THIS METHOD
		byte[] array1 = new byte[plainText.length]; // we initialize a byte array of same length as plaintext, we will return it at the end
		for(int i = 0; i < plainText.length; i++){
						
			if(plainText[i] != 32){
				array1[i] = (byte) (plainText[i] ^ key);
			}else{
				array1[i] = (byte) 32;
			}			
		}
		return array1; // TODO: to be modified

	}
	//-----------------------Vigenere-------------------------
	
	/**
	 * Method to encode a byte array using a byte array keyword
	 * The keyword is repeated along the message to encode
	 * The bytes of the keyword are added to those of the message to encode
	 * @param plainText the byte array representing the message to encode
	 * @param keyword the byte array representing the key used to perform the shift
	 * @param spaceEncoding if false, then spaces are not encoded
	 * @return an encoded byte array 
	 */
	public static byte[] vigenere(byte[] plainText, byte[] keyword, boolean spaceEncoding) {
		// TODO: COMPLETE THIS METHOD		

		assert(plainText != null);
		return byteArrayPlusKeys(plainText, keyword, spaceEncoding);

	}
	
	/**
	 * Method to encode a byte array using a byte array keyword
	 * The keyword is repeated along the message to encode
	 * spaces are not encoded
	 * The bytes of the keyword are added to those of the message to encode
	 * @param plainText the byte array representing the message to encode
	 * @param keyword the byte array representing the key used to perform the shift
	 * @return an encoded byte array 
	 */
	public static byte[] vigenere(byte[] plainText, byte[] keyword) {
		// TODO: COMPLETE THIS METHOD
		assert(plainText != null);
		return byteArrayPlusKeys(plainText, keyword, false);
	}
	
	
	
	//-----------------------One Time Pad-------------------------
	
	/**
	 * Method to encode a byte array using a one time pad of the same length.
	 *  The method  XOR them together.
	 * @param plainText the byte array representing the string to encode
	 * @param pad the one time pad
	 * @return an encoded byte array
	 */
	public static byte[] oneTimePad(byte[] plainText, byte[] pad) {
		// TODO: COMPLETE THIS METHOD
		assert plainText.length <= pad.length : "Pad length is too short";
		byte[] array1 = new byte[plainText.length]; // we initialize a byte array of same length as plaintext, we will return it at the end
		for(int i = 0; i < plainText.length; i++){
			array1[i] = (byte) (plainText[i] ^ pad[i]);	
		}
		return array1; // TODO: to be modified
	}
	
	
	
	
	//-----------------------Basic CBC-------------------------
	
	/**
	 * Method applying a basic chain block counter of XOR without encryption method. Encodes spaces.
	 * @param plainText the byte array representing the string to encode
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return an encoded byte array
	 */
	public static byte[] cbc(byte[] plainText, byte[] iv) {
		// TODO: COMPLETE THIS METHOD
		byte[] encrypted = chooseCBC(plainText, iv, false);
		return encrypted;

	}
	public static byte[] chooseCBC(byte[] plainText, byte[] iv, boolean decrypt) {
		
		byte[] array1 = new byte[plainText.length]; // we initialize a byte array of same length as plaintext, we will return it at the end
		int max = Math.floorDiv(plainText.length, iv.length); // we  get the number of iteration of "Otp" needed
		int mod = plainText.length % iv.length; // important to know when to stop in the final loop
		byte[] sub = new byte[iv.length]; // it will be easier to store the data in that array for each iteration, we could delete it but i made it so it is easier to understand the code
		
		if(max == 0){ //only iterate until remainder // This is ontTimePad (length of pad greater than length of text)
			array1 = oneTimePad(plainText, iv);
			return array1;
		}else { // length text is greater or equal to pad - so it will be one iteration at least
			
	
				for(int i = 0; i < sub.length; i++){ // this is to copy values into sub[i], in C you would do it like that, so just in case to be sure it works
					sub[i] = iv[i];
				}
						
				for(int i = 0; i < max; i++){ // macro loop to iterate a smaller loop, max time a smaller loop
	
					for(int j = 0; j < sub.length; j++){ // handle all the iterations
							
						if(decrypt == false) { //to encrypt
							array1[i*sub.length + j] = (byte) (plainText[i*sub.length + j] ^ sub[j]);
							sub[j] = array1[i*sub.length + j];
						}else { //to decrypt
							array1[i*sub.length + j] = (byte) (plainText[i*sub.length + j] ^ sub[j]);
							sub[j] = plainText[i*sub.length + j];
						}					
							
					}			
	
				}
	
				if(mod != 0){ // i don't know if that if statement is rlly necessary
					for(int k = 0; k < mod; k ++){
						array1[max*sub.length + k] = (byte) (plainText[max*sub.length + k] ^ sub[k]); //handle the remainder 
					}
				}
				
							
				return array1; // TODO: to be modified
		}
	}
		
		
	
	
	
	/**
	 * Generate a random pad/IV of bytes to be used for encoding
	 * @param size the size of the pad
	 * @return random bytes in an array
	 */
	public static byte[] generatePad(int size) {
		// TODO: COMPLETE THIS METHOD
		byte[] pad_array = new byte[size];
		for(int i = 0; i < size; i++){
			pad_array[i] = (byte) rand.nextInt(256);
		}
		return pad_array; // TODO: to be modified


	}
	
	
	
}

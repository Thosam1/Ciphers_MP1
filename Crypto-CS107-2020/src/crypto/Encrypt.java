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
	
	final static Random rand = new Random(2l);
	
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
		byte[] plainText = stringToBytes(message);
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
		 * @param plainText The byte array of the string to encrypt
		 * @param key the byte corresponding to the char we use to shift
		 * @param spaceEncoding Boolean variable that determines if spaces are encrypted
		   (if false, they are not encrypted)
		 * @return an encoded byte array
		 */
		public static byte[] caesar(byte[] plainText, byte key, boolean spaceEncoding) {
			assert(plainText != null); //error if plainText is null

			byte[] newkey = new byte[] {key}; //creating a byte array with @param key as unique entry
			return byteArrayPlusKeys(plainText, newkey, spaceEncoding); //byteArrayPlusKeys adds the value of the key to every byte in -plainText-
		}
/**
		 * Method to encode a byte array message using a single character key
		 * the key is simply added to each byte of the original message
		 * @param plainText The byte array of the string to encrypt
		 * @param key the byte corresponding to the char we use to shift
		 * default is that spaces are not encrypted
		 * @return an encoded byte array
		 */
		public static byte[] caesar(byte[] plainText, byte key) {
			assert(plainText != null); //error if plainText is null

			byte[] newkey = new byte[] {key}; //creating a byte array with @param key as unique entry
			return byteArrayPlusKeys(plainText, newkey, false); //byteArrayPlusKeys adds the value of the key to every byte in -plainText-
		}
	//=========================================================================================================

	/**Method that encrypts a byte input with a byte key and returns the encrypted message as a byte array
	 * @param plainText The byte array of the string to encrypt
	 * @param keyArray The byte array that is looped to encrypt each value(spaces may or may not be encrypted)
	   in plainText
	 * @param spaceEncoding Boolean variable which determines if spaces are encrypted or not
	*/
		public static byte[] byteArrayPlusKeys(byte[] plainText, byte[] keyArray, boolean spaceEncoding) {
			byte[] encodedText = new byte[plainText.length];

			int encryptedCharacters =0; // This integer is very important for vigenere because it decides which index of the key array is used
			for (int i=0; i<plainText.length; i++) {
				if (plainText[i] == 32) { //The byte value 32 corresponds to spaces

					if(spaceEncoding){ //If true, spaces are encrypted
						encodedText[i] = bytePlusKey(i, encryptedCharacters, plainText, keyArray);
						encryptedCharacters +=1; //allows the method -bytePlusKey- to encrypt the next character in -plainText- with the next value in -KeyArray-
					}
					else {
						encodedText[i] = plainText[i]; //encrypted value set to 32 (stays a space because spaceEncoding is false)
					}
				}

				else { //if the byte is not equal to 32
					encodedText[i] = bytePlusKey(i, encryptedCharacters, plainText, keyArray);
					encryptedCharacters +=1;
				}
			}

			return encodedText;
		}


	//=========================================================================================================
	/**
	 * This method takes 4 parameters
	 * @param i The integer representing the index of the value in @param plainText that is being encrypted
	 * @param encryptedCharacters The integer representing the number of characters that have been encrypted
	 * @param plainText The byte array representing the string to encode
	 * @param keyArray The byte array that is used as the key for Caesar and Vigenere
	*/
		public static byte bytePlusKey(int i, int encryptedCharacters, byte[] plainText, byte[] keyArray) {
			int keyIndex = encryptedCharacters%(keyArray.length); 
			//for Caesar keyIndex is always equal to 0
			//for Vigenere it cycles between 0 and the length of the keyArray
			byte byteValue = plainText[i]; //byte value in -plainText- that is being encrypted

			int encryptedByteValue = byteValue + keyArray[keyIndex];

			return (byte) encryptedByteValue;
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
		byte[] array1 = new byte[plainText.length]; // we initialize a byte array of same length as plaintext, we will return it at the end
		for(int i = 0; i < plainText.length; i++){
						
			if(plainText[i] != 32){
				array1[i] = (byte) (plainText[i] ^ key);
			}else{
				array1[i] = (byte) 32;
			}			
		}
		return array1; 

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
		assert(plainText != null);
		return byteArrayPlusKeys(plainText, keyword, spaceEncoding); 
		//byteArrayPlusKeys repeats -keyword- along -plainText- 
		//values in keyword are added to to those in -plaintext-

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
		assert(plainText != null);
		return byteArrayPlusKeys(plainText, keyword, false);//does not encrypt spaces
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
		
		assert plainText.length <= pad.length : "Pad length is too short";
		byte[] array1 = new byte[plainText.length]; // we initialize a byte array of same length as plaintext, we will return it at the end
		for(int i = 0; i < plainText.length; i++){
			array1[i] = (byte) (plainText[i] ^ pad[i]);	
		}
		return array1; 
	}
	
	
	
	
	//-----------------------Basic CBC-------------------------
	
	/**
	 * Method applying a basic chain block counter of XOR without encryption method. Encodes spaces.
	 * @param plainText the byte array representing the string to encode
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return an encoded byte array
	 */
	public static byte[] cbc(byte[] plainText, byte[] iv) {
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
					} //same code for encoding and decoding
				}
				
							
				return array1;
		}
	}
	
	public static byte[] encryptAdvancedCBC(byte[] plainText, byte[] iv, byte xor) {
		
		byte[] encrypted = chooseAdvancedCBC(plainText, iv, xor, false);
		return encrypted;

	}

	
	public static byte[] chooseAdvancedCBC(byte[] plainText, byte[] iv, byte xor, boolean decrypt) { //cbc with an additional xor between each line
		
		byte[] array1 = new byte[plainText.length]; // we initialize a byte array of same length as plaintext, we will return it at the end
		int max = Math.floorDiv(plainText.length, iv.length); // we  get the number of iteration of "Otp" needed
		int mod = plainText.length % iv.length; // important to know when to stop in the final loop
		byte[] sub = new byte[iv.length]; // it will be easier to store the data in that array for each iteration, we could delete it but i made it so it is easier to understand the code
		
		if(max == 0){ //only iterate until remainder // This is ontTimePad (length of pad greater than length of text)
			array1 = oneTimePad(plainText, iv);
			for(int i = 0; i < array1.length; i ++) {
				array1[i] = (byte) (array1[i] ^ xor);
			}			
			return array1;
		}else { // length text is greater or equal to pad - so it will be one iteration at least
			
	
				for(int i = 0; i < sub.length; i++){ // this is to copy values into sub[i], in C you would do it like that, so just in case to be sure it works
					sub[i] = iv[i];
				}
						
				for(int i = 0; i < max; i++){ // macro loop to iterate a smaller loop, max time a smaller loop
	
					for(int j = 0; j < sub.length; j++){ // handle all the iterations
							
						if(decrypt == false) { //to encrypt
							array1[i*sub.length + j] = (byte) (plainText[i*sub.length + j] ^ sub[j]);
							array1[i*sub.length + j] = (byte) (array1[i*sub.length + j] ^ xor); //we do xorAfter
							sub[j] = array1[i*sub.length + j];
						}else { //to decrypt
							array1[i*sub.length + j] = (byte) (plainText[i*sub.length + j] ^ xor); //we do xorBefore
							array1[i*sub.length + j] = (byte) (array1[i*sub.length + j] ^ sub[j]);
							sub[j] = plainText[i*sub.length + j];
						}					
							
					}			
	
				}
	
				if(mod != 0){ // i don't know if that if statement is rlly necessary
					if(decrypt == false) {
						for(int k = 0; k < mod; k ++){
							array1[max*sub.length + k] = (byte) (plainText[max*sub.length + k] ^ sub[k]); //handle the remainder 
							array1[max*sub.length + k] = (byte) (array1[max*sub.length + k] ^ xor);
						}
					}else {
						for(int k = 0; k < mod; k ++){
							array1[max*sub.length + k] = (byte) (plainText[max*sub.length + k] ^ xor);
							array1[max*sub.length + k] = (byte) (array1[max*sub.length + k] ^ sub[k]); //handle the remainder 
						}
					}
					
				}
				
							
				return array1;
		}
	}
		
		
	
	
	
	/**
	 * Generate a random pad/IV of bytes to be used for encoding
	 * @param size the size of the pad
	 * @return random bytes in an array
	 */
	public static byte[] generatePad(int size) {
	
		byte[] pad_array = new byte[size];
		for(int i = 0; i < size; i++){
			pad_array[i] = (byte) rand.nextInt(256);
		}
		return pad_array; 


	}
	
	
	
}

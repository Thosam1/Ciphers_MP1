package crypto;

import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static crypto.Encrypt.generatePad;
import static crypto.Helper.bytesToString;

/*
 * Part 1: Encode (with note that one can reuse the functions to decode)
 * Part 2: bruteForceDecode (caesar, xor) and CBCDecode
 * Part 3: frequency analysis and key-length search
 * Bonus: CBC with encryption, shell
 */
public class Main {
	public static String encryptInterpreteurDeCommande(String message, String type) {
		byte[] plainText = stringToBytes(cleanString(message));
		byte[] encodedArray;
		Scanner scanner = new Scanner(System.in);

		if (type.equals("0")) {
			System.out.println("To use Caesar Cipher I need you to give me a key (any character that you would like) which will be stored as a byte. If you don't know what I refer to when talking about keys and characters, please refer to the 'Help'. Remember, it is very important that you remember your key and keep it secret! Without this key it is very difficult to decrypt your message.");
			String caesarCipherKey = scanner.next();
			byte[] caesarCipherKeyByteArray = stringToBytes(caesarCipherKey);
			encodedArray = Encrypt.caesar(plainText, caesarCipherKeyByteArray[0]);
		}
		else if(type.equals("1")) {
			System.out.println("To use Vigenere Cipher I need you to give me a key which will be stored as an array of bytes. (An Array is also known as a List) Your key can be anything from a word to a random sequence of characters. If you don't know what I refer to when talking about keys and characters, please refer to the 'Help'. Remember, it is very important that you remember your key and that you keep it secret!");
			String vigenereCipherKeyString = scanner.next();
			encodedArray = Encrypt.vigenere(plainText, stringToBytes(vigenereCipherKeyString));
		}
		else if(type.equals("2")) {
			System.out.println("To use XOR Cipher I need you to give me a key (any character that you would like) which will be stored as a byte. If you don't know what I refer to when talking about keys and characters, please refer to the 'Help'. Remember, it is very important that you remember this key and keep it secret! Without this key it is very difficult to decrypt your message.");
			byte xorCipherKey = scanner.nextByte();
			encodedArray = Encrypt.xor(plainText, xorCipherKey);
		}
		else if(type.equals("3")) {
			System.out.println("To use OneTime I need you to give me a key which will be stored as an array of bytes. (An Array is also known as a List) Your key can be anything from a word to a random sequence of characters. If you don't know what I refer to when talking about keys and characters, please refer to the 'Help'. Remember, it is very important that you remember your key and that you keep it secret!");
			String oneTimeCipherString = scanner.next();
			encodedArray = Encrypt.oneTimePad(plainText, stringToBytes(oneTimeCipherString));
		}
		else if(type.equals("4")) {
			System.out.println("To use CBC I need you to give me a key which will be stored as an array of bytes. (An Array is also known as a List) Your key can be anything from a word to a random sequence of characters. If you don't know what I refer to when talking about keys and characters, please refer to the 'Help'. Remember, it is very important that you remember your key and that you keep it secret!");
			String cbcCipherString = scanner.next();
			encodedArray = Encrypt.cbc(plainText, stringToBytes(cbcCipherString));
		}
		else {
			encodedArray = stringToBytes(message);
		}

	return bytesToString(encodedArray);
	}


	//============================================================================================
	public static void breakCipher(String cipher, int type) {
		byte[] cipherByteArray = stringToBytes(cleanString(cipher));
		byte[][] decodedCipherArray;
		Scanner scanner = new Scanner(System.in);
		if (type==0) {
			System.out.println("When it comes to the decryption of Caesar Cipher, there are two different techniques that I can use: Brute Force and Frequencies. If you want to try out the second method, which requires no effort from your part, type '1'. However, if you are willing to look through 256 possible decryptions to find the valid one, then type '0'. Brute Force takes more time; however, it works 100% of the time.");
			if (scanner.nextInt()==0) {
				decodedCipherArray = Decrypt.caesarBruteForce(cipherByteArray);
				System.out.println("To find the decrypted text, look for the lines with letters and words that make sense. Here are the 256 possibilites:");
				System.out.println(Decrypt.arrayToString(decodedCipherArray));
			}
			else {
				byte caesarKey = Decrypt.caesarWithFrequencies(cipherByteArray);
				System.out.println("And the encoded text was: " + Encrypt.caesar(stringToBytes(cipher), caesarKey));
			}
		}

		else if (type==1){
			System.out.println("When it comes to the decryption of Vigenere, I use the frequencies of letters in the English Alphabet to give you the answer that is most likely to be the right decryption.");
			byte[] vigenereKey = Decrypt.vigenereWithFrequencies(cipherByteArray);

		}
		else if (type==2) {
			System.out.println("When it comes to the decryption of XOR there is one method: Brute Force. It consists in printing the 256 possibilities of encryption. Then, it will be up to you to find the true meaning of your encrypted message.");
			decodedCipherArray = Decrypt.xorBruteForce(cipherByteArray);
			System.out.println("All you have to do is to look for the line that has letters and that makes sense. Here are the 256 possibilities:");
			System.out.println(Decrypt.arrayToString(decodedCipherArray));
		}
		else if (type==3) {
			System.out.println("When it comes to the decryption of CBC, I can only perform it if you have the pad used to encode the message. Please input it now.");
			String padString = scanner.next();
			Decrypt.decryptCBC(cipherByteArray, stringToBytes(padString));
		}
		else {
			System.out.println("You didn't enter a valid value. But don't worry because you will get another chance.");}


	}
	public static boolean endOfShell() {
		Scanner scanner = new Scanner(System.in);
		String yesOrNo ="";
		while(!((yesOrNo.equals("Yes"))||(yesOrNo.equals("No")))) {
			System.out.println("Would you like to stop the program? 'Yes'or 'No'");
			yesOrNo = scanner.next();
		}
		if (yesOrNo.equals("Yes")){
			return true;
		}
		else {
			return false;
		}
	}
	public static void interpreteurDeCommande(){
		boolean isFinished = false;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Hello! I am a program that has many capabilities when it comes to encryption and decryption.");
		while (!(isFinished)) {
			System.out.println("If you would like to encrypt a string, type 'E'. However, if you would like to decrypt a string, type 'D'. Finally, if you desire a precision about the types of encryptions and decryptions that this program can do, please type 'Help'.");
			String desiredTask = scanner.next();
			if (desiredTask.equals("E")){
				System.out.println("What phrase would you like me to Encrypt? Remember that I work with bytes; therefore, you can enter other characters than letters! (spaces won't be encrypted)");
				String messageToEncode = scanner.next();
				System.out.println("Now that you have entered your phrase, you need to choose the type of encryption that you want me to perform. Enter '0' to use Caesar, '1' for Vigenere , '2' for XOR,'3' for One Time, and '4' to use CBC! If you input anything else, I will output your phrase back to you, and you will get a chance to start again!" );
				String encryptIndex = scanner.next();
				System.out.println("Here is your encrypted phrase: " + encryptInterpreteurDeCommande(messageToEncode, encryptIndex));
				isFinished = endOfShell();
			}
			else if (desiredTask.equals("D")) {
				System.out.println("Please input the encoded phrase that you would like me to Decrypt");
				String cipher = scanner.next();
				System.out.println("Now that you have entered the phrase that I will Decrypt, you need to select what cipher is needed to Decrypt this message. Type '0' for Caesar, '1' for Vigenere, '2' for XOR and '3' for CBC. ");
				int typeOfDecryption = scanner.nextInt();
				breakCipher(cipher, typeOfDecryption);
				isFinished = endOfShell();
		}
			else if (desiredTask.equals("Help")) {
				System.out.println("This program can encrypt messages in five different ways:" + System.lineSeparator()+" * Caesar"+System.lineSeparator()+" * Vigenere"+System.lineSeparator()+" * XOR"+System.lineSeparator()+ " * OneTime"+System.lineSeparator()+" * CBC");
				System.out.println("It can also decrypt messages encrypted in Caesar, Vigenere, XOR and CBC.");
				System.out.println("To encrypt or decrypt you will usually need a key which is composed of one or more characters that can be represented by a byte." + System.lineSeparator() + "If you are not sure what they are, type 'Print' and I will output all of them." +System.lineSeparator()+  "If you want to encrypt or decrypt a message of your choice, input anything else");
				if (scanner.next().equals("Print")) {

					System.out.println("Here are all the possible characters:");
					int j=0;
					for (int i=0; i<256; i++) {
						if ((i>(31) && i<(128))||(i>(159) && i<(257))) {
							j+=1;
							byte[] singleByteArray = {(byte)i};
							if((j%20)==0) {
								System.out.println(bytesToString(singleByteArray));
						}
							else {System.out.print(bytesToString(singleByteArray)+ " ");}


						}
					}
				}
				System.out.println();
				isFinished = endOfShell();
		}
			else {
				System.out.println("I am sorry, but I don't know what I should do with that input. I will redirect you to the other possibilities");
				isFinished = endOfShell();
			}
	}	}

	
	//---------------------------MAIN---------------------------
	public static void main(String args[]) {
		
//		checkCaesar();
//		checkVigenere();
//		checkXor();
//		checkOtp();
//		checkCbc();
//		checkChallenge();


	}
	
	
	
	
	//----------------------------------------------------------------------------------------------------------
//	String am = "A"; -> maj (65-90)
//	String zm = "Z";			
//	String al = "a"; -> min (97-122)
//	String zl = "z";
	
	//-----------------------------------------------------------------------------------------------
	
	public static void checkChallenge() {
		System.out.println("Empty program for now... But it runs!");
		byte[] challengeEncrypted = stringToBytes(Helper.readStringFromFile("challenge-"
				+ "encrypted.txt"));
        System.out.println(Arrays.toString(challengeEncrypted));
		//Decoding without the key using frequency
		String BD = bytesToString(Decrypt.vigenereWithFrequencies(challengeEncrypted));
		System.out.println("Decoded without knowing the key using frequency : " + BD);
	}
	
	public static void checkCaesar() {
		String inputMessage = Helper.readStringFromFile("text_two.txt");
		String key = "o";
		
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
		//Loading dictionary
		Decrypt.load();
//		String sDA = Decrypt.arrayToString(bruteForceResult);
//		Helper.writeStringToFile(sDA, "bruteForceCaesar.txt");
		byte[] brutelikely = Decrypt.bruteSolution(bruteForceResult);
		System.out.println("Decoded without knowing the key using brute force : " + bytesToString(brutelikely));
		
		byte decodingKey = Decrypt.caesarWithFrequencies(result);
		String sFD = bytesToString(Encrypt.caesar(result, decodingKey));
		System.out.println("Decoded without knowing the key using frequency : " + sFD);
	}	
	
	//-----------------------------------------------------------------------------------------------
	
	public static void checkVigenere() {
		String inputMessage = Helper.readStringFromFile("text_two.txt");
		
		String messageClean = cleanString(inputMessage);
		
		
		byte[] messageBytes = stringToBytes(messageClean);
		String keylet = "abcxyz";
		byte[] keyBytes = stringToBytes(keylet);
		
		
		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();
		
		System.out.println("------Vigenere------");
		testVigenere(messageBytes, keyBytes); //test
	}
	
	public static void testVigenere(byte[] string , byte[] key) {
		//Encoding
		byte[] result = Encrypt.vigenere(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);
		
		//Decoding with key
		byte[] inverseKey = new byte[key.length]; //inverse the key	
		for(int i = 0; i < key.length; i ++) {
			inverseKey[i] = (byte) -key[i];
		}		
			
		String sD = bytesToString(Encrypt.vigenere(result, (byte[]) (inverseKey)));
		System.out.println("Decoded knowing the key : " + sD);
		
		
		//Decoding without the key using frequency
		String BD = bytesToString(Decrypt.vigenereWithFrequencies(result));
		System.out.println("Decoded without knowing the key using frequency : " + BD);

		
	}
	
	//-----------------------------------------------------------------------------------------------
	
	
	public static void checkXor() {
		String inputMessage = Helper.readStringFromFile("text_two.txt");
		String key = "M";
		
		String messageClean = cleanString(inputMessage);
		
		
		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);
		
		
		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();
		
		System.out.println("------Xor------");
		testXor(messageBytes, keyBytes[0]);
	}
	
	public static void testXor(byte[] string , byte key) {
		//Encoding
		byte[] result = Encrypt.xor(string, key, true);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);
		
		//Decoding with key
		String sD = bytesToString(Encrypt.xor(result, (byte) (key), true));
		System.out.println("Decoded knowing the key : " + sD);
		
		//Decoding without key
		byte[][] bruteForceResult = Decrypt.xorBruteForce(result);	
		Decrypt.load();
//		String sDA = Decrypt.arrayToString(bruteForceResult);
//		Helper.writeStringToFile(sDA, "bruteForceXor.txt");
		byte[] brutelikely = Decrypt.bruteSolution(bruteForceResult);
		System.out.println("Decoded without knowing the key using brute force : " + bytesToString(brutelikely));
		
	}
	
	
	//-----------------------------------------------------------------------------------------------
	
	public static void checkOtp() {
		String inputMessage = Helper.readStringFromFile("text_one.txt");
		
		String messageClean = cleanString(inputMessage);
		
		
		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = generatePad(messageBytes.length);
		
		
		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();
		
		System.out.println("------OTP------");
		testOtp(messageBytes, keyBytes);
	}
	
	
	
	public static void testOtp(byte[] string , byte[] key) {
		//Encoding
		byte[] result = Encrypt.oneTimePad(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded : " + s);
				
		//Decoding with key
		String sD = bytesToString(Encrypt.oneTimePad(result, (byte[]) (key)));
		System.out.println("Decoded knowing the key : " + sD);
		
	}

	//-----------------------------------------------------------------------------------------------
	public static void checkCbc() {
		String inputMessage = Helper.readStringFromFile("text_one.txt");
		String key = "pasdecerveau";
		byte xor = (byte) 122;
		
		String messageClean = cleanString(inputMessage);
		
		
		byte[] messageBytes = stringToBytes(messageClean);
		byte[] keyBytes = stringToBytes(key);
		
		
		System.out.println("Original input sanitized : " + messageClean);
		System.out.println();
		
		System.out.println("------CBC------");
		testCbc(messageBytes, keyBytes, xor);
	}
	
	public static void testCbc(byte[] string , byte[] key, byte xor) {
		//Encoding
		byte[] result = Encrypt.cbc(string, key);
		String s = bytesToString(result);
		System.out.println("Encoded using cbc : " + s);
		
		//Decoding with key
		String sD = bytesToString(Decrypt.decryptCBC(result, (byte[]) (key)));
		System.out.println("Decoded cbc knowing the key : " + sD);
		
		//Encoding using advancedCbc
		byte[] result2 = Encrypt.encryptAdvancedCBC(string, key, xor);
		String t = bytesToString(result2);
		System.out.println("Encoded using advanced cbc : " + t);
		
		//Decoding using advancedCbc
		String tD = bytesToString(Decrypt.decryptAdvancedCBC(result2, (byte[]) (key), xor));
		System.out.println("Decoded cbc knowing the key : " + tD);
		
	    
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
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
/**
	 * This method takes care of the encryption part of the Shell
	 * The method is interactive in that it prints certain strings and asks for inputs
	 * nextLine isnt called once to prevent errors!
	 * @param type the string value that will determine what type of encryption is going to be done
	 * @param message the string input that is to be encrypted
	 * @return string corresponding to encrypted message
	*/
	public static String encryptInterpreteurDeCommande(String message, String type) {
		byte[] plainText = stringToBytes(message);
		String cleanedString = cleanString(message);
		byte [] cleanedByteArray = stringToBytes(cleanedString);
		byte[] encodedArray;
		Scanner scanner = new Scanner(System.in);

		if (type.equals("0")) {
			System.out.println("To use Caesar Cipher I need you to give me a key (any character that you would like) which will be stored as a byte." + System.lineSeparator()+"If you don't know what I refer to when talking about keys and characters, please refer to the 'Help'. " + System.lineSeparator()+"Remember, it is very important that you remember your key and keep it secret! " + System.lineSeparator()+"Without this key it is very difficult to decrypt your message.");
			String caesarCipherKey = scanner.nextLine();
			byte[] caesarCipherKeyByteArray = stringToBytes(caesarCipherKey);
			encodedArray = Encrypt.caesar(plainText, caesarCipherKeyByteArray[0], false);
		}
		else if(type.equals("1")) {
			System.out.println("To use Vigenere Cipher I need you to give me a key which will be stored as an array of bytes. (An Array is also known as a List) " + System.lineSeparator()+"Your key can be anything from a word to a random sequence of characters. " + System.lineSeparator()+"If you don't know what I refer to when talking about keys and characters, please refer to the 'Help'. " + System.lineSeparator()+"Remember, it is very important that you remember your key and that you keep it secret!");
			String vigenereCipherKeyString = scanner.nextLine();
			encodedArray = Encrypt.vigenere(plainText, stringToBytes(vigenereCipherKeyString));
		}
		else if(type.equals("2")) {
			System.out.println("To use XOR Cipher I need you to give me a key (any character that you would like) which will be stored as a byte. " + System.lineSeparator()+"If you don't know what I refer to when talking about keys and characters, please refer to the 'Help'. " + System.lineSeparator()+"Remember, it is very important that you remember this key and keep it secret! " + System.lineSeparator()+"Without this key it is very difficult to decrypt your message.");
			byte xorCipherKey = (byte) scanner.next().charAt(0);//If a word is inputed the program keeps the first character and turns it into a byte
			scanner.nextLine();//this line is essential to prevent any error later in the code in the case that more than one word is inputed
			encodedArray = Encrypt.xor(plainText, xorCipherKey);
		}
		else if(type.equals("3")) {
			System.out.println("To use OneTime I need you to give me a pad which will be stored as an array of bytes. (An Array is also known as a List) " +System.lineSeparator()+"A pad is a string that is at least of the same length as the message that you want to encrypt" + System.lineSeparator()+"It can be anything from a phrase to a random sequence of characters. " + System.lineSeparator()+"If you don't know what I refer to when talking about characters and bytes, please refer to the 'Help'. " + System.lineSeparator()+"Remember, it is very important that you remember your pad and that you keep it secret!");
			String oneTimeCipherString = scanner.nextLine();
			encodedArray = Encrypt.oneTimePad(cleanedByteArray, stringToBytes(oneTimeCipherString));
		}
		else if(type.equals("4")) {
			System.out.println("To use CBC I need you to give me a pad which will be stored as an array of bytes. (An Array is also known as a List) "+System.lineSeparator()+"A pad is a string that is at least of the same length as the message that you want to encrypt"  + System.lineSeparator()+"It can be anything from a phrase to a random sequence of characters. " + System.lineSeparator()+"If you don't know what I refer to when talking about characters and bytes, please refer to the 'Help'. " + System.lineSeparator()+"Remember, it is very important that you remember your pad and that you keep it secret!");
			String cbcCipherString = scanner.nextLine();
			encodedArray = Encrypt.cbc(cleanedByteArray, stringToBytes(cbcCipherString));
		}
		else {
			encodedArray = stringToBytes(message);
		}
	return bytesToString(encodedArray);
	}


	//============================================================================================
	/**
	 * This method takes care of the decryption part of the Shell
	 * the method prints certain strings and asks for inputs
	 * nextLine isnt called once to prevent errors!
	 * @param type the string value that will determine what type of decryption is going to be done
	 * @param cipher the string input that is
	 * method is void
	*/
	public static void breakCipher(String cipher, String type) {
		byte[] cipherByteArray = stringToBytes(cipher);
		byte[][] decodedCipherArray;//used for bruteForce
		Scanner scanner = new Scanner(System.in);
		if (type.equals("0")) {
			System.out.println("When it comes to the decryption of Caesar Cipher, there are two different techniques that I can use: " + System.lineSeparator()+"Brute Force and Frequencies. " + System.lineSeparator()+"If you want to try out the second method, which requires no effort from your part, type '1'. " + System.lineSeparator()+"However, if you are willing to look through 256 possible decryptions to find the valid one, then type '0'. " + System.lineSeparator()+"Brute Force takes more time; however, it works 100% of the time. " + System.lineSeparator()+"Frequencies is less reliable with small ciphers");
			String bruteOrFrequencies = scanner.nextLine();
			if (bruteOrFrequencies.equals("0")) {
				decodedCipherArray = Decrypt.caesarBruteForce(cipherByteArray);
				System.out.println("If you want, this program has an implemented dictionary which looks at the output of the Brute Force and returns the line that has the most words from the english language!!" + System.lineSeparator + "If you would like to try out this cool method type 'Yes', and if you want to find your solution on your own with good old Brute Force, type anything else.");
				String useDictionary = scanner.nextLine();
				if (useDictionary.equals("Yes"){
					Decrypt.load(); //loads dictionary and turns it into a hash table
					byte[] bruteLikely = Decrypt.bruteSolution(decodedCipherArray);
					System.out.println("If I am not mistaken, I believe that this is the original message:" + System.lineSeparator() + bytesToString(bruteLikely));
					System.out.println("You can check for yourself:");
			}
				System.out.println("All you have to do is to look for the line that has letters and that makes sense. " + System.lineSeparator()+"Here are the 256 possibilities:");
				System.out.println(Decrypt.arrayToString(decodedCipherArray));
			}
			else if (bruteOrFrequencies.equals("1")) {
				byte caesarKey = Decrypt.caesarWithFrequencies(cipherByteArray);
				System.out.println("The text was: " + Encrypt.caesar(cipherByteArray, caesarKey));
			}
			else{}//nothing is printed if something else than 0 and 1 is entered
		}

		else if (type.equals("1")){
			System.out.println("When it comes to the decryption of Vigenere, I use the frequencies of letters in the English Alphabet " + System.lineSeparator()+"to give you the answer that is most likely to be the right decryption.");
			byte[] vigenereKey = Decrypt.vigenereWithFrequencies(cipherByteArray);
			System.out.println(bytesToString(vigenereKey));

		}
		else if (type.equals("2")) {
			System.out.println("When it comes to the decryption of XOR there is one method: " + System.lineSeparator()+"Brute Force. " + System.lineSeparator()+"It consists in printing the 256 possibilities of encryption. " + System.lineSeparator()+"Then, it will be up to you to find the true meaning of your encrypted message.");
			decodedCipherArray = Decrypt.xorBruteForce(cipherByteArray);
			System.out.println("If you want, this program has an implemented dictionary which looks at the output of the Brute Force and returns the line that has the most words from the english language!!" + System.lineSeparator + "If you would like to try out this cool method type 'Yes', and if you want to find your solution on your own with good old Brute Force, type anything else.");
			String useDictionary = scanner.nextLine();
			if (useDictionary.equals("Yes"){
				Decrypt.load();//loads dictionary and turns it into a hash table
				byte[] bruteLikely = Decrypt.bruteSolution(decodedCipherArray);
				System.out.println("If I am not mistaken, I believe that this is the original message:" + System.lineSeparator() + bytesToString(bruteLikely));
				System.out.println("You can check for yourself:");
			}
			System.out.println("All you have to do is to look for the line that has letters and that makes sense. " + System.lineSeparator()+"Here are the 256 possibilities:");
			System.out.println(Decrypt.arrayToString(decodedCipherArray));
		}
		else if (type.equals("3")) {
			System.out.println("When it comes to the decryption of CBC, I can only perform it if you have the pad used to encrypt the message. " + System.lineSeparator()+"Please input it now.");
			String padString = scanner.nextLine();
			Decrypt.decryptCBC(cipherByteArray, stringToBytes(padString));
		}
		else {
			System.out.println("You didn't enter a valid value. But don't worry because you will get as many chances as you like.");}


	}
	//============================================================================================
	/**
	 * This method asks the user if he would like to continue with the shell
	 * the method asks for a Yes or No answer and loops until one of the two values is entered
	 * nextLine isnt called once to prevent errors!
	 *There is no need for parameters
	 *
	*/
	public static boolean endOfShell() {
		Scanner scanner = new Scanner(System.in);
		String yesOrNo ="";
		while(!((yesOrNo.equals("Yes"))||(yesOrNo.equals("No")))) {
			System.out.println("Would you like to stop the program? " + System.lineSeparator()+"'Yes'or 'No'");
			yesOrNo = scanner.nextLine();
		}
		if (yesOrNo.equals("Yes")){
			return true;//the while loop in the method Shell will stop
		}
		else {
			return false;
		}
	}
	//============================================================================================
	/**
	 * This method is the Shell
	 * It is an interface between a user and the program
	 * The user can navigate the Shell as long as he wants
	 * The method prints certain strings and asks for inputs
	 * Various methods are called by the shell which give it certain capacities
	   such as encryption and decryption
	 * nextLine isnt called once to prevent errors!
	 * The method Shell does not take parameters and is of type void
	*/
	
	public static void Shell(){
		boolean isFinished = false;
		Scanner scanner = new Scanner(System.in);
		System.out.println("Hello! I am a program that has many capabilities when it comes to encryption and decryption.");
		while (!(isFinished)) {
			System.out.println("If you would like to encrypt a string, type 'E'. " + System.lineSeparator()+"However, if you would like to decrypt a string, type 'D'. " + System.lineSeparator()+"Finally, if you desire a precision about the types of encryptions and decryptions that this program can do, please type 'Help'.");
			String desiredTask = scanner.nextLine();
			//encryption part of the shell
			if (desiredTask.equals("E")){
				System.out.println("What phrase would you like me to Encrypt? " + System.lineSeparator()+"Remember that I work with bytes; therefore, you can enter other characters than letters! (spaces won't be encrypted)");
				String messageToEncode = scanner.nextLine();
				System.out.println("Now that you have entered your phrase, you need to choose the type of encryption that you want me to perform. " + System.lineSeparator()+"Enter '0' to use Caesar, '1' for Vigenere , '2' for XOR,'3' for One Time, and '4' to use CBC! " + System.lineSeparator()+"If you input anything else, I will output your phrase back to you, and you will get a chance to start again!" );
				String encryptIndex = scanner.nextLine();
				System.out.println("Here is your encrypted phrase: " + encryptInterpreteurDeCommande(messageToEncode, encryptIndex));
				isFinished = endOfShell();
			}
			//decryption part of the shell
			else if (desiredTask.equals("D")) {
				System.out.println("Please input the encrypted phrase that you would like me to Decrypt");
				String cipher = scanner.nextLine();
				System.out.println("Now that you have entered the phrase that I will Decrypt, you need to select what cipher is needed to Decrypt this message. " + System.lineSeparator()+"Type '0' for Caesar, '1' for Vigenere, '2' for XOR and '3' for CBC. ");
				String typeOfDecryption = scanner.nextLine();
				breakCipher(cipher, typeOfDecryption);
				isFinished = endOfShell();
		}
			//help menu that is capable of printing all characters that are stored as bytes
			else if (desiredTask.equals("Help")) {
				System.out.println("This program can encrypt messages in five different ways:" + System.lineSeparator()+" * Caesar"+System.lineSeparator()+" * Vigenere"+System.lineSeparator()+" * XOR"+System.lineSeparator()+ " * OneTime"+System.lineSeparator()+" * CBC");
				System.out.println("It can also decrypt messages encrypted in Caesar, Vigenere, XOR and CBC.");
				System.out.println("To encrypt or decrypt you will usually need a key which is composed of one or more characters that can be represented by a byte." + System.lineSeparator() + "If you are not sure what they are, type 'Print' and I will output all of them." +System.lineSeparator()+  "If you want to encrypt or decrypt a message of your choice, input anything else");
				if (scanner.next().equals("Print")) {

					System.out.println("Here are all the possible characters:");
					int j=0;
					for (int i=0; i<256; i++) {
						if ((i>(31) && i<(128))||(i>(159) && i<(257))) { //all bytes between these values don't represent any characters
							byte[] singleByteArray = {(byte)i};
							if((j%20)==0) {//prints the characters in rows of 20 to make them look neat
								System.out.println(bytesToString(singleByteArray));
						}
							else {System.out.print(bytesToString(singleByteArray)+ " ");}
							j+=1;
						}
					}
				}
				System.out.println();
				isFinished = endOfShell();
		}
			else {//we don't want the shell to have an error everytime that the user doesn't enter a valid input
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

package crypto;

import static crypto.Helper.bytesToString;

import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Decrypt {
	
	
	public static final int ALPHABETSIZE = Byte.MAX_VALUE - Byte.MIN_VALUE + 1 ; //256
	public static final int APOSITION = 97 + ALPHABETSIZE/2; 
	
	//source : https://en.wikipedia.org/wiki/Letter_frequency
	public static final double[] ENGLISHFREQUENCIES = {0.08497,0.01492,0.02202,0.04253,0.11162,0.02228,0.02015,0.06094,0.07546,0.00153,0.01292,0.04025,0.02406,0.06749,0.07507,0.01929,0.00095,0.07587,0.06327,0.09356,0.02758,0.00978,0.0256,0.0015,0.01994,0.00077};
	
	/**
	 * Method to break a string encoded with different types of cryptosystems
	 * @param type the integer representing the method to break : 0 = Caesar, 1 = Vigenere, 2 = XOR
	 * @return the decoded string or the original encoded message if type is not in the list above.
	 */
	public static String breakCipher(String cipher, int type) {

		byte[] cipherByteArray = stringToBytes(cleanString(cipher));
		byte[][] decodedCipherArray;
		switch (type) {
		case 0:
			decodedCipherArray = caesar(plainText,cipherKey[0]);
			break;
		case 1:
			decodedCipherArray[0] = vigenereWithFrequencies(cipherByteArray);
			break;
		case 2:
			decodedCipherArray = xorBruteForce(cipherByteArray);
			break;
		default:
			decodedCipherArray = stringToBytes(cipher);
			
		}
		return null;

	}
	
	
	/**
	 * Converts a 2D byte array to a String
	 * @param bruteForceResult a 2D byte array containing the result of a brute force method
	 */
	public static String arrayToString(byte[][] bruteForceResult) {

		/* print null between spaces and
		 *  didnt manage to use System.lineSeperator()*/
		String bruteForceStringResult = "";
		for (int i = 0; i<256; i++) {
			bruteForceStringResult = bruteForceStringResult.concat(bytesToString(bruteForceResult[i])+System.lineSeparator());
		}
		return bruteForceStringResult;
	}
	
	
	//-----------------------Caesar-------------------------
	
	/**
	 *  Method to decode a byte array  encoded using the Caesar scheme
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return a 2D byte array containing all the possibilities
	 */
	public static byte[][] caesarBruteForce(byte[] cipher) {
		byte[][] allCaesarPossibilities = new byte[256][];

		for (int i=0; i<256; i++) {
			byte oppositeOfPotentialKey = (byte)-i;
			allCaesarPossibilities[i] = Encrypt.caesar(cipher, oppositeOfPotentialKey); //im not sure about that line
		}
		return allCaesarPossibilities;

	}	
	
	
	/**
	 * Method that finds the key to decode a Caesar encoding by comparing frequencies
	 * @param cipherText the byte array representing the encoded text
	 * @return the encoding key
	 */
	public static byte caesarWithFrequencies(byte[] cipherText) {

		float[] characterFrequencies = computeFrequencies(cipherText);

		return caesarFindKey(characterFrequencies);
	
	}
	
	/**
	 * Method that computes the frequencies of letters inside a byte array corresponding to a String
	 * @param cipherText the byte array 
	 * @return the character frequencies as an array of float
	 */
	public static float[] computeFrequencies(byte[] cipherText) {
		float[] characterFrequenceInCypher = new float[256];
		int i=0;
		int numberOfSpaces = 0;
		while(i<cipherText.length) {
			characterFrequenceInCypher[cipherText[i]+128] +=1;
			if(cipherText[i]==32) {
				numberOfSpaces +=1;	
			}
			i++;
		}
		int numberOfCharacters = i-numberOfSpaces;
		for(int j=0; j<256; j++) {
			if(j!=160) {
				characterFrequenceInCypher[j]=characterFrequenceInCypher[j]/numberOfCharacters;
			}
			else {
				characterFrequenceInCypher[j]=0;
			}
		}
		return characterFrequenceInCypher; 

	}
	
	
	/**
	 * Method that finds the key used by a  Caesar encoding from an array of character frequencies
	 * @param charFrequencies the array of character frequencies
	 * @return the key
	 */
	public static byte caesarFindKey(float[] charFrequencies) {
		float[] scalarProductOfEachIteration = new float[256];
		for (int i=0;i<256;i++) {
			for (int j=0; j<26; j++) {
				scalarProductOfEachIteration[i] += ENGLISHFREQUENCIES[j]*charFrequencies[(j+i)%256];
			}
		}
		
		float[] maxScalarProduct = {scalarProductOfEachIteration[0],0};
		for(int j=1; j<256; j++) {
			if (scalarProductOfEachIteration[j]>maxScalarProduct[0]) {
				maxScalarProduct[0] = scalarProductOfEachIteration[j];
				maxScalarProduct[1] = j;
			}
		}
		int indexMaxScalarProduct = (int) maxScalarProduct[1];
		byte caesarKey = (byte) (97+128-indexMaxScalarProduct);
		return caesarKey;

	}
	
	
	
	//-----------------------XOR-------------------------
	
	/**
	 * Method to decode a byte array encoded using a XOR 
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return the array of possibilities for the clear text
	 */
	public static byte[][] xorBruteForce(byte[] cipher) {
		//TODO : COMPLETE THIS METHOD

		byte[][] xor_possibilities = new byte[256][];
		for(int i = 0; i < 256; i++){
			byte key = (byte) i; //no opposite like you did, if you can explain that to me...
			xor_possibilities[i] = Encrypt.xor(cipher, key);
		}

		return xor_possibilities; //TODO: to be modified

	}
	
	
	
	//-----------------------Vigenere-------------------------
	// Algorithm : see  https://www.youtube.com/watch?v=LaWp_Kq0cKs	
	/**
	 * 
	 * Method to decode a byte array encoded following the Vigenere pattern, but in a clever way, 
	 * saving up on large amounts of computations
	 * @param cipher the byte array representing the encoded text
	 * @return the byte encoding of the clear text
	 */
	public static byte[] vigenereWithFrequencies(byte[] cipher) {
		//TODO : COMPLETE THIS METHOD
		List<Byte> spaceRemoved = removeSpaces(cipher);	
		int keyLength = vigenereFindKeyLength(spaceRemoved);
		byte[] key = vigenereFindKey(spaceRemoved, keyLength);
		byte[] vigenereCracked = Encrypt.vigenere(cipher, key, false); //if false, spaces are not encoded/decoded
		
		return vigenereCracked; //TODO: to be modified
	}
	
	
	
	/**
	 * Helper Method used to remove the space character in a byte array for the clever Vigenere decoding
	 * @param array the array to clean
	 * @return a List of bytes without spaces
	 */
	public static List<Byte> removeSpaces(byte[] array){
		//TODO : COMPLETE THIS METHOD
		List<Byte> list = new ArrayList<Byte>();
		for(int i = 0; i < array.length; i++) { //adding all non spaces values to <byte> list
			if(array[i] != 32) {
				list.add(array[i]);
			}
		}
		return list;
	}
	
	
	/**
	 * Method that computes the key length for a Vigenere cipher text.
	 * @param cipher the byte array representing the encoded text without space
	 * @return the length of the key
	 */
	public static int vigenereFindKeyLength(List<Byte> cipher) {
		//TODO : COMPLETE THIS METHOD
		int length = cipher.size();
		int coincidence[] = new int[length - 1]; //coincidence[0] would be the number of coincidences between line 0 and line 1, [length-1] would be between line 0 and line length-1
		
		
		// remplissage tableau coincidence
		for(int i = 0; i < coincidence.length; i++) { //change lines
			int coincid = 0;
			for(int j = 0; j < length -1 - i; j++) { //as line is more décalé, the number of iteration per line decreases //second line 
				if(cipher.get(j) == cipher.get(j+i+1)) { // the second line always starts at index 0 (j loop), the first line starts at index i+1 
					coincid += 1;
				}
			}
			coincidence[i] = coincid;
		}
		
		System.out.println("Coincidences for decryption");
		for(int i = 0; i < coincidence.length; i++) {										//Debugging Seems to have about 20 (many) coincidences for every line
			System.out.println(coincidence[i]);
		}																					
		
		// calcul des maximums locaux et taille de cle potentielle 
		int half;
		if(coincidence.length %2 == 0) {
			half = coincidence.length / 2;
		}else {
			half = (coincidence.length+1) /2;
		}
		
		List<Integer> stock = new ArrayList<Integer>();		// stocker l'indice des maximums locaux dans une ArrayList stock
		
		if(maxAfter(coincidence[0], coincidence[1], coincidence[2], 0, 0) == true) { //index 0
			stock.add(0);
		}
		if(maxAfter(coincidence[1], coincidence[2], coincidence[3], coincidence[0], 0) == true) { //index 1
			stock.add(1);
		}
		for(int k = 2; k <= half-3; k++) { // general cases
			if(maxAfter(coincidence[k], coincidence[k-2], coincidence[k-1], coincidence[k+1], coincidence[k+2]) == true) {
				stock.add(k);
			}
		}
		if(maxAfter(coincidence[half-2], coincidence[half-3], coincidence[half-4], coincidence[half-1], 0) == true) { //index length-2
			stock.add(half-2);
		}
		if(maxAfter(coincidence[half-1], coincidence[half-2], coincidence[half-3], 0, 0) == true) { //index length-1
			stock.add(half-1);
		}
		// --------------------
		
		//Calcul de la taille potentielle de la clé, stocker et comparer les nombres de décalge qui reviennent le plus souvent
		int bonds[] = new int[40];  // stocker le nombre de bonds pour des keys de longueurs 0 à 40
		for(int l = 0; l < stock.size() - 1; l++) {
			int jump = stock.get(l+1) - stock.get(l); //le nombre de saut selon les indices des maximums locaux
			bonds[jump] += 1;
		}
		//Trouver le saut qui survient le plus
		int maxKey = 0;
		for(int m = 0; m < bonds.length; m++) {
			if(bonds[m] > maxKey) {
				maxKey = m; //the indice of jump that occurs most is stored
			}
		}
		
		return maxKey; //TODO: to be modified
	}

	
	
	private static boolean maxAfter(int actual, int one, int two, int three, int four) { //true if actual is the maximum with the four after, use 0 if non existent
		// TODO Auto-generated method stub
		int firstMax = Math.max(actual, one);
		int secondMax = Math.max(actual, two);
		int thirdMax = Math.max(actual, three);
		int fourthMax = Math.max(actual, four);
		if(firstMax == secondMax && secondMax == thirdMax && thirdMax == fourthMax) {
			return true;
		}else {
			return false;
		}
		
	}


	/**
	 * Takes the cipher without space, and the key length, and uses the dot product with the English language frequencies 
	 * to compute the shifting for each letter of the key
	 * @param cipher the byte array representing the encoded text without space
	 * @param keyLength the length of the key we want to find
	 * @return the inverse key to decode the Vigenere cipher text
	 */
	public static byte[] vigenereFindKey(List<Byte> cipher, int keyLength) { //would be nice to add the spaces at their respective indexes again...
		//TODO : COMPLETE THIS METHOD
		int remainder = cipher.size() % keyLength; //number of iterations on the last, optional
		int multiple = Math.floorDiv(cipher.size(), keyLength); //number of iterations
		byte caesarTables[][] = new byte[keyLength][multiple + 1]; //first dimension is the index of table, second dimension will be the characters of the table in order
		//you can say the second dim size is multiple + 1 (for remainder) since caesarWtihFrequencies will take care of spaces
		
		for(int p = 0; p < multiple; p++) { //for multiple
			for(int q = 0; q < keyLength; q++) {
				caesarTables[q][p] = cipher.get(p*keyLength + q);
			}
		}
		for(int r = 0; r < remainder; r++){// for remainder
			caesarTables[r][multiple] = cipher.get(multiple * keyLength + r);
		}
		for(int s = 0; s < keyLength-remainder; s++) {//fill remainder with spaces
			caesarTables[s+remainder][multiple] = 32; //32 is space in byte
		}
		
		//so at the end we get n(keyLength) tableaux, perform caesare frequency on each of them to find the respective key, then add all together in a byte array.
		byte[] respectiveKey = new byte[keyLength];
		for(int s = 0; s < keyLength; s++) {
			
			respectiveKey[s] = caesarWithFrequencies(caesarTables[s]);
		}
		
//		byte[] inverseKey = new byte[respectiveKey.length]; //inverse the key
//		for(int i = 0; i < respectiveKey.length; i ++) {
//			inverseKey[i] = (byte) -respectiveKey[i];
//		}
		return respectiveKey; //TODO: to be modified
	}
	
	
	//-----------------------Basic CBC-------------------------
	
	/**
	 * Method used to decode a String encoded following the CBC pattern
	 * @param cipher the byte array representing the encoded text
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return the clear text
	 */
	public static byte[] decryptCBC(byte[] cipher, byte[] iv) {
		//TODO : COMPLETE THIS METHOD	
							//		byte[] reverseCipher = reverse(cipher); // i thought we had to do from the end, may be useful later
							//		byte[] reverseIv = reverse(iv);
							//		byte[] decrypted = Encrypt.cbc(reverseCipher, reverseIv);
		byte[] decrypted = Encrypt.chooseCBC(cipher, iv, true);
		
		return decrypted; //TODO: to be modified
	}
	
	public static byte[] reverse(byte[] toReverse) {
		int length = toReverse.length;
		byte[] buffer = new byte[length];
		for(int i = 0; i < length; i++) {
			buffer[i] = toReverse[length-1];
		}
		return buffer;
	}
	
	
	

		
		
		
		
		
}

package crypto;

import static crypto.Helper.bytesToString;

import static crypto.Helper.cleanString;
import static crypto.Helper.stringToBytes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.tools.javac.util.Context.Key;

import java.io.BufferedReader;

import java.io.FileInputStream; // Import the File class
import java.io.IOException; // Import this class to handle errors
import java.io.InputStreamReader; // Import this class to read text files



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
			byte caesarKey = Decrypt.caesarWithFrequencies(cipherByteArray);
		 	decodedCipherArray[0] = Encrypt.caesar(stringToBytes(cipherByteArray), caesarKey));
			break;
		case 1:
			decodedCipherArray[0] = vigenereWithFrequencies;
			break;
		case 2:
			decodedCipherArray = xorBruteForce(cipherByteArray);
			break;
		default:
			decodedCipherArray = stringToBytes(cipher);

		}
		return arrayToString(decodedCipherArray);

	}


	/**
	 * Converts a 2D byte array to a String
	 * @param bruteForceResult a 2D byte array containing the result of a brute force method
	 */
	public static String arrayToString(byte[][] bruteForceResult) {
		String bruteForceStringResult = "";
		for (int i = 0; i<256; i++) { //goes through the 256 rows of the 2D array to concatenate the string equivalence
			bruteForceStringResult = bruteForceStringResult.concat(bytesToString(bruteForceResult[i])+System.lineSeparator()); //a line is added between each decryption to keep it clear
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

		for (int i=0; i<256; i++) { //goes through 256 potential keys
			byte oppositeOfPotentialKey = (byte)-i; //if the decryption is found with a key of value x, then we know that the encryption key was -x
			allCaesarPossibilities[i] = Encrypt.caesar(cipher, oppositeOfPotentialKey); //each row in the byte array contains a possible decryption of the cipher
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
		int numberOfSpaces = 0;//This value will help the method know how many characters there are in the cipherText
		for(int i=0; i<cipherText.length; i++) {
			characterFrequenceInCypher[cipherText[i]+128] +=1; //indexes can't be negative so we add 128
			if(cipherText[i]==32) {//32 is the byte value for spaces
				numberOfSpaces +=1;
			}
		}
		int numberOfCharacters = (cipherText.length)-numberOfSpaces;
		for(int j=0; j<256; j++) {
			if(j!=160) {//32 + 128 = 160; index 160 of characterFrequenceInCypher corresponds to spaces
				characterFrequenceInCypher[j]=characterFrequenceInCypher[j]/numberOfCharacters; //dividing by the number of characters gives us a max value of 1
			}
			characterFrequenceInCypher[160]=0;//we don't want the frequence of spaces
		}
		return characterFrequenceInCypher;

	}


	/**
	 * Method that finds the key used by a  Caesar encoding from an array of character frequencies
	 * This is done by finding the maxScalarProduct
	 * The algorithm iterates through the array ENGLISHFREQUENCIES and takes a "sub array" of characterFrequencies
	   of the same length(26). The scalar product will then be the sum of the product of each of their corresponding values
		 example: subArray[0]*ENGLISHFREQUENCIES[0]+...+subArray[25]*ENGLISHFREQUENCIES[25]
		 This is done for the 256 possible subArrays of length 26
	 * @param charFrequencies the array of character frequencies
	 * @return the key
	 */
	public static byte caesarFindKey(float[] charFrequencies) {
		float[] scalarProductOfEachIteration = new float[256];
		for (int i=0;i<256;i++) {
			for (int j=0; j<26; j++) {
				scalarProductOfEachIteration[i] += ENGLISHFREQUENCIES[j]*charFrequencies[(j+i)%256];//%256 keeps the value of j+i between 0 and 255
			}}
		//this part of the method iterates through scalarProductOfEachIteration and finds the max value
		/*the array maxScalarproduct has two entries:
	 	 the first is a scalar product and the second is the index associated to that maximum scalar product*/
		float[] maxScalarProduct = {scalarProductOfEachIteration[0],0};
		for(int j=1; j<256; j++) {
			if (scalarProductOfEachIteration[j]>maxScalarProduct[0]) {
				maxScalarProduct[0] = scalarProductOfEachIteration[j];
				maxScalarProduct[1] = j;
			}
		}
		int indexMaxScalarProduct = (int) maxScalarProduct[1];
		byte caesarKey = (byte) (97+128-indexMaxScalarProduct);// 97 is the byte value for a and 97+128 is the index for a in charFrequencies
		return caesarKey; //caesarKey is the opposite of the key that was used to encrypt the message

	}

	
	
	
	//-----------------------XOR-------------------------
	
	/**
	 * Method to decode a byte array encoded using a XOR 
	 * This is done by the brute force generation of all the possible options
	 * @param cipher the byte array representing the encoded text
	 * @return the array of possibilities for the clear text
	 */
	public static byte[][] xorBruteForce(byte[] cipher) {

		byte[][] xor_possibilities = new byte[256][];
		for(int i = 0; i < 256; i++){
			byte key = (byte) i; //no opposite like you did, if you can explain that to me...
			xor_possibilities[i] = Encrypt.xor(cipher, key, true);
		}

		return xor_possibilities;

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
		List<Byte> spaceRemoved = removeSpaces(cipher);	
		int keyLength = vigenereFindKeyLength(spaceRemoved); 
		byte[] key = vigenereFindKey(spaceRemoved, keyLength); 
		byte[] vigenereCracked = Encrypt.vigenere(cipher, (byte[]) key); //if false, spaces are not encoded/decoded
		
		return vigenereCracked;
	}
	
	
	
	/**
	 * Helper Method used to remove the space character in a byte array for the clever Vigenere decoding
	 * @param array the array to clean
	 * @return a List of bytes without spaces
	 */
	public static List<Byte> removeSpaces(byte[] array){
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
		int length = cipher.size();
		int coincidence[] = new int[length - 1]; //coincidence[0] would be the number of coincidences between line 0 and line 1, [length-1] would be between line 0 and line length-1
		
		
		// remplissage tableau coincidence
		for(int i = 0; i < coincidence.length; i++) { //change lines

			for(int j = 0; j < coincidence.length - i; j++) { //as line is more décalé, the number of iteration per line decreases //second line 
				if(cipher.get(j) == cipher.get(j+i+1)) { // the second line always starts at index 0 (j loop), the first line starts at index i+1 
					coincidence[i] += 1;
				}
			}
	
		}																				
		
		// calcul des maximums locaux et taille de cle potentielle 
		int half; //index 0 to index half-1
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
			if(maxAfter(coincidence[k], coincidence[k-1], coincidence[k+1], coincidence[k-2],  coincidence[k+2]) == true) {
				stock.add(k);
			}
		}
		if(maxAfter(coincidence[half-2], coincidence[half-3], coincidence[half-4], coincidence[half-1], 0) == true) { //index length-2
			stock.add(half-2);
		}
		if(maxAfter(coincidence[half-1], coincidence[half-2], coincidence[half-3], 0, 0) == true) { //index length-1
			stock.add(half-1);
		}
		
		//System.out.println(stock);		//for debugging,  sometimes get 159,160 which means that max(index159, index160) is 159=60, these two are the same values
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
			if(bonds[m] >= maxKey) {
				maxKey = m; //the indice of jump that occurs most is stored
			}
		}
		
		return maxKey;
		
	}

	
	
	public static boolean maxAfter(int actual, int one, int two, int three, int four) { //true if actual is the maximum with the four after, use 0 if non existent
		int firstMax = Math.max(actual, one);
		int secondMax = Math.max(actual, two);
		int thirdMax = Math.max(actual, three);
		int fourthMax = Math.max(actual, four);
		if(actual == one || actual == two || actual == three || actual == four) {
			return false;
		}
		if((actual == firstMax) && (actual == secondMax) && (actual == thirdMax) && (actual == fourthMax)) {
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
	public static byte[] vigenereFindKey(List<Byte> cipher, int keyLength) { 
		int remainder = cipher.size() % keyLength; //number of iterations on the last, optional
		int multiple = cipher.size() / keyLength; //number of iterations
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
		
		
		
		//so at the end we get n(keyLength) tableaux, perform caesar frequency on each of them to find the respective key, then add all together in a byte array.
		byte[] respectiveKey = new byte[keyLength];
		
		for(int s = 0; s < keyLength; s++) {
//			
//			System.out.println(caesarTables[s]);
//			for(int j = 0; j < caesarTables[s].length; j++) {
//				System.out.println(caesarTables[s][j]);
//			}
			
			byte[] sub = new byte[caesarTables[s].length];
			for(int j = 0; j < caesarTables[s].length; j++) {
				sub[j] = caesarTables[s][j];
//				System.out.println(caesarTables[s][j]);
			}
			respectiveKey[s] = caesarWithFrequencies(sub);
		}
		
		return respectiveKey;
		
	}
	
	
	//-----------------------Basic CBC-------------------------
	
	/**
	 * Method used to decode a String encoded following the CBC pattern
	 * @param cipher the byte array representing the encoded text
	 * @param iv the pad of size BLOCKSIZE we use to start the chain encoding
	 * @return the clear text
	 */
	public static byte[] decryptCBC(byte[] cipher, byte[] iv) {	
							//		byte[] reverseCipher = reverse(cipher); // i thought we had to do from the end, may be useful later
							//		byte[] reverseIv = reverse(iv);
							//		byte[] decrypted = Encrypt.cbc(reverseCipher, reverseIv);
		byte[] decrypted = Encrypt.chooseCBC(cipher, iv, true);
		
		return decrypted;
	}
	
	public static byte[] decryptAdvancedCBC(byte[] plainText, byte[] iv, byte xor) { 
		byte[] decrypted = Encrypt.chooseAdvancedCBC(plainText, iv, xor, true);
		return decrypted;

	}
	
	public static byte[] reverse(byte[] toReverse) {
		int length = toReverse.length;
		byte[] buffer = new byte[length];
		for(int i = 0; i < length; i++) {
			buffer[i] = toReverse[length-1];
		}
		return buffer;
	}
	
	
	//-----------------------Vigenere Brute Force -------------------------
	
//	public static byte[] vigenereBruteForce(byte[] cipher) { //only return brute filtered, which means only lines with only letters (also store their respective keys)
//		ArrayList<byte[]> possible = new ArrayList<byte[]>(); //contains only the possible lines
//		int maxKeyLength = 12;
//		int[] length = new int[maxKeyLength];
//		int testingLength = 2;
//		boolean finished = false;
//		
//		byte[][] workingKeys = new byte[maxKeyLength][]; //we will add only keys that work
//		for(int i = 2; i <= maxKeyLength; i++) { //we will test 26 -keys for each length : taking the same code as vigenere frequency and testing for everykey length
//			
//			//create new cipher with only position i
//			ArrayList<Byte>spaceRemoved = (ArrayList<Byte>) removeSpaces(cipher);
//			
//			int remainder = spaceRemoved.size() % i; //number of iterations on the last, optional
//			int multiple = spaceRemoved.size() / i; //number of iterations
//			byte caesarTables[][] = new byte[i][multiple + 1]; //first dimension is the index of table, second dimension will be the characters of the table in order
//			//you can say the second dim size is multiple + 1 (for remainder) since caesarWtihFrequencies will take care of spaces
//			
//			for(int p = 0; p < multiple; p++) { //for multiple
//				for(int q = 0; q < i; q++) {
//					caesarTables[q][p] = spaceRemoved.get(p*i + q);
//				}
//			}
//			for(int r = 0; r < remainder; r++){// for remainder
//				caesarTables[r][multiple] = spaceRemoved.get(multiple * i + r);
//			}
//			for(int s = 0; s < i-remainder; s++) {//fill remainder with spaces
//				caesarTables[s+remainder][multiple] = 32; //32 is space in byte
//			}
//			
//			
//			//so at the end we get n(keyLength) tableaux, perform caesar frequency on each of them to find the respective key, then add all together in a byte array.
//			byte[][] bruteForceResult = new byte[256][];			
//			for(int s = 0; s < i; s++) {
//				byte[] sub = new byte[caesarTables[s].length];
//				for(int j = 0; j < caesarTables[s].length; j++) {
//					sub[j] = caesarTables[s][j];
//					System.out.println(caesarTables[s][j]);
//				}
//				bruteForceResult = caesarBruteForce(sub);
//			}
//		}
//		
//		while(finished == false) {
//			
//			byte[] temp = new byte[cipher.length];
//			byte[] key = new byte[testingLength];	//code to test all possible keys for every possible length
//		
//			
//			temp = Encrypt.vigenere(cipher, (byte[]) (key));		//testing out and adding if it only contains letter and spaces
//			boolean feasable = true;
//			for(int i = 0; i < temp.length; i++) {
//				if((temp[i] != 32) && !(temp[i]>=97 && temp[i]<=122)) {
//					feasable = false;
//					break;
//				}
//			}
//			if(feasable == true) {
//				possible.add(temp);
//			}
//			
//			if(testingLength == 1) {
//				finished = true;
//				break;
//			}
//		}	
//		
//		
//		
//		int bestSolutionIndex = 0;
//		int bestSolutionWords = 0;
//		for(int i = 0; i < possible.size(); i++) {
//			int n = foundInDictionary(possible.get(i));
//			if(bestSolutionWords < n) {
//				bestSolutionWords = n;
//				bestSolutionIndex = i;
//			}
//		}
//		return possible.get(bestSolutionIndex);
//		
//	}
//	
//	
	//-----------------------Otp Brute Force -------------------------

	
	//-----------------------CBC Brute Force -------------------------

//	public static byte[] cbcBruteForce(byte[] cipher) { //only return brute filtered, which means only lines with only letters (also store their respective keys)
//		byte[] solution = {32, 43};
//		final int keyLength = 5;		
//		
//		ArrayList<byte[]> possible = new ArrayList<byte[]>(); //contains only the possible lines
//		
//		for(int i = 1; i <= keyLength; i++) {
//			ArrayList<byte[]> possibleForLength = cbcBruteForceDecrypt(cipher, i); //adding all possible lines containing only letters in the "possibleForLength" arraylist (the loop goes over all possible keys until size keyLength)
//			for(int j = 0; j < possibleForLength.size(); j++) {
//				possible.add(possibleForLength.get(j)); //adding the values from possibleForLength to possible		//we could do all of that manually but it's cleaner to do it that way
//			}
//		}
//
//		//Call out dictionary method to count and select the line with most words included in the dictionary
//		int maxWords = 0;
//		int indexOfMaxWords = 0;
//		for(int i = 0; i < possible.size(); i++){
//			int currentMaxWords = foundInDictionary(possible.get(i));
//			if( currentMaxWords > maxWords) {
//				maxWords = currentMaxWords;
//				indexOfMaxWords = i;
//			}
//		}
//				
//
//		return possible.get(indexOfMaxWords); //TODO: to be modified
//	}
//		
//	private static ArrayList<byte[]> cbcBruteForceDecrypt(byte[] plainText, int keyLength){ //prend le texte et la longueur de clé, filtre et retourne seulement les lignes avec que des lignes de l'alphabet and une arrayliste contenant des byte
//			return null;	
//	}	

	
	
	
	//-----------------------Importation and Storage of a dictionary in a hash table-------------------------

		
	public static byte[] bruteSolution (byte[][] everything) {
		ArrayList<byte[]> possible = likelySolutions(everything); //contains only the possible lines		
		int maxWords = 0;
		int bestIndex = 0;
		for(int i = 0; i < possible.size(); i++) {
			int currentWords = foundInDictionary(possible.get(i));
			if(currentWords > maxWords) {
				maxWords = currentWords;
				bestIndex = i;
			}
		}
		
		return possible.get(bestIndex);
	}
	
	public static ArrayList<byte[]> likelySolutions (byte[][] everything) { //job is to look over and return the lines where there are only letters and spaces
		ArrayList<byte[]> possible = new ArrayList();		//only used for caesar and xor since already made
		int size = everything.length;
		int length = everything[0].length;
		for(int i = 0; i < size; i++) {
			int letterSpace = 0;
			for(int j = 0; j < length; j++) {
				
				if((everything[i][j] != 32) && !(everything[i][j]>=65 && everything[i][j]<=90) && !(everything[i][j]>=97 && everything[i][j]<=122)) {
					break;
				}
				letterSpace += 1;
				if(letterSpace >= 0.9 * length) { //0.9 because words like i are not in the dictionary...
					possible.add(everything[i]);
				}
			}
		}
		return possible;
	}
	
	//load - dictionary and store it into a hashtable
	
	public static void load() {
		try {
			 FileInputStream dictionary = new FileInputStream("dictionaries/large"); //input the dictionary there
		     BufferedReader  myReader = new BufferedReader(new InputStreamReader((dictionary)));
		     
		     String strLine;
		     
		     //initialize the hash table
		     HashTable();	     
		     
		     
		     //loop to find the tempWord
		     while((strLine = myReader.readLine()) != null) {
		    	String tempWord = strLine.stripTrailing();
		    	if(tempWord.length() < 2) {
		    		continue;//otherwise, hash gives me an error message
		    	}
		    	Node tempN = new Node();
		    	tempN.value = tempWord; //copy the current word into the temporary word
		    	
		    	//find the hash of the word (call the hash function which then will return an integer/index number) - so we need to look at the first letter and give the index according to that		    	
		        int key = hash(tempWord);
		       
		        //load the table - 1) nothing 2) already a value in it
	            if(table[key] == null){ //or  .next ? i'm not sure
	                tempN.next = null; //set the temporary node, next pointer to null, since it points to nothing
	                table[key] = tempN;//points to the temporary node automatically
	          
	            }else{
	                tempN.next = table[key].next; //the node gets the same next pointer as temp/the base
	                table[key].next = tempN; //the starting node points the new temp node	                
	            }
		     }
		     
		     dictionary.close();
			return true;
		}catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		      return false;
		    }		
	}
	
	
	private boolean unload() {
		table = null; //reset the first to null for each row	 
	    return true;
	}
	

	//Node in a hash table
	static private class Node{ //values with same keys are placed in a linked list
		Object value;
		Node next; //pointer to the next node in the linked list
					//null marks the end of the list
	}
	
	final static int layers = 2;
	final static int n = 26;
	final static int N = (int) Math.pow(n, layers); //2 times as fast
	
	
	 //Number of buckets in hash table
	static Node[] table = new Node[N]; //hash table as an array of linked list
	
	 private static void HashTable() {
         // Create a hash table with an initial size of 64.
       table = new Node[N];
    
    }
	
	 private static void HashTable(int initialSize) {
         // Create a hash table with a specified initial size.
         // Precondition: initalSize > 0.
       table = new Node[initialSize];
    }
	
	static public int hashPosition(String word, int n) { //first position is n=0, a = 0, b =1, ...
		int position = 0;	
		position = (int) word.charAt(n-1);
		position -= 97;
		return position;
	}
	
	static public int hash(String word) { //only made for 2 first letters bc then it gets harder to find a way
		int key = 0;
		int first = hashPosition(word, 1);
		int second = hashPosition(word, 2);
		key = first * 26 + second;
        return key;
	}
	
	

	public static int foundInDictionary (byte[] line) { //returns number of words found in the dictionary
		String completeLine = bytesToString(line);
		String[] wordsSeparated = completeLine.split(" ");
		int wordCount = 0;
		for(int i = 0; i < wordsSeparated.length; i++) {
			if(wordsSeparated[i].length() < layers) {
				continue;
			}else {
				boolean aWord = check(wordsSeparated[i]);
				if(aWord == true) {
					wordCount += 1;
				}
			}
		}
		return wordCount;
	}
	
	public static boolean check(String word) { //returns true if word is in dictionary
		word = word.toLowerCase();
		int key = hash(word);
		boolean found = false;
		Node toCheck = table[key];
		while(toCheck != null) {
			if(word.equals(toCheck.value)) {
				found = true;
				break;
			}else{
				toCheck = toCheck.next;
			}
		}
		
		if(found == true) {
			return true;
		}else {
			return false;
		}
	}
	
	
	
	
	
		
		
		
}

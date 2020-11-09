Information importantes : comme le fichier zip fait 511ko, nous ne pouvions pas tout upload sur le site.
Par conséquent nous avons utilisé des liens directs  dropbox pour lire les fichiers (don't un dictionaire de 143'000 mots). À cause de cela, le programme ralenti un peu lors de l'utilisation de la fonctionalité de tri pour la méthode brute de césar et xor.
Nous avons essayer avec google drive sans succès car le programme bloque à <DOCTYPE> et nous ne pouvons lire le fichier directement.
Dans la fonction load() dans Decrypt.java, il y a la lecture de ces fichiers originales en commentaire, indiqué par !!! 
!!!le projet faisait 105ko donc nous n'avons pas les fichiers challenge-encrypted.txt, text_one.txt, text_two.txt et text_three.txt

Informations sur le fichier Main :

En haut se trouve les méthodes du Shell:
	la première se charge de l'encryption
	la seconde du déchiffrement
	la troisième est une fonction qui appelle à terminer le Shell
	Finalement la quatrième est le Shell

Puis vous trouverez la méthode main et les méthodes de vérification

Dans la fonction Main :

Nous avons mis le shell et les fonctions de vérification en commentaire:
		//		shell();
		//		checkCaesar();
		//		checkVigenere();
		//		checkXor();
		//		checkOtp();
		//		checkCbc();
		//		checkChallenge();
		
Plus bas, vous pouvez modifier le fichier lu et mieux comprendre le code.

Dans chacun de ces codes, vous pouvez insérer un texte, le programme va le coder, ensuite le décoder en connaissant la clé, et également en ne connaissant pas la clé (césar et vigenere).

Bonus : 

1) pour le premier bonus, nous avons rajouté une fonction "xor" après chaque encryptement du message cbc. 

	La méthode pour encrypter s'appelle : 
	
		encryptAdvancedCBC()
		
			et se trouve dans le ficher Encrypt.java
			
		tandis que pour décrypter la fonction s'appelle :
		
		decryptAdvancedCBC()
		
			et se trouve dans le ficher Decrypt.java
		étant donné que pour cbc, l'encryptement et le déchiffrement sont très similaires, nous avons créé une fonction 
		
		chooseCBC (pour cbc normal) et chooseAdvancedCBC(pour cbc+xor) qui sont tous les deux dans le ficher Encrypt.java et prennent tous les deux une boolean false pour encrypter et true pour décrypter
		
2) Point important, dans le shell veuillez ne pas mettre d'espaces dans la clé. 
	Dans le shell, pour le décryptage de césar et xor par Brute Force, il y a la possibilité de trier les 256 possibilités automatiquement. (ceci fonctionne meme si le message original contient des points d'exclamations, points virgules ou autres)
	Le Shell permet d'encrypter un message en:
		-Caesar
		-Vigenere
		-XOR
		-One Time
		-CBC
	De plus, il est capable de décrypter certains cipher:
		-Caesar
			-Brute Force
				-avec ou sans l'aide du dictionnaire qui trouve la ligne qui contient le plus de mots valide
			-Frequencies
		-Vigenere
			-Frequencies
		-XOR
			-Brute Force
				-avec ou sans l'aide du dictionnaire qui trouve la ligne qui contient le plus de mots valide
		-CBC
			-le déchiffrement peut seulement etre effectué si l'utilisateur possède le pad
	Finalement, le Shell possède une section Help qui explique à l'utilisateur quels charactères il peut utiliser. De plus, le shell liste toutes ses capacités
	dans le cadre du chiffrement et du déchiffrement.
	
	A la fin de tous les chiffrements, déchiffrements et appels de "Help", l'utilisateur peut choisir d'arreter le shell.

3) Pour xor et caesar par force brute, dans les méthodes "testXor() et testCaesar()" qui se trouvent tous les deux après leur "check" respectives, nous avons mis en commentaire l'ajout du résultat dans un ficher créé dans le dossier /res
	
	Vous pouvez le décommenter si vous le voulez
	
	Nous nous sommes dit que ce serait cool de pouvoir trier et sélectionner la bonne réponse parmis les possibilités de la méthode brute.
	
	Donc après beaucoup de recherche nous avons importé un dictionaire contenant plus de 143'000 mots dans le dossier /res.
	
	Nous avons pensé à comparer les possibilités selon le nombres de mots contenu dans le dictionaire. (finalement ce dossier prenait trop de place donc nous l'importons 	      d'internet)
	
	La méthode :
	
		bruteSolution() se trouve en bas du fichier Decrypt.java, sa fonction est de prendre toutes le résultat de la méthode par force brute et de retourner la meilleure ligne.
	
	Aussi, pour pouvoir rechercher et trier les mots du dictionaire nous avons longuement cherché et nous avons décidé d'utiliser une hashtable (de taille 26*26) en relation avec une linked list.
	
	Donc tout fonctionne bien. !!! Attention à bien appeler la fonction load() (se situant dans Decrypt.java) avant d'utiliser bruteSolution() 
	
	À la base on voulait créer du code pour décrypter cbc, vigenere par méthode brut, mais on a réussi à trouver comment calculer le nombre de possibilité pour chaque longueur, mais pas comment itérer pour les 26^10 *25 + 26^9 *25 + 26^8 *25 + 26^7 25 + ... + 26^0 * 25; (Evidemment nos ordinateurs ne sont pas suffisament puissant)
	
Ce que nous aurrions fait si nous avions eu plus de temps:
	Nous avons eu l'idée de créer une mini-blockchain avec seulement quelques dizaines de blocs. L'idée était de nous servir de celle-ci pour encrypter des messages. 		Evidemment nous aurrions fait simple donc un bon ordinateur aurrait pu décrypter nos hash facilement. (Une difficulté d'environ 8 zéros, donc bien moins que les 		"grandes cryptos" comme le Bitcoin ou le Litcoin avec ses 450 000 zéros à résoudre!) Mais hypothétiquement, plus la chaine serait longue plus le déchiffrement 			serait difficile. 
		Malheureusementpar faute de temps nous n'avons pu terminer ce projet à temps, mais nous poursuivrons probablement dans notre temps libre.
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
			
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

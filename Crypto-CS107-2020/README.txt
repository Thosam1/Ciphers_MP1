Information importantes : comme le fichier zip fait 511ko, nous ne pouvions pas tout upload sur le site.
Par conséquent nous avons utilisé des liens directs  dropbox pour lire les fichiers (don't un dictionaire de 143'000 mots). À cause de cela, le programme ralenti un peu (peut-être est-ce pour télécharger ou accéder ?) lors de l'utilisation de la fonctionalité de tri pour la méthode brute de césar et xor.
Nous avons essayer avec google drive sans succès car le programme bloque à <DOCTYPE> et nous ne pouvons lire le fichier directement.
Dans la fonction load() dans Decrypt.java, il y a la lecture de ces fichiers originales en commentaire, indiqué par !!! 


Informations sur le fichier main :

En haut : SHELL

!!! utiliser le dossier res pour acceder utiliser les fonctions tests // dans main

Dans la fonction Main :

Nous avons mis les fonctions en commentaires pour les vérifiers, décommenter et lancer le programme.
	!!!le projet faisait 105ko donc nous n'avons pas les fichiers challenge-encrypted.txt, text_one.txt, text_two.txt et text_three.txt
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
	Dans le shell, pour le décryptage de césar et xor, il y a la possibilité de trier les 256 possibilités automatiquement. (ceci fonctionne meme si le message original contient des points d'exclamations, points virgules ou autres)

3) Pour xor et caesar par force brute, dans les méthodes "testXor() et testCaesar()" qui se trouvent tous les deux après leur "check" respectives, nous avons mis en commentaire l'ajout du résultat dans un ficher créé dans le dossier /res
	
	Vous pouvez le décommenter si vous le voulez
	
	Nous nous sommes dit que ce serait cool de pouvoir trier et sélectionner la bonne réponse parmis les possibilités de la méthode brute.
	
	Donc après beaucoup de recherche nous avons nous avons importé un dictionaire contenant plus de 143'000 mots dans le dossier /res.
	
	Nous avons pensé à comparer les possibilités selon le nombres de mots contenu dans le dictionaire. 
	
	La méthode :
	
		bruteSolution() se trouve en bas du fichier Decrypt.java, sa fonction est de prendre toutes le résultat de la méthode par force brute et de retourner la meilleure ligne.
	
	Aussi, pour pouvoir rechercher et trier les mots du dictionaire nous avons longuement cherché et nous avons décidé d'utiliser une hashtable (de taille 26*26) en relation avec une linked list.
	
	Donc tout fonctionne bien. !!! Attention à bien appeler la fonction load() (se situant dans Decrypt.java) avant d'utiliser bruteSolution() 
	
	À la base on voulait créer du code pour décrypter cbc, vigenere par méthode brut, mais on a réussi à trouver comment calculer le nombre de possibilité pour chaque longueur, mais pas comment itérer pour les 26^10 *25 + 26^9 *25 + 26^8 *25 + 26^7 25 + ... + 26^0 * 25;
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
			
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		

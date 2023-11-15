package fr.istic.prg1.list;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import fr.istic.prg1.list_util.Comparison;
import fr.istic.prg1.list_util.Iterator;
//import fr.istic.prg1.list_util.List;
import fr.istic.prg1.list_util.SmallSet;

/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 5.0
 * @since 2022-09-23
 */

/**
 * Tp réalisé par :
 * @author Thomas MESSMER <thomas.messmer@etudiant.univ-rennes.fr>
 */

public class MySet extends List<SubSet> {

	/**
	 * Borne superieure pour les rangs des sous-ensembles.
	 */
	private static final int MAX_RANG = 128;
	private static final int SMALL_SET_SIZE = 256;
	private static final String NEW_VALUE = " nouveau contenu :";

	/**
	 * Sous-ensemble de rang maximal à mettre dans le drapeau de la liste.
	 */
	private static final SubSet FLAG_VALUE = new SubSet(MAX_RANG, new SmallSet());
	/**
	 * Entrée standard.
	 */
	private static final Scanner standardInput = new Scanner(System.in);

	public MySet() {
		super();
		setFlag(FLAG_VALUE);
	}

	/**
	 * Fermer tout (actuellement juste l'entrée standard).
	 */
	public static void closeAll() {
		standardInput.close();
	}

	private static Comparison compare(int a, int b) {
		if (a < b) {
			return Comparison.INF;
		} else if (a == b) {
			return Comparison.EGAL;
		} else {
			return Comparison.SUP;
		}
	}

	public void print() {
		System.out.println(" [version corrigee de contenu]");
		this.print(System.out);
	}

	// //////////////////////////////////////////////////////////////////////////////
	// //////////// Appartenance, Ajout, Suppression, Cardinal
	// ////////////////////
	// //////////////////////////////////////////////////////////////////////////////

	/**
	 * @return true si le nombre saisi par l'utilisateur appartient à this, false
	 *         sinon
	 */
	public boolean contains() {
		return contains(standardInput.nextInt());
	}

	/**
	 * @param value valeur à tester
	 * @return true si valeur appartient à l'ensemble, false sinon
	 */

	public boolean contains(int value) {
		int rank = value/SMALL_SET_SIZE;
		if (rank >= 0 && rank < MAX_RANG) {
			Iterator<SubSet> it = this.iterator();
			SubSet subSet = it.getValue();

			while(subSet.rank < rank)
				subSet = it.nextValue();

			return subSet.rank == rank && subSet.set.contains(value%SMALL_SET_SIZE);
		}
		else return false;
	}

	/**
	 * Ajouter à this toutes les valeurs saisis par l'utilisateur et afficher le
	 * nouveau contenu.
	 */
	public void add() {
		System.out.println(" valeurs a ajouter (-1 pour finir) : ");
		this.add(System.in);
		System.out.println(NEW_VALUE);
		this.printNewState();
	}

	/**
	 * Ajouter à this toutes les valeurs prises dans is.
	 * 
	 * @param is flux d'entrée.
	 */
	public void add(InputStream is){
		Scanner scanner = new Scanner(is);
		int value = scanner.nextInt();
		while (value != -1){
			this.addNumber(value);
			value = scanner.nextInt();
		}
	}

	/**
	 * Ajouter element à this,
	 *
	 * @param value valuer à ajouter.
	 */
	public void addNumber(int value) {
		int rank = value/SMALL_SET_SIZE;
		if (rank >= 0 && rank < MAX_RANG) {
			Iterator<SubSet> it = this.iterator();
			SubSet subSet = it.getValue();

			while(subSet.rank < rank)
				subSet = it.nextValue();

			if(subSet.rank == rank)
				subSet.set.add(value%SMALL_SET_SIZE);
			else{
				subSet = new SubSet(rank, new SmallSet());
				subSet.set.add(value%SMALL_SET_SIZE);
				it.addLeft(subSet);
			}
		}
	}

	/**
	 * Supprimer de this toutes les valeurs saisies par l'utilisateur et afficher le
	 * nouveau contenu.
	 */
	public void remove() {
		System.out.println("  valeurs a supprimer (-1 pour finir) : ");
		this.remove(System.in);
		System.out.println(NEW_VALUE);
		this.printNewState();
	}

	/**
	 * Supprimer de this toutes les valeurs prises dans is.
	 * 
	 * @param is flux d'entrée
	 */
	public void remove(InputStream is) {
		Scanner scanner = new Scanner(is);
		int value = scanner.nextInt();
		while (value != -1){
			this.removeNumber(value);
			value = scanner.nextInt();
		}
	}

	/**
	 * Supprimer element de this.
	 * 
	 * @param value valeur à supprimer
	 */
	public void removeNumber(int value) {
		int rank = value/SMALL_SET_SIZE;
		if (rank >= 0 && rank < MAX_RANG) {
			Iterator<SubSet> it = this.iterator();
			SubSet subSet = it.getValue();

			while(subSet.rank < rank)
				subSet = it.nextValue();

			if(subSet.rank == rank) {
				subSet.set.remove(value % SMALL_SET_SIZE);
				if(subSet.set.isEmpty())
					it.remove();
			}
		}
	}

	/**
	 * @return taille de l'ensemble this
	 */
	public int size() {
		Iterator<SubSet> it = this.iterator();
		int rep = 0;

		while (!it.isOnFlag()){
			rep += it.getValue().set.size();
			it.goForward();
		}
		return rep;
	}

	// /////////////////////////////////////////////////////////////////////////////
	// /////// Difference, DifferenceSymetrique, Intersection, Union ///////
	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * This devient la différence de this et set2.
	 * 
	 * @param set2 deuxième ensemble
	 */
	public void difference(MySet set2) {
		if(this == set2){
			this.clear();
		}
		else {
			Iterator<SubSet> it1 = this.iterator();
			Iterator<SubSet> it2 = set2.iterator();
			while (!it1.isOnFlag() && !it2.isOnFlag()) {
				SubSet sub1 = it1.getValue();
				SubSet sub2 = it2.getValue();
				if (sub1.rank > sub2.rank) {
					it2.goForward();
				} else if (sub1.rank < sub2.rank) {
					it1.goForward();
				} else {
					sub1.set.difference(sub2.set);
					if (sub1.set.isEmpty()) it1.remove();
					else it1.goForward();
					it2.goForward();
				}
			}
		}
	}

	/**
	 * This devient la différence symétrique de this et set2.
	 * 
	 * @param set2 deuxième ensemble
	 */
	public void symmetricDifference(MySet set2) {
		if(this == set2){
			this.clear();
		}
		else {
			Iterator<SubSet> it1 = this.iterator();
			Iterator<SubSet> it2 = set2.iterator();
			while (!it2.isOnFlag()) {
				SubSet sub1 = it1.getValue();
				SubSet sub2 = it2.getValue();
				if (sub1.rank > sub2.rank) {
					it1.addLeft(sub2.copyOf());
					it2.goForward();
				} else if (sub1.rank < sub2.rank) {
					it1.goForward();
				} else {
					sub1.set.symmetricDifference(sub2.set);
					if (sub1.set.isEmpty()) it1.remove();
					else it1.goForward();
					it2.goForward();
				}
			}
		}
	}

	/**
	 * This devient l'intersection de this et set2.
	 * 
	 * @param set2 deuxième ensemble
	 */
	public void intersection(MySet set2) {
		if(this != set2) {
			Iterator<SubSet> it1 = this.iterator();
			Iterator<SubSet> it2 = set2.iterator();
			while(!it1.isOnFlag()){
				SubSet sub1 = it1.getValue();
				SubSet sub2 = it2.getValue();
				if (sub1.rank > sub2.rank) {
					it2.goForward();
				} else if (sub1.rank < sub2.rank) {
					it1.remove();
				} else {
					sub1.set.intersection(sub2.set);
					if(sub1.set.isEmpty())it1.remove();
					else it1.goForward();
					it2.goForward();
				}
			}
		}
	}

	/**
	 * This devient l'union de this et set2.
	 * 
	 * @param set2 deuxième ensemble
	 */
	public void union(MySet set2) {
		if(this != set2) {
			Iterator<SubSet> it1 = this.iterator();
			Iterator<SubSet> it2 = set2.iterator();
			while (!it2.isOnFlag()) {
				SubSet sub1 = it1.getValue();
				SubSet sub2 = it2.getValue();
				if (sub1.rank > sub2.rank) {
					it1.addLeft(sub2.copyOf());
					it2.goForward();
				} else if (sub1.rank < sub2.rank) {
					it1.goForward();
				} else {
					sub1.set.union(sub2.set);
					it1.goForward();
					it2.goForward();
				}
			}
		}
	}

	// /////////////////////////////////////////////////////////////////////////////
	// /////////////////// Egalite, Inclusion ////////////////////
	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * @param o deuxième ensemble
	 * 
	 * @return true si les ensembles this et o sont égaux, false sinon
	 */
	@Override
	public boolean equals(Object o) {
		boolean b = true;
		if (this == o) {
			b = true;
		} else if (!(o instanceof MySet)) {
			b = false;
		} else {
			MySet set2 = (MySet)o;

			Iterator<SubSet> it1 = this.iterator();
			Iterator<SubSet> it2 = set2.iterator();

			while (!it2.isOnFlag() && b){
				SubSet sub1 = it1.getValue();
				SubSet sub2 = it2.getValue();
				if (sub1.rank != sub2.rank)
					b = false;
				else {
					b = sub1.set.equals(sub2.set);
					it1.goForward();
					it2.goForward();
				}
			}
			if(b) b = it1.isOnFlag();
		}
		return b;
	}

	/**
	 * @param set2 deuxième ensemble
	 * @return true si this est inclus dans set2, false sinon
	 */
	public boolean isIncludedIn(MySet set2) {
		if(this == set2)
			return true;
		else {
			Iterator<SubSet> it1 = this.iterator();
			Iterator<SubSet> it2 = set2.iterator();
			boolean rep = true;

			while ((!it1.isOnFlag() || !it2.isOnFlag()) && rep) {
				SubSet sub1 = it1.getValue();
				SubSet sub2 = it2.getValue();
				if (sub1.rank > sub2.rank) {
					it2.goForward();
				} else if (sub1.rank < sub2.rank) {
					rep = false;
				} else {
					rep = sub1.set.isIncludedIn(sub2.set);
					it1.goForward();
					it2.goForward();
				}
			}
			return rep;
		}
	}

	// /////////////////////////////////////////////////////////////////////////////
	// //////// Rangs, Restauration, Sauvegarde, Affichage //////////////
	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * Afficher les rangs présents dans this.
	 */
	public void printRanks() {
		System.out.println(" [version corrigee de rangs]");
		this.printRanksAux();
	}

	private void printRanksAux() {
		int count = 0;
		Iterator<SubSet> it = this.iterator();
		StringBuilder line = new StringBuilder("Rangs presents : ");
		while (!it.isOnFlag()) {
			line.append(it.getValue().rank + "  ");
			count = count + 1;
			if (count == 10) {
				line.append("\n");
				count = 0;
			}
			it.goForward();

		}
		System.out.println(line.toString());
		if (count > 0) {
			System.out.println("\n");
		}
	}

	/**
	 * Créer this à partir d’un fichier choisi par l’utilisateur contenant une
	 * séquence d’entiers positifs terminée par -1 (cf f0.ens, f1.ens, f2.ens,
	 * f3.ens et f4.ens).
	 */
	public void restore() {
		String fileName = readFileName();
		InputStream inFile;
		try {
			inFile = new FileInputStream(fileName);
			System.out.println(" [version corrigee de restauration]");
			this.clear();
			this.add(inFile);
			inFile.close();
			System.out.println(NEW_VALUE);
			this.printNewState();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("fichier " + fileName + " inexistant");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("probleme de fermeture du fichier " + fileName);
		}
	}

	/**
	 * Sauvegarder this dans un fichier d’entiers positifs terminé par -1.
	 */
	public void save() {
		System.out.println(" [version corrigee de sauvegarde]");
		OutputStream outFile;
		try {
			outFile = new FileOutputStream(readFileName());
			this.print(outFile);
			outFile.write("-1\n".getBytes());
			outFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("pb ouverture fichier lors de la sauvegarde");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("probleme de fermeture du fichier");
		}
	}

	/**
	 * @return l'ensemble this sous forme de chaîne de caractères.
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		int count = 0;
		SubSet subSet;
		int startValue;
		Iterator<SubSet> it = this.iterator();
		while (!it.isOnFlag()) {
			subSet = it.getValue();
			startValue = subSet.rank * 256;
			for (int i = 0; i < 256; ++i) {
				if (subSet.set.contains(i)) {
					StringBuilder number = new StringBuilder(String.valueOf(startValue + i));
					int numberLength = number.length();
					for (int j = 6; j > numberLength; --j) {
						number.append(" ");
					}
					result.append(number);
					++count;
					if (count == 10) {
						result.append("\n");
						count = 0;
					}
				}
			}
			it.goForward();
		}
		if (count > 0) {
			result.append("\n");
		}
		return result.toString();
	}

	/**
	 * Imprimer this dans outFile.
	 *
	 * @param outFile flux de sortie
	 */
	private void print(OutputStream outFile) {
		try {
			String string = this.toString();
			outFile.write(string.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Afficher l'ensemble avec sa taille et les rangs présents.
	 */
	private void printNewState() {
		this.print(System.out);
		int size = this.size();
		System.out.println("Nombre d'elements : " + size);
		this.printRanksAux();
	}

	/**
	 * @param scanner
	 * @param min     valeur minimale possible
	 * @return l'entier lu au clavier (doit être entre min et 32767)
	 */
	private static int readValue(Scanner scanner, int min) {
		int value = scanner.nextInt();
		while (value < min || value > 32767) {
			System.out.println("valeur incorrecte");
			value = scanner.nextInt();
		}
		return value;
	}

	/**
	 * @return nom de fichier saisi psar l'utilisateur
	 */
	private static String readFileName() {
		System.out.println(" nom du fichier : ");
		return standardInput.next();
	}

}
package fr.istic.prg1.list;

import java.util.Arrays;

/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @version 5.0
 * @since 2022-09-23
 */

/**
 * Tp réalisé par :
 * @author Thomas MESSMER <thomas.messmer@etudiant.univ-rennes.fr>
 */

public class SmallSet {

	/**
	 * taille de l'ensemble
	 */
	private static final int SET_SIZE = 256;

	/**
	 * Ensemble est représenté comme un tableau de booléens.
	 */
	private boolean[] tab = new boolean[SET_SIZE];

	public SmallSet() {
		for (int i = 0; i < SET_SIZE; i++)
			tab[i] = false;
	}

	public SmallSet(boolean[] array) {
		tab = Arrays.copyOf(array, SET_SIZE);
	}

	/**
	 * @return nombre de valeurs appartenant à l’ensemble
	 */
	public int size() {
		int rep = 0;
		for (int i = 0; i < SET_SIZE; i++) {
			if(tab[i])rep++;
		}
		return rep;
	}

	/**
	 * @param x valeur à tester
	 * @return true, si l’entier x appartient à l’ensemble, false sinon
	 */
	public boolean contains(int x) {
		return tab[x];
	}

	/**
	 * @return true, si l’ensemble est vide, false sinon
	 */
	public boolean isEmpty() {
		for (int i = 0; i < SET_SIZE; i++) {
			if(tab[i])return false;
		}
		return true;
	}

	/**
	 * Ajoute x à l’ensemble (sans effet si x déjà présent)
	 *
	 * @param x valeur à ajouter
	 * @pre 0 <= x <= 255
	 */
	public void add(int x) {
		tab[x] = true;
	}

	/**
	 * Retire x de l’ensemble (sans effet si x n’est pas présent)
	 *
	 * @param x valeur à supprimer
	 * @pre 0 <= x <= 255
	 */
	public void remove(int x) {
		tab[x] = false;
	}

	/**
	 * Ajoute à l’ensemble les valeurs deb, deb+1, deb+2, ..., fin.
	 *
	 * @param deb début de l’intervalle
	 * @param fin   fin de l’intervalle
	 * @pre 0 <= begin <= end <= 255
	 */
	public void addInterval(int deb, int fin) {
		for (int i = deb; i < fin; i++)
			tab[i] = true;
	}

	/**
	 * Retire de l’ensemble les valeurs deb, deb+1, deb+2, ..., fin.
	 *
	 * @param deb début de l’intervalle
	 * @param fin   fin de l’intervalle
	 * @pre 0 <= begin <= end <= 255
	 */
	public void removeInterval(int deb, int fin) {
		for (int i = deb; i < fin; i++)
			tab[i] = false;
	}

	/**
	 * this devient l'union de this et set2
	 * 
	 * @param set2 deuxième ensemble
	 */
	public void union(SmallSet set2) {
		for (int i = 0; i < SET_SIZE; i++)
			tab[i] = tab[i] || set2.tab[i];
	}

	/**
	 * this devient l'intersection de this et set2
	 * 
	 * @param set2 deuxième ensemble
	 */
	public void intersection(SmallSet set2) {
		for (int i = 0; i < SET_SIZE; i++)
			tab[i] = tab[i] && set2.tab[i];
	}

	/**
	 * this devient la différence de this et set2
	 * 
	 * @param set2 deuxième ensemble
	 */
	public void difference(SmallSet set2) {
		for (int i = 0; i < SET_SIZE; i++)
			tab[i] = tab[i] && !set2.tab[i];
	}

	/**
	 * this devient la différence symétrique de this et set2
	 * 
	 * @param set2 deuxième ensemble
	 */
	public void symmetricDifference(SmallSet set2) {
		for (int i = 0; i < SET_SIZE; i++)
			tab[i] = tab[i] && !set2.tab[i] || !tab[i] && set2.tab[i];
	}

	/**
	 * this devient le complément de this.
	 */
	public void complement() {
		for (int i = 0; i < SET_SIZE; i++)
			tab[i] = !tab[i];
	}

	/**
	 * this devient l'ensemble vide.
	 */
	public void clear() {
		for (int i = 0; i < SET_SIZE; i++)
			tab[i] = false;
	}

	/**
	 * @param set2 second ensemble
	 * @return true, si this est inclus dans set2, false sinon
	 */
	public boolean isIncludedIn(SmallSet set2) {
		for (int i = 0; i < SET_SIZE; i++) {
			if (tab[i] && !set2.tab[i]) return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		// pour respecter les bonnes pratiques
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(tab);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		SmallSet testSet = (SmallSet) obj;
		for (int i = 0; i < SET_SIZE; i++) {
			if(tab[i] != testSet.tab[i])return false;
		}
		return true;
	}

	/**
	 * @return copie de this
	 */
	public SmallSet copyOf() {
		return new SmallSet(tab);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("elements presents : ");
		for (int i = 0; i < SET_SIZE; ++i) {
			if (tab[i]) {
				result.append(i + " ");
			}
		}
		return result.toString();
	}
}

package fr.istic.prg1.list;

import fr.istic.prg1.list_util.Iterator;
import fr.istic.prg1.list_util.SuperT;

import java.util.ArrayList;

/**
 * Tp réalisé par :
 * @author Thomas MESSMER <thomas.messmer@etudiant.univ-rennes.fr>
 */

public class List<T extends SuperT> {
	// liste en double chainage par references

	private class Element {
		// element de List<Item> : (Item, Element, Element)
		public T value;
		public Element left, right;

		public Element() {
			value = null;
			left = null;
			right = null;
		}
	} // class Element

	public class ListIterator implements Iterator<T> {
		private Element current;

		private ListIterator() {
			current = flag.right;
		}

		@Override
		public void goForward() {current = current.right;}

		@Override
		public void goBackward() {current = current.left;}

		@Override
		public void restart() {
			current = flag.right;
		}

		@Override
	        public boolean isOnFlag() { return current == flag; }

		@Override
		public void remove() {
			try {
				assert current != flag : "\n\n\nimpossible de retirer le drapeau\n\n\n";
			} catch (AssertionError e) {
				e.printStackTrace();
				System.exit(0);
			}
			current.left.right = current.right;
			current.right.left = current.left;
			current = current.right;
		}

		@Override		 
		public T getValue() { return current.value; }

		@Override
	        public T nextValue() {
			current = current.right;
			return current.value;
		}

		@Override
		public void addLeft(T v) {
			Element newElement = new Element();
			newElement.value = v;

			newElement.left = current.left;
			newElement.right = current;
			current.left.right = newElement;
			current.left = newElement;

			current = newElement;
		}

		@Override
		public void addRight(T v) {
			Element newElement = new Element();
			newElement.value = v;

			newElement.left = current;
			newElement.right = current.right;
			current.right.left = newElement;
			current.right = newElement;

			current = newElement;
		}

		@Override
		public void setValue(T v) { current.value = v;}

		@Override
		public void selfDestroy() {
			current = flag;
			flag.left = flag;
			flag.right = flag;
		}

		@Override
		public String toString() {
			return "parcours de liste : pas d'affichage possible \n";
		}

	} // class IterateurListe

	private Element flag;

	private ArrayList<ListIterator> itList = new ArrayList<>();
    
	public List() {
		flag = new Element();
		clear();
	}

	public ListIterator iterator() { return new ListIterator(); }

	public boolean isEmpty() { return flag.right.equals(flag) && flag.left.equals(flag); }

	public void clear() {
		flag.right = flag;
		flag.left = flag;
	}

	public void setFlag(T v) {
		flag.value = v;
	}

	public void addHead(T v) {
		Element newElement = new Element();
		newElement.value = v;

		newElement.right = flag.right;
		newElement.left = flag;
		flag.right.left = newElement;
		flag.right = newElement;
	}

	public void addTail(T v) {
		Element newElement = new Element();
		newElement.value = v;

		newElement.left = flag.left;
		newElement.right = flag;
		flag.left.right = newElement;
		flag.left = newElement;
	}

	@SuppressWarnings("unchecked")
	public List<T> clone() {
		List<T> nouvListe = new List<T>();
		ListIterator p = iterator();
		while (!p.isOnFlag()) {
			nouvListe.addTail((T) p.getValue().copyOf());
			// UNE COPIE EST NECESSAIRE !!!
			p.goForward();
		}
		p.selfDestroy();
		return nouvListe;
	}

	@Override
	public String toString() {
		String s = "contenu de la liste : \n";
		ListIterator p = iterator();
		while (!p.isOnFlag()) {
			s = s + p.getValue().toString() + " ";
			p.goForward();
		}
		p.selfDestroy();
		return s;
	}
}

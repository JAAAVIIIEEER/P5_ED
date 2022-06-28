package ule.edi.tree;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * arbol binario de busqueda (binary search tree, BST).
 * 
 * El codigo fuente esta en UTF-8, y la constante EMPTY_TREE_MARK definida en
 * AbstractTreeADT del proyecto API deberia ser el simbolo de conjunto vacio:
 * ∅
 * 
 * Si aparecen caracteres "raros", es porque el proyecto no esta bien
 * configurado en Eclipse para usar esa codificacion de caracteres.
 *
 * En el toString() que esta ya implementado en AbstractTreeADT se usa el
 * formato:
 * 
 * Un arbol vaci­o se representa como "∅". Un Ã¡rbol no vacio como
 * "{(informacion rai­z), sub-arbol 1, sub-arbol 2, ...}".
 * 
 * Por ejemplo, {A, {B, ∅, ∅}, ∅} es un arbol binario con rai­z "A" y un
 * unico sub-arbol, a su izquierda, con rai­z "B".
 * 
 * El metodo render() tambien representa un arbol, pero con otro formato; por
 * ejemplo, un arbol {M, {E, ∅, ∅}, {S, ∅, ∅}} se muestra como:
 * 
 * M 
 * | E
 * | | ∅
 * | | ∅ 
 * | S 
 * | | ∅
 * | | ∅
 * 
 * Cualquier nodo puede llevar asociados pares (clave,valor) para adjuntar
 * informacion extra. Si es el caso, tanto toString() como render() mostraran
 * los pares asociados a cada nodo.
 * 
 * Con {@link #setTag(String, Object)} se inserta un par (clave,valor) y con
 * {@link #getTag(String)} se consulta.
 * 
 * 
 * Con <T extends Comparable<? super T>> se pide que exista un orden en los
 * elementos. Se necesita para poder comparar elementos al insertar.
 * 
 * Si se usara <T extends Comparable<T>> seria muy restrictivo; en su lugar se
 * permiten tipos que sean comparables no solo con exactamente T sino tambien
 * con tipos por encima de T en la herencia.
 * 
 * @param <T> tipo de la informacion en cada nodo, comparable.
 */
public class BinarySearchTreeImpl<T extends Comparable<? super T>> extends AbstractBinaryTreeADT<T> {

	BinarySearchTreeImpl<T> father; // referencia a su nodo padre)
	int count;  // contador de instancias 

	/**
	 * Devuelve el arbol binario de busqueda izquierdo.
	 */
	protected BinarySearchTreeImpl<T> getLeftBST() {
		// El atributo leftSubtree es de tipo AbstractBinaryTreeADT<T> pero
		// aqui­ se sabe que es ademas BST (binario de busqueda)
		//
		return (BinarySearchTreeImpl<T>) leftSubtree;
	}

	protected void setLeftBST(BinarySearchTreeImpl<T> left) {
		this.leftSubtree = left;
	}

	/**
	 * Devuelve el arbol binario de busqueda derecho.
     */
	protected BinarySearchTreeImpl<T> getRightBST() {
		return (BinarySearchTreeImpl<T>) rightSubtree;
	}

	protected void setRightBST(BinarySearchTreeImpl<T> right) {
		this.rightSubtree = right;
	}

	/**
	 * arbol BST vaci­o
	 */
	public BinarySearchTreeImpl() {
		this.leftSubtree = null;
		this.rightSubtree = null;
		this.father =  null;
		this.count = 1;
	}

	public BinarySearchTreeImpl(BinarySearchTreeImpl<T> father) {
		this.father = father;
		this.rightSubtree = null;
		this.leftSubtree = null;
		this.count = 1;

	}

	private BinarySearchTreeImpl<T> emptyBST(BinarySearchTreeImpl<T> father) {
		//Devuelve un nodo vacío
		return new BinarySearchTreeImpl<T>(father);
	}

	
	
	/**
	 * Inserta los elementos que no sean null, de una coleccion en el arbol. 
	 * (si alguno es 'null', no lo inserta)
	 * 
	 * No se permiten elementos null.
	 * 
	 * @param elements valores a insertar.
	 * @return numero de elementos insertados en el arbol (elementos diferentes de null)
	 */
	public int insert(Collection<T> elements) {
		int cont = 0;
		
		for (T elemento: elements) {
			if (elemento != null) {
				cont++;
				this.insert(elemento);	
			}
		}
		return cont;
	}

	/**
	 * Inserta los elementos que no sean null, de un array en el arbol. 
	 * (si alguno es 'null', no lo inserta)
	 * 
	 * No se permiten elementos null.
	 * 
	 * @param elements elementos a insertar.
	 * @return  	numero de elementos insertados en el arbol (elementos diferentes de null)
	 */
	public int insert(@SuppressWarnings("unchecked") T... elements) {
		int cont = 0;
		
		for (T elemento: elements) {
			if (elemento != null) {
				cont++;
				this.insert(elemento);	
			}
		}
		return cont;
	}

	/**
	 * Inserta (como hoja) un nuevo elemento en el arbol de busqueda.
	 * 
	 * Debe asignarse valor a su atributo father (referencia a su nodo padre o null
	 * si es la rai­z)
	 * 
	 * No se permiten elementos null. Si element es null dispara excepcion:IllegalArgumentException 
	 * Si el elemento ya existe en el arbol
	 *  no inserta un nodo nuevo, sino que incrementa el atributo count del nodo que tiene igual contenido.
	 * 
	 * @param element valor a insertar.
	 * @return true si se insertó en un nuevo nodo (no existia ese elemento en el arbol),
	 *         false en caso contrario
	 * @throws IllegalArgumentException si element es null
	 */
	public boolean insert(T element) {
		if (element == null) {
			throw new IllegalArgumentException();
		}
		if (this.contains(element)) {
			this.getElem(element).count++; 
			return false;
		} else {
			if (this.isEmpty()) {
				this.setContent(element);
				this.leftSubtree = new BinarySearchTreeImpl<T>(this);
				this.rightSubtree = new BinarySearchTreeImpl<T>(this);
				return true;
			} else {
				if (this.content.compareTo(element) > 0) {
					return this.getLeftBST().insert(element);
				} else {
					return this.getRightBST().insert(element); 
				}
			}
		}
	}

	/**
	 * Busca el elemento en el arbol.
	 * 
	 * No se permiten elementos null.
	 * 
	 * @param element valor a buscar.
	 * @return true si el elemento esta en el arbol, false en caso contrario
	 * @throws IllegalArgumentException si element es null
	 *
	 */
	public boolean contains(T element) {
		if (element == null) 
			throw new IllegalArgumentException();
		
		if (!isEmpty()) {
			if (element.equals(this.content)) {
				return true;
			} else {
				if (this.content.compareTo(element) > 0){
					return this.getLeftBST().contains(element);
				} else {
					return this.getRightBST().contains(element);
				}
			}
		} else {
			return false;
		}
	}
	
	/**
	 *  devuelve la cadena formada por el contenido del árbol teniendo en cuenta que 
	 *  si un nodo tiene su atributo count>1 pone entre paréntesis su valor justo detrás del atributo elem
	 *  También debe mostrar las etiquetas que tenga el nodo (si las tiene)
	 *  
	 *  CONSEJO: REVISAR LA IMPLEMENTACIÓN DE TOSTRING DE LA CLASE AbstractTreeADT 
	 * 
	 * Por ejemplo: {M, {E(2), ∅, ∅}, {K(5), ∅, ∅}}
	 * 
	 * @return cadena con el contenido del árbol incluyendo su atributo count entre paréntesis si elemento tiene más de 1 instancia
	 */
	public String toString() {
		if (! isEmpty()) {
			//	Construye el resultado de forma eficiente
			StringBuffer result = new StringBuffer();
				
			//	Raíz
			result.append("{" + content.toString());
			if(this.count > 1) {
				result.append("(" + this.count + ")");
			}
			
			if (! tags.isEmpty()) {
				result.append(" [");
				
				List<String> sk = new LinkedList<String>(tags.keySet());
				
				Collections.sort(sk);
				for (String k : sk) {
					result.append("(" + k + ", " + tags.get(k) + "), ");
				}
				result.delete(result.length() - 2, result.length());
				result.append("]");
			}
			
			//	Y cada sub-árbol
			for (int i = 0; i < getMaxDegree(); i++) {
				result.append(", " + getSubtree(i).toString());
			}
			//	Cierra la "}" de este árbol
			result.append("}");
			
			return result.toString();
		} else {
			return AbstractTreeADT.EMPTY_TREE_MARK;
		}
	}

		/**
	 * Importante: Solamente se puede recorrer el arbol una vez
	 * 
	 * Etiqueta cada nodo hoja con la etiqueta "height" y el valor correspondiente a la
	 * altura del nodo.
	 * 
	 * Por ejemplo, sea un arbol "A":
	 * 
	 * {10, {5, {2, ∅, ∅}, ∅}, {20, {15, ∅, ∅}, {30, ∅, ∅}}}
	 * 
	 * 10
	 * | 5 
	 * | | 2
	 * | | | ∅ 
	 * | | | ∅ 
	 * | | ∅ 
	 * | 20 
	 * | | 15 
	 * | | | ∅
	 * | | | ∅ 
	 * | | 30 
	 * | | | ∅ 
	 * | | | ∅
	 * 
	 * 
	 * el arbol quedara etiquetado:
	 * 
	 * {10, 
	 * {5, {2 [(height, 3)],∅, ∅}, ∅}, 
	 * {20,{15, {12 [(height, 4)], ∅, ∅}, ∅}, ∅}
	 * }
	 * 
	 */
	public void tagHeightLeaf() {
		if(!this.isEmpty()) {
			if(this.isLeaf()) {
				this.setTag("height", this.getHeight(1));
			} else {
				this.getLeftBST().tagHeightLeaf();
				this.getRightBST().tagHeightLeaf();			
			}
		}
	}
	
	
	/**
	 * Devuelve un iterador que recorre los elementos (sin tener en cuenta el número de instancias)del arbol por niveles segun
	 * el recorrido en anchura
	 * 
	 * Por ejemplo, con el arbol
	 * 
	 * {50, {30(2), {10, ∅, ∅}, {40, ∅, ∅}}, {80(2), {60, ∅, ∅}, ∅}}
	 * 
	 * y devolvera el iterador que recorrera los nodos en el orden: 50, 30, 80, 10, 40, 60
	 * 
	 * 
	 * 
	 * @return iterador para el recorrido en anchura
	 */
    public Iterator<T> iteratorWidth() {
    	LinkedList<T> results = new LinkedList<T>();
    	results = iteratorWidthAuxiliar (this, results);
		return results.iterator();
	}

	private LinkedList<T> iteratorWidthAuxiliar (BinarySearchTreeImpl<T> nodoActual, LinkedList<T> resultados) {
		LinkedList<BinarySearchTreeImpl<T>> cola = new LinkedList<BinarySearchTreeImpl<T>>();
		cola.addLast(nodoActual);
		
		while(!cola.isEmpty()) {
			BinarySearchTreeImpl<T> actual = cola.poll();
			if (actual.content!=null) {
				resultados.addLast(actual.content);	
			}

			if (actual.getLeftBST()!= null) {
				cola.addLast(actual.getLeftBST());
			}
			if (actual.getRightBST() != null) {
				cola.addLast(actual.getRightBST());
			}
		}
		
		return resultados;
	}

	/**
	 * Devuelve un iterador que recorre los elementos (teniendo en cuenta el número de instancias)del arbol por niveles segun
	 * el recorrido en anchura
	 * 
	 * Por ejemplo, con el arbol
	 * 
	 * {50, {30(2), {10, ∅, ∅}, {40, ∅, ∅}}, {80(2), {60, ∅, ∅}, ∅}}
	 * 
	 * y devolvera el iterador que recorrera los nodos en el orden: 50, 30, 30, 80, 80, 10, 40, 60
	 *  
	 * @return iterador para el recorrido en anchura
	 */
     public Iterator<T> iteratorWidthInstances() {
     	LinkedList<T> resultados = new LinkedList<T>();
     	BinarySearchTreeImpl<T> nodoActual = this;
     	
     	LinkedList<BinarySearchTreeImpl<T>> cola = new LinkedList<BinarySearchTreeImpl<T>>();
		cola.addLast(nodoActual);
		
		while(!cola.isEmpty()) {
			BinarySearchTreeImpl<T> actual = cola.poll();
			if (actual.content!=null) {
				for(int i = 1; i <= actual.count; i++)
					resultados.addLast(actual.content);	
			}

			if (actual.getLeftBST()!= null) {
				cola.addLast(actual.getLeftBST());
			}
			if (actual.getRightBST() != null) {
				cola.addLast(actual.getRightBST());
			}
		}
		return resultados.iterator();
	 }
	
		
	/**
	 * Cuenta el número de elementos diferentes del arbol (no tiene en cuenta las instancias)
	 * 
	 * Por ejemplo, con el arbol
	 * 
	 * {50, {30(2), {10, ∅, ∅}, {40(4), ∅, ∅}}, {80(2), {60, ∅, ∅}, ∅}}
	 * 
	 * la llamada a ejemplo.instancesCount() devolvera 6
	 * 
	 * @return el numero de elementos diferentes del arbol 
	 */
    public int size() {
		if(!this.isEmpty()) {
			return 1 + getLeftBST().size()+ getRightBST().size();
		} else {
			return 0;
		}
	}
	
    /**
	 * Cuenta el número de instancias de elementos diferentes del arbol 
	 * 
	 * Por ejemplo, con el arbol ejemplo=
	 * 
	 * {50, {30(2), {10, ∅, ∅}, {40(4), ∅, ∅}}, {80(2), {60, ∅, ∅}, ∅}}
	 * 
	 * la llamada a ejemplo.instancesCount() devolvera 11
	 * 
	 * @return el número de instancias de elementos del arbol 
	 */
	public int instancesCount() {
		if(!this.isEmpty()) {
			return this.count + getLeftBST().instancesCount()+ getRightBST().instancesCount();
		} else {
			return 0;
		}
	}
	
	/**
	 * Devuelve el sub-árbol indicado. (para tests)
	 * path será el camino para obtener el sub-arbol. Está formado por L y R.
	 * Si se codifica "bajar por la izquierda" como "L" y
	 * "bajar por la derecha" como "R", el camino desde un 
	 * nodo N hasta un nodo M (en uno de sus sub-árboles) será la
	 * cadena de Ls y Rs que indica cómo llegar desde N hasta M.
     *
     * Se define también el camino vacío desde un nodo N hasta
     * él mismo, como cadena vacía.
     *  
     *  Por ejemplo, con el arbol ejemplo=
	 * 
	 * {50, {30(2), {10, ∅, ∅}, {40(4), ∅, ∅}}, {80(2), {60, ∅, ∅}, ∅}}
	 * 
	 * la llamada a ejemplo.getSubtreeWithPath("LL").toString() devolvera "{10, ∅, ∅}"
	 * la llamada a ejemplo.getSubtreeWithPath("R").toString() devolvera "{80(2), {60, ∅, ∅}, ∅}"
	 * la llamada a ejemplo.getSubtreeWithPath("").toString() devolvera "{50, {30(2), {10, ∅, ∅}, {40(4), ∅, ∅}}, {80(2), {60, ∅, ∅}, ∅}}"
	 * la llamada a ejemplo.getSubtreeWithPath("RR").toString() disparará excepción NoSuchElementException
	 * 
	 * Si el subarbol no existe lanzará la excepción NoSuchElementException.
	 * 
	 * @param path 
	 * @return
	 * @throws NoSuchElementException si el subarbol no existe
	 */
	public BinarySearchTreeImpl<T> getSubtreeWithPath(String path) {
		BinarySearchTreeImpl<T> valorDevuelto = this;
		
		if(path.isEmpty()) {
			return valorDevuelto;
		} 
		
		return getSubtreeWithPathRec(path, 0);
	}	
	
	public BinarySearchTreeImpl<T> getSubtreeWithPathRec(String path, int aux) {
		if(this.isEmpty()) 
			throw new NoSuchElementException();
		
		if(aux == path.length()) {
			return this;
		} else {
			if(path.charAt(aux) == 'L') {
				return this.getLeftBST().getSubtreeWithPathRec(path, ++aux);
			} else if(path.charAt(aux) == 'R') {
				return this.getRightBST().getSubtreeWithPathRec(path, ++aux);
			} else {
				return this.getSubtreeWithPathRec(path, ++aux);
			}
		}
	}
	
	/**
	 * Devuelve el String que representa el camino formado por L's y R's desde 
	 * la raiz hasta el elemento pasado como parámetro.
	 * Se codifica "bajar por la izquierda" como "L" y
	 * "bajar por la derecha" como "R", el camino desde un 
	 * nodo N hasta un nodo M (en uno de sus sub-árboles) será la
	 * cadena de Ls y Rs que indica cómo llegar desde N hasta M.
     *
     * Se define también el camino vacío desde un nodo N hasta
     * él mismo, como cadena vacía.
     *  
     *  Por ejemplo, con el arbol ejemplo=
	 * 
	 * {50, {30(2), {10, ∅, ∅}, {40(4), ∅, ∅}}, {80(2), {60, ∅, ∅}, ∅}}
	 * 
	 * la llamada a ejemplo.getPath(10) devolvera "LL"
	 * la llamada a ejemplo.getPath(60) devolvera "RL"
	 * la llamada a ejemplo.getPath(50) devolvera ""
	 * la llamada a ejemplo.getPath(100) disparará excepción NoSuchElementException
	 * 
	 * Si el elemento no existe lanzará la excepción NoSuchElementException.
	 * 
	 * @param elem 
	 * @return camino hasta el elemento
	 * @throws NoSuchElementException si el elemento no existe
	 */
	public String getPath(T elem) {
		if (!contains(elem)) 
			throw new NoSuchElementException();
		return  this.getPathRec(elem, "");
		
	}
	
	public String getPathRec(T elem,  String path) {
		if (elem.equals(this.content)) {
				return path;
		} else {
			if (this.content.compareTo(elem) > 0){
				return this.getLeftBST().getPathRec(elem, path.concat("L"));
			} else {
				return this.getRightBST().getPathRec(elem, path.concat("R"));
			}
		}
	}
	/**
	 * Importante: Solamente se puede recorrer el arbol una vez
	 * 
	 * Recorre en orden descendente el arbol etiquetando todos sus nodos con la etiqueta "descend" y
	 * el valor correspondiente a la posición en dicho recorrido.
	 * 
	 * 
	 * Por ejemplo, sea el arbol ejemplo
	 * 
	 * {50, {30(2), {10, ∅, ∅}, {40(4), ∅, ∅}}, {80(2), {60, ∅, ∅}, ∅}}
	 * 
	 * la llamada a ejemplo.tagPosDescend() el arbol quedaria etiquetado:
	 * 
     *  {50 [(descend, 3)], {30(2) [(descend, 5)], {10 [(descend, 6)], ∅, ∅}, 
     *   {40(4) [(descend, 4)], ∅, ∅}}, {80(2) [(descend, 1)], {60 [(descend, 2)], ∅, ∅}, ∅}}
	 * 
	 */
	public void tagPosDescend() {
		if(!this.isEmpty()) {
			this.tagPosDescendRec(0);
		}
	}
	
	public int tagPosDescendRec(int descendValue) {
		if(this.isEmpty()) {
			return descendValue;
		}
		if(this.isLeaf()) {
			this.setTag("descend", ++descendValue);
			return descendValue;
		} else {
			descendValue = this.getRightBST().tagPosDescendRec(descendValue);
			this.setTag("descend", ++descendValue);
			return this.getLeftBST().tagPosDescendRec(descendValue);
		}	
	}
	
	
	/**
	 * Importante: Solamente se puede recorrer el arbol una vez
	 * 
	 * Calcula y devuelve el numero de nodos internos del árbol (no sean hojas) y etiqueta cada
	 * nodo interno con la etiqueta "internal" y el valor correspondiente a su posicion segun el
	 * recorrido inorden en este arbol.
	 * 
	 * La rai­z se considera nodo interno.
	 * 
	 * Por ejemplo, sea un arbol ejemplo:
	 * 
	 * {30, {10, {5, {2, ∅, ∅}, ∅}, {20, {15, {12, ∅, ∅}, ∅}, ∅}, ∅}
	 * 
	 *tras la llamada a ejemplo.tagInternalInorder() devolvería 5 y dejaría el arbol etiquetado:
	 * 
	 * {30[(internal, 7)], {10 [(internal, 3)], {5[(internal, 2)], {2, ∅, ∅}, ∅}, {20 [(internal, 6)], 
	 *  {15 [(internal, 5)], {12, ∅, ∅}, ∅}, ∅}, ∅}
	 
	 * 
	 */
	public int tagInternalInorder() {
		return (this.tagInternalInorderRec(0,0)[1]);
	}
	
	public int[] tagInternalInorderRec(int internalValue, int cont) {
		int[] returnValue = new int[2];
		if(this.isEmpty())  {
			returnValue[0] = internalValue;
			returnValue[1] = cont;
			return returnValue;
		} else {
			if (this.isLeaf()) {
				returnValue[0] = ++internalValue;
				returnValue[1] = cont;
				return returnValue;
			} else {
				returnValue = this.getLeftBST().tagInternalInorderRec(internalValue, cont);
				internalValue = returnValue[0];
				cont = returnValue[1];
				cont++;
				this.setTag("internal", ++internalValue);
				return this.getRightBST().tagInternalInorderRec(internalValue, cont);
			}
		}	
	}
	
	/**
	 * Importante: Solamente se puede recorrer el arbol una vez
	 * 
	 * Calcula y devuelve el numero de nodos que son hijos unicos y etiqueta cada
	 * nodo que sea hijo unico (no tenga hermano hijo del mismo padre) con la
	 * etiqueta "onlySon" y el valor correspondiente a su posicion segun el
	 * recorrido preorden en este arbol.
	 * 
	 * La rai­z no se considera hijo unico.
	 * 
	 * Por ejemplo, sea un arbol ejemplo, que tiene 3 hijos unicos, 
	 * la llamada a ejemplo.tagOnlySonPreorder() devuelve 3 y los va etiquetando
	 * segun su recorrido en preorden.
	 * 
	 * {30, {10, {5, {2, ∅, ∅}, ∅}, {20, {15, {12, ∅, ∅}, ∅}, ∅}, ∅}
	 * 
	 *
	 * el arbol quedari­a etiquetado:
	 * 
	 * {30, {10 [(onlySon, 2)], {5, {2 [(onlySon, 4)], ∅, ∅}, ∅}, {20, {15 [(onlySon, 6)], {12
	 * [(onlySon, 7)], ∅, ∅}, ∅}, ∅}, ∅}
	 * 
	 */
	public int tagOnlySonPreorder() {
		return tagOnlySonPreorderRec(0, 0)[1];
	}
	
	public int[] tagOnlySonPreorderRec(int preOrdenValue, int contOnlySon) {
		int[] returnValue = new int[2];
		
		// CASO TRIVIAL 
		if(this.isEmpty())  {
			returnValue[0] = preOrdenValue;
			returnValue[1] = contOnlySon;
			return returnValue;
		} else {
			// RAIZ
			if (this.father == null) {
				++preOrdenValue;
				returnValue = this.getLeftBST().tagOnlySonPreorderRec(preOrdenValue, contOnlySon);
				preOrdenValue = returnValue[0];
				contOnlySon = returnValue[1];
				return this.getRightBST().tagOnlySonPreorderRec(preOrdenValue, contOnlySon);
			} else {
				// HIJO UNICO
				if(this.father.getLeftBST().isEmpty() || this.father.getRightBST().isEmpty()) {
					this.setTag("onlySon", ++preOrdenValue);
					contOnlySon++;
					returnValue = this.getLeftBST().tagOnlySonPreorderRec(preOrdenValue, contOnlySon);
					preOrdenValue = returnValue[0];
					contOnlySon = returnValue[1];
					return this.getRightBST().tagOnlySonPreorderRec(preOrdenValue, contOnlySon);
				// NO HIJO UNICO
				} else {
					++preOrdenValue;
					returnValue = this.getLeftBST().tagOnlySonPreorderRec(preOrdenValue, contOnlySon);
					preOrdenValue = returnValue[0];
					contOnlySon = returnValue[1];
					return this.getRightBST().tagOnlySonPreorderRec(preOrdenValue, contOnlySon);
				}
			}
		}	
	}
	
	/**
	 * Busca y devuelve a partir del nodo que contiene el elemento pasado como parámetro 
	 * el elemento que está up posiciones hacia arriba y right hacia abajo bajando por la rama derecha. 
	 * Primero debe encontrar el elemento y despues comprueba si el nodo que contiene ese elemento
	 * tiene nodo a través del camino indicado por los otros dos parámetros.
     * Debe etiquetar desde el nodo que contiene el elemento,  hasta su objetivo,  los nodos del camino 
     * con un número consecutivo empezando por el 1 en el elemento buscado. 
     * 
     * Por ejemplo: para el árbol ejemplo= {10, {5, {2, ∅, ∅}, {7,∅, ∅},}, {20, {15, {12, ∅, ∅}, ∅ },{30, ∅, ∅}}}. 
     * 
     * Si se hace ejemplo.getRoadUpRight("7",2,2) devolverá el elemento 30 y etiquetará los nodos 7, 5, 10, 20, 30 con numeros consecutivos
     *  y la etiqueta road. 
     *  
     * Así el árbol quedaría etiquetado: 10 [(road, 3)],{5[(road, 2)], {2, ∅, ∅}, {7 [(road, 1)],∅, ∅},}, {20 [(road, 4)], {15, {12, ∅, ∅}, ∅},{30 [(road, 5)], ∅, ∅}}}
     *  siendo el nodo que contiene el 30 el nodo que devuelve.
	 * 
	 * @throws NoSuchElementException si el elemento a comprobar no esta en el arbol	
	 * @throws IllegalArgumentException si element es null
	 */
	@SuppressWarnings("unchecked")
	public T getRoadUpRight(T elem, int up, int right) {
		if (elem == null) 
			throw new IllegalArgumentException();
		if (!this.contains(elem)) 
			throw new NoSuchElementException();
		
		// BUSCAR ELEM
		BinarySearchTreeImpl<T> nodo = this.searchNode(elem);
		BinarySearchTreeImpl<T> aux = nodo.upRoute(up, 0, right);
		if(aux == null) {
			return null;
		} else {
			return aux.getContent();
		}
	}
	
	public BinarySearchTreeImpl<T> searchNode(T elem) {
		if (elem.equals(this.content)) {
			return this;
		} else {
			if (this.content.compareTo(elem) > 0){
				return this.getLeftBST().searchNode(elem);
			} else {
				return this.getRightBST().searchNode(elem);
			}
		}
	}
	
	public BinarySearchTreeImpl<T> upRoute(int up, int cont, int right) {
		if(up == 0) {
			return this.rightRoute(right, cont);
		} else {
			if(this.father != null) {
				this.setTag("road", ++cont);
				return this.father.upRoute(--up, cont, right);
			} else {
				throw new NoSuchElementException();
			}
		}
	}
	
	public BinarySearchTreeImpl<T> rightRoute(int right, int cont) {
		if(this.isEmpty()) {
			throw new NoSuchElementException();
		} else {
			if(right == 0) {
				this.setTag("road", ++cont);
				return this;
			} else {
				this.setTag("road", ++cont);
				return this.getRightBST().rightRoute(--right, cont);
			}
		}
	}
	
	/**
	 * Crea y devuelve un árbol exactamente igual que el this
	 * 
	 * @return un arbol exactamente igual (misma estructura y contenido) que el arbol this
	 */
	public BinarySearchTreeImpl<T> copy(){
		BinarySearchTreeImpl<T> copia = new BinarySearchTreeImpl<T>();
		return this.copyRec(copia);
		
	}
	
	public BinarySearchTreeImpl<T> copyRec(BinarySearchTreeImpl<T> copia) {
		if(this.isEmpty()) {
			return copia;
		} else {
			if(this.isLeaf()) {
				copia.insert(this.getContent());
				return copia;
			} else {
				copia.insert(this.getContent());
				copia = this.getLeftBST().copyRec(copia);
				return this.getRightBST().copyRec(copia);
			}
		}
		
	}
	/**
	 * Elimina los valores en un array del Arbol.
	 * Devuelve el número de elementos que pudo eliminar del árbol
	 *  (no podrá eliminar los elemenots 'null' o que no los contiene el arbol)
	 * 
	 * return numero de elementos eliminados del arbol
	 */
	public int  remove(@SuppressWarnings("unchecked") T... elements) {
		int cont = 0;
		
		for (T elemento: elements) {
			if (elemento != null) {
				if(this.contains(elemento)) {
					cont++;
					this.remove(elemento);

				}
			}
		}
		return cont;
	}

	/**
	 * Elimina un elemento del arbol. Si el atributo count del nodo que contiene el elemento es >1, simplemente se decrementará este valor en una unidad
	 * 
	 * Si hay que eliminar el nodo, y tiene dos hijos, se tomara el criterio de sustituir el
	 * elemento por el menor de sus mayores y eliminar el menor de los mayores.
	 * 
	 * @throws NoSuchElementException si el elemento a eliminar no esta en el arbol
	 * @throws IllegalArgumentException si element es null
     *
	 */
	public void remove(T element) {
		if (element  == null) 
			throw new IllegalArgumentException();
		if (!contains(element)) 
			throw new NoSuchElementException();
		
		BinarySearchTreeImpl<T> aux = this.getElem(element);
		
		if(aux.count <= 1) {
			aux.count = 0;
			removeAux(element);
		} else {
			aux.count--;
		}
	}

	private void removeAux(T element) {
		BinarySearchTreeImpl<T> nodoActual = this.getElem(element);
		
		//caso particular 1: compruebo que mi nodo es una hoja
		if(nodoActual.isLeaf()) {
			if(nodoActual.getContent().compareTo(nodoActual.father.getContent()) > 0) { 
				nodoActual.father.setRightBST(emptyBST(nodoActual.father));
			} else {
				nodoActual.father.setLeftBST(emptyBST(nodoActual.father));
			}
			// caso particular 2: mi nodo tiene unicamente un hijo izquierdo
		} else if(nodoActual.getRightBST().isEmpty() && !nodoActual.getLeftBST().isEmpty()) {
			if(nodoActual.father == null) {
				nodoActual = nodoActual.getLeftBST();
			} else {
				if(nodoActual.getContent().compareTo(nodoActual.father.getContent())>0) {
					nodoActual.father.setRightBST(nodoActual.getLeftBST());
				}
				else {
					nodoActual.father.setLeftBST(nodoActual.getLeftBST());
				}
			}
			//caso particular 3: mi nodo actual tiene unicamente un hijo derecho
		} else if(!nodoActual.getRightBST().isEmpty() && nodoActual.getLeftBST().isEmpty()) {
			if(nodoActual.father == null) {
				nodoActual = nodoActual.getLeftBST();
			} else {
				if(nodoActual.getContent().compareTo(nodoActual.father.getContent())>0) {
					nodoActual.father.setRightBST(nodoActual.getRightBST());
				}
				else {
					nodoActual.father.setLeftBST(nodoActual.getRightBST());
				}
			}
			// caso general: mi nodo actual tiene dos hijos.
		} else {
			nodoActual.setContent(minMaxSubTreeElement(nodoActual));
		}	
	}
	
	private T minMaxSubTreeElement(BinarySearchTreeImpl<T> nodoActual) {
		
		if(nodoActual.getLeftBST().getContent().compareTo(nodoActual.getRightBST().getContent()) > 0) { 
			if(nodoActual.getLeftBST().getLeftBST().isEmpty() && nodoActual.getLeftBST().getRightBST().isEmpty()) {
				// HIJO IZQ 
				T element = nodoActual.getLeftBST().getContent();
				remove(element);
				return element;
			} else {
				if(!nodoActual.getLeftBST().getLeftBST().isEmpty() && !nodoActual.getLeftBST().getRightBST().isEmpty()) {
					if(nodoActual.getLeftBST().getLeftBST().getContent().compareTo(nodoActual.getLeftBST().getRightBST().getContent()) > 0){
						T element = nodoActual.getLeftBST().getRightBST().getContent();
						remove(element);
						return element;
					} else {
						T element = nodoActual.getLeftBST().getLeftBST().getContent();
						remove(element);
						return element;
					}
				} else {
					T element = nodoActual.getLeftBST().getContent();
					remove(element);
					return element;
				}
			}
		} else {
			if(nodoActual.getRightBST().getLeftBST().isEmpty() && nodoActual.getRightBST().getRightBST().isEmpty()) {
				// HIJO IZQ 
				T element = nodoActual.getRightBST().getContent();
				remove(element);
				return element;
			} else {
				if(!nodoActual.getRightBST().getLeftBST().isEmpty() && !nodoActual.getRightBST().getRightBST().isEmpty()) {
					if(nodoActual.getRightBST().getLeftBST().getContent().compareTo(nodoActual.getRightBST().getRightBST().getContent()) > 0){
						T element = nodoActual.getRightBST().getRightBST().getContent();
						remove(element);
						return element;
					} else {
						T element = nodoActual.getRightBST().getLeftBST().getContent();
						remove(element);
						return element;
					}
				} else {
					T element = nodoActual.getRightBST().getContent();
					remove(element);
					return element;
				}
			}
		}
	}
	
	/**
	 * Decrementa el número de instancias del elemento en num unidades.
	 * Si count queda en cero o negativo, se elimina el elemento del arbol. 
	 * 
	 * 
	 * Si hay que eliminar el nodo, y tiene dos hijos, se tomara el criterio de sustituir el
	 * elemento por el menor de sus mayores y eliminar el menor de los mayores.
	 * 
	 * @throws NoSuchElementException si el elemento a eliminar no esta en el arbol	
	 * @throws IllegalArgumentException si element es null
	 */
	public void remove(T element, int num) {
		if (element  == null) 
			throw new IllegalArgumentException();
		if (!contains(element)) 
			throw new NoSuchElementException();
		
		BinarySearchTreeImpl<T> aux = this.getElem(element);
		
		if(aux.count - num <= 0) {
			aux.count = 0;
			removeAux(element);
		} else {
			aux.count = aux.count - num;
		}
	}

	
	/**
	 * Elimina todas las instancias del elemento en el árbol 
	 * eliminando del arbol el nodo que contiene el elemento .
	 * 
	 * 
	 * Se tomara el criterio de sustituir el elemento por el menor de sus mayores 
	 * y eliminar el menor de los mayores.
	 * 
	 * @throws NoSuchElementException si el elemento a eliminar no esta en el arbol	
	 * @throws IllegalArgumentException si element es null
	 */
	public int removeAll(T element) {
		if (element  == null) 
			throw new IllegalArgumentException();
		if (!contains(element)) 
			throw new NoSuchElementException();
		
		BinarySearchTreeImpl<T> aux = this.getElem(element);
		int aux2 = aux.count;
		aux.count = 0;
		removeAux(element);
		return aux2;
	}
	
	public BinarySearchTreeImpl<T> getElem(T element) {
		if (element == null) 
			throw new IllegalArgumentException();
		
		if (!isEmpty()) {
			if (element.equals(this.content)) {
				return this;
			} else {
				if (this.content.compareTo(element) > 0){
					return this.getLeftBST().getElem(element);
				} else {
					return this.getRightBST().getElem(element);
				}
			}
		} else {
			return null;
		}
	}
	
	public int getHeight(int cont) {
		if(this.father == null) {
			return cont;
		} else {
			return this.father.getHeight(++cont);
		}
	}

	
}
	
	


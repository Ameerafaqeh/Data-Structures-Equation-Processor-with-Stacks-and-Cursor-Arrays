 
public  interface Stackable<T> {
	
	boolean isEmpty();

	T peek();

	void push(T data);

	T pop();

	void clear();

	int size();
}

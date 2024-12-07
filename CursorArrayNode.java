public class CursorArrayNode<T extends Comparable<T>> {
	T data;
	int next;
 
	public CursorArrayNode(T data, int next) {
		this.data = data;
		this.next = next;
	}

	public void setData(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public int getNext() {
		return next;
	}

	public void setNext(int next) {
		this.next = next;
	}

	@Override
	public String toString() {
		return " " + data + " ";
	}

}
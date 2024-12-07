public class Stack<T extends Comparable<T>> implements Stackable<T> {

	CursorArrayLinkedList<T> cursorList = new CursorArrayLinkedList<>();
	int top = cursorList.createList();

	// Check if the Stack is empty.
	@Override
	public boolean isEmpty() {
		return cursorList.isEmpty(top);
	}
 
	// Get the top element of the Stack without removing it.
	@Override
	public T peek() {

		return cursorList.getFirstElement(top);
	}

	// Push an element onto the Stack.
	@Override
	public void push(T data) {

		cursorList.insertAtHead(data, top);
	}

	// Pop the top element from the Stack.
	@Override
	public T pop() {
		return cursorList.deleteAtHead(top);

	}

	// Clear all elements from the Stack.
	@Override
	public void clear() {

		while (!isEmpty())
			pop();
	}

	// Get the number of elements in the Stack.
	@Override
	public int size() {
	    return cursorList.length(top);
	}



}

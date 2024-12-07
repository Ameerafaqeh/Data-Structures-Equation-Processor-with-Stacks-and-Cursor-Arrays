public class CursorArrayLinkedList<T extends Comparable<T>> {

    // Array to store nodes forming the linked list
    CursorArrayNode<T>[] cursorArray = new CursorArrayNode[100];

    // Constructors
 
    public CursorArrayLinkedList(CursorArrayNode<T>[] cursorArray) {
        this.cursorArray = cursorArray;
    }

    public CursorArrayLinkedList() {
        initialization();
    }

    // Initialize the cursorArray with nodes
    public int initialization() {
        for (int i = 0; i < cursorArray.length - 1; i++)
            cursorArray[i] = new CursorArrayNode<>(null, i + 1);
        cursorArray[cursorArray.length - 1] = new CursorArrayNode<>(null, 0);
        return 0;
    }

    // Allocate memory for a new node
    public int malloc() {
        int p = cursorArray[0].next;
        cursorArray[0].next = cursorArray[p].next;
        return p;
    }

    // Free memory for a node
    public void free(int p) {
        cursorArray[p] = new CursorArrayNode(null, cursorArray[0].next);
        cursorArray[0].next = p;
    }

    // Check if a node is null
    public boolean isNull(int l) {
        return cursorArray[l] == null;
    }

    // Check if a list is empty
    public boolean isEmpty(int l) {
        return cursorArray[l].next == 0;
    }

    // Check if a node is the last node in the list
    public boolean isLast(int p) {
        return cursorArray[p].next == 0;
    }

    // Create a new list and return its index
    public int createList() {
		int l = malloc();
		if (l == 0)
			System.out.println("Error: Out of space!!!");
		else
			cursorArray[l] = new CursorArrayNode("-", 0);
		return l;
	}

    // Insert an element at the head of the list
    public void insertAtHead(T data, int l) {
        if (isNull(l)) // list not created
            return;
        int p = malloc();
        if (p != 0) {
            cursorArray[p] = new CursorArrayNode(data, cursorArray[l].next);
            cursorArray[l].next = p;
        } else
            System.out.println("Error: Out of space!!!");
    }

    // Insert an element at the tail of the list
    public void insertAtTail(T data, int l) {
        if (isNull(l)) { // list not created
            return;
        }

        int p = malloc();

        int tail = l;
        if (p != 0) {
            while (!isLast(l)) {
                l = cursorArray[l].getNext();
            }
            cursorArray[p] = new CursorArrayNode<>(data, 0);
            cursorArray[l].setNext(p);
        } else {
            System.out.println("Error: Out of space!!!");
        }
    }

    // Traverse the list iteratively and print elements
    public void traverseListIteration(int l) {
        System.out.print("list " + l + " --> ");
        while (!isNull(l) && !isEmpty(l)) {
            l = cursorArray[l].next;
            System.out.print(cursorArray[l] + " --> ");
        }
        System.out.println("null");
    }

    // Traverse the list recursively and print elements
    public void traverseListRecursion(int l) {
        if (isNull(l)) {
            System.out.println("null");
            return;
        }

        System.out.print(cursorArray[l] + " --> ");
        if (!isEmpty(l)) {
            traverseListRecursion(cursorArray[l].next);
        }
    }

    // Helper method to initiate recursive traversal
    public void traverseListRecursionHelperMethod(int l) {
        traverseListRecursion(cursorArray[l].getNext());
    }

    // Delete the element at the head of the list and return its value
    public T deleteAtHead(int l) {
        if (!isEmpty(l)) {
            int c = cursorArray[l].getNext();
            T temp = cursorArray[c].getData();
            cursorArray[l].setNext(cursorArray[c].getNext());
            free(c);
            return temp;
        }
        return null;
    }

    // Calculate the length of the list recursively
    public int length(int i) {
        if (isEmpty(i)) {
            return 0;
        }
        return 1 + length(cursorArray[i].next);
    }

    // Find the index of the node containing the previous element with specified data
    public int findPrevious(T data, int l) {
        while (!isNull(l) && !isEmpty(l)) {
            if (cursorArray[cursorArray[l].next].data.equals(data))
                return l;
            l = cursorArray[l].next;
        }
        return -1;
    }

    // Insert an element in sorted order
    public void insertSorted(int l, T data) {
        if (isNull(l) == true) {
            return;
        } else {
            int p = malloc();
            if (p != 0) {
                int pre = l, curr = l;
                while (!isNull(curr) && !isLast(curr)) {
                    pre = curr;
                    curr = cursorArray[curr].getNext();
                    if (cursorArray[curr].getData().compareTo(data) >= 0) {
                        break;
                    }
                }
                if (curr == l) {// insert at head
                    cursorArray[p] = new CursorArrayNode(data, cursorArray[l].getNext());
                    cursorArray[l].setNext(p);
                } else if (cursorArray[curr].getData().compareTo(data) < 0) {// insert at tail
                    cursorArray[p] = new CursorArrayNode(data, 0);
                    cursorArray[curr].setNext(p);
                } else {// data between
                    cursorArray[p] = new CursorArrayNode(data, curr);
                    cursorArray[pre].setNext(p);
                }
            } else {
                System.out.println("out of space");
            }
        }
    }

    // Retrieve the data of the first element in the list
    public T getFirstElement(int l) {
        if (!isEmpty(l))
            return cursorArray[cursorArray[l].next].data;
        return null;
    }

}

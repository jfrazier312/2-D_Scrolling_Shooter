
public class LinkedNodeList {

	private Node head;
	private Node tail;
	private int size;

	public LinkedNodeList() {
		head = null;
		tail = null;
		size = 0;
	}

	public Node getHead() {
		return head;
	}

	public Node getTail() {
		return tail;
	}

	public int getSize() {
		return size;
	}
	
	/**
	 * This method will reverse the linked list and 
	 * correctly modify head and tail pointers.
	 * @return
	 */
	public void reverseLinkedList(){
		tail = head;
		Node cur = head;
		Node prev = null;
		Node next;
		
		while(cur != null) {
			next = cur.next;
			cur.next = prev;
			prev = cur;
			cur = next;
		}
		head = prev;
	}
	
	/**
	 * this method will print out your list, starting from head
	 */
	public void printLinkedList(){
		Node cur = head;
		while(cur.next != null){
			System.out.println(cur.value + ", ");
			cur = cur.next;
		}
		System.out.println(cur.value);
	}
}

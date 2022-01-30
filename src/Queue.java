
import java.util.ArrayList;

class QNode {
	private ArrayList<TransactionTuple> transaction_tuples;
	QNode next;

	// constructor to create a new linked list node
	public QNode(ArrayList<TransactionTuple> tuples)
	{
		this.transaction_tuples = new ArrayList<>(tuples.size());
		for(int i=0; i<tuples.size(); i++)
			this.transaction_tuples.add(tuples.get(i));
		this.next = null;
	}
	public ArrayList<TransactionTuple> get_tuples()
	{
		return this.transaction_tuples;
	}
	
	public void display_data()
	{
		for (int i=0; i<this.transaction_tuples.size(); i++)
			this.transaction_tuples.get(i).display_data();
		System.out.println("");
	}
}

class Queue {
	int size;
	int capacity;
	QNode front, rear;

	public Queue()
	{
		this.size=0;
		this.capacity = 0;
		this.front = this.rear = null;
	}
	Queue(int cap)
	{
		this.size = 0;
		this.capacity = cap;
		this.front = this.rear = null;
	}
	
	public int get_size()
	{
		return this.size;
	}
	
	QNode getNode(int index)
	{
		QNode to_ret = this.front;
		for(int i=0; i<index; i++)
		{
			if (to_ret == null)
				break;
			to_ret = to_ret.next;
		}
		
		return to_ret;
	}
	
	// Method to add an key to the queue.
	void enqueue(ArrayList<TransactionTuple> tuples)
	{
		if (this.size == this.capacity)
		{
			this.dequeue();
		}
		// Create a new LL node
		QNode temp = new QNode(tuples);
		
		// If queue is empty, then new node is front and rear both
		if (this.rear == null) {
			this.front = this.rear = temp;
		}
		else
		{
		// Add the new node at the end of queue and change rear
			this.rear.next = temp;
			this.rear = temp;
		}
		this.size += 1;
	}

	// Method to remove an key from queue.
	QNode dequeue()
	{
		// If queue is empty, return NULL.
		if (this.front == null)
			return null;

		// Store previous front and move front one node ahead
		QNode temp = this.front;
		this.front = this.front.next;

		// If front becomes NULL, then change rear also as NULL
		if (this.front == null)
			this.rear = null;
		this.size -= 1;
		return temp;
	}
	
	boolean isEmpty()
	{
		if (this.capacity == this.size)
			return true;
		else
			return false;
	}
	
	void print()
	{
		if (this.front == null)
		{
			System.out.println("The queue is empty");
			return;
		}
		QNode temp = this.front;
		while (temp != null)
		{
			temp.display_data();
			temp = temp.next;
		}
	}
}

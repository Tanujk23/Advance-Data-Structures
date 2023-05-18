public class MinHeapp {
	Ride heap[]; // Creating an Heap Array
	int rideDuration;
	int heap_size;

	// getiing the root of min heap i.e the smallest element
	Ride getMin() {
		return heap[0];
	}

	// To get the parent index
	int parent(int i) {
		return (i - 1) / 2;
	}

	// To get the left child index
	int left(int i) {
		return (2 * i + 1);
	}

	// To get the right child index
	int right(int i) {
		return (2 * i + 2);
	}

	MinHeapp(int cap) {
		heap_size = 0;
		rideDuration = cap;
		heap = new Ride[cap];
	}

	// this function is used checking if the min heap property is satisfied after any chabges done to the heap
	boolean minHeap_check(int i, int j) {
		if ((heap[i].cost > heap[j].cost) || ((heap[i].cost == heap[j].cost) && (heap[i].duration > heap[j].duration)))
			return true;
		else
			return false;
	}

	// Function used to swap two elements in a heap in case of delete operations
	void flip(Ride x, Ride y) {
		int temp = x.index_in_heap;
		x.index_in_heap = y.index_in_heap;
		y.index_in_heap = temp;
		heap[x.index_in_heap] = x;
		heap[y.index_in_heap] = y;
	}

	boolean insert(Ride ride) {
		if (heap_size == rideDuration) {
			return false; // Heap is full, return false
		}

		// Insert the new ride at the end of the heap
		heap_size++;
		int i = heap_size - 1;
		ride.index_in_heap = i;
		heap[heap_size - 1] = ride;

		// Fix the min heap property if it is violated by bubbling up the inserted ride
		bubbleUp(i);

		return true; // Ride successfully inserted, return true
	}

	boolean insertRide(Ride node) {
		return insert(node);
	}

	void deleteRide(int index) {
		// Swap the ride at the given index with the last ride in the heap
		heap[index] = heap[heap_size - 1];
		heap[index].index_in_heap = index;
		heap_size--;

		// Perform bubble down operation to maintain the min heap property
		bubbleDown(heap[index]);
	}

	// Function to get the minimum element from the minHeap and deleting that element
	int extractMin() {
		if (heap_size <= 0)
			return -1;
		Ride root = heap[0];
		if (heap_size == 1) {
			heap_size--;
			return root.Id;
		}

		heap[0] = heap[heap_size - 1];
		heap[0].index_in_heap = 0;
		heap_size--;
		bubbleDown(heap[0]);

		return root.Id;
	}

	// Helps to maintain the min heap property
	void bubbleDown(Ride node) {
		int currentIndex = node.index_in_heap;
		int leftChildIndex = left(currentIndex);
		int rightChildIndex = right(currentIndex);
		int smallestIndex = currentIndex;
		// Check if left child exists and is smaller than current node
		if (leftChildIndex < heap_size && !minHeap_check(leftChildIndex, currentIndex)) {
			smallestIndex = leftChildIndex;
		}
		// Check if right child exists and is smaller than current node and left child
		// (if applicable)
		if (rightChildIndex < heap_size && !minHeap_check(rightChildIndex, smallestIndex)) {
			smallestIndex = rightChildIndex;
		}
		// If the smallest child is not the current node, swap them and recursively call bubbleDown on the smallest child
		if (smallestIndex != currentIndex) {
			flip(heap[currentIndex], heap[smallestIndex]);
			bubbleDown(heap[smallestIndex]);
		}
	}

	// used to maintian the min heap property
	void bubbleUp(int i) {
		while (i != 0 && minHeap_check(parent(i), i)) {
			flip(heap[i], heap[parent(i)]);
			i = parent(i);
		}
	}

}

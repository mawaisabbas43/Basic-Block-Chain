public interface Map<K extends Comparable<K>, T> {

	// Return true if the tree is empty. Must be O(1).
	boolean empty();

	// Return true if the tree is full. Must be O(1).
	boolean full();

	// Return the key and data of the current element
	T retrieve();

	// Update the data of current element.
	void update(T e);

	// Search for element with key k and make it the current element if it exists.
	// If the element does not exist the current is unchanged and false is returned.
	// This method must be O(log(n)) in average.
	boolean find(K key);

	// Insert a new element if does not exist and return true. The current points to
	// the new element. If the element already exists, current does not change and
	// false is returned. This method must be O(log(n)) in average.
	boolean insert(K key, T data);

	// Remove the element with key k if it exists and return true. If the element
	// does not exist false is returned (the position of current is unspecified
	// after calling this method). This method must be O(log(n)) in average.
	boolean remove(K key);
}

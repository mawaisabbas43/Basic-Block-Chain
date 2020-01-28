public interface Blockchain {

	// This is the initial hash used in the first block.
	public final static byte[] initHash = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0 };

	// Reward for miner.
	public static final int minerReward = 100;

	// Return the length of the chain
	int length();

	// Get the hash of the last block
	byte[] getLastBlockHash();

	// Add a block to the chain. If the block is not the first block and does not
	// contain a transaction, the block is not inserted and false is returned,
	// otherwise the block is inserted at the end and true is returned.
	boolean addBlock(Block b);

	// Return the list of blocks in their order (first block must be the first in
	// the list).
	List<Block> getBlocks();

	// Return the balance of pbk. If pbk does not exist in the chain, 0 is returned.
	// If the balance of pbk becomes negative at any point, the method returns -1.
	int getBalance(byte[] pbk);

	// Remove all invalid blocks. A block is valid if all blocks before it are
	// valid, its hash is correct, and its transactions are valid. A transaction is
	// valid if it's signature is valid, and the sender has enough money to make the
	// transfer.
	void removeInvalid();

}

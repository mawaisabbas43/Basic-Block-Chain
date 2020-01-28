public interface Block {

	// Return the miner public key.
	byte[] getMiner();

	// Return the nonce of the block.
	int getNonce();

	// Return the transaction included in the block
	Transaction getTransaction();

	// Return the hash value of the block.
	byte[] getHash();

	// Return the hash of the previous block, 0 otherwise.
	byte[] getPrevHash();

	// Set the miner's public key.
	void setMiner(byte[] miner);

	// Set the nonce.
	void setNonce(int nonce);

	// Set the transaction
	void setTransaction(Transaction transaction);

	// Set previous hash
	void setPrevHash(byte[] prevHash);

	// Set the hash value of the block.
	void setHash(byte[] hash);

	// Check if the hash is valid.
	boolean isHashValid();

	// Compute hash of the block using Utils.getHash. The order of the data is as
	// specified in toBytes.
	void compHash();

	// Find the smallest non-negative nonce that makes the hash of the block valid
	// according to Utils.validHash.
	void mine();

	// Converts block data (except hash) to bytes in the order: (nonce, transactions
	// (converted to bytes using Transaction.toBytes()), hash of previous block)
	byte[] toBytes();

}

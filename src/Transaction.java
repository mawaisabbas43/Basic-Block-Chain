public interface Transaction {

	// Set the sender.
	void setSender(byte[] sender);

	// Set the receiver.
	void setReceiver(byte[] receiver);

	// Set the amount of transfered money.
	void setAmount(int amount);

	// Return the signature of the transaction.
	void setSignature(byte[] signature);

	// Return the public key of the sender.
	byte[] getSender();

	// Return the public key of the receiver.
	byte[] getReceiver();

	// Return the amount of transfered money.
	int getAmount();

	// Return the signature of the transaction.
	byte[] getSignature();

	// Sign the transaction using pvk as private key. The signature must be made
	// using Utils.sign.
	void sign(byte[] pvk);

	// Return true if the transaction is signed with a valid signature by the
	// sender. The signature must be verified using Utils.isSignatueValid.
	boolean isSignatureValid();

	// Converts transaction data to bytes in the order: (sender public key, receiver
	// public key, amount, signature)
	byte[] toBytes();
}

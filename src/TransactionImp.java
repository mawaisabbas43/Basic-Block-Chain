public class TransactionImp implements Transaction {
        private byte[] sender;
        private byte[] receiver;
        private int amount;
        private byte[] amountByte;
        private byte[] signature;
	public TransactionImp() {
	}

	@Override
	public void sign(byte[] pvk) {
            signature=Utils.sign(amountByte, pvk);
	}

	@Override
	public boolean isSignatureValid() {
		return Utils.isSignatueValid(amountByte, signature, sender);
	}

	@Override
	public byte[] toBytes() {
            byte[] sR=Utils.concat(sender, receiver);
            byte[] sRA=Utils.concat(sR, amountByte);
            byte[] sRAS=Utils.concat(sRA, signature);
		return sRAS;
	}

	@Override
	public void setSender(byte[] sender) {
            this.sender=sender;
	}

	@Override
	public void setReceiver(byte[] receiver) {
            this.receiver=receiver;
	}

	@Override
	public void setAmount(int amount) {
            this.amount=amount;
            amountByte=Utils.toBytes(amount);
	}

	@Override
	public void setSignature(byte[] signature) {
            this.signature=signature;
	}

	@Override
	public byte[] getSender() {
		return sender;
	}

	@Override
	public byte[] getReceiver() {
		return receiver;
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public byte[] getSignature() {
		return signature;
	}

}

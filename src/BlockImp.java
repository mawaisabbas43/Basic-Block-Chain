public class BlockImp implements Block {
        private byte[] miner;
        private int nonce;
        private Transaction transaction;
        private byte[] prevHash;
        public final static byte[] initHash = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0 };
        private byte[] hash;
	public BlockImp() {
            prevHash=initHash;
	}

	@Override
	public void setMiner(byte[] miner) {
            this.miner=miner;
            compHash();
	}

	@Override
	public void setNonce(int nonce) {
            this.nonce=nonce;
            compHash();
	}

	@Override
	public void setTransaction(Transaction transaction) {
            this.transaction=transaction;
            compHash();
	}

	@Override
	public void setPrevHash(byte[] prevHash) {
            this.prevHash=prevHash;
            compHash();
	}

	@Override
	public void setHash(byte[] hash) {
            this.hash=hash;
            compHash();
	}

	@Override
	public byte[] getMiner() {
		return miner;
	}

	@Override
	public int getNonce() {
		return nonce;
	}

	@Override
	public byte[] getHash() {
//            compHash();
		return hash;
	}

        //Check later
	@Override
	public byte[] getPrevHash() {
		return prevHash;
	}

	@Override
	public Transaction getTransaction() {
		return transaction;
	}

	@Override
	public void compHash() {
          
            hash=Utils.getHash(toBytes());
	}

	@Override
	public boolean isHashValid() {
//            compHash();
		return Utils.validBlockHash(hash);
	}

	@Override
	public void mine() {
            nonce=0;
            compHash();
            while(!isHashValid()){
                nonce++;
                compHash();
            }
	}

	@Override
	public byte[] toBytes() {
            byte[] nTP;
            if(transaction==null){
                byte[] nonceByte=Utils.toBytes(nonce);//converting nonce into bytes
                byte[] nT=Utils.concat(nonceByte,initHash);//nonceByte+transactionBytes
                nTP=Utils.concat(nT,prevHash);//nonceByte+transactionBytes+hash of previous Block
            }else{
                byte[] nonceByte=Utils.toBytes(nonce);//converting nonce into bytes
                byte[] nT=Utils.concat(nonceByte,transaction.toBytes());//nonceByte+transactionBytes
                nTP=Utils.concat(nT,prevHash);//nonceByte+transactionBytes+hash of previous Block
            }
             
		return nTP;
	}

}

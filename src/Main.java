public class Main {

	public static void print(KPair[] kps) {
		for (int i = 0; i < kps.length; i++) {
			System.out.println("Public key  " + i + ": " + Utils.toHex(kps[i].publicKey));
			System.out.println("Private key " + i + ": " + Utils.toHex(kps[i].privateKey));
			System.out.println();
		}
	}

	public static void printBalance(Blockchain bc, KPair[] kps) {
		System.out.println("---------------------------------------------");
		for (int i = 0; i < kps.length; i++) {
			System.out.println("Balance for user " + i + " :" + bc.getBalance(kps[i].publicKey));
		}
		System.out.println("---------------------------------------------");
	}

	public static void example1() {
		System.out.println("\n\nExample 1:-------------------------------------------------");
		KPair kp = Utils.getKeyPair();
		System.out.println("Private key: " + Utils.toHex(kp.privateKey));
		System.out.println("Public key: " + Utils.toHex(kp.publicKey));
		int a = 10;
		String s = "Hello";
		byte[] aBytes = Utils.toBytes(a);
		byte[] sBytes = s.getBytes();
		byte[] input = Utils.concat(aBytes, sBytes);
		byte[] signature = Utils.sign(input, kp.privateKey);
		boolean valid = Utils.isSignatueValid(input, signature, kp.publicKey);
		System.out.println(valid); // Prints true
		input[0]++; // Change input
		valid = Utils.isSignatueValid(input, signature, kp.publicKey);
		System.out.println(valid); // Prints false
	}

	public static void example2() {
		System.out.println("\n\nExample 2:-------------------------------------------------");
		int a = 10;
		String s = "Hello";
		byte[] aBytes = Utils.toBytes(a);
		byte[] sBytes = s.getBytes();
		byte[] input = Utils.concat(aBytes, sBytes);
		byte[] hash = Utils.getHash(input);
		System.out.println("Hash value: " + Utils.toHex(hash));
	}

	public static void example3() {
		System.out.println("\n\nExample 3:-------------------------------------------------");
		KPair kp0 = Utils.getKeyPair();
		KPair kp1 = Utils.getKeyPair();
		Transaction t =new TransactionImp();
		t.setSender(kp0.publicKey);
		t.setReceiver(kp1.publicKey);
		t.setAmount(10);
		t.sign(kp0.privateKey);
                Block b = new BlockImp();
		b.setMiner(kp1.publicKey);
		b.setTransaction(t);
		b.setPrevHash(Blockchain.initHash);
		b.mine();
		System.out.println("Nonce: " + b.getNonce());
		System.out.println("Hash valid: " + b.isHashValid()+"\tSignsture Valid:"+t.isSignatureValid());
		t.setAmount(20); 
		b.setTransaction(t); // Change block
		System.out.println("Hash valid: " + b.isHashValid()+"\tSignsture Valid:"+t.isSignatureValid());
                
	}
	
	public static void example4() {
		System.out.println("\n\nExample 4:-------------------------------------------------");
		// Create users
		int n = 10;
		KPair[] kps = new KPair[10];
		for (int i = 0; i < n; i++) {
			kps[i] = Utils.getKeyPair();
		}
		print(kps);

		// Create block chain
		Blockchain bc = new BlockchainImp();
		{
			Block b0 = new BlockImp();
			b0.setMiner(kps[0].publicKey);
			b0.setPrevHash(bc.getLastBlockHash());
			b0.mine();
			bc.addBlock(b0);
			printBalance(bc, kps);
                }
		Utils.write(bc.getBlocks(), "peer1.txt");

		{
			Block b1 = new BlockImp();
			Transaction t1 = new TransactionImp();
			t1.setSender(kps[0].publicKey);
			t1.setReceiver(kps[1].publicKey);
			t1.setAmount(10);
			t1.sign(kps[0].privateKey);
			b1.setMiner(kps[0].publicKey);
			b1.setTransaction(t1);
			b1.setPrevHash(bc.getLastBlockHash());
			b1.mine();
			bc.addBlock(b1);
			printBalance(bc, kps);

			Block b2 = new BlockImp();
			Transaction t2 = new TransactionImp();
			t2.setSender(kps[0].publicKey);
			t2.setReceiver(kps[2].publicKey);
			t2.setAmount(30);
			t2.sign(kps[0].privateKey);
			b2.setMiner(kps[0].publicKey);
			b2.setTransaction(t2);
			b2.setPrevHash(bc.getLastBlockHash());
			b2.mine();
			bc.addBlock(b2);
			printBalance(bc, kps);

			Block b3 = new BlockImp();
			Transaction t3 = new TransactionImp();
			t3.setSender(kps[0].publicKey);
			t3.setReceiver(kps[3].publicKey);
			t3.setAmount(20);
			t3.sign(kps[0].privateKey);
			b3.setMiner(kps[1].publicKey);
			b3.setTransaction(t3);
			b3.setPrevHash(bc.getLastBlockHash());
			b3.mine();
			bc.addBlock(b3);
			printBalance(bc, kps);
		}
		Utils.write(bc.getBlocks(), "peer2.txt");

		{
			Block b1 = new BlockImp();
			Transaction t1 = new TransactionImp();
			t1.setSender(kps[2].publicKey);
			t1.setReceiver(kps[3].publicKey);
			t1.setAmount(10);
			t1.sign(kps[2].privateKey);
			b1.setMiner(kps[6].publicKey);
			b1.setTransaction(t1);
			b1.setPrevHash(Blockchain.initHash); // Error here, wrong previous block hash
			b1.mine();
			bc.addBlock(b1);
			printBalance(bc, kps);

			Block b2 = new BlockImp();
			Transaction t2 = new TransactionImp();
			t2.setSender(kps[0].publicKey);
			t2.setReceiver(kps[2].publicKey);
			t2.setAmount(30);
			t2.sign(kps[0].privateKey);
			b2.setMiner(kps[0].publicKey);
			b2.setTransaction(t2);
			b2.setPrevHash(bc.getLastBlockHash());
			b2.mine();
			b2.setNonce(0); // Error here: block hash not valid
			bc.addBlock(b2);
			printBalance(bc, kps);

			Block b3 = new BlockImp();
			Transaction t3 = new TransactionImp();
			t3.setSender(kps[8].publicKey); // Error here: not enough credit
			t3.setReceiver(kps[3].publicKey);
			t3.setAmount(20);
			t3.sign(kps[8].privateKey);
			b3.setMiner(kps[1].publicKey);
			b3.setTransaction(t3);
			b3.setPrevHash(bc.getLastBlockHash());
			b3.mine();
			bc.addBlock(b3);
			printBalance(bc, kps);

			Block b4 = new BlockImp();
			Transaction t4 = new TransactionImp();
			t4.setSender(kps[2].publicKey);
			t4.setReceiver(kps[3].publicKey);
			t4.setAmount(10);
			t4.sign(kps[0].privateKey);  // Error here, wrong signature (not by sender)
			b4.setMiner(kps[6].publicKey);
			b4.setTransaction(t4);
			b4.setPrevHash(bc.getLastBlockHash());
			b4.mine();
			bc.addBlock(b4);
			printBalance(bc, kps);

                }
		Utils.write(bc.getBlocks(), "peer3.txt");

		bc.removeInvalid();
		printBalance(bc, kps);
		Utils.write(bc.getBlocks(), "peer4.txt");

	}

	public static void main(String[] args) {
		example1();
		example2();
		example3();
		example4();
	}
}

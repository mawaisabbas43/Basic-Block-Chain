import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class Utils {

	// Generate a pair of public/private keys.
	public static KPair getKeyPair() {
		KPair rkp = null;
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(512);
			KeyPair kp = keyGen.genKeyPair();
			rkp = new KPair(kp.getPublic().getEncoded(), kp.getPrivate().getEncoded());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rkp;
	}

	// Private method used in this class.
	private static PublicKey getPublicKey(byte[] pbkb) {
		PublicKey pbk = null;
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(512);
			pbk = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pbkb));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pbk;
	}

	// Private method used in this class.
	private static PrivateKey getPrivateKey(byte[] pvkb) {
		PrivateKey pvk = null;
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(512);
			pvk = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(pvkb));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pvk;
	}

	// Concatenate two byte arrays.
	public static byte[] concat(byte[] a, byte[] b) {
		int al = 0;
		int bl = 0;
		if (a != null) {
			al = a.length;
		}
		if (b != null) {
			bl = b.length;
		}
		byte[] c = new byte[al + bl];
		for (int i = 0; i < al; i++) {
			c[i] = a[i];
		}
		for (int i = 0; i < bl; i++) {
			c[i + a.length] = b[i];
		}
		return c;
	}

	// Convert an int to byte[].
	public static byte[] toBytes(int k) {
		return BigInteger.valueOf(k).toByteArray();
	}

	// Return the hash value for a given input.
	public static byte[] getHash(byte[] input) {
		byte[] hv = null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			hv = digest.digest(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hv;
	}

	// Convert byte to hexadecimal string format.
	public static String toHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X ", b));
		}
		return sb.toString();
	}

	// Check if a block hash value is valid.
	public static boolean validBlockHash(byte[] hv) {
		if (hv == null || hv.length != 256 / 8) {
			return false;
		}
		return (hv[0] == 0);
	}

	// Sign input using using the private key pvkb. The signature is returned.
	public static byte[] sign(byte[] input, byte[] pvkb) {
		byte[] signature = null;
		try {
			PrivateKey pvk = getPrivateKey(pvkb);
			Signature sign;
			sign = Signature.getInstance("SHA256withRSA");
			sign.initSign(pvk);
			sign.update(input);
			signature = sign.sign();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signature;
	}

	// Check that the given signature is valid for the given input using the public
	// key pbkb.
	public static boolean isSignatueValid(byte[] input, byte[] signature, byte[] pbkb) {
		try {
			PublicKey pbk = getPublicKey(pbkb);
			Signature sign = Signature.getInstance("SHA256withRSA");
			sign.initVerify(pbk);
			sign.update(input);
			return sign.verify(signature);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Write blockchain to file and return true if successful, false otherwise. Use
	// Utils.toHex to format byte arrays.
	public static boolean write(List<Block> chain, String fileName) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(fileName);
			if (!chain.empty()) {
				chain.findFirst();
				while (!chain.last()) {
					Block b = chain.retrieve();
					writer.println("Miner: " + Utils.toHex(b.getMiner()));
					writer.println("Nonce: " + b.getNonce());
					Transaction t = b.getTransaction();
					if (t != null) {
						writer.println("Transaction: " + true);
						writer.println("Sender: " + Utils.toHex(t.getSender()));
						writer.println("Receiver: " + Utils.toHex(t.getReceiver()));
						writer.println("Amount: " + t.getAmount());
						writer.println("Signature: " + Utils.toHex(t.getSignature()));
					} else {
						writer.println("Transaction: " + false);
					}
					writer.println("Previous block hash: " + Utils.toHex(b.getPrevHash()));
					writer.println("Hash: " + Utils.toHex(b.getHash()));
					writer.println();
					chain.findNext();
				}
				Block b = chain.retrieve();
				writer.println("Miner: " + Utils.toHex(b.getMiner()));
				writer.println("Nonce: " + b.getNonce());
				Transaction t = b.getTransaction();
				if (t != null) {
					writer.println("Transaction: " + true);
					writer.println("Sender: " + Utils.toHex(t.getSender()));
					writer.println("Receiver: " + Utils.toHex(t.getReceiver()));
					writer.println("Amount: " + t.getAmount());
					writer.println("Signature: " + Utils.toHex(t.getSignature()));
				} else {
					writer.println("Transaction: " + false);
				}
				writer.println("Previous block hash: " + Utils.toHex(b.getPrevHash()));
				writer.println("Hash: " + Utils.toHex(b.getHash()));
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}
}

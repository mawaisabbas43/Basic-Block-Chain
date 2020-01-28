import java.nio.ByteBuffer;

public class BlockchainImp implements Blockchain {
        private List<Block> blocks;
        private int length;
	public BlockchainImp() {
            blocks=new LinkedList<Block>();
            length=0;
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public List<Block> getBlocks() {
		return blocks;
	}

	@Override
	public int getBalance(byte[] pbk) {
            int sent=0;
            int receive=0;
            int earn=0;
            blocks.findFirst();
            Transaction transaction;
            if(!blocks.empty()){
                while(!blocks.last()){
                    transaction=blocks.retrieve().getTransaction();
                    if(transaction==null)
                    {
                        if(blocks.retrieve().getMiner().equals(pbk)){
                            earn+=100;
                        }
                    }else{
                        if(transaction.getReceiver().equals(pbk))
                        {
                            receive+=transaction.getAmount();
                        }else if(transaction.getSender().equals(pbk))
                        {
                            sent+=transaction.getAmount();
                        }
                        //allso for miner
                        if(blocks.retrieve().getMiner().equals(pbk))
                        {
                            earn+=100;
                        }
                    }
                    blocks.findNext();
                }
            }
            //for last block repeat above
                transaction=blocks.retrieve().getTransaction();
                if(transaction==null)
                {
                    if(blocks.retrieve().getMiner().equals(pbk)){
                            earn+=100;
                    }
                }else{
                    if(transaction.getReceiver().equals(pbk))
                    {
                        receive+=transaction.getAmount();
                    }else if(transaction.getSender().equals(pbk))
                    {
                        sent+=transaction.getAmount();
                    }
                    if(blocks.retrieve().getMiner().equals(pbk))
                    {
                        earn+=100;
                    }
                }
                
            int balance=(earn+receive)-sent;
            if(balance<0)
                return -1;
            else
		return balance;
	}

	@Override
	public void removeInvalid() {
            Map<ByteBuffer, Integer> balanceMap=new BST<ByteBuffer, Integer>();
            
            if(blocks.empty())
                return;
            else
                blocks.findFirst();
            int count=0,bNum=0;
            byte[] previousHash=initHash;
            if(!blocks.empty()){
                while(!blocks.last()){
                //==========Checking validity of sending money and balaance========
                    Block block=blocks.retrieve();
                    byte[] miner=block.getMiner();
                    byte[] receiver=null;
                    byte[] sender=null;
                    int amount=0;
                    if(block.getTransaction()==null && balanceMap.empty())
                    {

                        balanceMap.insert(ByteBuffer.wrap(miner), 100);
                    }else{
                         receiver=block.getTransaction().getReceiver();
                        sender=block.getTransaction().getSender();
                        amount=block.getTransaction().getAmount();
                        //Sender balance
                        if(balanceMap.find(ByteBuffer.wrap(sender))){
                            if(balanceMap.retrieve()>=amount){//sender have enough money
                                balanceMap.update(balanceMap.retrieve()-amount);
                            }
                            else{//sender have insufficient money
                                count=deleteBlocks();
//                                System.out.println("Deleted: sender have insufficient money");
                                length-=count;
                                return;
                            }
                        }else{//sender do not have money
                            count=deleteBlocks();
//                            System.out.println("Deleted:sender do not have money");
                            length-=count;
                            return;
                        }

                        //receiver balance
                        if(balanceMap.find(ByteBuffer.wrap(receiver))){
                            balanceMap.update(balanceMap.retrieve()+amount);//updating reciever balance
                        }else{
                            balanceMap.insert(ByteBuffer.wrap(receiver), amount);//inserting sender balance
                        }
                        //Miner balance
                        if(balanceMap.find(ByteBuffer.wrap(miner))){
                            balanceMap.update(balanceMap.retrieve()+100);//updating miner balance
                        }else{
                            balanceMap.insert(ByteBuffer.wrap(miner), 100);//inserting sender balance
                        }


                    }

                //Checking validity of hash and signature
                    if(!isValid(blocks.retrieve()))
                    {
                        count=deleteBlocks();
//                        System.out.println("Deleted:Not Valid");
                        length-=count;
                        return;
                    }
                    //checking precious hash
                    bNum++;
                    if(bNum!=1){
                        if(!blocks.retrieve().getPrevHash().equals(previousHash)){
                        count=deleteBlocks();
//                        System.out.println("Deleted:Not Valid previous hash.");
                        length-=count;
                        return;
                        }
                    }
                    previousHash=null;
                    previousHash=blocks.retrieve().getHash();
                    blocks.findNext();
                }
            }
            //for last block repeat above
            //==========Checking validity of sending money and balaance========
                Block block=blocks.retrieve();
                byte[] miner=block.getMiner();
                byte[] receiver=null;
                byte[] sender=null;
                int amount=0;
                if(block.getTransaction()==null && balanceMap.empty())
                {
                   
                    balanceMap.insert(ByteBuffer.wrap(miner), 100);
                }else{
                     receiver=block.getTransaction().getReceiver();
                    sender=block.getTransaction().getSender();
                    amount=block.getTransaction().getAmount();
                    //Sender balance
                    if(balanceMap.find(ByteBuffer.wrap(sender))){
                        if(balanceMap.retrieve()>=amount){//sender have enough money
                            balanceMap.update(balanceMap.retrieve()-amount);
                        }
                        else{//sender have insufficient money
                            count=deleteBlocks();
//                            System.out.println("Deleted: sender have insufficient money");
                            length-=count;
                            return;
                        }
                    }else{//sender do not have money
                        count=deleteBlocks();
//                        System.out.println("Deleted:sender do not have money");
                        length-=count;
                        return;
                    }
                    
                    //receiver balance
                    if(balanceMap.find(ByteBuffer.wrap(receiver))){
                        balanceMap.update(balanceMap.retrieve()+amount);//updating reciever balance
                    }else{
                        balanceMap.insert(ByteBuffer.wrap(receiver), amount);//inserting sender balance
                    }
                    //Miner balance
                    if(balanceMap.find(ByteBuffer.wrap(miner))){
                        balanceMap.update(balanceMap.retrieve()+100);//updating miner balance
                    }else{
                        balanceMap.insert(ByteBuffer.wrap(miner), 100);//inserting sender balance
                    }
                    
                    
                }
                
            //Checking validity of hash and signature
                if(!isValid(blocks.retrieve()))
                {
                    count=deleteBlocks();
//                    System.out.println("Deleted:Not Valid");
                    length-=count;
                    return;
                }
                //checking precious hash
                bNum++;
                    if(bNum!=1){
                        if(!blocks.retrieve().getPrevHash().equals(previousHash)){
                        count=deleteBlocks();
//                        System.out.println("Deleted:Not Valid previous hash.");
                        length-=count;
                        return;
                        }
                    }
                    previousHash=null;
                    previousHash=blocks.retrieve().getHash();
                
	}
        
        private boolean isValid(Block block){
            if(!block.isHashValid()){
                return false;
            }
            if(block.getTransaction()!=null){
                if(!block.getTransaction().isSignatureValid()){
                    return false;
                }
            }
                return true;
        }
        
        private int deleteBlocks(){
            int count=0;
           if(!blocks.empty()){
                while(!blocks.last())
                {
                    blocks.remove();
                    count++;
                }
            
                blocks.remove();//remove last also
                count++;//count last also
            }
            return count;
                    
        }

	@Override
	public boolean addBlock(Block b) {
            if(length!=0){
                while(!blocks.last()){
                    blocks.findNext();
                }
            }
            if(length==0){
                blocks.insert(b);
                length++;
                return true;
            }else if(b.getTransaction()!=null){
                blocks.insert(b);
                length++;
                return true;
            }
            else{
             return false;   
            }
	}

	@Override
	public byte[] getLastBlockHash() {
            if(length==0||blocks.empty()){
                return initHash;
            }else{
                while(!blocks.last()){
                    blocks.findNext();
                }
                    return blocks.retrieve().getHash();
            }
	}
}

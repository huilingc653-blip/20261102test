package com.bytter.core.sequence.transaction;
import org.redisson.api.RTransaction;
/**
 * @author zhoul
 */
public class RedisTransactionHolder extends MemoryTransactionHolder {
   private RTransaction transaction;
   public RedisTransactionHolder(RTransaction transaction) {
       super();
       this.transaction = transaction;
   }
   public RTransaction getTransaction() {
       return transaction;
   }
   public void setTransaction(RTransaction transaction) {
       this.transaction = transaction;
   }
}

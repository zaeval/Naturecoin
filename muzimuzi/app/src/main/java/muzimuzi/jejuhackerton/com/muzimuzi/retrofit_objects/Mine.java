package muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects;

import java.util.List;

/**
 * Created by hongseung-ui on 2017. 10. 28..
 */

public class Mine {
    /*
    {
  "index": 3,
  "message": "New Block Forged",
  "previous_hash": "4600d27d90865cf283334e31701d86255d111f05eccdc47c672ad2a19676efb9",
  "proof": 35089,
  "transactions": [
    {
      "amount": 1,
      "recipient": "859573d29edb43da8740a2c6c90c134c",
      "sender": "0"
    }
  ]
}
     */
    int index;
    String message;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPrevious_hash() {
        return previous_hash;
    }

    public void setPrevious_hash(String previous_hash) {
        this.previous_hash = previous_hash;
    }

    public int getProof() {
        return proof;
    }

    public void setProof(int proof) {
        this.proof = proof;
    }

    public List<Transaction> getTransactions() {
        return Transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        Transactions = transactions;
    }

    String previous_hash;
    int proof;
    List<Transaction> Transactions;

}

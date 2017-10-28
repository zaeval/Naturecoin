package muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects;

import java.util.List;

/**
 * Created by hongseung-ui on 2017. 10. 28..
 */

public class ChainObject {
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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

    public float getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(float timestamp) {
        this.timestamp = timestamp;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /*
         "index": 1,
          "previous_hash": "1",
          "proof": 100,
          "timestamp": 1509134285.2403717,
          "transactions": []
         */
    int index;
    String previous_hash;
    int proof;
    float timestamp;
    List<Transaction> transactions;
}

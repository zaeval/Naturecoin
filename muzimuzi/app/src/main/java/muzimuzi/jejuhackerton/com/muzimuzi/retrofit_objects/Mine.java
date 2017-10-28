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
    String previous_hash;
    int proof;
    List<Transaction> Transactions;

}

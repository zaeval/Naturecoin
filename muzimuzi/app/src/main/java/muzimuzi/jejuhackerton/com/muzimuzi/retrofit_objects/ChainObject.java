package muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects;

import java.util.List;

/**
 * Created by hongseung-ui on 2017. 10. 28..
 */

public class ChainObject {
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

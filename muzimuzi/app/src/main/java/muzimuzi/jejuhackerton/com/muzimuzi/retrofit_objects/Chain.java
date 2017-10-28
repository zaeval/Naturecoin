package muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects;

import java.util.List;

/**
 * Created by hongseung-ui on 2017. 10. 28..
 */

public class Chain {
    List<ChainObject> chain;
    int length;

    public List<ChainObject> getChain() {
        return chain;
    }

    public void setChain(List<ChainObject> chain) {
        this.chain = chain;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
    /*
    {

  "chain": [
    {
      "index": 1,
      "previous_hash": "1",
      "proof": 100,
      "timestamp": 1509134285.2403717,
      "transactions": []
    },
    {
      "index": 2,
      "previous_hash": "2bd40ebb49c71158bd263f7e616aba1d95caf52fb797bc4a4fb036f817dd9cd1",
      "proof": 35293,
      "timestamp": 1509134286.4869506,
      "transactions": [
        {
          "amount": 1,
          "recipient": "859573d29edb43da8740a2c6c90c134c",
          "sender": "0"
        }
      ]
    },
    {
      "index": 3,
      "previous_hash": "4600d27d90865cf283334e31701d86255d111f05eccdc47c672ad2a19676efb9",
      "proof": 35089,
      "timestamp": 1509134488.4817765,
      "transactions": [
        {
          "amount": 1,
          "recipient": "859573d29edb43da8740a2c6c90c134c",
          "sender": "0"
        }
      ]
    }
  ],
  "length": 3
}
     */

}

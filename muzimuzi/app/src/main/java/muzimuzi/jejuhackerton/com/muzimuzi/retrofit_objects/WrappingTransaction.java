package muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects;

public class WrappingTransaction extends Transaction{
    public WrappingTransaction(Transaction tr,float timestamp){
        super(tr);
        this.timestamp = timestamp;

    }

    public float getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(float timestamp) {
        this.timestamp = timestamp;
    }

    float timestamp;
}

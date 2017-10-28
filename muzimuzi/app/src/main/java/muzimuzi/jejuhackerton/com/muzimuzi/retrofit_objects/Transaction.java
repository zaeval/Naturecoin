package muzimuzi.jejuhackerton.com.muzimuzi.retrofit_objects;

/**
 * Created by hongseung-ui on 2017. 10. 28..
 */

public class Transaction {
    float amount;
    String recipient;
    String sender;

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}

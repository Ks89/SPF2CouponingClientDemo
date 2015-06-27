package it.polimi.spf.demo.couponing.client.detail;

/**
 * Created by Stefano Cappa on 22/06/15.
 */

public class InputException extends Exception {

    private int mMessageResId;

    public InputException(int messageResId) {
        super();
        mMessageResId = messageResId;
    }

    public int getMessageResId() {
        return mMessageResId;
    }
}
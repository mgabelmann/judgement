package ca.mikegabelmann.judgement.batch;

/**
 *
 */
public abstract class AbstractBatchJob {
    private boolean disable = false;

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

}

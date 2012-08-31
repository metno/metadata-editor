package no.met.metadataeditor.dataTypes;


public class DataAttributeValidationResult {

    public final boolean isValid;

    public final String errorMsg;

    public DataAttributeValidationResult(boolean isValid, String errorMsg) {
        this.isValid = isValid;
        this.errorMsg = errorMsg;
    }

    @Override
    public boolean equals(Object o){

        if( !( o instanceof DataAttributeValidationResult ) ){
            return false;
        }

        DataAttributeValidationResult davr = (DataAttributeValidationResult) o;

        if( this.isValid != davr.isValid ){
            return false;
        }

        if( this.errorMsg == null && davr.errorMsg != null ){
            return false;
        }

        if( this.errorMsg != null && davr.errorMsg == null ){
            return false;
        }

        if( this.errorMsg == null && davr.errorMsg == null ){
            return true;
        }

        return this.errorMsg.equals(davr.errorMsg);

    }

    @Override
    public int hashCode(){

        int result = 17;
        result = 31 * result + (isValid ? 0 : 1);
        result = 31 * result + (errorMsg != null ? errorMsg.hashCode() : 0);

        return result;

    }

    @Override
    public String toString(){
        return "[ isValid: " + isValid + ", errorMsg: " + errorMsg + " ]";
    }

}

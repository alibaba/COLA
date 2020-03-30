/**
 * Validator Interface
 * @author fulan.zjf 2017-11-04
 */
public interface ValidatorI<T> {

    /**
     * Validate candidate, throw according exception if failed
     * @param candidate
     */
    public void validate(T candidate);

}
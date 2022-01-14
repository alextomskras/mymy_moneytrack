package com.dreamer.mymy_moneytrack.util.validator;


public interface IValidator<T> {
    long MAX_ABS_VALUE = Integer.MAX_VALUE * 1024L;

    /**
     * @return true if validation passed or false otherwise
     */
    boolean validate();
}

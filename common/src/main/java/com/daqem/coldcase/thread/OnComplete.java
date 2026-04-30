package com.daqem.coldcase.thread;

@FunctionalInterface
public interface OnComplete<T> {

    void onComplete(T type);
}

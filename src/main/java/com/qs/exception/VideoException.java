package com.qs.exception;

/**
 * @author FBin
 * @time 2018/12/3 21:10
 */
public class VideoException extends RuntimeException {

    public VideoException(){
        super();
    }

    public VideoException(String message){
        super(message);
    }

    public VideoException(String message, Throwable throwable){
        super(message, throwable);
    }

}

package com.vuclip.abtesthttp.exception;

/**
 * Created by Administrator on 2015/5/15.
 */
public class AppException extends RuntimeException {
    private static final long serialVersionUID = -6866265499195848091L;

    private String msg;

    public AppException(String msg) {
        super();
        this.msg = msg;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Exception exp, String msg) {
        super(exp);
        this.msg = msg;
    }

    public String getMessage() {
        return msg;
    }
}

package com.mz.nlpservice.model;

/**
 * Created by meizu on 2016/7/19.
 */
public class Status {
    public final static  int SUCCESS = 0;

    public final static  int ERROR_OTHER = 1;

    public final static int ERROR_SERVER=1000;

    public final static int ERROR_VERIFY=1001;

    public final static int ERROR_NO_LICENSE=1002;

    public final static int ERROR_NO_CLIENT=1003;

    public final static int ERROR_ENCODING=1004;

    public final static int ERROR_PARAMETER=1005;

    public final static int ERROR_UNSUPPORT_METHOD=1006;

    public final static int ERROR_SALT=1007;

    public final static int UN_HANDLE = 2000;

    public final static int HANDLE_ERR = 2001;

}

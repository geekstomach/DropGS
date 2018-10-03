package com.geekstomach.cloud.common.protocol;

public final class Command {

    public static final short START_MSG = 0;
    public static final short FINISH_MSG = 1;
    public static final byte AUTH = 2;
    public static final short AUTH_OK = 3;//надо попытаться возващать статусы отдельным перечислением наверно
    public static final short AUTH_FAILED = 4;
    public static final short USER_DELETE = 5;
    public static final short USER_CHANGE_USERNAME = 6;
    public static final short USER_CHANGE_PASSWORD = 7;
    public static final short RESPONSE = 8;
    public static final short REGISTER = 9;
//handel files
public static final short FILE_UPLOAD = 10;
    public static final short FILE_DOWNLOAD = 11;
    public static final short FILE_GET_LIST = 12;
    public static final short FILE_DELETE_FILE = 13;

//handle cloud
    public static final short STORAGE_UPDATE = 20;


//Status massage
    public static final short STATUS_OK = 90;//надо попытаться возващать статусы отдельным перечислением наверно
    public static final short STATUS_FAILED = 91;
    public static final short STATUS_NO_SUCH_FILE = 92;

    public static final int LAST_COMMAND_INDEX = 100;

    private Command() {
    }

}

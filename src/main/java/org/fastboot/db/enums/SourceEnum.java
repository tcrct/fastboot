package org.fastboot.db.enums;

public enum SourceEnum {

    CONSOLE("console", "console","控制台"),
    PHONE("phone", "phone","手机端"),
    INTERNAL_APPLICATION("InternalApplication", "InternalApplication","内部应用"),

    ;
    /**
     * mongodb的key
     */
    private final String mkey;
    /**
     * mysql的key
     */
    private final String skey;
    /**
     * 说明
     */
    private final String desc;

    /**
     * Constructor.
     */
    private SourceEnum(String mkey, String skey, String desc) {
        this.mkey = mkey;
        this.skey = skey;
        this.desc = desc;
    }

    /**
     * Get the value.
     *
     * @return the value
     */
    public String getMkey() {
        return mkey;
    }

    public String getSkey() {
        return skey;
    }

    public String getDesc() {
        return desc;
    }

}

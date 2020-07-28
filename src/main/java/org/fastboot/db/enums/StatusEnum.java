package org.fastboot.db.enums;

public enum StatusEnum {



    PASS("审核通过", 0,"持久状态"),
    DELETE("已删除", 1,"逻辑删除状态"),

    ;
    /**
     * mongodb的key
     */
    private final String mkey;
    /**
     * mysql的key
     */
    private final Integer skey;
    /**
     * 说明
     */
    private final String desc;

    /**
     * Constructor.
     */
    private StatusEnum(String mkey, Integer skey, String desc) {
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

    public Integer getSkey() {
        return skey;
    }

    public String getDesc() {
        return desc;
    }

}

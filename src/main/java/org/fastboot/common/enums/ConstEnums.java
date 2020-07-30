package org.fastboot.common.enums;

public enum ConstEnums {
    ;

    /**
     * 公用常量枚举
     */
    public enum COMMON implements IEnum {

        UTF8("UTF-8", "统一编码"),
        DATETIME_FORMAT("yyyy-MM-dd HH:mm:ss", "时间格式"),
        ;
        private final String value;
        private final String desc;
        private COMMON(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }
        @Override
        public String getValue() {
            return value;
        }
        @Override
        public String getDesc() {
            return desc;
        }
    }

    /**
     * BaseController类枚举
     */
    public enum BASE_CONTROLLER_PARAM implements IEnum {
        
        ID("id", "BaseController类里findById时使用的参数名称"),
        
        ;
        private final String value;
        private final String desc;
        private BASE_CONTROLLER_PARAM(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }
        @Override
        public String getValue() {
            return value;
        }
        @Override
        public String getDesc() {
            return desc;
        }
    }


}

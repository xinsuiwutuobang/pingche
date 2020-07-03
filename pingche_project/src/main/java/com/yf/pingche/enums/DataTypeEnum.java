package com.yf.pingche.enums;

/**
 * <p>
 *  数据类型
 * </p>
 *
 * @author yangfei
 * @since 2019-05-18
 */
public enum DataTypeEnum {
    RESOURCE_INFO("1101","资源信息上报"),
    SUB_RESOURCE("1102","子资源信息上报"),
    RESOURCE_PERFORMANCE("1201","资源性能信息上报"),
    PROPERTY_WARM("1301","资源告警信息上报"),
    SYNERGY_OPS_UP("1401","协同运维信息上报"),
    SYNERGY_OPS_DW("1402","协同运维信息下发"),
    NODE_UP("9902", "接入节点信息上报");
    String key;
    String value;

    public static String getValueByKey(String code) {
        for (DataTypeEnum value : values()) {
            if (code.equals(value.getKey())) {
                return value.getValue();
            }
        }
        return null;
    }

    public static String getKeyByValue(String value) {
        for (DataTypeEnum element : values()) {
            if (value.equals(element.getValue())) {
                return element.getKey();
            }
        }
        return null;
    }
    DataTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

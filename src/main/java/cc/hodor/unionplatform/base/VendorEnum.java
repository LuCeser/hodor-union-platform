package cc.hodor.unionplatform.base;

/***************************************************************************************
 *
 *  Project:        hodor
 *
 *  Copyright ©     
 *
 ***************************************************************************************
 *
 *  Header Name: WellJoint
 *
 *  Description: 
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2018/12/27-18:15
 *
 ****************************************************************************************/
public enum VendorEnum implements BaseEnum {

    ALI(1, "ali"),BAIDU(2, "baidu"),HUAWEI(3, "huawei"),TENCENT(4, "tencent"),XUNFEI(5, "xunfei");
    private int code;
    private String desc;

    VendorEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static final String getDesc(String name) {
        return VendorEnum.valueOf(name).desc;
    }

    public static final int getCode(String name) {
        return VendorEnum.valueOf(name).code;
    }

    public static VendorEnum valueOf(int code) {
        switch (code) {
            case 1:
                return ALI;
            case 2:
                return BAIDU;
            case 3:
                return HUAWEI;
            case 4:
                return TENCENT;
            case 5:
                return XUNFEI;
        }
        return null;
    }

    @Override
    public int getCode() {
        return code;
    }
}

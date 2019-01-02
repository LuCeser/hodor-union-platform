package cc.hodor.unionplatform.util;

import cc.hodor.unionplatform.base.BaseEnum;

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
 *  使用一个枚举工作类来完成从枚举 code 值获得枚举实例的工作
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/2-15:45
 *
 ****************************************************************************************/
public class EnumUtils {

    public static <T extends Enum<?> & BaseEnum> T codeOf(Class<T> enumClass, int code) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T t : enumConstants) {
            if (t.getCode() == code) {
                return t;
            }
        }
        return null;
    }

}

package cc.hodor.unionplatform.web.asr;

import cc.hodor.unionplatform.base.constant.VendorEnum;
import lombok.Getter;
import lombok.Setter;

/***************************************************************************************
 *
 *  Project:        hodor
 *
 *  Copyright ©     
 *
 ***************************************************************************************
 *
 *
 *  Description: 
 *  ${DESCRIPTION}
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/8-17:34
 *
 ****************************************************************************************/
@Getter
@Setter
public class AsrDTO {

    /**
     * 使用的识别引擎类型
     */
    private VendorEnum engine;

    /**
     * 结果回调地址
     */
    private String callbackUrl;

    /**
     * 使用的对象存储类型
     */
    private VendorEnum ossType;

    /**
     * 任务并发数量
     */
    private int concurrentNumber;

    /**
     * 是否为免费账户
     */
    private boolean freeAccount;

    /**
     * file directory, if use local directory
     */
    private String fileDirectory;

    /**
     * use file from marker
     */
    private String marker;

}

package cc.hodor.unionplatform.web.authentication;

import cc.hodor.unionplatform.base.constant.VendorEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

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
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/2-11:59
 *
 ****************************************************************************************/
@Setter
@Getter
public class AuthenticationDTO {

    private VendorEnum engine;

    private String appAccessKey;

    private String appAccessSecret;

    private String appId;

    private Map extendInfo;

}

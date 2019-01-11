package cc.hodor.unionplatform.service.authentication;

import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.model.AuthenticationDO;
import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.web.authentication.AuthenticationDTO;

import java.util.List;
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
 *   zhanglu               2019/1/2-14:49
 *
 ****************************************************************************************/
public interface IAuthenticationService {
    ServiceResult createAuthentication(AuthenticationDTO authenticationDTO);

    ServiceResult createAuthentication(List<AuthenticationDTO> authenticationDTOList);

    ServiceResult updateAuthentication(String engine, Map param);

    ServiceResult getAllAuthentication();

    ServiceResult<AuthenticationDO> getAuthentication(VendorEnum engine);

    ServiceResult deleteAuthentication(String engine);
}

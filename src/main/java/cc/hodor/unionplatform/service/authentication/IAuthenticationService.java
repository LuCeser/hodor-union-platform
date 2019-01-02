package cc.hodor.unionplatform.service.authentication;

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
 *  Header Name: WellJoint
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

    ServiceResult getAuthentication(String engine);

    ServiceResult deleteAuthentication(String engine);
}

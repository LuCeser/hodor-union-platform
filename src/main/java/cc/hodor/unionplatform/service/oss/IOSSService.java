package cc.hodor.unionplatform.service.oss;

import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.web.oss.OSSDTO;

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
 *  ${DESCRIPTION}
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/8-9:05
 *
 ****************************************************************************************/
public interface IOSSService {
    ServiceResult uploadFile(OSSDTO ossDTO);
}

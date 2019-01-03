package cc.hodor.unionplatform.service;

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
 *  Header Name: WellJoint
 *
 *  Description: 
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2018/12/28-9:46
 *
 ****************************************************************************************/
@Setter
@Getter
public class ServiceResult<T> {

    private boolean success;

    private T data;


}

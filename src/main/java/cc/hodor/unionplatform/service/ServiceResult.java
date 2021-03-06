package cc.hodor.unionplatform.service;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class ServiceResult<T> {

    public ServiceResult(boolean success) {
        this.success = success;
    }

    private boolean success;

    private T data;


}

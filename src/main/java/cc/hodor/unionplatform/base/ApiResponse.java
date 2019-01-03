package cc.hodor.unionplatform.base;

import lombok.AllArgsConstructor;
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
 *   zhanglu               2018/12/28-9:42
 *
 ****************************************************************************************/
@Setter
@Getter
@AllArgsConstructor
public class ApiResponse {

    private int code;
    private String message;

    public ApiResponse(int code) {
        this.code = code;
    }

}

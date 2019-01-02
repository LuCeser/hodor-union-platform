package cc.hodor.unionplatform.model;

import cc.hodor.unionplatform.web.authentication.AuthenticationDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

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
 *   zhanglu               2019/1/2-11:53
 *
 ****************************************************************************************/
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "c_authentication")
public class AuthenticationDO extends AuthenticationDTO implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    private String id;

}

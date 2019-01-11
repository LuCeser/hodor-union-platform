package cc.hodor.unionplatform.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

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
 *   zhanglu               2019/1/9-9:33
 *
 ****************************************************************************************/
@Getter
@Setter
@Table(name = "t_record")
public class RecordDO implements Serializable {

    @Id
    private long id;

    private String filename;

    private Date date;

    private String owner;

    private String uniqueId;
}

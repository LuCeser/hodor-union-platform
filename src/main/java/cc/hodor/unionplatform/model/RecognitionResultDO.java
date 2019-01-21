package cc.hodor.unionplatform.model;

import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.base.entity.Sentence;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

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
 *   zhanglu               2019/1/10-15:27
 *
 ****************************************************************************************/
@Setter
@Getter
@Table(name = "fact_recognition_result")
public class RecognitionResultDO {

    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;

    private long fileId;

    private VendorEnum engine;

    private long duration;

    private List<Sentence> result;

    private Date createTime;

    private long recognitionDuration;

}

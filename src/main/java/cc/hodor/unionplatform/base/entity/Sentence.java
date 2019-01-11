package cc.hodor.unionplatform.base.entity;

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
 *  ${DESCRIPTION}
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/10-18:26
 *
 ****************************************************************************************/
@Setter
@Getter
public class Sentence {

    private long beginTime;

    private long endTime;

    private long silenceDuration;

    private int channelId;

    private int speechRate;

    private int emotionValue;

    private String text;

}

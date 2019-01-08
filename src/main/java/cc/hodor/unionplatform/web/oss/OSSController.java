package cc.hodor.unionplatform.web.oss;

import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.service.oss.IOSSService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
 *  ${DESCRIPTION}
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/7-16:01
 *
 ****************************************************************************************/
@Controller
@RequestMapping("v1.0/oss")
public class OSSController {

    private IOSSService ossService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity uploadFile(@RequestBody OSSDTO ossDTO) {
        ServiceResult ret = ossService.uploadFile(ossDTO);
        return null;
    }

    public OSSController(IOSSService ossService) {
        this.ossService = ossService;
    }
}

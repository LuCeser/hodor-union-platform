package cc.hodor.unionplatform.core;

import cc.hodor.unionplatform.core.entity.OSSResult;
import cc.hodor.unionplatform.model.AuthenticationDO;
import cc.hodor.unionplatform.model.RecordDO;
import cc.hodor.unionplatform.service.asr.IAsrService;
import cc.hodor.unionplatform.util.OSSUtils;
import cc.hodor.unionplatform.web.asr.AsrDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

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
 *   zhanglu               2019/1/18-11:59
 *
 ****************************************************************************************/
@Slf4j
@RequiredArgsConstructor
public class TaskProducer implements Runnable {

    @NonNull
    private AsrDTO asrDTO;
    @NonNull
    private AuthenticationDO authenticationDO;

    @NonNull
    private IAsrService asrService;

    private static ExecutorService executorService = Executors.newFixedThreadPool(64);

    @Override
    public void run() {


        // 创建阻塞队列，控制长语音识别任务并发
        // 考虑到并发限制只存在于引擎，因此队列只需要在不同引擎下新建
        BlockingQueue<BaseCloudTask> taskQueue = new LinkedBlockingQueue<>(asrDTO.getConcurrentNumber());

        String appId = authenticationDO.getAppId();
        String appAccessKey = authenticationDO.getAppAccessKey();
        String appAccesSecret = authenticationDO.getAppAccessSecret();

        String endpoint = (String) authenticationDO.getExtendInfo().get("endpoint");
        String bucketName = (String) authenticationDO.getExtendInfo().get("bucket");
        Date expiration = new Date(new Date().getTime() + 3600 * 1000); // 设置临时URL有效时间为1小时
        OSSResult ossResult = OSSUtils.generatePresignedUrls(endpoint, appAccessKey, appAccesSecret,
                bucketName, "", asrDTO.getConcurrentNumber(), expiration);

        List<TaskConsumer> consumerList = new ArrayList<>(asrDTO.getConcurrentNumber());
        for (int i = 0; i < asrDTO.getConcurrentNumber(); i++) {
            TaskConsumer taskConsumer = new TaskConsumer(taskQueue);
            executorService.execute(taskConsumer);
        }

        while (!StringUtils.isEmpty(ossResult.getNextMarker())) {
            for (Map fileUrlMap : ossResult.getFileUrls()) {
                String fileName = (String) fileUrlMap.get("filename");

                String uid = fileName.substring(15, 51);
                RecordDO recordDO = asrService.findFileId(uid);
                String fileUri = (String) fileUrlMap.get(fileName);

                BaseCloudTask cloudTask = new AliCloudTask(appId, appAccessKey, appAccesSecret, fileUri, recordDO.getId(), asrService);

                try {
                    taskQueue.put(cloudTask);
                    log.info("任务队列大小: {}", taskQueue.size());
                } catch (InterruptedException e) {
                    log.error("任务无法放入队列", e);
                }

            }
            ossResult = OSSUtils.generatePresignedUrls(endpoint, appAccessKey, appAccesSecret,
                    bucketName, ossResult.getNextMarker(), asrDTO.getConcurrentNumber(), expiration);
        }

        for (TaskConsumer taskConsumer : consumerList) {
            taskConsumer.setShutdown(true);
        }

    }

}

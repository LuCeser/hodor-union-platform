package cc.hodor.unionplatform.core;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;

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
 *  长语音识别任务消费者
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/18-9:08
 *
 ****************************************************************************************/
@Slf4j
@RequiredArgsConstructor
public class TaskConsumer implements Runnable {

    @NonNull
    private BlockingQueue<BaseCloudTask> taskQueue;
    @Setter
    private boolean shutdown = false;

    @Override
    public void run() {

        while (!shutdown) {
            try {
                BaseCloudTask cloudTask = taskQueue.take();
                log.info("从长语音识别队列中获取任务", cloudTask);
                boolean status = cloudTask.startTask();
                if (!status) {
                    shutdown = true;
                }

            } catch (InterruptedException e) {
                shutdown = true;
            }
        }

        log.info("长语音识别任务结束");
    }

}

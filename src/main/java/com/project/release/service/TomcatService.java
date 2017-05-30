package com.project.release.service;

import com.project.release.bean.OS;
import com.project.release.framework.ssh.InputStreamHandler;
import com.project.release.framework.ssh.SSHClient;
import org.springframework.stereotype.Service;

/**
 * Created by david.yun on 2017/5/30.
 */
@Service
public class TomcatService {
    //如果有返回值就判断为tomcat正在运行
    public boolean isRunning(OS ssh) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamHandler inputStreamHandler = new InputStreamHandler() {
            @Override
            public void onSuccess(String content) {
                stringBuilder.append(content);
            }

            @Override
            public void onError(String content) {
                System.err.println(content);
            }
        };
        String command = "ps -ef | grep " + ssh.getTomcat() + " | grep -v grep";
        SSHClient.execute(ssh, command, inputStreamHandler);
        if (stringBuilder.toString().length() > 0) {
            return true;
        }
        return false;
    }
}

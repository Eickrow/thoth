package com.project.release.framework.ssh;

/**
 * Created by david.yun on 2017/5/30.
 */
public interface InputStreamHandler {
    void onSuccess(String content);

    void onError(String content);
}

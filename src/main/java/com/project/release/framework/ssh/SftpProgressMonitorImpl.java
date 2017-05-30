package com.project.release.framework.ssh;

import com.jcraft.jsch.SftpProgressMonitor;

/**
 * Created by david.yun on 2017/5/30.
 */
public class SftpProgressMonitorImpl implements SftpProgressMonitor {

    private boolean isSuccess = false;

    @Override
    public void init(int op, String src, String dest, long max) {
        System.out.println("start to upload " + src + " to " + dest + "\t" + op + "\t size:" + max);
    }

    @Override
    public boolean count(long count) {
        return true;
    }

    @Override
    public void end() {
        System.out.println("upload success");
        this.isSuccess = true;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
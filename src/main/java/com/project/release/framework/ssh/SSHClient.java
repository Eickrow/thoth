package com.project.release.framework.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.project.release.bean.OS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by david.yun on 2017/5/30.
 */
public class SSHClient {
    private static Session openSession(OS ssh) throws Exception {
        JSch jsch = new JSch();
        Session session = null;
        session = jsch.getSession(ssh.getUsername(), ssh.getHost(), ssh.getPort());
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.setPassword(ssh.getPassword());
        session.connect(3000);
        return session;
    }

    private static void closeSession(Session session) {
        if (session != null) {
            try {
                session.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void execute(OS ssh, String commands, InputStreamHandler inputStreamHandler) {
        Session session = null;
        ChannelExec channelExec = null;
        try {
            session = openSession(ssh);
            channelExec = (ChannelExec) session.openChannel("exec");
            InputStream inputStream = channelExec.getInputStream();
            InputStream errorStream = channelExec.getErrStream();
            channelExec.setCommand(commands);
            channelExec.connect();
            new Thread(inputStreamHandler(errorStream, inputStreamHandler, "error")).start();
            inputStreamHandler(inputStream, inputStreamHandler, null).run();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (channelExec != null) {
                    channelExec.disconnect();
                }
                closeSession(session);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Runnable inputStreamHandler(InputStream inputStream, InputStreamHandler inputStreamHandler, String type) {
        return () -> {
            InputStreamReader inputStreamReader = null;
            BufferedReader bufferedReader = null;
            try {
                inputStreamReader = new InputStreamReader(inputStream, System.getProperty("sun.jnu.encoding"));
                bufferedReader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if ("error".equals(type)) {
                        inputStreamHandler.onError(line);
                    } else {
                        inputStreamHandler.onSuccess(line);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                    if (inputStreamReader != null) {
                        inputStreamReader.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public static boolean canConnected(OS ssh) {
        boolean flag = false;
        Session session = null;
        try {
            session = openSession(ssh);
            flag = session.isConnected();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSession(session);
        }
        return flag;
    }


    public static boolean upload(OS ssh, String file) {
        boolean flag = false;
        Session session = null;
        ChannelSftp channelSftp = null;
        try {
            session = openSession(ssh);
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
            SftpProgressMonitorImpl sftpProgressMonitor = new SftpProgressMonitorImpl();
            channelSftp.put(file, ssh.getTmp(), sftpProgressMonitor);
            flag = sftpProgressMonitor.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeSession(session);
        }
        return flag;
    }
}

package com.project.release.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by david.yun on 2017/5/30.
 */
public class FileUtils {
    public static InputStream getResource(String filePath) {
        return FileUtils.class.getClassLoader().getResourceAsStream(filePath);
    }

    public static String readResource(String filePath) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        String result = null;
        try {
            inputStream = getResource(filePath);
            if (inputStream == null) {
                throw new FileNotFoundException();
            } else {
                byte[] bytes = new byte[1024];
                int readSize = 0;
                byteArrayOutputStream = new ByteArrayOutputStream();
                while ((readSize = inputStream.read(bytes)) > 0) {
                    byteArrayOutputStream.write(bytes, 0, readSize);
                }
                result = byteArrayOutputStream.toString("UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}

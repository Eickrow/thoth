package com.project.release.service;

import com.alibaba.fastjson.JSON;
import com.project.release.bean.OS;
import com.project.release.repository.OSRepository;
import com.project.release.utils.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by david.yun on 2017/5/30.
 */
@Service
@Order(1)
public class DetectService implements InitializingBean {
    @Autowired
    OSRepository osRepository;

    @Override
    public void afterPropertiesSet() throws Exception {
        String OSstr = FileUtils.readResource("configuration/os.json");
        List<OS> OSs = JSON.parseArray(OSstr, OS.class);
        OSs.forEach(OS -> {
            osRepository.save(OS);
        });
    }
}

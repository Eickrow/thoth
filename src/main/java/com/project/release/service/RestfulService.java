package com.project.release.service;

import com.project.release.bean.OS;
import com.project.release.framework.ssh.SSHClient;
import com.project.release.repository.OSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by david.yun on 2017/5/30.
 */
@Service
public class RestfulService {
    @Autowired
    OSRepository osRepository;
    @Autowired
    TomcatService tomcatService;

    public List<Map<String, Object>> listOS() {
        List<Map<String, Object>> list = new ArrayList<>();
        Iterable<OS> osList = osRepository.findAll();
        osList.forEach(os -> {
            Map<String, Object> osMap = new HashMap<>();
            osMap.put("id", os.getId());
            osMap.put("tag", os.getTag());
            osMap.put("ssh_status", SSHClient.canConnected(os));
            osMap.put("tomcat_status", tomcatService.isRunning(os));
            list.add(osMap);
        });
        return list;
    }
}

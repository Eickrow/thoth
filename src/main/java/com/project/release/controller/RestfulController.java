package com.project.release.controller;

import com.project.release.repository.OSRepository;
import com.project.release.service.RestfulService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by david.yun on 2017/5/30.
 */
@RestController
@RequestMapping("/api/")
public class RestfulController {
    @Autowired
    RestfulService restfulService;

    @RequestMapping(value = "os/list", method = RequestMethod.GET)
    public Object test() {
        return restfulService.listOS();
    }
}

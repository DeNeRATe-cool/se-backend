package com.se.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.se.constant.ResourceConstant;
import com.se.dao.ProcessDao;
import com.se.exception.ParamNotEnoughException;
import com.se.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.se.entity.Process;
import com.se.utils.ResUtil;

import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    private ProcessDao processDao;

    @Override
        public void createProcess(Process process) {
        if (process.getTime() == null || StrUtil.isBlank(process.getName())
        || process.getClass_id() == null || process.getCourse_id() == null)
            throw new ParamNotEnoughException();
        processDao.insert(process);
    }

    @Override
    public List<Process> queryByClass(Integer course_id, Integer class_id) {
        if(course_id == null || class_id == null)
            throw new ParamNotEnoughException();
        return processDao.queryByClass(course_id, class_id);
    }

    @Override
    public void addResource(String fileName, Integer process_id, Integer course_id, Integer class_id, String url, Boolean is_public, String tags) {
        String resCode = ResUtil.generateCode();
        String type = fileName.contains(".")
                ? fileName.substring(fileName.lastIndexOf(".") + 1)
                : ResourceConstant.defaultType;
        if(is_public) processDao.addResource(resCode, fileName, -1, course_id, class_id, type, url, DateUtil.now(), tags);
        processDao.addResource(resCode, fileName, process_id, course_id, class_id, type, url, DateUtil.now(), tags);
    }
}

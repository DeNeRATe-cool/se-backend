package com.se.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.intern.InternUtil;
import cn.hutool.core.util.StrUtil;
import com.se.dao.ProcessDao;
import com.se.exception.ParamNotEnoughException;
import com.se.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.se.entity.Process;

import java.util.Date;
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
}

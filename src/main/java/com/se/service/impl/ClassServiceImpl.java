package com.se.service.impl;

import com.se.constant.ClassEntityConstant;
import com.se.dao.ClassDao;
import com.se.entity.Class;
import com.se.exception.classException.DuplicateClassException;
import com.se.service.ClassService;
import com.se.utils.FakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassDao classDao;

    @Override
    public void add(Class classEntity) {
        if(!classDao.getClassEntityByName(classEntity.getName()).isEmpty())
        {
            throw new DuplicateClassException(ClassEntityConstant.CLASS_NAME_EXISTS);
        }
        String code = FakeUtils.generateCode(ClassEntityConstant.CLASS_CODE_PLACE_NUM);
        while(!classDao.getClassEntityByClassCode(code).isEmpty())
        {
            code = FakeUtils.generateCode(ClassEntityConstant.CLASS_CODE_PLACE_NUM);
        }
        classEntity.setClass_code(code);
        classDao.add(classEntity);
    }
}

package com.se.service.impl;

import com.se.dao.TagDao;
import com.se.entity.Tag;
import com.se.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagDao tagDao;


    @Override
    public List<Tag> findAll() {
        return tagDao.findAll();
    }
}

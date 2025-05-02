package com.se.service;

import com.se.entity.Tag;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface TagService {

    List<Tag> findAll();
}

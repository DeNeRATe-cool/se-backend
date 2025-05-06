package com.se.service.impl;

import com.se.dao.ProbDao;
import com.se.entity.Problem;
import com.se.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProblemServiceImpl implements ProblemService {
    @Autowired
    private ProbDao probDAO;
    @Override
    public List<Problem> PublicProb(Integer type) {
        return probDAO.getProbByType(type);
    }
}

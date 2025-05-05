package com.se.service.impl;

import com.se.dao.CollectDao;
import com.se.dao.ExerDao;
import com.se.dao.ProbDao;
import com.se.dto.WrongInfo;
import com.se.entity.Exercise;
import com.se.entity.Problem;
import com.se.exception.collectException.CollectAlreadyExistException;
import com.se.exception.collectException.CollectNotExistException;
import com.se.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private CollectDao collectDao;

    @Autowired
    private ProbDao probDao;

    @Autowired
    private ExerDao exerDao;

    @Override
    public void addLike(Integer probId, Integer userId) {
        Integer record = collectDao.getIfExist(userId, probId);
        if (record != null)
            throw new CollectAlreadyExistException();
        collectDao.insertCollect(userId, probId);
    }

    @Override
    public void addWrong(Integer probId, Integer exerId, Integer userId) {
        collectDao.insertWrong(userId, probId, exerId);
    }

    @Override
    public void deleteLike(Integer probId, Integer userId) {
        Integer record = collectDao.getIfExist(userId, probId);
        if(record == null)
            throw new CollectNotExistException();
        collectDao.deleteCollect(userId, probId);
    }

    @Override
    public List<List<?>> getWrong(Integer userId) {
        List<WrongInfo> probList = collectDao.getWrongByStuId(userId);
        List<Problem> resList = probList
                .stream()
                .map((prob) -> probDao.getProblemById(prob.getProb_id()))
                .collect(Collectors.toList());
        List<Exercise> exerList = probList
                .stream()
                .map((prob) -> exerDao.getExerById(prob.getExer_id()))
                .collect(Collectors.toList());
        List<List<?>> replyList = new ArrayList<>();
        replyList.add(resList);
        replyList.add(exerList);
        return replyList;
    }

    @Override
    public List<Problem> getLike(Integer userId) {
        List<Integer> idxList = collectDao.getCollectByStuId(userId);
        return idxList
                .stream()
                .map((prob) -> probDao.getProblemById(prob))
                .collect(Collectors.toList());
    }
}

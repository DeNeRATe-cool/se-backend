package com.se.service;

import com.se.entity.Problem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CollectService {
    void addLike(Integer probId, Integer userId);

    void addWrong(Integer probId, Integer exerId, Integer userId);

    void deleteLike(Integer probId, Integer userId);

    List<List<?>> getWrong(Integer userId);

    List<Problem> getLike(Integer userId);
}

package com.se.service;

import com.se.dto.CreateProbDTO;
import com.se.entity.Problem;

import java.util.List;

public interface ProblemService {
    List<Problem> PublicProb(Integer type);

    Problem createProb(Problem problem);

    List<Problem> SelfProb(Integer userid, Integer types);
}

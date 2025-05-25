package com.se.service.impl;

import com.se.dao.ProbDao;
import com.se.dto.CreateProbDTO;
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
        if(type==null){
            return probDAO.getAllPublicProb();
        }
        return probDAO.getProbByType(type);
    }

    @Override
    public Problem createProb(Problem problem) {
        List<String> content_list=problem.getContent();
        String str_content="[";
        for(String str:content_list){
            str_content+="\"";
            str_content+=str;
            str_content+="\"";
            str_content+=",";
        }
        str_content+="]";
        problem.setStr_content(str_content);
        int r=probDAO.createProb(problem);
        return problem;
    }

    @Override
    public List<Problem> SelfProb(Integer userid, Integer types) {
        if(types==null){
            return probDAO.getSelfAllProb(userid);
        }
        return probDAO.getSelfProb(userid,types);
    }


}

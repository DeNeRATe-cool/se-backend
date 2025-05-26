package com.se.service.impl;

import cn.hutool.core.date.DateUtil;
import com.se.dao.ProbDao;
import com.se.dto.CreateProbDTO;
import com.se.entity.Problem;
import com.se.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        List<String> content_list=problem.getStr_content();
        String str_content="[";
        for(int i = 0; i < content_list.size(); i++){
            String str = content_list.get(i);
            str_content+="\"";
            str_content+=str;
            str_content+="\"";
            if(i!=content_list.size()-1)
                str_content+=",";
        }
        str_content+="]";
        problem.setContent(str_content);
        problem.setCreate_time(new Date());
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

    @Override
    public Problem getProb(Integer probId) {
        return probDAO.getProblemById(probId);
    }
}

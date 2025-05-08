package com.se;

import com.se.dao.UserDao;
import com.se.entity.Exercise;
import com.se.service.ExerService;
import com.se.utils.ReportGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class SeBackendApplicationTests {

    @Autowired
    private ExerService exerService;

    @Autowired
    private UserDao userDao;

    @Test
    void exerciseReportTest() throws IOException {
        Integer userId = 27;
        List<List<?>> resList = exerService.getHistory(userId);
        String url = ReportGenerator.generateExerciseReport(
                userDao.getSingleUserByID(userId),
                (List<Exercise>) resList.get(0),
                (List<Integer>) resList.get(1),
                (List<Integer>) resList.get(2)
        );
        System.out.println(url);
    }

}

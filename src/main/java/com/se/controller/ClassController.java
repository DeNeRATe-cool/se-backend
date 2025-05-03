package com.se.controller;

import com.se.dto.AddAdminInClassDTO;
import com.se.dto.ApplyJoinClassDTO;
import com.se.dto.DelStuInClassDTO;
import com.se.dto.Result;
import com.se.entity.Class;
import com.se.entity.User;
import com.se.service.ClassService;
import com.se.utils.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    @PostMapping("/create")
    public Result add(@RequestBody Class classEntity)
    {
        classService.add(classEntity);
        return Result.ok(classEntity);
    }

    @PostMapping("/addAdmin")
    public Result addAdmin(@RequestBody AddAdminInClassDTO addAdminInClassDTO)
    {
        List<User> res = classService.addAdmin(addAdminInClassDTO);
        return Result.ok(res,res.size());
    }

    @GetMapping("/admins")
    public Result listTeacherAndTutor(Integer class_id)
    {
        List<User> res = classService.listTeacherAndTutor(class_id);
        return Result.ok(res,res.size());
    }

    @GetMapping("/all")
    public Result list()
    {
        List<Class> res = classService.list();
        return Result.ok(res,res.size());
    }

    @PostMapping("/apply")
    public Result apply(@RequestBody @Validated ApplyJoinClassDTO applyJoinClassDTO)
    {
        classService.apply(applyJoinClassDTO);
        return Result.ok();
    }

    @GetMapping("/stu")
    public Result listByClassID(Integer course_id, Integer class_id, Integer user_id)
    {
        List<User>stuList = classService.listByClassID(course_id,class_id,user_id);
        return Result.ok(stuList,stuList.size());
    }

    @PostMapping("/addStu")
    public Result addStu(@RequestBody Map<String,String> mapperer)
    {
        Integer course_id = Integer.parseInt(mapperer.get("course_id"));
        Integer class_id = Integer.parseInt(mapperer.get("class_id"));
        Integer user_id = Integer.parseInt(mapperer.get("user_id"));
        String username = mapperer.get("username");
        List<User>res = classService.addStu(course_id,class_id,user_id,username);
        return Result.ok(res,res.size());
    }

    @GetMapping("/studentin")
    public Result listClassByStu(@RequestParam Integer user_id)
    {
        List<Class> classList = classService.listClassByStu(user_id);
        return Result.ok(classList,classList.size());
    }

    @PostMapping("/delStu")
    public Result delStu(@RequestBody @Validated DelStuInClassDTO delStuInClassDTO)
    {
        Integer course_id = delStuInClassDTO.getCourse_id();
        Integer class_id = delStuInClassDTO.getClass_id();
        Integer user_id = delStuInClassDTO.getUser_id();
        List<User> res = classService.delStu(course_id,class_id,user_id);
        return Result.ok(res,res.size());
    }

    @PostMapping("/addFile")
    public Result addFile(@RequestParam MultipartFile file,
                          @RequestParam Integer user_id,
                          @RequestParam Integer class_id)
    {
        List<List<String>>res;
        try {
            res = ExcelUtils.readFile(file);
            res.remove(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<User> userList = classService.addFile(res,user_id, class_id);
        return Result.ok(userList,userList.size());
    }
}

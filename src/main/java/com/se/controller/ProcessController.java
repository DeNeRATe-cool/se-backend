package com.se.controller;

import com.se.dto.Result;
import com.se.entity.Process;
import com.se.service.ProcessService;
import com.se.utils.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private OssService ossService;

    @Autowired
    private ProcessService processService;

    @PostMapping("/create")
    public Result createProcess(@RequestBody Process process) {
        processService.createProcess(process);
        return Result.ok(process);
    }

    @PostMapping("/resource/add")
    public Result upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("course_id") Integer course_id,
            @RequestParam("class_id") Integer class_id,
            @RequestParam("process_id") Integer process_id,
            @RequestParam("tags") String tags,
            @RequestParam("is_public") Boolean is_public) throws IOException {
        String fileName = file.getOriginalFilename();
        String url = ossService.uploadFile(fileName, file.getInputStream());
        return Result.ok(url);
    }

    @GetMapping("/get")
    public Result queryByClass(
            @RequestParam("course_id") Integer course_id,
            @RequestParam("class_id") Integer class_id) {
        List<Process> processList = processService.queryByClass(course_id, class_id);
        return Result.ok(processList, processList.size());
    }
}

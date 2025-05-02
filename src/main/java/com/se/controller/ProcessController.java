package com.se.controller;

import com.se.dto.Result;
import com.se.entity.Process;
import com.se.entity.Resource;
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
            @RequestParam("data") MultipartFile file,
            @RequestParam("course_id") Integer course_id,
            @RequestParam("class_id") Integer class_id,
            @RequestParam("process_id") Integer process_id,
            @RequestParam("tags") String tags,
            @RequestParam("is_public") Boolean is_public) throws IOException {
        String fileName = file.getOriginalFilename();
        String url = ossService.uploadFile(fileName, file.getInputStream());
        processService.addResource(fileName, process_id, course_id, class_id, url, is_public, tags);
        return Result.ok(url);
    }

    @GetMapping("/resource/get")
    public Result download(
            @RequestParam("res_id") Integer id) {
        String url = processService.downloadResource(id);
        return Result.ok(url);
    }

    @GetMapping("/resource/all")
    public Result getResourceByProcess(
            @RequestParam("process_id") Integer processId,
            @RequestParam("course_id") Integer courseId) {
        List<Resource> resList = processService.getResourceByProcess(processId, courseId);
        return Result.ok(resList, resList.size());
    }

    @GetMapping("/get")
    public Result queryByClass(
            @RequestParam("course_id") Integer course_id,
            @RequestParam("class_id") Integer class_id) {
        List<Process> processList = processService.queryByClass(course_id, class_id);
        return Result.ok(processList, processList.size());
    }
}

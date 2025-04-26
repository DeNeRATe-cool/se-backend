package com.se.controller;

import com.se.dto.Result;
import com.se.utils.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private OssService ossService;

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
}

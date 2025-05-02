package com.se.controller;

import com.se.dto.Result;
import com.se.entity.Tag;
import com.se.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/all")
    public Result getAll() {
        List<Tag> tags = tagService.findAll();
        return Result.ok(tags, tags.size());
    }
}

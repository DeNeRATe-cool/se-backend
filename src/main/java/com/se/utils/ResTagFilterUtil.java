package com.se.utils;

import cn.hutool.core.util.StrUtil;
import com.se.entity.Resource;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ResTagFilterUtil {
    public static List<Resource> filterByTag(List<Resource> resources, String tags) {
        List<String> tagIdList = StrUtil.split(
                StrUtil.replace(tags, " ", ""), ',');
//        System.out.println("tags : " + tagIdList);
        List<Resource> resList = new ArrayList<>();
        for(Resource resource : resources) {
            boolean allIn = true;
            for(String tagId : tagIdList) {
                if(!resource.getTag().contains(tagId)) {
                    allIn = false;
                    break;
                }
            }
            if(allIn) {
                resList.add(resource);
            }
        }
        return resList;
    }

    public static List<Resource> uniqueResource(List<Resource> resources) {
        return new ArrayList<Resource>(
                resources.stream()
                        .collect(Collectors.toMap(
                                Resource::getRes_code,
                                Function.identity(),
                                // 如果 key 重复，就比较 date 大小，保留最新的
                                BinaryOperator.maxBy(Comparator.comparing(Resource::getDate))
                        ))
                        .values()
        );
    }
}

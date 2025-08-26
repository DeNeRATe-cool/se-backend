package com.se.service.impl;

import com.se.dao.CollectDao;
import com.se.exception.collectException.CollectAlreadyExistException;
import com.se.exception.collectException.CollectNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectServiceImplTest {

    @Mock
    private CollectDao collectDao;

    @InjectMocks
    private CollectServiceImpl collectService;

    // ==================== addLike 方法测试 ====================
    
    @Test
    void addLike_Success_WhenNotCollected() {
        // 准备测试数据
        int userId = 1;
        int probId = 1001;
        
        // 模拟依赖行为
        when(collectDao.getIfExist(userId, probId)).thenReturn(null);
        
        // 调用被测试方法
        collectService.addLike(probId, userId);
        
        // 验证依赖调用
        verify(collectDao).insertCollect(userId, probId);
    }

    @Test
    void addLike_Failure_WhenAlreadyCollected() {
        // 准备测试数据
        int userId = 1;
        int probId = 1001;
        
        // 模拟依赖行为
        when(collectDao.getIfExist(userId, probId)).thenReturn(1);
        
        // 验证异常
        assertThrows(CollectAlreadyExistException.class, () -> {
            collectService.addLike(probId, userId);
        });
        
        // 验证未调用插入方法
        verify(collectDao, never()).insertCollect(anyInt(), anyInt());
    }

    // ==================== deleteLike 方法测试 ====================
    
    @Test
    void deleteLike_Success_WhenCollected() {
        // 准备测试数据
        int userId = 1;
        int probId = 1001;
        
        // 模拟依赖行为
        when(collectDao.getIfExist(userId, probId)).thenReturn(1);
        
        // 调用被测试方法
        collectService.deleteLike(probId, userId);
        
        // 验证依赖调用
        verify(collectDao).deleteCollect(userId, probId);
    }

    @Test
    void deleteLike_Failure_WhenNotCollected() {
        // 准备测试数据
        int userId = 1;
        int probId = 1001;
        
        // 模拟依赖行为
        when(collectDao.getIfExist(userId, probId)).thenReturn(null);
        
        // 验证异常
        assertThrows(CollectNotExistException.class, () -> {
            collectService.deleteLike(probId, userId);
        });
        
        // 验证未调用删除方法
        verify(collectDao, never()).deleteCollect(anyInt(), anyInt());
    }
}
package com.luke.luketools.testDB;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Slf4j
@Service
public class UserInfoService {
    @Autowired
    private UserRepository userRepository;


    @Transactional(rollbackOn = Exception.class)
    public void testDemo() {
        UserInfo byUserId = userRepository.findByUserId(1);
        log.info("当前线程：{}，当前年龄：{}",Thread.currentThread().getName(),byUserId.getAge());
        byUserId.setAge(byUserId.getAge() + 1);
        userRepository.save(byUserId);
        log.info("当前线程：{}，当前年龄：{}",Thread.currentThread().getName(),byUserId.getAge());
    }
}

package com.luke.luketools.testDB;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoService userInfoService;

    @PostMapping(path = "save",consumes = {MediaType.APPLICATION_JSON_VALUE})
    public UserInfo addNewUser(@RequestBody UserInfo userInfo){
        return userRepository.save(userInfo);
    }

    @GetMapping(path = "users")
    @ResponseBody
    public Page<UserInfo> getAllUsers(Pageable request){
        return userRepository.findAll(request);
    }

    @GetMapping(path = "test")
    @ResponseBody
    public void test(Pageable request) {
        for (int i = 0; i < 100; i++) {
            //新建线程处理
            new Thread(() -> {
                synchronized (UserController.class) {
                    userInfoService.testDemo();
                }
            }).start();
        }
    }
}
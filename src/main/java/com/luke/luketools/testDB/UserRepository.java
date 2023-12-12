package com.luke.luketools.testDB;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserInfo,Integer> {
    UserInfo findByUserId(Integer id);
}


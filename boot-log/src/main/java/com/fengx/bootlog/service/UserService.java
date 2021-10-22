package com.fengx.bootlog.service;

import com.fengx.bootlog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * @author: Fengx
 * @date: 2021-09-26
 * @description: 业务接口
 **/
public interface UserService {

    void save(User user);

    void deleteById(String id);

    User queryUserById(String id);

    Iterable<User> queryAll();

    Page<User> findByName(String name, PageRequest request);
}

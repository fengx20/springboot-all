package com.fengx.bootlog.service;

import com.fengx.bootlog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author: Fengx
 * @date: 2021-09-26
 * @description: 扩展需要的方法
 **/
// 使用ES CRUD，继承 ElasticsearchRepository 接口
public interface UserRepository extends ElasticsearchRepository<User, String> {


    Page<User> findByName(String name, Pageable pageable);

}

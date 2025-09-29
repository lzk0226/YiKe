package com.ruoyi.yike.service;
import com.ruoyi.yike.domain.User;

/**
 * 用户服务接口
 * @author SockLightDust
 * @version 1.0
 */
public interface UserService {

    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册是否成功
     */
    boolean register(User user);

    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @return 用户信息，登录失败返回null
     */
    User login(String email, String password);

    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    User getUserById(Long id);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUsername(String username);

    /**
     * 根据邮箱获取用户信息
     * @param email 邮箱
     * @return 用户信息
     */
    User getUserByEmail(String email);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新是否成功
     */
    boolean updateUser(User user);

    /**
     * 更新最后登录时间
     * @param userId 用户ID
     * @return 更新是否成功
     */
    boolean updateLastLoginTime(Long userId);

    /**
     * 检查邮箱是否已存在
     * @param email 邮箱
     * @return 是否存在
     */
    boolean checkEmailExists(String email);

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 是否存在
     */
    boolean checkUsernameExists(String username);
}
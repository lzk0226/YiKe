package com.ruoyi.yike.service.impl;
import com.ruoyi.yike.domain.User;
import com.ruoyi.yike.service.UserService;
import com.ruoyi.yike.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean register(User user) {
        // 参数校验
        if (user == null || !StringUtils.hasText(user.getEmail()) ||
                !StringUtils.hasText(user.getPassword()) || !StringUtils.hasText(user.getUsername())) {
            throw new RuntimeException("用户信息不完整");
        }

        // 检查邮箱是否已存在
        if (userMapper.existsByEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 检查用户名是否已存在
        if (userMapper.existsByUsername(user.getUsername())) {
            throw new RuntimeException("用户名已被使用");
        }

        // 密码加密
        String encodedPassword = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(encodedPassword);

        // 设置默认值
        if (!StringUtils.hasText(user.getNickname())) {
            user.setNickname(user.getUsername());
        }
        if (!StringUtils.hasText(user.getAvatar())) {
            user.setAvatar("/default-avatar.jpg");
        }

        // 插入用户
        int result = userMapper.insert(user);
        return result > 0;
    }

    @Override
    public User login(String email, String password) {
        // 参数校验
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            throw new RuntimeException("邮箱和密码不能为空");
        }

        // 根据邮箱查询用户
        User user = userMapper.selectByEmail(email);
        if (user == null) {
            return null;
        }

        // 验证密码
        String encodedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!encodedPassword.equals(user.getPassword())) {
            return null;
        }

        // 更新最后登录时间
        userMapper.updateLastLoginTime(user.getId());

        // 不返回密码信息
        user.setPassword(null);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        if (id == null) {
            return null;
        }
        return userMapper.selectById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        User user = userMapper.selectByUsername(username);
        if (user != null) {
            user.setPassword(null); // 不返回密码信息
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return null;
        }
        User user = userMapper.selectByEmail(email);
        if (user != null) {
            user.setPassword(null); // 不返回密码信息
        }
        return user;
    }

    @Override
    public boolean updateUser(User user) {
        // 参数校验
        if (user == null || user.getId() == null) {
            throw new RuntimeException("用户信息不完整");
        }

        // 检查用户是否存在
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser == null) {
            throw new RuntimeException("用户不存在");
        }

        // 更新用户信息
        int result = userMapper.updateById(user);
        return result > 0;
    }

    @Override
    public boolean updateLastLoginTime(Long userId) {
        if (userId == null) {
            return false;
        }
        int result = userMapper.updateLastLoginTime(userId);
        return result > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkEmailExists(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }
        return userMapper.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkUsernameExists(String username) {
        if (!StringUtils.hasText(username)) {
            return false;
        }
        return userMapper.existsByUsername(username);
    }
}
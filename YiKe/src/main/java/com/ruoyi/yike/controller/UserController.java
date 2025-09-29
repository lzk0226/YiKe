package com.ruoyi.yike.controller;

import com.ruoyi.yike.common.Result;
import com.ruoyi.yike.domain.User;
import com.ruoyi.yike.service.UserService;
import com.ruoyi.yike.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;


    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册结果
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        try {
            System.out.println("注册用户：" + user);
            boolean success = userService.register(user);
            System.out.println("注册结果：" + success);

            if (success) {
                System.out.println("1111111111111");
                return Result.success("注册成功");
            } else {
                System.out.println("1111111111111");
                return Result.error("注册失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @return 登录结果
     */
    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @return 登录结果
     */
    @PostMapping("/login")
    public Result<Object> login(@RequestParam String email,
                                @RequestParam String password,
                                HttpServletRequest request) {
        try {
            User user = userService.login(email, password);
            if (user != null) {
                // 生成 JWT token
                String accessToken = jwtUtils.generateToken(user.getId(), user.getUsername());
                String refreshToken = jwtUtils.generateRefreshToken(user.getId(), user.getUsername());

                // 构建返回数据（用 Map 代替，不额外建类）
                Map<String, Object> loginResponse = new HashMap<>();
                loginResponse.put("accessToken", accessToken);
                loginResponse.put("refreshToken", refreshToken);
                loginResponse.put("tokenType", "Bearer");
                loginResponse.put("expiresIn", jwtUtils.getExpiration());
                loginResponse.put("user", user);

                return Result.success("登录成功", loginResponse);
            } else {
                return Result.error("邮箱或密码错误");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 根据ID获取用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                return Result.success("获取成功", user);
            } else {
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/username/{username}")
    public Result<User> getUserByUsername(@PathVariable String username) {
        try {
            User user = userService.getUserByUsername(username);
            if (user != null) {
                return Result.success("获取成功", user);
            } else {
                return Result.error("用户不存在");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新结果
     */
    @PutMapping("/update")
    public Result<String> updateUser(@RequestBody User user) {
        try {
            boolean success = userService.updateUser(user);
            if (success) {
                return Result.success("更新成功");
            } else {
                return Result.error("更新失败");
            }
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查邮箱是否已存在
     * @param email 邮箱
     * @return 检查结果
     */
    @GetMapping("/check/email/{email}")
    public Result<Boolean> checkEmailExists(@PathVariable String email) {
        try {
            boolean exists = userService.checkEmailExists(email);
            return Result.success("检查完成", exists);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 检查用户名是否已存在
     * @param username 用户名
     * @return 检查结果
     */
    @GetMapping("/check/username/{username}")
    public Result<Boolean> checkUsernameExists(@PathVariable String username) {
        try {
            boolean exists = userService.checkUsernameExists(username);
            return Result.success("检查完成", exists);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
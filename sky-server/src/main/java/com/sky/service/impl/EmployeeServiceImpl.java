package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        // 将密码进行md5加密之后再比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    public void addEmployee(EmployeeDTO employeeDTO){
        Employee employee = new Employee();
        // 对象属性拷贝 设置传递过来的属性
        BeanUtils.copyProperties(employeeDTO,employee);
        // 设置md5加密之后的默认密码
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setStatus(StatusConstant.ENABLE);  // 设置状态
        employee.setCreateTime(LocalDateTime.now());  // 设置创建和修改时间
        employee.setUpdateTime(LocalDateTime.now());

        // 从线程局部变量中获取员工id
        Long empId = BaseContext.getCurrentId();
        employee.setCreateUser(empId);   // 设置创建人和更新的人 暂时设置为10
        employee.setUpdateUser(empId);

        employeeMapper.insert(employee);
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 使用pagehelper工具进行分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.selectByPage(employeePageQueryDTO);
        // 加工page 返回PageResult
        PageResult pageResult = new PageResult();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(page.getResult());
        return pageResult;
    }


    /**
     * 设置员工状态(启用/禁用)
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        // update employee set status = ? where id = ?;
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();
        employeeMapper.update(employee);
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Override
    public Employee selectById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        if(employee==null){
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        employee.setPassword("****");
        return employee;
    }
}

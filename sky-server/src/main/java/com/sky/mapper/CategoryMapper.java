package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {


    /**
     * 分页查询分类表
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> selectByPage(CategoryPageQueryDTO categoryPageQueryDTO);


    /**
     * 启用禁用分类
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);


    /**
     * 插入数据
     * @param category
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into category(type, name, sort, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);


    /**
     * 删除分类
     * @param id
     */
    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);


    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> list(Integer type);


    /**
     * 根据id查询分类表
     * @param categoryId
     * @return
     */
    @Select("select * from category where id = #{categoryId}")
    Category selectById(Long categoryId);
}

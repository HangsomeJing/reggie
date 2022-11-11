package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomerException;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.mapper.CategoryMapper;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.DishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 判断是否成功删除类别，条件：此类别中是否包含 dish或setmeal
     * @param id
     *  这里是抛出异常代替返回值
     */
    @Override
    public void removeById(Long id) {
        //1、判读是否包含dish
        LambdaQueryWrapper<Dish> lambdaQueryWrapper1=new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Dish::getId,id);
        int count1 = dishService.count(lambdaQueryWrapper1);
        if (count1>0){
            throw new CustomerException("类别中包含dish，请先移除所有的dish，再删除此类别");
        }
        //2、
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper2=new LambdaQueryWrapper<>();
        lambdaQueryWrapper2.eq(Setmeal::getId,id);
        int count2 = setmealService.count(lambdaQueryWrapper2);
        if (count2>0){
            throw new CustomerException("类别中包含dish，请先移除所有的Setmeal，再删除此类别");
        }
        //3、
        LambdaQueryWrapper<Category> lambdaQueryWrapper3=new LambdaQueryWrapper<>();
        lambdaQueryWrapper3.eq(Category::getId,id);
        remove(lambdaQueryWrapper3);
    }
}

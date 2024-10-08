package com.robin.iot.common.mybatis.injector;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import org.apache.ibatis.session.Configuration;

import java.util.List;

/**
 * 增强的 SQL 注入器
 * <p>
 * 注入了 MyBatis-Plus 实现的 SQL 级的批量 Insert 数据的 AbstractMethod 子类 InsertBatchSomeColumn
 *
 * @see InsertBatchSomeColumn
 * @author zhao peng
 * @date 2024/8/29 0:06
 **/
public class EnhancedSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Configuration configuration, Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> methodList = super.getMethodList(configuration, mapperClass, tableInfo);
        // 更新时自动填充的字段不用插入值、ID自增的主键不用插入值
        methodList.add(new InsertBatchSomeColumn(column -> {
            TableId tableId = column.getField().getAnnotation(TableId.class);
            return column.getFieldFill() != FieldFill.UPDATE || (tableId != null && tableId.type() == IdType.AUTO);
        }));
        return methodList;
    }
}

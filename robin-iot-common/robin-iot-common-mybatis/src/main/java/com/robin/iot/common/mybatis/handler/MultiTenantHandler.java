package com.robin.iot.common.mybatis.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.robin.iot.common.mybatis.autoconfigure.EnhancedMybatisProperties;
import com.robin.iot.common.mybatis.util.TenantContextHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

import java.util.HashSet;
import java.util.Set;

/**
 * 多租户处理器
 * <p>
 * 注意：
 * <ul>
 * <li>
 * 启用多租户处理器后所有要执行的 sql 方法都会自动拼接多租户条件，包括自定义的 sql
 * <li>
 * 自写的 sql 请按规范书写 sql ，涉及到多个表的每个表都要给别名，特别是 inner join 的要写标准的 inner join
 *
 * @author zhao peng
 * @date 2024/9/10 21:59
 **/
public class MultiTenantHandler implements TenantLineHandler {

    private final Set<String> ignoredTables = new HashSet<>();

    public MultiTenantHandler(EnhancedMybatisProperties properties) {
        // 大小写数据表名都添加，以防数据表定义的习惯不统一
        properties.getIgnoredTables().forEach(item -> {
            ignoredTables.add(item.toLowerCase());
            ignoredTables.add(item.toUpperCase());
        });
    }

    @Override
    public Expression getTenantId() {
        return new StringValue(TenantContextHolder.getCurrentTenant());
    }

    @Override
    public String getTenantIdColumn() {
        // 默认就是 tenant_id
        return TenantLineHandler.super.getTenantIdColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        return ignoredTables.contains(tableName);
    }

}

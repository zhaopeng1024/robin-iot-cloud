package com.robin.iot.common.mybatis.handler;

import com.robin.iot.common.mybatis.util.StringUtils;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 英文逗号字符串 <--> List<String> 类型转换器
 * <p>
 * 支持数据库中英文逗号分隔的 varchar 类型 与 List<String> 类型之间转换
 *
 * @author zhao peng
 * @date 2024/9/10 21:46
 **/
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(List.class)
@SuppressWarnings("unused")
public class CommaStringTypeHandler implements TypeHandler<List<String>> {

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, StringUtils.listToCommaString(parameter));
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        return StringUtils.commaStringToList(columnValue);
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        return StringUtils.commaStringToList(columnValue);
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        return StringUtils.commaStringToList(columnValue);
    }

}

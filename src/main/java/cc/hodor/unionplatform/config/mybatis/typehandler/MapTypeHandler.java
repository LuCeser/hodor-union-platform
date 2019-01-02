package cc.hodor.unionplatform.config.mybatis.typehandler;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/***************************************************************************************
 *
 *  Project:        hodor
 *
 *  Copyright ©     
 *
 ***************************************************************************************
 *
 *  Header Name: WellJoint
 *
 *  Description: 
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/2-17:11
 *
 ****************************************************************************************/
public class MapTypeHandler<T extends Object> extends BaseTypeHandler<T> {

    private Class<T> clazz;

    public MapTypeHandler(Class<T> clazz) {
        if (clazz == null) throw new IllegalArgumentException("Type argument cannot be null");
        this.clazz = clazz;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T t, JdbcType jdbcType) throws SQLException {
        ps.setString(i, JSON.toJSONString(t));
    }

    @Override
    public T getNullableResult(ResultSet rs, String s) throws SQLException {
        return JSON.parseObject(rs.getString(s), clazz);
    }

    @Override
    public T getNullableResult(ResultSet rs, int i) throws SQLException {
        return JSON.parseObject(rs.getString(i), clazz);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int i) throws SQLException {
        return JSON.parseObject(cs.getString(i), clazz);
    }
}

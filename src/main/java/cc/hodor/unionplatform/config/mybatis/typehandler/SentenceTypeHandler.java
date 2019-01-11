package cc.hodor.unionplatform.config.mybatis.typehandler;

import cc.hodor.unionplatform.base.entity.Sentence;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
 *  ${DESCRIPTION}
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/10-18:28
 *
 ****************************************************************************************/
public class SentenceTypeHandler extends BaseTypeHandler<List<Sentence>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<Sentence> sentences, JdbcType jdbcType) throws SQLException {
        String sentenceStr = JSONObject.toJSONString(sentences);
        ps.setString(i, sentenceStr);
    }

    @Override
    public List<Sentence> getNullableResult(ResultSet rs, String s) throws SQLException {
        return JSON.parseArray(rs.getString(s), Sentence.class);
    }

    @Override
    public List<Sentence> getNullableResult(ResultSet rs, int i) throws SQLException {
        return JSON.parseArray(rs.getString(i), Sentence.class);
    }

    @Override
    public List<Sentence> getNullableResult(CallableStatement cs, int i) throws SQLException {
        return JSON.parseArray(cs.getString(i), Sentence.class);
    }
}

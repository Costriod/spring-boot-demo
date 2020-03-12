package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.entity.UserMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public Page<User> findPage(String name, int pageIndex, int pageSize) {
        //PageHelper.startPage(pageIndex, pageSize, true)必须紧挨着下一句的mapper查询才会自动分页查询以及自动count
        PageHelper.startPage(pageIndex, pageSize, true);
        //紧挨着PageHelper.startPage(pageIndex, pageSize, true)的查询返回的就是Page对象，否则就是普通的ArrayList
        return (Page<User>) userMapper.selectByName(name);
    }

    /**
     * 这一个sql是测试多表关联，表设置了别名a，查询的是a.id，此时查询字段的时候依旧是会自动映射到BaseResultMap
     * @param role
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public Page<User> findRolePage(String role, int pageIndex, int pageSize) {
        //PageHelper.startPage(pageIndex, pageSize, true)必须紧挨着下一句的mapper查询才会自动分页查询以及自动count
        PageHelper.startPage(pageIndex, pageSize, true);
        //紧挨着PageHelper.startPage(pageIndex, pageSize, true)的查询返回的就是Page对象，否则就是普通的ArrayList
        return (Page<User>) userMapper.selectByRole(role);
    }

    /**
     * 通过ExecutorType.BATCH能实现批量操作，这种情况下mapper的sql语句都是先在内存缓存起来，只有等到sqlSession.flushStatements()
     * 才是真正的执行sql，就算执行了sqlSession.commit()（底层执行了sqlSession.flushStatements()），spring也是能回滚的（如事务抛异常回滚）
     * 只不过需要注意一点，mapper.insert或者mapper.updateByPrimaryKey返回值可能是0也可能是负数，也就意味着是没法统计真正修改了多少条记录
     * @param records
     * @return
     */
    @Transactional
    public int batchInsert(List<User> records) {
        SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        for (User user : records) {
            mapper.insertSelective(user);
        }
        sqlSession.flushStatements();
        //sqlSession.commit();
        return records.size();
    }
}

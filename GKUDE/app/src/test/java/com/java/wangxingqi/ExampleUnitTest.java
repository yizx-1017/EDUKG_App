package com.java.wangxingqi;

import org.junit.Test;

import static org.junit.Assert.*;

import com.java.wangxingqi.bean.CourseType;
import com.java.wangxingqi.bean.EntityBean;
import com.java.wangxingqi.bean.PropertyBean;
import com.java.wangxingqi.bean.RelationBean;
import com.java.wangxingqi.server.Result;
import com.java.wangxingqi.server.UserDataSource;
import com.java.wangxingqi.server.UserRepository;
import com.java.wangxingqi.server.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orm.SugarApp;

import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest extends SugarApp {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void fetchFunctionTest() {
        Fetch fetch = new Fetch();
        System.out.println(fetch.getId());
        List<EntityBean> list = fetch.fetchInstanceList(CourseType.CHINESE.getCourseType(),"李白");
        EntityBean entityBean = list.get(0);
        System.out.println(entityBean.getLabel());
        fetch.fetchInfoByInstanceName(entityBean);
        System.out.println(entityBean.getUri());
        List<PropertyBean> properties = entityBean.getPropertiesFromStore();
        System.out.println(properties.get(0).getObject().contains("http://"));
        properties.removeIf(p->p.getObject().contains("http://"));
        System.out.println(properties);
//        System.out.println(entityBean.getRelations().size());
//        System.out.println(entityBean.getRelationStore());
//        System.out.println("I got here before gson!");
//        System.out.println(entityBean.getRelationsFromStore().size());
//        System.out.println(entityBean.getRelations());
    }

    @Test
    public void gsonTest() {
        Gson gson = new Gson();
        String test = "{\"status\": 200, \"msg\":OK, \"data\": null}";
        Type type = new TypeToken<Result<List<RelationBean>>>() {}.getType();
        Result<List<RelationBean>> relationBean = gson.fromJson(test, type);
        System.out.println(relationBean.getStatus());
    }

    @Test
    public void favoriteTest() {
        Fetch fetch = new Fetch();
        List<EntityBean> list = fetch.fetchInstanceList(CourseType.CHINESE.getCourseType(),"李白");
        EntityBean entityBean = list.get(0);
        System.out.println(entityBean.getLabel());
        UserRepository userRepository = UserRepository.getInstance(new UserDataSource());
        Result<User> userResult = userRepository.login("Jackson", "123456");
        String token = userResult.getData().getUserToken();
        System.out.println(token);
        List<EntityBean> entityBeanList = userRepository.syncFavorites().getData();
        System.out.println(entityBeanList);
        userRepository.addFavorite(entityBean);
        entityBeanList = userRepository.syncFavorites().getData();
        System.out.println(entityBeanList.size());
        userRepository.cancelFavorite(entityBean);
        entityBeanList = userRepository.syncFavorites().getData();
        System.out.println(entityBeanList);
    }

    @Test
    public void stringSortTest() {
        Fetch fetch = new Fetch();
        List<EntityBean> entityBeanList = fetch.fetchInstanceList("chinese", "中");
        entityBeanList.sort(Comparator.comparing(EntityBean::getLabel)); // 按label ascii码排序
        entityBeanList.sort(Comparator.comparing(EntityBean::getLabel).reversed()); // 逆序
        entityBeanList.sort(Comparator.comparing(EntityBean::getLabel, Comparator.comparingInt(String::length))); // 按字符串长度排序
        entityBeanList.sort(Comparator.comparing(EntityBean::getLabel, Comparator.comparingInt(String::length)).reversed()); // 逆序
    }

    @Test
    public void stringTest() {
        String a = "李白是一个伟大的诗人";
        System.out.println(a.substring(a.length()-1));
    }
}
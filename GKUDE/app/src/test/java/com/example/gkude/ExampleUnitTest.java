package com.example.gkude;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.gkude.bean.CourseType;
import com.example.gkude.bean.EntityBean;
import com.example.gkude.bean.PropertyBean;
import com.example.gkude.bean.RecognitionBean;
import com.example.gkude.bean.RelationBean;
import com.example.gkude.server.Result;
import com.example.gkude.server.UserDataSource;
import com.example.gkude.server.UserRepository;
import com.example.gkude.server.model.User;
import com.google.gson.Gson;
import com.orm.SugarApp;

import java.util.List;

import okhttp3.Request;

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
        String test = "{\"predicate\":\"http://edukg.org/knowledge/0.1/property/common#includes\",\"subject_label\":\"科普文章的基本知识\",\"subject\":\"http://edukg.org/knowledge/0.1/instance/chinese#kepuwenzhangdejibenzhishi-ae085387c0f93174d1d5a1173490bddb\",\"predicate_label\":\"包含\"}";
        RelationBean relationBean = gson.fromJson(test, RelationBean.class);
        System.out.println(relationBean.getEntityUri());
        System.out.println(relationBean.getSubjectName());
        System.out.println(relationBean.getObjectName());
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
        List<EntityBean> entityBeanList = userRepository.getFavorites().getData();
        System.out.println(entityBeanList);
        userRepository.addFavorite(entityBean);
        entityBeanList = userRepository.getFavorites().getData();
        System.out.println(entityBeanList.size());
        userRepository.cancelFavorite(entityBean);
        entityBeanList = userRepository.getFavorites().getData();
        System.out.println(entityBeanList);
    }
}
package com.vuclip.abtesthttp.service;

import java.util.*;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import com.vuclip.abtesthttp.redis.RedisFactory;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/6/2
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
@Component
public class TableSyncToRedis {
    private Logger logger = Logger.getLogger(this.getClass());
    @Resource
    private JdbcTemplate jdbcTemplate;

    private Jedis jedis = null;
    public void syncProjectTable(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                projectFun();
            }
        }).start();
    }
    public synchronized void projectFun(){
        Jedis proJedis = RedisFactory.getJedis();
        try {
            Set<String> keys = proJedis.keys("PROJECT_*");
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                proJedis.del(it.next());
            }
            String sql = "select id,name, description,active,createtime,updatetime,userid,del from track_project where del = 0";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            int i = 0;
            for (Map<String, Object> m : list) {
                Map<String, String> convertM = new HashMap<String, String>();
                for (String key : m.keySet()) {
                    convertM.put(key, m.get(key) == null ? "" : m.get(key).toString());
                }
                proJedis.hmset("PROJECT_" + String.valueOf(convertM.get("name")), convertM);
                i++;
            }
            logger.info("===============update project table to redis succssfully! total count:" + i + "=================");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (proJedis != null) {
                proJedis.close();
            }
        }
    }
    public void syncTargetTable(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                targetFun();
            }
        }).start();
    }
    public synchronized void targetFun(){
        Jedis tarJedis = RedisFactory.getJedis();
        try {
            Set<String> keys = tarJedis.keys("TARGET_*");
            Iterator<String> tarIt = keys.iterator();
            while (tarIt.hasNext()) {
                tarJedis.del(tarIt.next());
            }
            String tarSql = "select id,project_id,name, description,type,active,createtime,updatetime,country,pageId,del from track_target where del = 0";
            List<Map<String, Object>> tarList = jdbcTemplate.queryForList(tarSql);
            int i = 0;
            for (Map<String, Object> tarm : tarList) {
                Map<String, String> tarConvertM = new HashMap<String, String>();
                for (String tarKey : tarm.keySet()) {
                    tarConvertM.put(tarKey, tarm.get(tarKey) == null ? "" : tarm.get(tarKey).toString());
                }
                tarJedis.hmset("TARGET_" + String.valueOf(tarConvertM.get("name")), tarConvertM);
                i++;
            }
            logger.info("===============update target table to redis succssfully! total count:" + i + "=================");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tarJedis != null) {
                tarJedis.close();
            }
        }
    }
    public void syncFactorTable(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                factorFun();
            }
        }).start();
    }
    public synchronized void factorFun(){
        Jedis facJedis = RedisFactory.getJedis();
        try {
            Set<String> keys = facJedis.keys("FACTOR_*");
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                facJedis.del(it.next());
            }
            String sql = "select id,target_id,name, description,createtime,updatetime,del from track_factor where del = 0";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            int i = 0;
            for (Map<String, Object> m : list) {
                Map<String, String> convertM = new HashMap<String, String>();
                for (String key : m.keySet()) {
                    convertM.put(key, m.get(key) == null ? "" : m.get(key).toString());
                }
                facJedis.hmset("FACTOR_" + String.valueOf(convertM.get("name")), convertM);
                i++;
            }
            logger.info("===============update factor table to redis succssfully! total count:" + i + "=================");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (facJedis != null) {
                facJedis.close();
            }
        }
    }
    public void syncTargetConditionTable(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                targetConditionFun();
            }
        }).start();
    }
    public synchronized void targetConditionFun(){
        Jedis tarConJedis = RedisFactory.getJedis();
        try {
            Set<String> keys = tarConJedis.keys("TARCONDITION_*");
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                tarConJedis.del(it.next());
            }

            String conTargetSql = "select a.id from track_target a,track_target_condition b where a.id=b.target_id and a.del = 0 group by a.id";
            List<Map<String, Object>> contarList = jdbcTemplate.queryForList(conTargetSql);
            int i = 0;
            for (Map<String, Object> m : contarList) {
                String targetId = m.get("id") == null ? "" : m.get("id").toString();
                String sql = "select id,target_id,condition_id,createtime,updatetime from track_target_condition where target_id = " + targetId;
                List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
                String conditionIds = "";
                for (Map<String, Object> conm : list) {
                    conditionIds += conm.get("condition_id") + ",";
                }
                if (!"".equals(conditionIds)) {
                    tarConJedis.set("TARCONDITION_" + targetId, conditionIds.substring(0, conditionIds.length() - 1));
                    i++;
                }
            }
            logger.info("===============update track_target_condition table to redis succssfully! total count:" + i + "=================");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (tarConJedis != null) {
                tarConJedis.close();
            }
        }
    }
    public void syncFactorConditionTable(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                factorConditionFun();
            }
        }).start();
    }
    public synchronized void factorConditionFun(){
        Jedis facConJedis = RedisFactory.getJedis();
        try {
            Set<String> keys = facConJedis.keys("FACCONDITION_*");
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                facConJedis.del(it.next());
            }

            String conFactorSql = "select a.id from track_factor a,track_factor_condition b where a.id=b.factor_id and a.del = 0 group by a.id";
            List<Map<String, Object>> confacList = jdbcTemplate.queryForList(conFactorSql);
            int i = 0;
            for (Map<String, Object> m : confacList) {
                String facId = m.get("id") == null ? "" : m.get("id").toString();
                String sql = "select id,factor_id,condition_value_id,createtime,updatetime from track_factor_condition where factor_id = " + facId;
                List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
                String conditionIds = "";
                for (Map<String, Object> conm : list) {
                    conditionIds += conm.get("condition_value_id") + ",";
                }
                if (!"".equals(conditionIds)) {
                    facConJedis.set("FACCONDITION_" + facId, conditionIds.substring(0, conditionIds.length() - 1));
                    i++;
                }
            }
            logger.info("===============update track_factor_condition table to redis succssfully! total count:" + i + "=================");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (facConJedis != null) {
                facConJedis.close();
            }
        }
    }
    public void syncConditionAndValue(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                conditionAndValueFun();
            }
        }).start();
    }
    public synchronized void conditionAndValueFun(){
        Jedis conValueJedis = RedisFactory.getJedis();
        try {
            Set<String> conKeys = conValueJedis.keys("CONDITION_*");
            Iterator<String> conIt = conKeys.iterator();
            while (conIt.hasNext()) {
                conValueJedis.del(conIt.next());
            }
            String conSql = "select id,name,description,type,isshow,conditiontype,showcondition,pid from track_condition";
            List<Map<String, Object>> conList = jdbcTemplate.queryForList(conSql);
            int i = 0;
            for (Map<String, Object> m : conList) {
                Map<String, String> convertM = new HashMap<String, String>();
                for (String key : m.keySet()) {
                    convertM.put(key, m.get(key) == null ? "" : m.get(key).toString());
                }
                conValueJedis.hmset("CONDITION_" + String.valueOf(convertM.get("id")), convertM);
                i++;
            }
            logger.info("===============update track_condition table to redis succssfully! total count:" + i + "=================");

            Set<String> valKeys = conValueJedis.keys("CONDITIONVALUE_*");
            Iterator<String> valIt = valKeys.iterator();
            while (valIt.hasNext()) {
                conValueJedis.del(valIt.next());
            }
            String valSql = "select id,condition_id,name,description from track_condition_value";
            List<Map<String, Object>> valList = jdbcTemplate.queryForList(valSql);
            int j = 0;
            for (Map<String, Object> m : valList) {
                Map<String, String> convertM = new HashMap<String, String>();
                for (String key : m.keySet()) {
                    convertM.put(key, m.get(key) == null ? "" : m.get(key).toString());
                }
                conValueJedis.hmset("CONDITIONVALUE_" + String.valueOf(convertM.get("id")), convertM);
                j++;
            }
            logger.info("===============update track_condition_value table to redis succssfully! total count:" + j + "=================");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conValueJedis != null) {
                conValueJedis.close();
            }
        }
    }
    public void syncProjectAbout(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                projectFun();
                targetFun();
                targetConditionFun();
                factorFun();
                factorConditionFun();
                conditionAndValueFun();
            }
        }).start();
    }
    public void syncTargetAbout(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                targetFun();
                targetConditionFun();
                factorFun();
                factorConditionFun();
                conditionAndValueFun();
            }
        }).start();
    }
    public void syncFactorAbout(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                factorFun();
                factorConditionFun();
                conditionAndValueFun();
            }
        }).start();
    }
    @Before
    public void testBefore(){
//        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
//        this.jdbcTemplate  = (JdbcTemplate)context.getBean("jdbcTemplate");
        jedis = RedisFactory.getJedis();
    }
    @Test
    public void testProject(){
//        syncProjectTable();
//        jedis.set("project".getBytes(),"ceshishifoutihuan".getBytes());
//        List active = jedis.hmget("PROJECT_TEST-PROJECT-REDIS", "id","active","description","name","createtime");
//        System.out.println(active.toString());
        System.out.println(String.valueOf(1));
    }
    @Test
    public void testTarget(){
//        syncTargetTable();
//        List active = jedis.hmget("TARGET_CONDITION_50", "id", "active", "description", "name", "createtime");
//        System.out.println(active.toString());
        Set<String> conKeys = jedis.keys("CONDITIONVALUE_*");
        Iterator<String> conIt= conKeys.iterator();
        while (conIt.hasNext()){
            System.out.println(conIt.next());
        }
//        System.out.println(conKeys.size());
//        System.out.println(jedis.hmget("CONDITIONVALUE_32","id","condition_id", "name", "description", "updatetime", "createtime").toString());
//        System.out.println(jedis.hmget("FACTOR_TEST222","id", "name", "description", "updatetime", "createtime").toString());
//        System.out.println(jedis.get("FACCONDITION_35"));
//        jedis.del("TARGET_CONDITION_50");
//        jedis.del("TARGET_CONDITION_49");
    }
    @Test
    public void testFactor(){
//        syncFactorTable();
        List active = jedis.hmget("FACTOR_TEST-FACTOR-REDIS1", "id", "active", "description", "name", "createtime");
        System.out.println(active.toString());
    }
}

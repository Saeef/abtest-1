package com.vuclip.abtesthttp.service;

import com.vuclip.abtesthttp.dao.ReportDao;
import com.vuclip.abtesthttp.util.ConvertUtil;

import com.vuclip.abtesthttp.util.DateUtil;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2015/5/28
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ReportService {
    private Logger log = Logger.getLogger(this.getClass());
    @Resource
    private ReportDao reportDao;

    public JSONObject abtestDPane(int startTime, int endTime, int project, int target, String facterids,String country,String pageid) {
        StringBuffer sql = new StringBuffer();
        sql.append("select sum(a.count) count,b.name name from track_record_daily a,track_factor b where a.factor_id = b.id and factor_id in (")
        .append(facterids)
        .append(") and date>=")
        .append(startTime)
        .append(" and date <=")
        .append(endTime);
        if(!"0".equals(country)){
        	sql.append(" and a.country = '").append(country+"'");
        }
        if(!"0".equals(pageid)){
        	sql.append(" and a.pageId = '").append(pageid+"'");
        }
        sql.append(" group by b.name");
        List<Map<String,Object>> listPie = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> listLine = new ArrayList<Map<String,Object>>();
        JSONObject obj = new JSONObject();
        log.info(sql.toString());
        try{
        	listPie = reportDao.abtestDPane(sql.toString());
        	listLine = abtestDayToDay(startTime, endTime, project, target, facterids, country, pageid);
        }catch (Exception e){
            e.printStackTrace();
        }
        obj.element("listPie", listPie);
        obj.element("listLine", listLine);
        return obj;
    }

    public JSONObject abtestHPane(int startTime, int endTime, int pid, int tid, String fids,String country,String pageid) {
        String argumentField = "date";
        String[] xenonPalette = new String[]{"#68b828","#7c38bc","#0e62c7","#fcd036","#4fcdfc","#00b19d","#ff6264","#f7aa47"};
        String[] factorIds = fids.split(",");
        JSONObject obj = new JSONObject();
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> perList = new ArrayList<Map<String,Object>>();
        List<Map<String, Object>> series = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> pieList = new ArrayList<Map<String,Object>>();
        List<String> dateList = DateUtil.getDaysBetwSDateAndEDate(startTime, endTime);
        String currentDate = DateUtil.monthDateFormat(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMin = calendar.get(Calendar.MINUTE);
        for(String date:dateList){
            if(date!=null&&!date.equals(currentDate)){
                for(int j=0;j<=23;j++){
                    Map<String,Object> m = new HashMap<String,Object>();
                    m.put(argumentField,date.concat(" ").concat(String.valueOf(j)));
                    maps.add(m);
                }
            }else{
                for(int j=0;j<=currentHour;j++){
                    Map<String,Object> m = new HashMap<String,Object>();
                    m.put(argumentField,date.concat(" ").concat(String.valueOf(j)));
                    maps.add(m);
                }
            }
        }
        String endTimeStr = String.valueOf(endTime);
        String endTimeFormat = endTimeStr.substring(0,4)+"-"+endTimeStr.substring(4,6)+"-"+endTimeStr.substring(6,8);
        if(endTimeFormat.equals(currentDate)){
            if(currentMin<=5){
                maps.remove(maps.size()-1);
                maps.remove(maps.size()-1);
            }else{
                maps.remove(maps.size() - 1);
            }
        }
        try{
            for(int i=0;i<factorIds.length;i++){
                StringBuffer sql = new StringBuffer("select sum(a.count) count,b.name name,a.date day,a.hour hour,b.id factorid from track_record_hourly a,track_factor b where a.factor_id = b.id");
                sql.append(" and factor_id =").append(ConvertUtil.converterToInt(factorIds[i])).append(" and date>=").append(startTime).append(" and date <=").append(endTime);
                if(!"0".equals(country)){
                	sql.append(" and a.country = '").append(country+"'");
                }
                if(!"0".equals(pageid)){
                	sql.append(" and a.pageId = '").append(pageid+"'");
                }
                sql.append(" group by hour,date");
                log.info(sql.toString());
                List<Map<String, Object>> list = reportDao.abtestHPane(sql.toString());
                if(list.size()>0){
                    for(int j=0;j<maps.size();j++){
                        Map<String,Object> m = new HashMap<String,Object>();
                        Map m1 = maps.get(j);
                        Set<String> keys = m1.keySet();
                        Iterator it = keys.iterator();
                        while (it.hasNext()){
                            String key = it.next().toString();
                            String value = m1.get(key).toString();
                            if(!argumentField.equals(key)){
                                m.put(key, Integer.parseInt(value));
                            }else{
                                m.put(key, value);
                            }

                        }
                        m.put(list.get(0).get("name").toString(),0);
                        maps.set(j,m);
                    }
                    Map<String,Object> ser = new HashMap<String,Object>();
                    ser.put("valueField",list.get(0).get("name"));
                    ser.put("name",list.get(0).get("name"));
                    ser.put("color",xenonPalette[i]);
                    series.add(ser);
                }
                for (int k=0;k<maps.size();k++){
                    Map<String, Object> m=(Map<String, Object>)maps.get(k);
                    for(int l=0;l<list.size();l++){
                        String datestr = list.get(l).get("day").toString();
                        String date = datestr.substring(0,4)+"-"+datestr.substring(4,6)+"-"+datestr.substring(6,8);
                        if(m.get(argumentField).toString().equals(date+" "+list.get(l).get("hour").toString())){
                            m.put(list.get(l).get("name").toString(), list.get(l).get("count"));
                            maps.set(k,m);
                        }
                    }

                }
            }
            StringBuffer sql = new StringBuffer();
            sql.append("select sum(a.count) count,b.name name from track_record_hourly a,track_factor b where a.factor_id = b.id and factor_id in (")
            .append(fids).append(") and date>=").append(startTime)
            .append(" and date <=").append(endTime);
            if(!"0".equals(country)){
            	sql.append(" and a.country = '").append(country+"'");
            }
            if(!"0".equals(pageid)){
            	sql.append(" and a.pageId = '").append(pageid+"'");
            }
            sql.append(" group by b.name");
            log.info(sql.toString());
            pieList = reportDao.abtestHPane(sql.toString());

            perList.addAll(maps);
            for(int i=0;i<perList.size();i++){
                Map<String,Object> m = new HashMap<String,Object>();
                Map m1 = perList.get(i);
                Set<String> keys = m1.keySet();
                Iterator it = keys.iterator();
                int numCount=0;
                while (it.hasNext()){
                    String key = it.next().toString();
                    String value = m1.get(key).toString();
                    if(!argumentField.equals(key)){
                        m.put(key, Integer.parseInt(value));
                        numCount+=Integer.parseInt(value);
                    }else{
                        m.put(key, value);
                    }
                }
                m.put("count",numCount);
                perList.set(i,m);
            }
            for(int i=0;i<perList.size();i++){
                Map<String,Object> m = new HashMap<String,Object>();
                Map m1 = perList.get(i);
                Set<String> keys = m1.keySet();
                Iterator it = keys.iterator();
                while (it.hasNext()){
                    String key = it.next().toString();
                    String value = m1.get(key).toString();
                    if(!argumentField.equals(key)&&!"count".equals(key)){
                        double total = Double.parseDouble(m1.get("count").toString());
                        if(total<=0){
                            m.put(key, 0);
                        }else{
                            double accuracy_num = Double.parseDouble(value) / total*100;
                            String per = String.valueOf(new BigDecimal(accuracy_num).setScale(0, BigDecimal.ROUND_HALF_UP));
                            m.put(key, Integer.parseInt(per));
                        }
                    }else if(argumentField.equals(key)){
                        m.put(key, value);
                    }
                }
                perList.set(i, m);
            }
            Map<String,Object> rmap = new HashMap<String,Object>();
            rmap.put("record",maps);
            rmap.put("series", series);
            rmap.put("perrecord",perList);
            returnList.add(rmap);
            obj.element("listLine", returnList);
            obj.element("listPie", pieList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;
    }

    public List<Map<String,Object>> trackDPane(int startTime, int endTime, int project, int target,String country,String pageid) {
        String argumentField="date";
        String[] xenonPalette = new String[]{"#68b828","#7c38bc","#0e62c7","#fcd036","#4fcdfc","#00b19d","#ff6264","#f7aa47"};
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> series = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
        List<String> dateList = DateUtil.getDaysBetwSDateAndEDate(startTime, endTime);
        for(String date:dateList){
            Map<String,Object> m = new HashMap<String,Object>();
            m.put(argumentField, "'"+date+"'");
            maps.add(m);
        }
        try{
            StringBuffer sql = new StringBuffer("select sum(a.count) count,b.name name,a.date date,b.id targetid from track_record_daily a,track_target b where a.target_id = b.id ");
            sql.append(" and target_id =").append(target).append(" and date>=").append(startTime).append(" and date <=").append(endTime);
            if(!"0".equals(country)){
            	sql.append(" and a.country = '").append(country+"'");
            }
            if(!"0".equals(pageid)){
            	sql.append(" and a.pageid = '").append(pageid+"'");
            }
            sql.append(" group by date");
            log.info(sql.toString());
            List<Map<String, Object>> list = reportDao.trackDPane(sql.toString());
            if(list.size()>0){
            	int count=0;
            	for(Map<String,Object> map:list){
            		count+=Integer.parseInt(map.get("count").toString());
            	}
                for(int j=0;j<maps.size();j++){
                    Map<String,Object> m = new HashMap<String,Object>();
                    Map m1 = maps.get(j);
                    Set<String> keys = m1.keySet();
                    Iterator it = keys.iterator();
                    while (it.hasNext()){
                        String key = it.next().toString();
                        String value = m1.get(key).toString();
                        if(!argumentField.equals(key)){
                            m.put(key, Integer.parseInt(value));
                        }else{
                            m.put(key, value);
                        }

                    }
                    m.put(list.get(0).get("name").toString(),0);
                    maps.set(j,m);
                }
                Map<String, Object> ser = new HashMap<String, Object>();
                ser.put("valueField","\""+list.get(0).get("name")+"\"");
                ser.put("name","\""+list.get(0).get("name")+"(count:"+count+")\"");
                ser.put("color","\""+xenonPalette[0]+"\"");
                series.add(ser);
            }
            for (int j=0;j<maps.size();j++){
                Map<String, Object> m=(Map<String, Object>)maps.get(j);
                for(int k=0;k<list.size();k++){
                    String datestr = list.get(k).get("date").toString();
                    String date = datestr.substring(0,4)+"-"+datestr.substring(4,6)+"-"+datestr.substring(6,8);
                    if(m.get(argumentField).toString().equals("'"+date+"'")){
                        m.put(list.get(k).get("name").toString(), list.get(k).get("count"));
                        maps.set(j,m);
                    }
                }

            }
            Map<String,Object> rmap = new HashMap<String,Object>();
            rmap.put("record",maps);
            rmap.put("series", series);
            returnList.add(rmap);

        }catch (Exception e){
            e.printStackTrace();
        }
        return returnList;
    }

    public List<Map<String, Object>> trackHPane(int startTime, int endTime, int pid, int tid,String country,String pageid) {
        String argumentField = "date";
        String[] xenonPalette = new String[]{"#68b828","#7c38bc","#0e62c7","#fcd036","#4fcdfc","#00b19d","#ff6264","#f7aa47"};
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>> ();
        List<Map<String, Object>>  series = new ArrayList<Map<String, Object>> ();
        List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
        List<String> dateList = DateUtil.getDaysBetwSDateAndEDate(startTime, endTime);
        String currentDate = DateUtil.monthDateFormat(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMin = calendar.get(Calendar.MINUTE);
        for(String date:dateList){
            if(date!=null&&!date.equals(currentDate)) {
                for (int j = 0; j <= 23; j++) {
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put(argumentField, "'".concat(date.concat(" ").concat(String.valueOf(j))).concat("'"));
                    maps.add(m);
                }
            }else{
                for(int j=0;j<=currentHour;j++){
                    Map<String, Object> m = new HashMap<String, Object>();
                    m.put(argumentField, "'".concat(date.concat(" ").concat(String.valueOf(j))).concat("'"));
                    maps.add(m);
                }
            }
        }
        String endTimeStr = String.valueOf(endTime);
        String endTimeFormat = endTimeStr.substring(0,4)+"-"+endTimeStr.substring(4,6)+"-"+endTimeStr.substring(6,8);
        if(endTimeFormat.equals(currentDate)){
            if(currentMin<=5){
                maps.remove(maps.size()-1);
                maps.remove(maps.size()-1);
            }else{
                maps.remove(maps.size() - 1);
            }
        }
        try{
            StringBuffer sql = new StringBuffer("select sum(a.count) count,b.name name,a.date "+argumentField+",a.hour hour,b.id tid from track_record_hourly a,track_target b where a.target_id = b.id");
            sql.append(" and target_id =").append(tid).append(" and date>=").append(startTime).append(" and date <=").append(endTime);
            if(!"0".equals(country)){
            	sql.append(" and a.country = '").append(country+"'");
            }
            if(!"0".equals(pageid)){
            	sql.append(" and a.pageid = '").append(pageid+"'");
            }
            sql.append(" group by hour,date");
            log.info(sql.toString());
            List<Map<String, Object>> list = reportDao.trackHPane(sql.toString());
            if(list.size()>0){
                for(int j=0;j<maps.size();j++){
                    Map<String,Object> m = new HashMap<String,Object>();
                    Map m1 = maps.get(j);
                    Set<String> keys = m1.keySet();
                    Iterator it = keys.iterator();
                    while (it.hasNext()){
                        String key = it.next().toString();
                        String value = m1.get(key).toString();
                        if(!argumentField.equals(key)){
                            m.put(key, Integer.parseInt(value));
                        }else{
                            m.put(key, value);
                        }

                    }
                    m.put(list.get(0).get("name").toString(),0);
                    maps.set(j,m);
                }
                Map<String,Object> ser = new HashMap<String,Object>();
                ser.put("valueField","\""+list.get(0).get("name")+"\"");
                ser.put("name","\""+list.get(0).get("name")+"\"");
                ser.put("color","\""+xenonPalette[0]+"\"");
                series.add(ser);
            }
            for (int j=0;j<maps.size();j++){
                Map<String,Object> m=(Map<String,Object>)maps.get(j);
                for(int k=0;k<list.size();k++){
                    String datestr = list.get(k).get(argumentField).toString();
                    String date = datestr.substring(0,4)+"-"+datestr.substring(4,6)+"-"+datestr.substring(6,8);
                    if(m.get(argumentField).toString().equals("'"+date+" "+list.get(k).get("hour").toString()+"'")){
                        m.put(list.get(k).get("name").toString(), list.get(k).get("count"));
                        maps.set(j,m);
                    }
                }

            }

            Map<String,Object> rmap = new HashMap<String,Object>();
            rmap.put("record",maps);
            rmap.put("series", series);
            returnList.add(rmap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return returnList;
    }
    
    public List<Map<String,Object>> abtestDayToDay(int startTime, int endTime, int pid, int tid, String fids,String country,String pageid) {
        String argumentField = "date";
        String[] xenonPalette = new String[]{"#68b828","#7c38bc","#0e62c7","#fcd036","#4fcdfc","#00b19d","#ff6264","#f7aa47"};
        String[] factorIds = fids.split(",");
        List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> perList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> series = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
        List<String> dateList = DateUtil.getDaysBetwSDateAndEDate(startTime, endTime);
        for(String date:dateList){
            Map<String,Object> m = new HashMap<String,Object>();
            m.put(argumentField, date);
            maps.add(m);
        }
        try{
        	for(int i=0;i<factorIds.length;i++){
	            StringBuffer sql = new StringBuffer("select a.date "+argumentField+",b.name name,sum(a.count) count from track_record_daily a,track_factor b where a.factor_id = b.id");
	            sql.append(" and b.id=").append(ConvertUtil.converterToInt(factorIds[i]))
	            .append(" and a.target_id=").append(tid)
	            .append(" and a.date>=").append(startTime)
	            .append(" and a.date<= ").append(endTime);
	            String orderby = " order by date asc";
	            if(!"0".equals(country)){
	            	sql.append(" and a.country = '").append(country+"'");
	            }
	            if(!"0".equals(pageid)){
	            	sql.append(" and a.pageid = '").append(pageid+"'");
	            }
                sql.append(" group by date ");
	            sql.append(orderby);
	            log.info(sql.toString());
	            List<Map<String,Object>> list = reportDao.abtestDayToDay(sql.toString());

	            if(list.size()>0){
                    for(int j=0;j<maps.size();j++){
                        Map<String,Object> m = new HashMap<String,Object>();
                        Map m1 = maps.get(j);
                        Set<String> keys = m1.keySet();
                        Iterator it = keys.iterator();
                        while (it.hasNext()){
                            String key = it.next().toString();
                            String value = m1.get(key).toString();
                            if(!argumentField.equals(key)){
                                m.put(key, Integer.parseInt(value));
                            }else{
                                m.put(key, value);
                            }

                        }
                        m.put(list.get(0).get("name").toString(),0);
                        maps.set(j,m);
                    }
	                Map<String,Object> ser = new HashMap<String,Object>();
	                ser.put("valueField",list.get(0).get("name"));
	                ser.put("name",list.get(0).get("name"));
	                ser.put("color", xenonPalette[i]);
	                series.add(ser);
                }
	            for (int j=0;j<maps.size();j++){
	                Map<String,Object> m=(Map<String,Object>)maps.get(j);
	                for(int k=0;k<list.size();k++){
                        String datestr = list.get(k).get(argumentField).toString();
                        String date = datestr.substring(0,4)+"-"+datestr.substring(4,6)+"-"+datestr.substring(6,8);
                        if(m.get(argumentField).toString().equals(date)){
                            m.put(list.get(k).get("name").toString(), list.get(k).get("count"));
                            maps.set(j,m);
                        }
	                }
	            }
        	}
            perList.addAll(maps);
            for(int i=0;i<perList.size();i++){
                Map<String,Object> m = new HashMap<String,Object>();
                Map m1 = perList.get(i);
                Set<String> keys = m1.keySet();
                Iterator it = keys.iterator();
                int numCount=0;
                while (it.hasNext()){
                    String key = it.next().toString();
                    String value = m1.get(key).toString();
                    if(!argumentField.equals(key)){
                        m.put(key, Integer.parseInt(value));
                        numCount+=Integer.parseInt(value);
                    }else{
                        m.put(key, value);
                    }
                }
                m.put("count",numCount);
                perList.set(i,m);
            }
            for(int i=0;i<perList.size();i++){
                Map<String,Object> m = new HashMap<String,Object>();
                Map m1 = perList.get(i);
                Set<String> keys = m1.keySet();
                Iterator it = keys.iterator();
                while (it.hasNext()){
                    String key = it.next().toString();
                    String value = m1.get(key).toString();
                    if(!argumentField.equals(key)&&!"count".equals(key)){
                        double total = Double.parseDouble(m1.get("count").toString());
                        if(total<=0){
                            m.put(key, 0);
                        }else{
                            double accuracy_num = Double.parseDouble(value) / total*100;
                            String per = String.valueOf(new BigDecimal(accuracy_num).setScale(0, BigDecimal.ROUND_HALF_UP));
                            m.put(key, Integer.parseInt(per));
                        }
                    }else if(argumentField.equals(key)){
                        m.put(key, value);
                    }
                }
                perList.set(i, m);
            }
            Map<String,Object> rmap = new HashMap<String,Object>();
            rmap.put("record",maps);
            rmap.put("series", series);
            rmap.put("perrecord",perList);
            returnList.add(rmap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return returnList;
    }
    public List<Map<String,Object>> rateDayToDay(Map<String,Object> params) {
        String argumentField = "date";
        String[] xenonPalette = new String[]{"#68b828","#7c38bc","#0e62c7","#fcd036","#4fcdfc","#00b19d","#ff6264","#f7aa47"};
        String[] factorIds = params.get("fids").toString().split(",");
        List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> countMaps = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> vpcountMaps = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> series = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
        List<String> dateList = DateUtil.getDaysBetwSDateAndEDate(Integer.parseInt(params.get("startTime").toString()), Integer.parseInt(params.get("endTime").toString()));
        for(String date:dateList){
            Map<String,Object> m = new HashMap<String,Object>();
            m.put(argumentField, date);
            maps.add(m);
            countMaps.add(m);
            vpcountMaps.add(m);
        }
        try{
            for(int i=0;i<factorIds.length;i++){
                StringBuffer sql = new StringBuffer("select a.date "+argumentField+",b.name name,sum(a.count) count,CASE sum(a.vp_count) when 0 then 1 else sum(a.vp_count) end vpcount,cast(format(sum(a.count)/(CASE sum(a.vp_count) when 0 then 1 else sum(a.vp_count) end),2)*100 as signed) rate from track_record_daily a,track_factor b where a.factor_id = b.id");
                sql.append(" and b.id=").append(ConvertUtil.converterToInt(factorIds[i]))
                        .append(" and a.target_id=").append(params.get("target"))
                        .append(" and a.date>=").append(params.get("startTime"))
                        .append(" and a.date<= ").append(params.get("endTime"));
                String orderby = " order by date asc";
                if(!"0".equals(params.get("country").toString())){
                    sql.append(" and a.country = '").append(params.get("country").toString()+"'");
                }
                if(!"0".equals(params.get("pageid").toString())){
                    sql.append(" and a.pageid = '").append(params.get("pageid").toString()+"'");
                }
                sql.append(" group by date ");
                sql.append(orderby);
                log.info(sql.toString());
                List<Map<String,Object>> list = reportDao.abtestDayToDay(sql.toString());

                if(list.size()>0){
                    for(int j=0;j<maps.size();j++){
                        Map<String,Object> m = new HashMap<String,Object>();
                        Map m1 = maps.get(j);
                        Set<String> keys = m1.keySet();
                        Iterator it = keys.iterator();
                        while (it.hasNext()){
                            String key = it.next().toString();
                            String value = m1.get(key).toString();
                            if(!argumentField.equals(key)){
                                m.put(key, Integer.parseInt(value));
                            }else{
                                m.put(key, value);
                            }

                        }
                        m.put(list.get(0).get("name").toString(), 0);
                        maps.set(j, m);
                    }
                    for(int j=0;j<countMaps.size();j++){
                        Map<String,Object> m = new HashMap<String,Object>();
                        Map m1 = countMaps.get(j);
                        Set<String> keys = m1.keySet();
                        Iterator it = keys.iterator();
                        while (it.hasNext()){
                            String key = it.next().toString();
                            String value = m1.get(key).toString();
                            if(!argumentField.equals(key)){
                                m.put(key, Integer.parseInt(value));
                            }else{
                                m.put(key, value);
                            }

                        }
                        m.put(list.get(0).get("name").toString(),0);
                        countMaps.set(j,m);
                    }
                    for(int j=0;j<vpcountMaps.size();j++){
                        Map<String,Object> m = new HashMap<String,Object>();
                        Map m1 = vpcountMaps.get(j);
                        Set<String> keys = m1.keySet();
                        Iterator it = keys.iterator();
                        while (it.hasNext()){
                            String key = it.next().toString();
                            String value = m1.get(key).toString();
                            if(!argumentField.equals(key)){
                                m.put(key, Integer.parseInt(value));
                            }else{
                                m.put(key, value);
                            }

                        }
                        m.put(list.get(0).get("name").toString(),0);
                        vpcountMaps.set(j,m);
                    }
                    Map<String,Object> ser = new HashMap<String,Object>();
                    ser.put("valueField",list.get(0).get("name"));
                    ser.put("name",list.get(0).get("name"));
                    ser.put("color", xenonPalette[i]);
                    series.add(ser);
                }
                for (int j=0;j<maps.size();j++){
                    Map<String,Object> m=(Map<String,Object>)maps.get(j);
                    for(int k=0;k<list.size();k++){
                        String datestr = list.get(k).get(argumentField).toString();
                        String date = datestr.substring(0,4)+"-"+datestr.substring(4,6)+"-"+datestr.substring(6,8);
                        if(m.get(argumentField).toString().equals(date)){
                            m.put(list.get(k).get("name").toString(), list.get(k).get("rate"));
                            maps.set(j, m);
                        }
                    }
                }
                for (int j=0;j<countMaps.size();j++){
                    Map<String,Object> countM=(Map<String,Object>)countMaps.get(j);
                    for(int a=0;a<list.size();a++){
                        String datestr = list.get(a).get(argumentField).toString();
                        String date = datestr.substring(0,4)+"-"+datestr.substring(4,6)+"-"+datestr.substring(6,8);
                        if(countM.get(argumentField).toString().equals(date)){
                            countM.put(list.get(a).get("name").toString(), list.get(a).get("count"));
                            countMaps.set(j, countM);
                        }
                    }
                }

                for (int j=0;j<vpcountMaps.size();j++){
                    Map<String,Object> vpcountM=(Map<String,Object>)vpcountMaps.get(j);
                    for(int o=0;o<list.size();o++){
                        String datestr = list.get(o).get(argumentField).toString();
                        String date = datestr.substring(0,4)+"-"+datestr.substring(4,6)+"-"+datestr.substring(6,8);
                        if(vpcountM.get(argumentField).toString().equals(date)){
                            vpcountM.put(list.get(o).get("name").toString(), list.get(o).get("vpcount"));
                            vpcountMaps.set(j, vpcountM);
                        }
                    }
                }
            }
            Map<String,Object> rmap = new HashMap<String,Object>();
            rmap.put("record",maps);
            rmap.put("countmaps",countMaps);
            rmap.put("vpcountmaps",vpcountMaps);
            rmap.put("series", series);
            returnList.add(rmap);
        }catch(Exception e){
            e.printStackTrace();
        }
        return returnList;
    }
    public List<Map<String,Object>> trackDayToDay(String selectDate, int pid, int tid, String name,String country,String pageid) {
        String argumentField = "date";
        String[] xenonPalette = new String[]{"#68b828","#7c38bc","#0e62c7","#fcd036","#4fcdfc","#00b19d","#ff6264","#f7aa47"};
        List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> series = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
        String currentDate = DateUtil.monthDateFormat(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMin = calendar.get(Calendar.MINUTE);
        if(selectDate!=null&&!selectDate.equals(currentDate)) {
            for (int j = 0; j <= 23; j++) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put(argumentField, "'".concat(String.valueOf(selectDate).concat(" ").concat(String.valueOf(j))).concat("'"));
                maps.add(m);
            }
        }else{
            for (int j = 0; j <= currentHour; j++) {
                Map<String, Object> m = new HashMap<String, Object>();
                m.put(argumentField, "'".concat(String.valueOf(selectDate).concat(" ").concat(String.valueOf(j))).concat("'"));
                maps.add(m);
            }
        }
        if(selectDate!=null&&selectDate.equals(currentDate)) {
            if(currentMin<=5){
                maps.remove(maps.size()-1);
                maps.remove(maps.size()-1);
            }else{
                maps.remove(maps.size()-1);
            }
        }
        try{
            StringBuffer sql = new StringBuffer("select a.date date,a.hour hour,b.name name,sum(a.count) count from track_record_hourly a,track_target b where a.target_id = b.id");
            sql.append(" and a.target_id=").append(tid).append(" and a.date=").append(ConvertUtil.converterToInt(selectDate.replaceAll("-","")));
            if(!"0".equals(country)){
            	sql.append(" and a.country = '").append(country+"'");
            }
            if(!"0".equals(pageid)){
            	sql.append(" and a.pageid = '").append(pageid+"'");
            }
            sql.append(" group by hour,date");
            log.info(sql.toString());
            List<Map<String,Object>> list = reportDao.trackDayToDay(sql.toString());
            if(list.size()>0){
                for(int j=0;j<maps.size();j++){
                    Map<String,Object> m = new HashMap<String,Object>();
                    Map m1 = maps.get(j);
                    Set<String> keys = m1.keySet();
                    Iterator it = keys.iterator();
                    while (it.hasNext()){
                        String key = it.next().toString();
                        String value = m1.get(key).toString();
                        if(!argumentField.equals(key)){
                            m.put(key, Integer.parseInt(value));
                        }else{
                            m.put(key, value);
                        }

                    }
                    m.put(list.get(0).get("name").toString(),0);
                    maps.set(j,m);
                }
                Map<String,Object> ser = new HashMap<String,Object>();
                ser.put("valueField","\""+list.get(0).get("name")+"\"");
                ser.put("name","\""+list.get(0).get("name")+"\"");
                ser.put("color","\""+xenonPalette[0]+"\"");
                series.add(ser);
            }
            for (int j=0;j<maps.size();j++){
                Map<String,Object> m=(Map<String,Object>)maps.get(j);
                for(int k=0;k<list.size();k++){
                    String datestr = list.get(k).get("date").toString();
                    String date = datestr.substring(0,4)+"-"+datestr.substring(4,6)+"-"+datestr.substring(6,8);
                    if(m.get(argumentField).toString().equals("'"+date+" "+list.get(k).get("hour").toString()+"'")){
                        m.put(list.get(k).get("name").toString(), list.get(k).get("count"));
                        maps.set(j,m);
                    }
                }
            }
            Map<String,Object> rmap = new HashMap<String,Object>();
            rmap.put("record",maps);
            rmap.put("series", series);
            returnList.add(rmap);
        }catch (Exception e){
            e.printStackTrace();
        }

        return returnList;
    }

	public List<Map<String, Object>> getTargetCountry(String targetId, String type,String doh) {
		String table ="track_record_hourly";
		if("d".equals(doh)){
			table = "track_record_daily";
		}
		String sql = "select "+type+" from "+table+" where target_id = "+targetId+" group by "+type;
		List<Map<String, Object>> list  = new ArrayList<Map<String, Object>>();
		try {
			list = reportDao.getTargetCountry(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

    public JSONObject rateDPane(Map<String, Object> params) {
        StringBuffer sql = new StringBuffer();
        List<Map<String,Object>> listPie = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> listLine = new ArrayList<Map<String,Object>>();
        JSONObject obj = new JSONObject();
        //,format(sum(a.count)/sum(a.vp_count),2)*100 rate
        sql.append("select sum(a.count) count,sum(a.vp_count) vpcount,b.name name from track_record_daily a,track_factor b where a.factor_id = b.id and factor_id in (")
                .append(params.get("fids"))
                .append(") and date>=")
                .append(params.get("startTime"))
                .append(" and date <=")
                .append(params.get("endTime"));
        if(!"0".equals(params.get("country"))){
            sql.append(" and a.country = '").append(params.get("country")+"'");
        }
        if(!"0".equals(params.get("pageid"))){
            sql.append(" and a.pageId = '").append(params.get("pageid")+"'");
        }
        sql.append(" group by b.name");

        log.info(sql.toString());
        try{
            listPie = reportDao.abtestDPane(sql.toString());
            listLine = rateDayToDay(params);
        }catch (Exception e){
            e.printStackTrace();
        }
        obj.element("listPie", listPie);
        obj.element("listLine", listLine);
        return obj;
    }

    public JSONObject rateHPane(Map<String, Object> params) {
        String argumentField = "date";
        String[] xenonPalette = new String[]{"#68b828","#7c38bc","#0e62c7","#fcd036","#4fcdfc","#00b19d","#ff6264","#f7aa47"};
        String[] factorIds = params.get("fids").toString().split(",");
        JSONObject obj = new JSONObject();
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> countMaps = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> vpcountMaps = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> series = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> pieList = new ArrayList<Map<String,Object>>();
        List<String> dateList = DateUtil.getDaysBetwSDateAndEDate(Integer.parseInt(params.get("startTime").toString()), Integer.parseInt(params.get("endTime").toString()));
        String currentDate = DateUtil.monthDateFormat(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMin = calendar.get(Calendar.MINUTE);
        for(String date:dateList){
            if(date!=null&&!date.equals(currentDate)){
                for(int j=0;j<=23;j++){
                    Map<String,Object> m = new HashMap<String,Object>();
                    m.put(argumentField,date.concat(" ").concat(String.valueOf(j)));
                    maps.add(m);
                    countMaps.add(m);
                    vpcountMaps.add(m);
                }
            }else{
                for(int j=0;j<=currentHour;j++){
                    Map<String,Object> m = new HashMap<String,Object>();
                    m.put(argumentField,date.concat(" ").concat(String.valueOf(j)));
                    maps.add(m);
                    countMaps.add(m);
                    vpcountMaps.add(m);
                }
            }
        }
        String endTimeStr = String.valueOf(Integer.parseInt(params.get("endTime").toString()));
        String endTimeFormat = endTimeStr.substring(0,4)+"-"+endTimeStr.substring(4,6)+"-"+endTimeStr.substring(6,8);
        if(endTimeFormat.equals(currentDate)){
            if(currentMin<=5){
                maps.remove(maps.size()-1);
                maps.remove(maps.size()-1);

                countMaps.remove(countMaps.size()-1);
                countMaps.remove(countMaps.size()-1);

                vpcountMaps.remove(vpcountMaps.size()-1);
                vpcountMaps.remove(vpcountMaps.size()-1);
            }else{
                maps.remove(maps.size() - 1);
                countMaps.remove(countMaps.size() - 1);
                vpcountMaps.remove(vpcountMaps.size() - 1);
            }
        }
        try{
            for(int i=0;i<factorIds.length;i++){
                StringBuffer sql = new StringBuffer("select sum(a.count) count,CASE sum(a.vp_count) when 0 then 1 else sum(a.vp_count) end vpcount,cast(format(sum(a.count)/(CASE sum(a.vp_count) when 0 then 1 else sum(a.vp_count) end) ,2)*100 as signed) rate,b.name name,a.date day,a.hour hour,b.id factorid from track_record_hourly a,track_factor b where a.factor_id = b.id");
                sql.append(" and factor_id =").append(ConvertUtil.converterToInt(factorIds[i])).append(" and date>=").append(Integer.parseInt(params.get("startTime").toString())).append(" and date <=").append(Integer.parseInt(params.get("endTime").toString()));
                if(!"0".equals(params.get("country"))){
                    sql.append(" and a.country = '").append(params.get("country")+"'");
                }
                if(!"0".equals(params.get("pageid"))){
                    sql.append(" and a.pageId = '").append(params.get("pageid")+"'");
                }
                sql.append(" group by hour,date");
                log.info(sql.toString());
                List<Map<String, Object>> list = reportDao.abtestHPane(sql.toString());
                if(list.size()>0){
                    for(int j=0;j<maps.size();j++){
                        Map<String,Object> m = new HashMap<String,Object>();
                        Map m1 = maps.get(j);
                        Set<String> keys = m1.keySet();
                        Iterator it = keys.iterator();
                        while (it.hasNext()){
                            String key = it.next().toString();
                            String value = m1.get(key).toString();
                            if(!argumentField.equals(key)){
                                m.put(key, Integer.parseInt(value));
                            }else{
                                m.put(key, value);
                            }

                        }
                        m.put(list.get(0).get("name").toString(),0);
                        maps.set(j,m);
                    }
                    for(int j=0;j<countMaps.size();j++){
                        Map<String,Object> m = new HashMap<String,Object>();
                        Map m1 = countMaps.get(j);
                        Set<String> keys = m1.keySet();
                        Iterator it = keys.iterator();
                        while (it.hasNext()){
                            String key = it.next().toString();
                            String value = m1.get(key).toString();
                            if(!argumentField.equals(key)){
                                m.put(key, Integer.parseInt(value));
                            }else{
                                m.put(key, value);
                            }

                        }
                        m.put(list.get(0).get("name").toString(),0);
                        countMaps.set(j,m);
                    }
                    for(int j=0;j<vpcountMaps.size();j++){
                        Map<String,Object> m = new HashMap<String,Object>();
                        Map m1 = vpcountMaps.get(j);
                        Set<String> keys = m1.keySet();
                        Iterator it = keys.iterator();
                        while (it.hasNext()){
                            String key = it.next().toString();
                            String value = m1.get(key).toString();
                            if(!argumentField.equals(key)){
                                m.put(key, Integer.parseInt(value));
                            }else{
                                m.put(key, value);
                            }

                        }
                        m.put(list.get(0).get("name").toString(),0);
                        vpcountMaps.set(j,m);
                    }
                    Map<String,Object> ser = new HashMap<String,Object>();
                    ser.put("valueField",list.get(0).get("name"));
                    ser.put("name",list.get(0).get("name"));
                    ser.put("color",xenonPalette[i]);
                    series.add(ser);
                }
                for (int k=0;k<maps.size();k++){
                    Map<String, Object> m=(Map<String, Object>)maps.get(k);
                    for(int l=0;l<list.size();l++){
                        String datestr = list.get(l).get("day").toString();
                        String date = datestr.substring(0,4)+"-"+datestr.substring(4,6)+"-"+datestr.substring(6,8);
                        if(m.get(argumentField).toString().equals(date+" "+list.get(l).get("hour").toString())){
                            m.put(list.get(l).get("name").toString(), list.get(l).get("rate"));
                            maps.set(k,m);
                        }
                    }
                }

                for (int k=0;k<countMaps.size();k++){
                    Map<String, Object> m=(Map<String, Object>)countMaps.get(k);
                    for(int l=0;l<list.size();l++){
                        String datestr = list.get(l).get("day").toString();
                        String date = datestr.substring(0,4)+"-"+datestr.substring(4,6)+"-"+datestr.substring(6,8);
                        if(m.get(argumentField).toString().equals(date+" "+list.get(l).get("hour").toString())){
                            m.put(list.get(l).get("name").toString(), list.get(l).get("count"));
                            countMaps.set(k,m);
                        }
                    }
                }

                for (int k=0;k<vpcountMaps.size();k++){
                    Map<String, Object> m=(Map<String, Object>)vpcountMaps.get(k);
                    for(int l=0;l<list.size();l++){
                        String datestr = list.get(l).get("day").toString();
                        String date = datestr.substring(0,4)+"-"+datestr.substring(4,6)+"-"+datestr.substring(6,8);
                        if(m.get(argumentField).toString().equals(date+" "+list.get(l).get("hour").toString())){
                            m.put(list.get(l).get("name").toString(), list.get(l).get("vpcount"));
                            vpcountMaps.set(k,m);
                        }
                    }
                }
            }
            StringBuffer sql = new StringBuffer();
            sql.append("select sum(a.count) count,sum(a.vp_count) vpcount, b.name name from track_record_hourly a,track_factor b where a.factor_id = b.id and factor_id in (")
                    .append(params.get("fids").toString()).append(") and date>=").append(Integer.parseInt(params.get("startTime").toString()))
                            .append(" and date <=").append(Integer.parseInt(params.get("endTime").toString()));
            if(!"0".equals(params.get("country"))){
                sql.append(" and a.country = '").append(params.get("country").toString()+"'");
            }
            if(!"0".equals(params.get("pageid"))){
                sql.append(" and a.pageId = '").append(params.get("pageid").toString()+"'");
            }
            sql.append(" group by b.name");
            log.info(sql.toString());
            pieList = reportDao.abtestHPane(sql.toString());

            Map<String,Object> rmap = new HashMap<String,Object>();
            rmap.put("record",maps);
            rmap.put("countmaps",countMaps);
            rmap.put("vpcountmaps",vpcountMaps);
            rmap.put("series", series);
            returnList.add(rmap);
            obj.element("listLine", returnList);
            obj.element("listPie", pieList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;
    }
}

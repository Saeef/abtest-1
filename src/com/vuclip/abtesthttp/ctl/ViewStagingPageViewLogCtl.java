package com.vuclip.abtesthttp.ctl;

import com.vuclip.abtesthttp.bean.TargetBean;
import com.vuclip.abtesthttp.bean.UserBean;
import com.vuclip.abtesthttp.service.*;
import com.vuclip.abtesthttp.util.ConvertUtil;
import com.vuclip.abtesthttp.util.JsonUtil;
import com.vuclip.abtesthttp.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Wangtiecai
 * Date: 2016/3/1
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/log")
public class ViewStagingPageViewLogCtl {
    private Logger logger = Logger.getLogger(this.getClass());
    @Resource
    private LogViewService logViewService;


    @RequestMapping("/logview")
    public String toManager(HttpServletRequest request,HttpServletResponse response){
        return ResponseUtil.LOGVIEW;
    }

    @ResponseBody
    @RequestMapping("/userLogview")
    public void logview(HttpServletRequest request,HttpServletResponse response) {
        int start = 0, end = 10000, sEcho = 1;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("start", start);
            params.put("end", end);
            params.put("key", "pageviewlogredis");
            List<String> list = Arrays.asList(logViewService.getLogs(params));
            String back="{data:"+list.toString()+"}";
            response.getWriter().print(back);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @ResponseBody
    @RequestMapping("/robotLogview")
    public void robotLogview(HttpServletRequest request,HttpServletResponse response) {
        int start = 0, end = 10000, sEcho = 1;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("start", start);
            params.put("end", end);
            params.put("key", "robootpageviewlogredis");
            List<String> list = Arrays.asList(logViewService.getLogs(params));
            String back="{data:"+list.toString()+"}";
            response.getWriter().print(back);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/deleteLogs")
    public void deleteLogs(HttpServletRequest request,HttpServletResponse response) {
        logViewService.deleteLogs();
    }
}

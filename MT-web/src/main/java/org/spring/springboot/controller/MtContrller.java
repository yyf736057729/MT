package org.spring.springboot.controller;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.spring.springboot.mq.MtSenderService;
import org.spring.springboot.mq.MtSenderServiceImpl;
import org.spring.springboot.config.SunCallable;
import org.spring.springboot.domain.*;
import org.spring.springboot.service.MongodbPhoneService;
import org.spring.springboot.util.ReportExcel;
import org.spring.springboot.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yuyunfeng
 * @create_time 2018/11/21
 * @describe 美团
 */
@Controller("mtContrller")
@RequestMapping
public class MtContrller {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    private long count;

    private static final Logger LOG = Logger.getLogger(Mt.class);
    @Autowired
    MtSenderService mtSenderService;

    @Autowired
    private MongodbPhoneService mongodbPhoneService;

    @ResponseBody
    @RequestMapping(value = "/statistic", method = RequestMethod.POST)
    public void statistic(HttpServletResponse httpServletResponse) throws IOException {

        int countAll =0;
        int countByDay = 0;
        List<Statistic> findstatistics = null;
        CountByRedis countByRedis = null;
        int notCount = 0;
        boolean hasKey = redisTemplate.hasKey("newFindCount");
        if (hasKey) { // 从缓存中取
            Object o  =  redisTemplate.opsForValue().get("newFindCount");
            JSONObject v = JSONObject.fromObject(o);
             countByRedis=(CountByRedis)JSONObject.toBean(v, CountByRedis.class);
            countAll = countByRedis.getCountAll();
            countByDay = countByRedis.getCountByDay();
            findstatistics = countByRedis.getFindstatistics();
            notCount = countByRedis.getNotCount();
        }
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        jsonObject.put("count_all", countAll);
        jsonObject.put("count_by_day", countByDay);
        jsonObject.put("find_statistics", findstatistics);
        jsonObject.put("notCount",notCount);


        try {
            ResultUtil.writeResult(httpServletResponse, jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @ResponseBody
    @RequestMapping(value = "/excel", method = RequestMethod.GET)
    public String excel(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Messages> messagesList = new ArrayList<>();
        boolean hasKey = redisTemplate.hasKey("newMessagesList");
        Messages messages = new Messages();
        if (hasKey) { // 从缓存中取
            Object o2 =(Object) redisTemplate.opsForValue().get("newMessagesList");
            JSONArray j1 = JSONArray.fromObject(o2);
            for(int i =0;i<j1.size();i++){
                Object o1 = (Object)j1.get(i);
                messages=(Messages)JSONObject.toBean((JSONObject) o1, Messages.class);
                messagesList.add(messages);
            }
        }
        ReportExcel reportExcel = new ReportExcel();
        reportExcel.excelExport(messagesList,"统计", Messages.class,1,resp,req);
        return "index";
    }



    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }




    @ResponseBody
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String uploadFile(@RequestParam(required = false, value = "files") MultipartFile[] files, HttpServletRequest request) throws IOException {
        if(files== null ) {
            return "";
        }
        MtSenderServiceImpl MTSenderServiceImpl1 = new MtSenderServiceImpl();
        long s1 = System.currentTimeMillis();
        List<String> list = new ArrayList();
        Phone phone = new Phone();
        InputStream is = null;
        BufferedReader br = null;
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {

                MultipartFile file = files[i];
                String name = file.getOriginalFilename();
                if (!file.isEmpty()) try {

                    is = file.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is));
                    String sb;
                    sb = br.readLine();
                    long l = System.currentTimeMillis();
                    while ((sb = br.readLine()) != null) {
                        list.add(sb);
                    }
                    long l2 = System.currentTimeMillis();
                    System.out.println("---" + (l2 - l) + "ms");

                    fixedThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {
//                            mtSenderService.send(new ArrayList<>());
                            try {
                                doReadList(list);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (Exception e) {
                    return "读取错误";
                } finally {
                    br.close();
                    is.close();
                }
            }
        } else {
            return "请选择文件";
        }
        long s2 = System.currentTimeMillis();
        System.out.println("运行用时" + (s2 - s1) + "ms");

            return "上传成功";
    }

    private void doReadList(List list) throws InterruptedException, ExecutionException, ExecutionException {
        ExecutorService threadPoolTaskExecutor = Executors.newFixedThreadPool(10);
//        /**初始化集合**/
//        List<String> list = new ArrayList<String>();


        /**接收集合各段的 执行的返回结果**/
        List<Future<Boolean>> futureList = new ArrayList<Future<Boolean>>();

        /**集合总条数**/
        int size = list.size();
        /**将集合切分的段数**/
        int sunSum = 10;
        int listStart,listEnd;
        /***当总条数不足10条时 用总条数 当做线程切分值**/
        if(sunSum > size){
            sunSum = size;
        }
        /**定义子线程**/
        SunCallable sunCallable ;
        /**将list 切分10份 多线程执行**/
        for (int i = 0; i < sunSum; i++) {
            /***计算切割  开始和结束**/
            listStart = size / sunSum * i ;
            listEnd = size / sunSum * ( i + 1 );
            /**最后一段线程会 出现与其他线程不等的情况**/
            if(i == sunSum - 1){
                listEnd = size;
            }
            /**线程切断**/
            List<String> sunList = list.subList(listStart,listEnd);
            /**子线程初始化**/
            sunCallable = new SunCallable(i,sunList, mongoTemplate,redisTemplate, mtSenderService);
            /***多线程执行***/
            futureList.add(threadPoolTaskExecutor.submit(sunCallable));
        }
        /**对各个线程段结果进行解析**/
//        for(Future<Boolean> future : futureList){
//            if(null != future && future.get()){
//                System.err.println("成功");
//            }else{
//                System.err.println("失败");
//            }
//        }
    }


    /**
     * 去除空格或回车
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }


    /**
     * 验证手机号
     *
     * @param str
     * @return
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        String s2 = "^[1](([0-9][0-9])|([4][5,7,9])|([5][^4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$";// 验证手机号
        if (StringUtils.isNotBlank(str)) {
            p = Pattern.compile(s2);
            m = p.matcher(str);
            b = m.matches();
        }
        return b;
    }

    public static  String  yestedayDate(int day){
        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, -day);    //得到前一天
        String  yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        return yestedayDate;
    }

    //    sign值是 token&手机号明文明文
// *            需要被加密的字符串
//     *            加密需要的密码
    public static void main(String[] args) {
//

        Calendar calendar = Calendar.getInstance();//此时打印它获取的是系统当前时间
        calendar.add(Calendar.DATE, -1);    //得到前一天
        String  yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        System.out.println(yestedayDate);

        //        int notCount = 0;
//        int byCount1 = 0;
//        int countAll = 0;
//        int listSize = 0;
//        int listSize1 = 0;
//        List<Mobile> mobiles = null;
//        boolean hasKey = redisTemplate.hasKey("FindMtexcel");
//        if (hasKey) { // 从缓存中取
//            mobiles = (List<Mobile>) redisTemplate.opsForValue().get("FindMtexcel");
//
//        }
//        Map<String ,Integer> map = null;
//        boolean hasKeyMap = redisTemplate.hasKey("MtexcelNotCount");
//        if (hasKeyMap) { // 从缓存中取
//            map = (Map<String ,Integer>) redisTemplate.opsForValue().get("MtexcelNotCount");
//            notCount = map.get("notCount");
//            byCount1 = map.get("byCount1");
//            countAll = map.get("countAll");
//            listSize = map.get("listSize");
//            listSize1 = map.get("listSize1");
//        }
//
//        String number = "15814448903";
//        String token = "masgyz3";
//        AES aes = new AES();
//        String sign = token + "&" + number;
////        String sign = "token&"+number;
////        String sign = "token="+token+"&number="+number;
//        sign = aes.encrypt(sign, "4523567890194556");
//
//        number = aes.encrypt(number, "4523567890194556");
//        token = aes.encrypt(token, "4523567890194556");
//
//        String param = "number=" + number + "&token=" + token + "&sign=" + sign;
//        String s = HttpRequest.sendGet("http://47.93.191.113/meituanApi/request", param);
//        System.out.println(s);
    }

}

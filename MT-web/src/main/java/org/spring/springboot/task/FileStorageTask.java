package org.spring.springboot.task;

import org.spring.springboot.config.SunCallable;
import org.spring.springboot.domain.Phone;
import org.spring.springboot.domain.PhoneFile;
import org.spring.springboot.domain.Statistic;
import org.spring.springboot.mq.MtSenderService;
import org.spring.springboot.util.fileutil.FileCallable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author yuyunfeng
 * @create_time 2018/12/4
 * @describe ${class}
 * //读取文件，号码入库
 */
@Component
public class FileStorageTask {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    MtSenderService mtSenderService;

    @Autowired
    MongoTemplate mongoTemplate;


    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }


    @Scheduled(cron = "0/15 * * * * ?")
    private void fileUpload() throws IOException, InterruptedException {
        //当钱文件夹下的所有文件
        //已经入库的文件不做读取，过滤（入库的文件名存入mongo）
        List<File> files = getFiles(Common.UPLOADFILEPATH);
        Map<String,String> map=new HashMap<String,String>();
        PhoneFile  phoneFile = new PhoneFile();
        List<PhoneFile> phoneFiles_add =new ArrayList<>();
        List<PhoneFile> phoneFiles = mongoTemplate.findAll( PhoneFile.class, "phone_file");
        for(File file : files){
            //检测文件是否被读取
            for(PhoneFile file1 :phoneFiles){
                map.put(file1.getFile_path(),"1");
            }
            if(!map.containsKey(file.getAbsolutePath())){//不存在为true,入库的文件添加到mongo
                phoneFile = new PhoneFile();
                phoneFile.setCreate(new Date());
                phoneFile.setFile_name(file.getName());
                phoneFile.setFile_path(file.getAbsolutePath());
                phoneFiles_add.add(phoneFile);
            }
        }
        if(phoneFiles_add.size()>0){
            mongoTemplate.insertAll(phoneFiles_add);
        }
        for(File file : files){
            //检测文件是否被读取
            if(!map.containsKey(file.getAbsolutePath())){//不存在为true,入库的文件添加到mongo
                phoneFile.setCreate(new Date());
                phoneFile.setFile_name(file.getName());
                phoneFile.setFile_path(file.getAbsolutePath());
                phoneFiles_add.add(phoneFile);
                uploadFile(file);
            }

        }
    }

    public static List<File> getFiles(String path){
        File root = new File(path);
        List<File> files = new ArrayList<File>();
        if(!root.isDirectory()){
            files.add(root);
        }else{
            File[] subFiles = root.listFiles();
            for(File f : subFiles){
                files.addAll(getFiles(f.getAbsolutePath()));
            }
        }
        return files;
    }

    private void uploadFile(File file) throws IOException {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
        BufferedReader br = null;
        /* 读取数据 */
        try {
            List<String> list = new ArrayList();
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                list.add(lineTxt);
            }
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
            System.err.println("read errors :" + e);
        }finally {
                br.close();
        }
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
        FileCallable fileCallable ;
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
            fileCallable = new FileCallable(i,sunList, mongoTemplate,redisTemplate, mtSenderService);
            /***多线程执行***/
            futureList.add(threadPoolTaskExecutor.submit(fileCallable));
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
}

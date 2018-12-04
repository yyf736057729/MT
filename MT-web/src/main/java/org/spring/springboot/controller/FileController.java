package org.spring.springboot.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.springboot.task.Common;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class FileController {


    //记录log
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);



    //多文件上传
    @ResponseBody
    @PostMapping("/batch/upload")
    public String handleFileUpload(HttpServletRequest request){

//        String realPath = request.getServletContext().getRealPath("//WEB-INF//uploadmulti//");
        String realPath = Common.UPLOADFILEPATH;
        //添加时间文件夹
        //当前日期
        Date date = new Date();
        //格式化并转换String类型
        String path=realPath+new SimpleDateFormat("yyyy/MM/dd").format(date);

        logger.info("多文件上传路径："+ path);

        File dest = new File(path);
        //检测目录是否存在
        if (!dest.exists()) {
            dest.mkdirs();
        }


        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("files");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i =0;i<files.size();++i){
            file = files.get(i);
            if(!file.isEmpty()){
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(new File(path+"/"+ Common.count++ +"__"+file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();

                } catch (Exception e) {
//                    e.printStackTrace();
                    stream = null;
                    return "上传失败"+i+"=>"+e.getMessage();
                }
            }else{
                return "上传失败"+i+",文件为空";
            }
        }
        System.out.println("------号码文件上传成功------今日上传数量:{"+Common.count+"}");
        return "上传成功";
    }









    //文件上传相关代码
    @RequestMapping(value = "upload")
    @ResponseBody
    public String upload(@RequestParam("test") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return "文件为空";
        }

        //获取文件名
        String fileName = file.getOriginalFilename();
        logger.info("上传文件名称为：" + fileName);
        //获取文件名的后缀
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        logger.info("上传的后缀名为：" + suffixName);

        //文件上传后的路径
//        String filePath = "e://test//";
//        File saveFile = new File(request.getSession().getServletContext().getRealPath("/upload/") + saveFileName);
//        String filePath = request.getServletContext().getRealPath("//WEB-INF//upload/");

        String filePath = Common.UPLOADFILEPATH;
        logger.info("上传的路径："+filePath);


        //解决中文问题，linux下中文路径，图片显示问题
//        fileName = UUID.randomUUID() + suffixName;

        File dest = new File(filePath + fileName);
        //打印上传文件地址
        logger.info(dest.getAbsolutePath());
        //检测目录是否存在
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
            return "上传成功";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

    //文件下载相关代码
    @RequestMapping("/download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response){
        String fileName = "zk2.log";
        if (fileName != null){
            //当前是从工程的WEB-INF//FILE//下获取问价（该目录可以在下面一行代码配置）
            String realPath = request.getServletContext().getRealPath("//WEB-INF//download");
            logger.info("目录："+ realPath);
            File file = new File(realPath,fileName);

            if (file.exists()){
                // 设置强制下载不打开
                response.setContentType("application/force-download");
                //设置文件名
                response.setHeader("Content-Disposition","attachment;fileName="+fileName);

                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try{
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i!=-1){
                        os.write(buffer,0,i);
                        i =bis.read(buffer);
                    }
                    logger.info("下载成功");
                }catch (Exception e){
                    e.printStackTrace();

                }finally {
                    if (bis !=null){
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null){
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }

        }

        return null;
    }

}

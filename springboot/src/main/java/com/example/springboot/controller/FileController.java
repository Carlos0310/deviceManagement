package com.example.springboot.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.example.springboot.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    @Value("${server.port}")
    private String port;
    private static final String ip="http://118.195.192.23";

    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        //String rootFilePath = System.getProperty("user.dir")+"/springboot/src/main/resources/files/"+flag+"_"+originalFilename;
        String rootFilePath = "/root/dm/dist/files/"+flag+"_"+originalFilename;
        FileUtil.writeBytes(file.getBytes(),rootFilePath);
        return Result.success(ip+":"+port+"/files/"+flag);
    }

    @PostMapping("/editor/upload")
    public JSON editorUpload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        //String rootFilePath = System.getProperty("user.dir")+"/springboot/src/main/resources/files/"+flag+"_"+originalFilename;
        String rootFilePath = "/root/dm/dist/files/"+flag+"_"+originalFilename;
        FileUtil.writeBytes(file.getBytes(),rootFilePath);
        String url=ip+":"+port+"/files/"+flag;
        JSONObject json=new JSONObject();
        json.set("errno", 0);
        JSONArray arr = new JSONArray();
        JSONObject data = new JSONObject();
        arr.add(data);
        data.set("url", url);
        json.set("data", arr);
        return json;
    }

    @GetMapping("/{flag}")
    public void getFiles(@PathVariable String flag, HttpServletResponse response) {
        OutputStream os;  // ???????????????????????????
        //String basePath = System.getProperty("user.dir") + "/springboot/src/main/resources/files/";  // ??????????????????????????????
        String basePath = "/root/dm/dist/files/";  // ??????????????????????????????
        List<String> fileNames = FileUtil.listFileNames(basePath);  // ???????????????????????????
        String fileName = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");  // ??????????????????????????????
        try {
            if (StrUtil.isNotEmpty(fileName)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(basePath + fileName);  // ??????????????????????????????????????????
                os = response.getOutputStream();   // ???????????????????????????
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("??????????????????");
        }
    }

}


package com.husd.postman;

import com.alibaba.fastjson.JSONObject;
import com.husd.Application;
import com.husd.postman.domain.Postman;
import com.husd.postman.domain.PostmanItem;
import com.husd.postman.domain.PostmanItemWithFolder;
import com.husd.postman.util.PostmanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.InputStreamSource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * 发现所有的URL，生成postman的配置文件
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class PostmanControllerTest {

    private final String[] blacklist = {"wait", "toString", "hashCode", "getClass", "notify", "notifyAll", "clone"};

    //黑名单的URL，不会被生成到文件里
    private final String[] url_blacklist = {"/error"};

    // 开启文件夹 如果是以下场景，建议开启：
    // 你的controller的包是类似这样的结构:
    // com.xxx.controller.a
    // com.xxx.controller.b
    // com.xxx.controller.c
    // 就可以设置为true，这样生成的postman就会自动生成多个文件夹
    // 默认是不开启，这样所有的URL，就会平铺，没有按包进行文件夹归类
    private final static boolean folder = false;

    //这个是最终生成的文件的目录
    private final static String targetFilePath = "/tmp/";
    //文件的名字 名字的后缀是.json
    private final static String targetFileName = "测试postman工具01";

    @Autowired
    private WebApplicationContext applicationContext;

    /**
     * 这行这个方法，就会生成postman的导出文件。
     */
    @Test
    public void generatePostmanFile() {

        Postman postman = new Postman();
        postman.setInfo(PostmanUtil.postman_info_v2_1_0_json("123456", targetFileName));
        List<PostmanItem> postmanItemList = new ArrayList<>();

        Map<String, PostmanItemWithFolder> folderMap = new LinkedHashMap<>();

        RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping)applicationContext.getBean("requestMappingHandlerMapping");
        Map<RequestMappingInfo, HandlerMethod> methodMap = mapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : methodMap.entrySet()) {

            RequestMappingInfo requestMappingInfo = entry.getKey();
            HandlerMethod handlerMethod = entry.getValue();
            String url = getUrl(requestMappingInfo);
            url = replacePathParam(url);
            if (inUrlBlacklist(url)) {
                continue;
            }
            RequestMethod requestMethod = getRequestMethod(requestMappingInfo);
            //拿到请求的参数
            String json = "";
            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
            if (methodParameters != null && methodParameters.length > 0) {
                Type paramClass = handlerMethod.getMethodParameters()[0].getParameter().getParameterizedType();
                if(paramClass == InputStream.class ||
                paramClass == InputStreamSource.class ||
                paramClass == java.io.File.class) {
                    json = "";
                } else {
                    Object param = getMockParam(paramClass);
                    if (param != null) {
                        json = JSONObject.toJSONString(param);
                    }
                }
            }

            String className = classApiTags(handlerMethod);
            String methodName = methodApiTags(handlerMethod);
            String urlName = className + "-" + methodName;

            if (folder) {
                String folderName = getFolderName(handlerMethod);
                PostmanItemWithFolder postmanItem = folderMap.get(folderName);
                if (postmanItem == null) {
                    postmanItem = PostmanUtil.postman_item_folder(folderName, urlName, url, requestMethod, json);
                } else {
                    postmanItem.getItem().add(PostmanUtil.postman_item(urlName, url, requestMethod, json));
                }
                folderMap.put(folderName, postmanItem);
            } else {
                postmanItemList.add(PostmanUtil.postman_item(urlName, url, requestMethod, json));
            }
        }

        if (folder) {
            for (Map.Entry<String, PostmanItemWithFolder> entry : folderMap.entrySet()) {
                postmanItemList.add(entry.getValue());
            }
        }

        postman.setItem(postmanItemList);
        write2TargetFile(targetFilePath + targetFileName + ".json", JSONObject.toJSONString(postman));
        //也可以复制出去，自己保存。
        System.out.println(JSONObject.toJSONString(postman));
    }

    private String getFolderName(HandlerMethod handlerMethod) {

        String packageName = handlerMethod.getBeanType().getPackage().getName();
        int index = packageName.lastIndexOf(".");
        return packageName.substring(index + 1);
    }

    private String methodApiTags(HandlerMethod handlerMethod) {

        String value = "";
        ApiOperation apiOperation = handlerMethod.getMethod().getDeclaredAnnotation(ApiOperation.class);
        if (apiOperation == null) {
            value = handlerMethod.getMethod().getName();
        } else {
            value = apiOperation.value();
        }
        return value;
    }

    private String classApiTags(HandlerMethod handlerMethod) {

        String value = "";
        Api api = handlerMethod.getBeanType().getDeclaredAnnotation(Api.class);
        if (api == null) {
            value = String.valueOf(handlerMethod.getBean());
        } else {
            value = (api.tags() == null || api.tags().length == 0) ? String.valueOf(handlerMethod.getBean()) : api.tags()[0];
        }
        return value;
    }

    private Object getMockParam(Type genericType) {

        Object obj = null;
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] types = parameterizedType.getActualTypeArguments();
            for (Type type : types) {
                Class realType = (Class) type;
                obj = TestHelper.generateMany(realType, 2);
            }
        } else {
            Class realType = (Class) genericType;
            if (realType == HttpServletRequest.class ||
                    realType == HttpServletResponse.class ||
                    realType == CommonsMultipartFile.class) {
                return obj;
            }
            obj = TestHelper.newInstance(realType);
        }
        return obj;
    }

    //有些URL是带参数的URL 例如: /app/getInfo/{xxx} 需要替换为 /app/getInfo/123
    private String replacePathParam(String url) {

        if (url.contains("{") && url.contains("}")) {
            int index1 = url.lastIndexOf("{");
            int index2 = url.lastIndexOf("}");
            final int endIndex = url.length() - 1;
            if (index2 != endIndex) {
                url = url.substring(0, index1) + "123" + url.substring(index2, endIndex + 1);
            } else {
                url = url.substring(0, index1) + "123";
            }
        }
        return url;
    }

    private RequestMethod getRequestMethod(RequestMappingInfo requestMappingInfo) {

        Set<RequestMethod> set = requestMappingInfo.getMethodsCondition().getMethods();
        for (RequestMethod requestMethod : set) {
            return requestMethod;
        }
        return RequestMethod.POST;
    }

    private String getUrl(RequestMappingInfo requestMappingInfo) {

        Set<String> set = requestMappingInfo.getPatternsCondition().getPatterns();
        for (String url : set) {
            return url;
        }
        return "";
    }

    private boolean inBlacklist(String name) {

        for (String s : blacklist) {
            if (StringUtils.equalsIgnoreCase(name, s)) {
                return true;
            }
        }
        return false;
    }

    private boolean inUrlBlacklist(String name) {

        if (name.startsWith("/swagger")) {
            return true;
        }
        for (String s : url_blacklist) {
            if (StringUtils.equalsIgnoreCase(name, s)) {
                return true;
            }
        }
        return false;
    }

    private void write2TargetFile(String path, String content) {

        try {
            Path p = Paths.get(path);
            Files.deleteIfExists(p);
            Files.createFile(p);
            Files.write(p, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

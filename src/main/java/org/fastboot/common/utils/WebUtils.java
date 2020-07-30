package org.fastboot.common.utils;

import cn.hutool.http.Method;
import org.fastboot.common.dto.HeadDto;
import org.fastboot.common.enums.ConstEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by laotang on 2020/7/29.
 */
public class WebUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebUtils.class);

    public static void buildResponse(HttpServletRequest request, HttpServletResponse response, Exception e) {
        try {
            String encode = request.getCharacterEncoding();
            String contentType = request.getContentType();
            response.setCharacterEncoding(ToolsKit.isEmpty(encode) ? ConstEnums.COMMON.UTF8.getValue() : encode);
            response.setContentType(ToolsKit.isEmpty(contentType) ? MediaType.APPLICATION_JSON_VALUE : contentType);
            PrintWriter out = response.getWriter();
            HeadDto headDto = ToolsKit.getThreadLocalDto();
            headDto.setCode(1);
            // 服务器业务处理执行时间(毫秒)
            headDto.setProcessTime(System.currentTimeMillis() - headDto.getStartTime());
            // 异常信息
            headDto.setMsg(e.getMessage());
            // 移除
            ToolsKit.removeThreadLocalDto();
            out.append(ToolsKit.toJsonString(headDto));
        } catch (IOException ioe) {
            LogUtils.log(LOGGER, "构建Response时出错: " + ioe.getMessage(), ioe);
        }
    }

}

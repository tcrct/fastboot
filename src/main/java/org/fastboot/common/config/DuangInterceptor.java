package org.fastboot.common.config;

import org.fastboot.common.dto.R;
import org.fastboot.common.handler.HandlerFactory;
import org.fastboot.common.utils.LogUtils;
import org.fastboot.common.utils.ToolsKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 *
 * @author Laotang
 * @since 1.0
 */
public class DuangInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DuangInterceptor.class);

    /**
     * 前置拦截器
     * 预处理回调方法，若方法返回值为true，请求继续（调用下一个拦截器或处理器方法）；
     * 若方法返回值为false，请求处理流程中断，不会继续调用其他的拦截器或处理器方法，此时需要通过response产生响应；
     *
     * @param request   请求对象
     * @param response  返回对象
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            return HandlerFactory.handler(request, response);
        } catch (Exception e) {
            LOGGER.warn("框架在执行拦截器时出错: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 后处理回调方法，实现处理器的后处理（但在渲染视图之前），此时可以通过modelAndView对模型数据进行处理或对视图进行处理；
     * @param request   请求对象
     * @param response 返回对象
     * @param handler 处理器
     * @param modelAndView  视图对象
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        LogUtils.log(LOGGER,"request uri：{}",request.getRequestURI());
        if (null != modelAndView) {
            LogUtils.log(LOGGER,"result: " + ToolsKit.toJsonString(modelAndView.getModelMap()));
        }
        if(handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
            for (MethodParameter methodParameter : methodParameters) {
                Class clazz = methodParameter.getParameterType();
                if (ClassUtils.isAssignable(R.class, clazz)) {
                    System.out.println(methodParameter.getParameter());
                }
            }
            LogUtils.log(LOGGER,"当前拦截的方法为：{}",handlerMethod.getMethod().getName());
            LogUtils.log(LOGGER,"当前拦截的方法参数长度为：{}",handlerMethod.getMethod().getParameters().length);
            LogUtils.log(LOGGER,"当前拦截的方法为：{}",handlerMethod.getBean().getClass().getName());
            LogUtils.log(LOGGER,"开始拦截---------");
            String uri = request.getRequestURI();
            LogUtils.log(LOGGER,"拦截的uri："+uri);
        }

    }


    /**
     * 整个请求处理完毕回调方法，即在视图渲染完毕时调用。
     * @param request   请求对象
     * @param response 返回对象
     * @param handler 处理器
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
//        System.out.println(response.getTrailerFields());
//        if(handler instanceof HandlerMethod){
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            System.out.println(handlerMethod.getBean());
//            System.out.println(handlerMethod.getReturnType());
//            MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
//            for (MethodParameter methodParameter : methodParameters) {
//                Class clazz = methodParameter.getParameterType();
//                if (ClassUtils.isAssignable(R.class, clazz)) {
//                    System.out.println(methodParameter.getParameter());
//                }
//            }
//            HeadDto headDto = ToolsKit.getThreadLocalDto();
//            System.out.println("@@@@@@@@@@@: " + handlerMethod);
//            if (headDto.getUri().equalsIgnoreCase(request.getRequestURI())) {
//                headDto.setCode(1);
//                // 服务器业务处理执行时间(毫秒)
//                headDto.setProcessTime(System.currentTimeMillis() - headDto.getStartTime());
//                // 移除
//                ToolsKit.removeThreadLocalDto();
//            }
//            LogUtils.log(LOGGER,"当前拦截的方法为：{}",handlerMethod.getMethod().getName());
//            LogUtils.log(LOGGER,"当前拦截的方法参数长度为：{}",handlerMethod.getMethod().getParameters().length);
//            LogUtils.log(LOGGER,"当前拦截的方法为：{}",handlerMethod.getBean().getClass().getName());
//            LogUtils.log(LOGGER,"开始拦截---------");
//            String uri = request.getRequestURI();
//            LogUtils.log(LOGGER,"拦截的uri："+uri);
//        }
    }
}

package cn.xglory.service.common.controller.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 服务Controller基类
 * @author Bob
 *
 */
public class BaseServiceController {
	
	private static Logger logger = LoggerFactory.getLogger(BaseServiceController.class);
	
	@ExceptionHandler({Exception.class})  
	@ResponseBody
    public ModelAndView handleException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		logger.error("", ex);
		ModelAndView mv = new ModelAndView();
		return mv;
	}
}

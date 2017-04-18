package cn.xglory.service.common.controller.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 服务Controller基类
 * @author Bob
 *
 */
public abstract class BaseServiceController {
	
	private static Log logger = LogFactory.getLog(BaseServiceController.class);
	
	@ExceptionHandler({Exception.class})  
	@ResponseBody
    public ModelAndView handleException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		logger.error("", ex);
		ModelAndView mv = new ModelAndView();
		return mv;
	}
}

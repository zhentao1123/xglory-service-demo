package cn.xglory.service.demo.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.xglory.service.demo.entity.User;

@RestController  
@RequestMapping("/user")
public class DemoRestController {
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)  
    public User view(@PathVariable("id") Long id) {  
        User user = new User();  
        user.setName("Tom");  
        return user;  
    }  
}

package xyz.pplax.spider.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ViewsController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

}

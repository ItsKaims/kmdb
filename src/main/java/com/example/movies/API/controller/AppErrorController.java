package com.example.movies.API;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AppErrorController implements ErrorController {
    /*This is for displaying my custom error if anything goes wrong and page is not found 
    @RequestMapping("/error")
    public String handleError() {
        // return the name of a view, e.g. "customError"
        return "customError";
    }
    */
}

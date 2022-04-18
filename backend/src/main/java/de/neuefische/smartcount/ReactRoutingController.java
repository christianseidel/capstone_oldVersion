package de.neuefische.smartcount;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

public class ReactRoutingController {

    @Controller
    public class ReactRoutingForwarding {
        @RequestMapping(value = "/**/{[path:[^\\.]*}")
        public String forwardToRouteUrl() {
            return "forward:/";
        }
    }
}

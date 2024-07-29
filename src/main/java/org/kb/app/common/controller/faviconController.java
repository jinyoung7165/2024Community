package org.kb.app.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
public class faviconController {

    @GetMapping("favicon.ico")
    @ApiIgnore
    public void returnNoFavicon() {
    }
}

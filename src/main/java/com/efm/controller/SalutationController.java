package com.efm.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class SalutationController {

    @ApiOperation("Salutation")
    @RequestMapping("/salutation")
    public String getSalutation(){

        return "Hello World";
    }
}

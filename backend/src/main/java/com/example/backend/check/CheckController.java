package com.example.backend.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/checks")
public class CheckController {

    private final CheckService checkService;

    @Autowired
    public CheckController(CheckService checkService) {
        this.checkService = checkService;
    }

    @GetMapping
    public List<CheckDto> getCheckDtoList() {
        return checkService.getCheckDtoList();
    }

    @PostMapping("/run")
    public List<CheckResult> runCheckDtoList(@RequestBody List<CheckDto> checkDtoList) {
        return checkService.runCheckDtoList(checkDtoList);
    }

}

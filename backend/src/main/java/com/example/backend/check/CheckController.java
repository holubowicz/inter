package com.example.backend.check;

import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> runCheckDtoList(@Nullable @RequestBody List<CheckDto> checkDtoList) {
        if (checkDtoList == null || checkDtoList.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Check DTO list must be provided and cannot be empty");
        }

        boolean allMatchCheckDto = checkDtoList.stream()
                .allMatch(item -> item != null && item.getName() != null && !item.getName().isEmpty());

        if (!allMatchCheckDto) {
            return ResponseEntity.badRequest()
                    .body("Invalid item found in the list. All items must be of type Check DTO and not null nor any fields");
        }

        List<CheckResult> results = checkService.runCheckDtoList(checkDtoList);

        return ResponseEntity.ok(results);
    }

}

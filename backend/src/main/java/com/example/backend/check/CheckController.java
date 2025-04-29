package com.example.backend.check;

import com.example.backend.check.model.CheckDto;
import com.example.backend.check.model.CheckInputDto;
import com.example.backend.check.model.CheckResult;
import com.example.backend.database.schema.CheckHistory;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.backend.check.CheckErrorMessages.CHECK_INPUT_DTO_LIST_INCORRECT;
import static com.example.backend.check.CheckErrorMessages.CHECK_INPUT_DTO_LIST_ITEM_INCORRECT;

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

    @GetMapping("{checkName}/history")
    public List<CheckHistory> getCheckHistoryList(@PathVariable String checkName) {
        return checkService.getCheckHistoryList(checkName);
    }

    @PostMapping("run")
    public ResponseEntity<?> runCheckDtoList(@Nullable @RequestBody List<CheckInputDto> checkInputDtoList) {
        if (checkInputDtoList == null || checkInputDtoList.isEmpty()) {
            return ResponseEntity.badRequest().body(CHECK_INPUT_DTO_LIST_INCORRECT);
        }

        boolean allMatchCheckDto = checkInputDtoList.stream()
                .allMatch(item -> item != null && item.getName() != null && !item.getName().isEmpty());

        if (!allMatchCheckDto) {
            return ResponseEntity.badRequest().body(CHECK_INPUT_DTO_LIST_ITEM_INCORRECT);
        }

        List<CheckResult> results = checkService.runCheckDtoList(checkInputDtoList);

        return ResponseEntity.ok(results);
    }

}

package com.example.backend.check;

import com.example.backend.check.model.CheckDto;
import com.example.backend.check.model.CheckInputDto;
import com.example.backend.check.model.CheckResult;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/checks")
public class CheckController {

    public static final String CHECK_INPUT_DTO_LIST_INCORRECT_ERROR =
            "CheckInputDto list must be provided and cannot be empty";
    public static final String CHECK_INPUT_DTO_LIST_ITEM_INCORRECT_ERROR =
            "Invalid item found in the list. All items must be of type CheckInputDto and not null nor empty any fields";

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
    public ResponseEntity<?> runCheckDtoList(@Nullable @RequestBody List<CheckInputDto> checkInputDtoList) {
        if (checkInputDtoList == null || checkInputDtoList.isEmpty()) {
            return ResponseEntity.badRequest().body(CHECK_INPUT_DTO_LIST_INCORRECT_ERROR);
        }

        boolean allMatchCheckDto = checkInputDtoList.stream()
                .allMatch(item -> item != null && item.getName() != null && !item.getName().isEmpty());

        if (!allMatchCheckDto) {
            return ResponseEntity.badRequest().body(CHECK_INPUT_DTO_LIST_ITEM_INCORRECT_ERROR);
        }

        List<CheckResult> results = checkService.runCheckDtoList(checkInputDtoList);

        return ResponseEntity.ok(results);
    }

}

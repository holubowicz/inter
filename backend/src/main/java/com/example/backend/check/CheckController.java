package com.example.backend.check;

import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.dto.CheckDTO;
import com.example.backend.check.model.dto.CheckExecutionDTO;
import com.example.backend.check.model.dto.CheckInputDTO;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.backend.check.common.error.message.ApiErrorMessage.CHECK_INPUT_DTO_LIST_INCORRECT;
import static com.example.backend.check.common.error.message.ApiErrorMessage.CHECK_INPUT_DTO_LIST_ITEM_INCORRECT;

@RestController
@RequestMapping("api/checks")
@AllArgsConstructor
public class CheckController {

    private final CheckService checkService;

    @GetMapping
    public List<CheckDTO> getCheckDTOs() {
        return checkService.getCheckDTOs();
    }

    @GetMapping("{checkName}/history")
    public List<CheckExecutionDTO> getCheckExecutionDTOs(@PathVariable String checkName) {
        return checkService.getCheckExecutionDTOs(checkName);
    }

    @PostMapping("run")
    public ResponseEntity<?> runCheckDTOs(@Nullable @RequestBody List<CheckInputDTO> checkInputDTOs) {
        if (checkInputDTOs == null || checkInputDTOs.isEmpty()) {
            return ResponseEntity.badRequest().body(CHECK_INPUT_DTO_LIST_INCORRECT);
        }

        boolean allMatchCheckDTO = checkInputDTOs.stream()
                .allMatch(item -> item != null && item.getName() != null && !item.getName().isEmpty());

        if (!allMatchCheckDTO) {
            return ResponseEntity.badRequest().body(CHECK_INPUT_DTO_LIST_ITEM_INCORRECT);
        }

        List<CheckResult> results = checkService.runCheckDTOs(checkInputDTOs);

        return ResponseEntity.ok(results);
    }

}

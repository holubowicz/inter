package com.example.backend.check;

import com.example.backend.check.model.CheckCategory;
import com.example.backend.check.model.CheckMetadata;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.dto.CheckDTO;
import com.example.backend.check.model.dto.CheckExecutionDTO;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.backend.check.common.error.message.ApiErrorMessage.*;

@RestController
@RequestMapping("api/checks")
@CrossOrigin(origins = "*")
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
    public ResponseEntity<?> runCheckMetadataList(@Nullable @RequestBody List<CheckMetadata> metadataList) {
        if (metadataList == null || metadataList.isEmpty()) {
            return ResponseEntity.badRequest().body(CHECK_METADATA_LIST_INCORRECT);
        }

        boolean allMatchCheckDTO = metadataList.stream()
                .allMatch(item -> item != null && item.getName() != null && !item.getName().isEmpty());

        if (!allMatchCheckDTO) {
            return ResponseEntity.badRequest().body(CHECK_METADATA_LIST_ITEM_INCORRECT);
        }

        List<CheckResult> results = checkService.runCheckMetadataList(metadataList);
        return ResponseEntity.ok(results);
    }

    @GetMapping("categories")
    public List<CheckCategory> getCheckCategories() {
        return checkService.getCheckCategories();
    }

    @PostMapping("categories/run")
    public ResponseEntity<?> runCheckCategories(@Nullable @RequestBody List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return ResponseEntity.badRequest().body(CATEGORIES_INCORRECT);
        }
        List<CheckResult> results = checkService.runCheckCategories(categories);
        return ResponseEntity.ok(results);
    }

}

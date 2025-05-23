package com.example.backend.check;

import com.example.backend.check.common.exception.CheckCategoriesNullException;
import com.example.backend.check.common.exception.CheckMetadataListNullException;
import com.example.backend.check.loader.CheckLoader;
import com.example.backend.check.model.CheckCategory;
import com.example.backend.check.model.CheckMetadata;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.dto.CheckDTO;
import com.example.backend.check.model.dto.CheckExecutionDTO;
import com.example.backend.check.model.dto.factory.CheckExecutionDTOFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CheckService {

    private final CheckLoader checkLoader;
    private final CheckRunner checkRunner;

    public List<CheckDTO> getCheckDTOs() {
        return checkLoader.getCheckMetadataList().stream()
                .map(checkRunner::getCheckDTO)
                .sorted(
                        Comparator.comparing((CheckDTO dto) -> dto.getMetadata().getName())
                                .thenComparing(dto -> dto.getMetadata().getName())
                )
                .toList();
    }

    public List<CheckExecutionDTO> getCheckExecutionDTOs(String checkName) {
        return checkRunner.getCheckExecutions(checkName).stream()
                .map(CheckExecutionDTOFactory::createCheckExecutionDTO)
                .toList();
    }

    public List<CheckResult> runCheckMetadataList(List<CheckMetadata> metadataList) {
        if (metadataList == null) {
            throw new CheckMetadataListNullException();
        }
        return metadataList
                .parallelStream()
                .map(checkLoader::convertIntoCheck)
                .map(checkRunner::runCheck)
                .sorted(
                        Comparator.comparing((CheckResult result) -> result.getMetadata().getCategory())
                                .thenComparing(result -> result.getMetadata().getName())
                )
                .toList();
    }

    public List<CheckCategory> getCheckCategories() {
        return checkLoader.getCheckMetadataList().stream()
                .collect(Collectors.groupingBy(
                        CheckMetadata::getCategory,
                        Collectors.counting()
                ))
                .entrySet()
                .stream()
                .map(entry -> new CheckCategory(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(CheckCategory::getName))
                .toList();
    }

    public List<CheckResult> runCheckCategories(List<String> categories) {
        if (categories == null) {
            throw new CheckCategoriesNullException();
        }
        return checkLoader.getCheckMetadataList()
                .parallelStream()
                .filter(metadata -> categories.contains(metadata.getCategory()))
                .map(checkLoader::convertIntoCheck)
                .map(checkRunner::runCheck)
                .sorted(
                        Comparator.comparing((CheckResult result) -> result.getMetadata().getCategory())
                                .thenComparing(result -> result.getMetadata().getName())
                )
                .toList();
    }

}

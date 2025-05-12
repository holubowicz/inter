package com.example.backend.check;

import com.example.backend.check.common.exception.CheckMetadataListNullException;
import com.example.backend.check.loader.CheckLoader;
import com.example.backend.check.model.CheckMetadata;
import com.example.backend.check.model.CheckResult;
import com.example.backend.check.model.dto.CheckDTO;
import com.example.backend.check.model.dto.CheckExecutionDTO;
import com.example.backend.check.model.dto.factory.CheckExecutionDTOFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class CheckService {

    private final CheckLoader checkLoader;
    private final CheckRunner checkRunner;

    public List<CheckDTO> getCheckDTOs() {
        return checkLoader.getCheckMetadataList().stream()
                .map(checkRunner::getCheckDTO)
                .sorted(Comparator.comparing(dto -> dto.getMetadata().getName()))
                .toList();
    }

    public List<String> getCheckCategories() {
        return checkLoader.getCheckMetadataList().stream()
                .map(CheckMetadata::getCategory)
                .distinct()
                .sorted()
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
                .stream()
                .map(checkLoader::convertIntoCheck)
                .map(checkRunner::runCheck)
                .toList();
    }

}

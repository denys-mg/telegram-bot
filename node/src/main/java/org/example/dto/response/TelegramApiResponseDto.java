package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.Nullable;

public record TelegramApiResponseDto(
        boolean ok,
        @Nullable
        Result result,
        @Nullable
        @JsonProperty(ERROR_CODE_FIELD)
        Integer errorCode,
        @Nullable
        String description
) {
    private static final String ERROR_CODE_FIELD = "error_code";

    public boolean isResponseSuccessful() {
        return ok;
    }

    public record Result(
            @JsonProperty(FILE_ID_FIELD)
            String fileId,
            @JsonProperty(FILE_UNIQUE_ID_FIELD)
            String fileUniqueId,
            @JsonProperty(FILE_SIZE_FIELD)
            Integer fileSize,
            @JsonProperty(FILE_PATH_FIELD)
            String filePath
    ) {
        private static final String FILE_ID_FIELD = "file_id";

        private static final String FILE_UNIQUE_ID_FIELD = "file_unique_id";

        private static final String FILE_SIZE_FIELD = "file_size";

        private static final String FILE_PATH_FIELD = "file_path";
    }
}

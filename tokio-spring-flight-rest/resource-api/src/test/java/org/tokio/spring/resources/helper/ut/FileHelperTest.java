package org.tokio.spring.resources.helper.ut;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.test.context.ActiveProfiles;
import org.tokio.spring.resources.helper.FileHelper;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

@ActiveProfiles("test")
@Slf4j
class FileHelperTest {

    @TempDir
    private Path tempDir;

    @Test
    void givenNullSegment_whenGetCurrentWorking_thenReturnPathWorkingDirectory() {
        Path result = FileHelper.getCurrentWorking(null);
        Assertions.assertThat(result).isNotNull()
                .returns(FileSystems.getDefault().getPath(StringUtils.EMPTY).toString(), Path::toString);
    }

    @Test
    void givenSegmentPathUnknown_whenGetCurrentWorking_thenReturnPathWorkingDirectory() {
        final String segmentPath = "/home";
        Path result = FileHelper.getCurrentWorking(segmentPath);
        Assertions.assertThat(result).isNotNull()
                .returns(FileSystems.getDefault().getPath(segmentPath).toString(), Path::toString)
                .satisfies(path -> Assertions.assertThat(path.toFile().exists()).isFalse());
    }

    @Test
    void givenSegmentPathKnown_whenGetCurrentWorking_thenReturnPathWorkingDirectory() {
        final String segmentPath = "../../resources";
        Path result = FileHelper.getCurrentWorking(segmentPath);
        Assertions.assertThat(result).isNotNull()
                .returns(FileSystems.getDefault().getPath(segmentPath).toString(), Path::toString)
                .satisfies(path -> Assertions.assertThat(path.toFile().exists()).isTrue());
    }

    @Test
    void givenPathNotExits_whenCreateDirectoryIfNotExits_thenReturnTrue() throws IOException {
        final Path pathTemp = Path.of(tempDir.toAbsolutePath().toString(),"/one");
        log.debug("Path temp: {}", pathTemp);
        final boolean result = FileHelper.createWorkingIfNotExits(pathTemp);
        Assertions.assertThat(result)
                .isTrue();
    }
}
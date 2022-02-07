package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DirectoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Directory.class);
        Directory directory1 = new Directory();
        directory1.setId(1L);
        Directory directory2 = new Directory();
        directory2.setId(directory1.getId());
        assertThat(directory1).isEqualTo(directory2);
        directory2.setId(2L);
        assertThat(directory1).isNotEqualTo(directory2);
        directory1.setId(null);
        assertThat(directory1).isNotEqualTo(directory2);
    }
}

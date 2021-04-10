package com.techatpark.gurukulam.sql.service;

import com.techatpark.gurukulam.sql.model.Database;
import com.techatpark.gurukulam.sql.model.Practice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SQLExamServiceTest {
    /**
     * instance used for test cases.
     */
    private static final String EXAM1 = "Exam 1";
    /**
     * Service instance to be tested.
     */
    @Autowired
    private SQLPracticeService sqlExamService;

    @BeforeEach
    void beforeEach() {
        cleanUp();
    }

    @AfterEach
    void afterEach() {
        cleanUp();
    }

    void cleanUp() {
        sqlExamService.delete();
    }


    @Test
    void testCreate() throws IOException {
        Practice examToBeCrated = getExam();
        Practice createdExam = sqlExamService.create(examToBeCrated).get();
        assertEquals(EXAM1, createdExam.getName());
    }


    @Test
    void testUpdate() throws IOException {
        Practice examToBeCrated = getExam();
        Practice exam = sqlExamService.create(examToBeCrated).get();
        exam.setName("Updated Name");
        exam.setDatabase(Database.POSTGRES);
        Integer newExamId = exam.getId();
        exam = sqlExamService.update(newExamId, exam).get();
        assertEquals("Updated Name", exam.getName(), "Updated");
        assertEquals(Database.POSTGRES, exam.getDatabase(), "Updated");
    }

    @Test
    void testRead() throws IOException {
        Practice examToBeCrated = getExam();
        Practice exam = sqlExamService.create(examToBeCrated).get();
        Integer newExamId = exam.getId();
        Assertions.assertNotNull(sqlExamService.read(newExamId).get(), "Exam Created");
    }

    @Test
    void testDelete() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            Practice examToBeCrated = getExam();
            Practice exam = sqlExamService.create(examToBeCrated).get();
            Integer newExamId = exam.getId();
            sqlExamService.delete(newExamId);
            sqlExamService.read(newExamId).get();
        });
    }

    @Test
    void testList() throws IOException {
        Practice examToBeCrated = getExam();
        sqlExamService.create(examToBeCrated).get();
        Practice examToBeCrated2 = getExam();
        sqlExamService.create(examToBeCrated2);
        assertEquals(2,
                sqlExamService.page(PageRequest.of(0, 2)).getContent().size()
                , "Test Listing");
        assertEquals(1, sqlExamService.page(PageRequest.of(0, 1)).getContent().size(), "Test Listing with restricted page");
    }

    Practice getExam() {
        Practice exam = new Practice();
        exam.setName(EXAM1);
        exam.setDatabase(Database.POSTGRES);
        exam.setScript(TestUtil.getScript(exam));
        exam.setDescription("description");
        return exam;
    }


}

package com.techatpark.gurukulam.service;

import com.techatpark.gurukulam.model.Database;
import com.techatpark.gurukulam.model.sql.SqlPractice;
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
    private PracticeService sqlExamService;

    @BeforeEach
    void beforeEach() {
        cleanUp();
    }

    @AfterEach
    void afterEach() {
        cleanUp();
    }

    void cleanUp() {
        sqlExamService.delete("sql");
    }


    @Test
    void testCreate() throws IOException {
        SqlPractice examToBeCrated = getExam();
        SqlPractice createdExam =
                sqlExamService.create("sql", "user", examToBeCrated).get();
        assertEquals(EXAM1, createdExam.getName());
    }


    @Test
    void testUpdate() throws IOException {
        SqlPractice examToBeCrated = getExam();
        SqlPractice exam = sqlExamService.create("sql", "user", examToBeCrated).get();
        exam.setName("Updated Name");
        exam.setDatabase(Database.POSTGRES);
        Integer newExamId = exam.getId();
        exam = sqlExamService.update(newExamId, exam).get();
        assertEquals("Updated Name", exam.getName(), "Updated");
        assertEquals(Database.POSTGRES, exam.getDatabase(), "Updated");
    }

    @Test
    void testRead() throws IOException {
        SqlPractice examToBeCrated = getExam();
        SqlPractice exam = sqlExamService.create("sql", "user", examToBeCrated).get();
        Integer newExamId = exam.getId();
        Assertions.assertNotNull(sqlExamService.read(newExamId).get(),
                "Exam Created");
    }

    @Test
    void testDelete() {
        Assertions.assertThrows(NoSuchElementException.class, () -> {
            SqlPractice examToBeCrated = getExam();
            SqlPractice exam =
                    sqlExamService.create("sql", "user", examToBeCrated).get();
            Integer newExamId = exam.getId();
            sqlExamService.delete(newExamId);
            sqlExamService.read(newExamId).get();
        });
    }

    @Test
    void testList() throws IOException {
        SqlPractice examToBeCrated = getExam();
        sqlExamService.create("sql", "user", examToBeCrated).get();
        SqlPractice examToBeCrated2 = getExam();
        sqlExamService.create("sql", "user", examToBeCrated2);
        assertEquals(2,
                sqlExamService.page("sql", PageRequest.of(0, 2)).getContent()
                        .size()
                , "Test Listing");
        assertEquals(1,
                sqlExamService.page("sql", PageRequest.of(0, 1)).getContent()
                        .size(), "Test Listing with restricted page");
    }

    SqlPractice getExam() {
        SqlPractice exam = new SqlPractice();
        exam.setName(EXAM1);
        exam.setDatabase(Database.POSTGRES);
        exam.setScript(TestUtil.getScript(exam));
        exam.setDescription("description");
        return exam;
    }


}

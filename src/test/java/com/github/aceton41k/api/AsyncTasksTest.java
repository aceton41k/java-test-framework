package com.github.aceton41k.api;

import com.github.aceton41k.client.AuthApiClient;
import com.github.aceton41k.client.PostApiClient;
import com.github.aceton41k.model.Task;
import com.github.aceton41k.model.page.Page;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import jooq.tables.records.TasksRecord;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AsyncTasksTest extends BaseApiTest {

    PostApiClient postApi;
    String email = faker.internet().emailAddress();
    String fullName = faker.name().fullName();
    String password = faker.internet().password(5, 5);

    @BeforeClass
    public void beforeClass() {
        postApi = new PostApiClient();
        AuthApiClient authApi = new AuthApiClient();
        authApi.signUp(email, password, fullName);
        authApi.auth(email, password);
    }

    @Test(description = "Create task")
    public void createTask() throws InterruptedException {
        Response response = postApi.createTask(10);
        assertStatusCreated(response);
        Task task = response.as(Task.class);
        var taskId = task.getId();
        assertEquals(task.getStatus(), "idle");
        await().atMost(25, TimeUnit.SECONDS)
                .pollInterval(10, TimeUnit.SECONDS)
                .until(() -> postApi.getTask(taskId).as(Task.class).getStatus().equals("done"));
        task = postApi.getTask(taskId).as(Task.class);
        assertEquals(task.getProgress(), 100);
        TasksRecord taskFromDb = dbo.getTask(taskId);
        assertEquals(task.getId(), taskFromDb.getId());
        assertEquals(task.getProgress(), taskFromDb.getProgress());
        assertEquals(task.getStatus(), taskFromDb.getStatus());

    }

    @Test(description = "Get task")
    public void getTask() {
        TasksRecord taskFromDb = dbo.insertTask(new Task().withStatus("done").withProgress(100));
        Response response = postApi.getTask(taskFromDb.getId());
        assertStatusCodeOk(response);
        Task task = response.as(Task.class);
        assertEquals(task.getId(), taskFromDb.getId());
        assertEquals(task.getProgress(), taskFromDb.getProgress());
        assertEquals(task.getStatus(), taskFromDb.getStatus());
    }

    @Test
    public void getAllTasks() {
        long id1 = postApi.createTask(1).as(Task.class).getId();
        long id2 = postApi.createTask(1).as(Task.class).getId();
        postApi.createTask(1);
        Response response = postApi.getAllTasks();
        assertStatusCodeOk(response);
        Page<Task> tasks = response.as(new TypeRef<>() {
        });
        assertTrue(tasks.getContent().size() > 2);
        List<Long> ids = tasks.getContent().stream().map(Task::getId).toList();
        assertThat(ids, Matchers.hasItems(id1, id2));    }
}

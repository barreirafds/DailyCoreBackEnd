package be.dailycorebackend;

import be.dailycorebackend.api.dto.CompleteTaskRequest;
import be.dailycorebackend.api.dto.CreateRoutineRequest;
import be.dailycorebackend.api.dto.CreateTaskRequest;
import be.dailycorebackend.api.dto.UpdateRoutineRequest;
import be.dailycorebackend.api.dto.UpdateTaskRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RoutineTaskIntegrationTest extends IntegrationTestSupport {

    @Test
    void fullFlow_registerCreateRoutineCreateTaskAndComplete() throws Exception {
        AuthTokens auth = registerUser("Diego", uniqueEmail(), "secret123");

        CreateRoutineRequest routineRequest = new CreateRoutineRequest();
        routineRequest.setTitle("Morning Routine");
        routineRequest.setDescription("Start the day");

        MvcResult routineResult = mockMvc.perform(authorized(auth.token(), post("/api/routines"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routineRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Morning Routine")))
                .andExpect(jsonPath("$.tasks", hasSize(0)))
                .andReturn();

        long routineId = objectMapper.readTree(routineResult.getResponse().getContentAsString())
                .get("id").asLong();

        CreateTaskRequest taskRequest = new CreateTaskRequest();
        taskRequest.setTitle("Drink water");

        MvcResult taskResult = mockMvc.perform(authorized(auth.token(), post("/api/routines/" + routineId + "/tasks"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Drink water")))
                .andExpect(jsonPath("$.completed", is(false)))
                .andReturn();

        long taskId = objectMapper.readTree(taskResult.getResponse().getContentAsString())
                .get("id").asLong();

        CompleteTaskRequest completeRequest = new CompleteTaskRequest();
        completeRequest.setCompleted(true);

        mockMvc.perform(authorized(auth.token(), patch("/api/tasks/" + taskId + "/complete"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(completeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.completed", is(true)));

        mockMvc.perform(authorized(auth.token(), get("/api/routines/" + routineId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks", hasSize(1)))
                .andExpect(jsonPath("$.tasks[0].completed", is(true)));
    }

    @Test
    void getAllRoutines_returnsOnlyCurrentUserRoutines() throws Exception {
        AuthTokens userA = registerUser("User A", uniqueEmail(), "secret123");
        AuthTokens userB = registerUser("User B", uniqueEmail(), "secret123");

        CreateRoutineRequest routineRequest = new CreateRoutineRequest();
        routineRequest.setTitle("Private Routine");
        routineRequest.setDescription("Only for user A");

        MvcResult created = mockMvc.perform(authorized(userA.token(), post("/api/routines"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routineRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        long routineId = objectMapper.readTree(created.getResponse().getContentAsString())
                .get("id").asLong();

        mockMvc.perform(authorized(userA.token(), get("/api/routines")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Private Routine")));

        mockMvc.perform(authorized(userB.token(), get("/api/routines")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        mockMvc.perform(authorized(userB.token(), get("/api/routines/" + routineId)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Routine not found with id: " + routineId)));
    }

    @Test
    void updateAndDeleteRoutine() throws Exception {
        AuthTokens auth = registerUser("Diego", uniqueEmail(), "secret123");

        CreateRoutineRequest createRequest = new CreateRoutineRequest();
        createRequest.setTitle("Evening Routine");
        createRequest.setDescription("Wind down");

        MvcResult created = mockMvc.perform(authorized(auth.token(), post("/api/routines"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        long routineId = objectMapper.readTree(created.getResponse().getContentAsString())
                .get("id").asLong();

        UpdateRoutineRequest updateRequest = new UpdateRoutineRequest();
        updateRequest.setTitle("Night Routine");
        updateRequest.setDescription("Sleep prep");

        mockMvc.perform(authorized(auth.token(), put("/api/routines/" + routineId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Night Routine")))
                .andExpect(jsonPath("$.description", is("Sleep prep")));

        mockMvc.perform(authorized(auth.token(), delete("/api/routines/" + routineId)))
                .andExpect(status().isNoContent());

        mockMvc.perform(authorized(auth.token(), get("/api/routines/" + routineId)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateAndDeleteTask() throws Exception {
        AuthTokens auth = registerUser("Diego", uniqueEmail(), "secret123");

        CreateRoutineRequest routineRequest = new CreateRoutineRequest();
        routineRequest.setTitle("Workout");
        routineRequest.setDescription("Gym");

        MvcResult routineResult = mockMvc.perform(authorized(auth.token(), post("/api/routines"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routineRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        long routineId = objectMapper.readTree(routineResult.getResponse().getContentAsString())
                .get("id").asLong();

        CreateTaskRequest taskRequest = new CreateTaskRequest();
        taskRequest.setTitle("Stretch");

        MvcResult taskResult = mockMvc.perform(authorized(auth.token(), post("/api/routines/" + routineId + "/tasks"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        long taskId = objectMapper.readTree(taskResult.getResponse().getContentAsString())
                .get("id").asLong();

        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setTitle("Stretch for 10 minutes");
        updateRequest.setCompleted(true);

        mockMvc.perform(authorized(auth.token(), put("/api/tasks/" + taskId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Stretch for 10 minutes")))
                .andExpect(jsonPath("$.completed", is(true)));

        mockMvc.perform(authorized(auth.token(), delete("/api/tasks/" + taskId)))
                .andExpect(status().isNoContent());

        mockMvc.perform(authorized(auth.token(), get("/api/routines/" + routineId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks", hasSize(0)));
    }

    @Test
    void protectedEndpoints_withoutToken_return401() throws Exception {
        mockMvc.perform(get("/api/routines"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/routines")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"title": "Test"}
                                """))
                .andExpect(status().isUnauthorized());
    }
}

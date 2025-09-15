package com.example.exercise.demo.service;

import com.example.exercise.demo.dto.*;
import com.example.exercise.demo.dto.respMsgs.ResponseMsg;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TaskService {

    ViewAllTaskResp viewTasksByProjId (Integer projId, String tagIdListParam, String status,
                                       String sortBy, String order, Integer userId);

    TaskDetailResp viewTaskDetails(Integer projectId, Integer taskId, Integer id);

    /** New task with task name, creator, status, description and deadline */
    ResponseMsg addTask (Integer projectId, AddNewTaskReq req, Integer userId);

    ResponseMsg updateTask(Integer projectId, Integer taskId, EditTaskReq req, Integer id);

    ResponseMsg deleteTask(Integer projectId, Integer taskId, Integer id);

    ResponseMsg assignTaskMember(Integer projectId, Integer taskId, List<AssignTaskMemberReq> req, Integer id);

    ResponseMsg removeUserFromTask(Integer projectId, Integer taskId, Integer userId, Integer id);

    ResponseMsg addCommentToTask(Integer projectId, Integer taskId, CommentContentReq req, Integer id);

    ResponseMsg updateCommentInTask(Integer projectId, Integer taskId, Integer commentId, CommentContentReq req, Integer id);

    ResponseMsg deleteCommentFromTask(Integer projectId, Integer taskId, Integer commentId, Integer id);

    ResponseMsg addTagToTask(Integer projectId, Integer taskId, @Valid AddTaskTagsReq req, Integer id);

    ResponseMsg deleteTagFromTask(Integer projectId, Integer taskId, Integer tagId, Integer id);
}

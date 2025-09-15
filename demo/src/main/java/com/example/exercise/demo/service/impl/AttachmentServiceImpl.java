package com.example.exercise.demo.service.impl;

import com.example.exercise.demo.dto.respMsgs.ResponseMsg;
import com.example.exercise.demo.entity.ProjectMembersEntity;
import com.example.exercise.demo.entity.TaskAttachmentsEntity;
import com.example.exercise.demo.entity.TasksEntity;
import com.example.exercise.demo.enums.ProjectTaskEnums;
import com.example.exercise.demo.enums.ResponseStatusEnums;
import com.example.exercise.demo.enums.UserRolesEnums;
import com.example.exercise.demo.exception.DataNotFoundException;
import com.example.exercise.demo.exception.DeleteFailException;
import com.example.exercise.demo.exception.InsertionFailException;
import com.example.exercise.demo.repository.*;
import com.example.exercise.demo.service.AttachmentService;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Objects;


@Service
public class AttachmentServiceImpl implements AttachmentService {

//    /** File upload path */
//    @Value("${file.upload-dir}")
//    private String uploadDir;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private ProjectMemberRepo projectMemberRepo;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private TaskAttachmentsRepo taskAttachmentRepo;

    @Autowired
    private TaskUserRepo taskUserRepo;

    @Autowired
    private UserRepository userRepo;


    @Override
    public ResponseEntity<ByteArrayResource> getAttachmentById(Integer projectId, Integer taskId,
                                                               Integer attachId, Integer userId) {

        // Project user (member) can view the task and its details
        if ((!projectMemberRepo.existsByProjectIdAndUserId(projectId, userId)
                || !taskRepo.existsByProjectIdAndId(projectId, taskId))
                && !StringUtils.equals(userRepo.getReferenceById(userId).getRole(), UserRolesEnums.ADMIN.getRole())) {
            throw new DataNotFoundException("Task not found or you are not a member");
        }

        TaskAttachmentsEntity attachment = taskAttachmentRepo.findByIdAndTaskId(attachId, taskId)
                .orElseThrow(() -> new DataNotFoundException("Attachment not found"));
        // System.err.println(attachment);

        String encodedName = URLEncoder.encode(attachment.getOrigFileName().substring(0,
                        attachment.getOrigFileName().lastIndexOf(".")),
                StandardCharsets.UTF_8).replaceAll("\\+", "%20");

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename*=UTF-8''" + encodedName);
        header.add("Access-Control-Expose-Headers", "Content-Disposition");

        byte[] bytes = attachment.getContent();
        if (ObjectUtils.isEmpty(bytes)) {
            throw new DataNotFoundException("Attachment content is empty");
        }
        ByteArrayResource resource = new ByteArrayResource(bytes);
        String contentType = ObjectUtils.defaultIfNull(
                attachment.getContentType(),
                MediaType.APPLICATION_OCTET_STREAM_VALUE
        );

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(bytes.length)
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    @Override
    @Transactional
    public ResponseMsg addAttachmentToTask(Integer projectId, Integer taskId, MultipartFile file, Integer userId) {

        TasksEntity task = taskRepo.findByProjectIdAndId(projectId, taskId)
                .orElseThrow(() -> new DataNotFoundException("Task not found"));

        if (ObjectUtils.isEmpty(file)) {
            throw new InsertionFailException("File cannot be empty");
        }

        if (!checkTaskCanEdit(projectId, taskId, userId)) {
            throw new InsertionFailException("You cannot add attachment to this task");
        }

        if (StringUtils.equals(task.getProject().getStatus().getName(), ProjectTaskEnums.COMPLETED.getStatusName())
                || StringUtils.equals(task.getStatus().getName(), ProjectTaskEnums.COMPLETED.getStatusName())){
            throw new InsertionFailException("Cannot add attachment to a completed project or task");
        }

        // 安全檢查 & 產生儲存檔名
        String originalName = org.springframework.util.StringUtils
                .cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        if (originalName.contains("..")) {
            throw new InsertionFailException("Invalid filename");
        }

        if (originalName.lastIndexOf(".") < 0) {
            throw new InsertionFailException("File must have an extension");
        }

        TaskAttachmentsEntity attachment = new TaskAttachmentsEntity();
        attachment.setTask(task);
        attachment.setCreator(userRepo.getReferenceById(userId));
        attachment.setOrigFileName(originalName); // shown to user on download

        try {
            byte[] bytes = file.getBytes();
            attachment.setContent(bytes);
            attachment.setFileSize((long) bytes.length);
        } catch (IOException e) {
            throw new InsertionFailException("Failed to read file bytes: " + e.getMessage());
        }

        attachment.setContentType(file.getContentType());
        attachment.setIsDeleted(false);

        taskAttachmentRepo.save(attachment);
        task.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return createSuccessResponse("Attachment uploaded successfully");
    }

    @Override
    @Transactional
    public ResponseMsg deleteAttachmentFromTask(Integer projectId, Integer taskId,
                                                Integer attachmentId, Integer userId) {
        TaskAttachmentsEntity attachment = taskAttachmentRepo.findByIdAndTaskId(attachmentId, taskId)
                .orElseThrow(() -> new DataNotFoundException("Attachment not found"));

        if (!checkTaskCanEdit(projectId, taskId, userId)) {
            throw new DeleteFailException("You cannot delete attachment from this task");
        }

        taskAttachmentRepo.delete(attachment);
        attachment.getTask().setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        return createSuccessResponse("Attachment deleted successfully");
    }


    /** Check if user can edit the task in a project */
    private Boolean checkTaskCanEdit(Integer projectId, Integer taskId, Integer userId) {

        // project owner = true
        ProjectMembersEntity projectMember = projectMemberRepo.
                findByUserIdAndProjectId(userId, projectId).orElse(null);

        if (ObjectUtils.isEmpty(projectMember)) {
            return false;
        }

        if (StringUtils.equals(projectMember.getProjectRole(), UserRolesEnums.PROJECT_OWNER.getRole())){
            return true;
        }

        // task owner (task user) = true
        return taskUserRepo.existsByUserIdAndTaskId(userId, taskId);

    }

    private ResponseMsg createSuccessResponse(String message) {
        ResponseMsg resp = new ResponseMsg();
        resp.setStatus(ResponseStatusEnums.SUCCESS.getStatus());
        resp.setMessage(message);
        return resp;
    }
}

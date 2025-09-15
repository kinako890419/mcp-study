package com.example.exercise.demo.service;

import com.example.exercise.demo.dto.respMsgs.ResponseMsg;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AttachmentService {

    ResponseEntity<ByteArrayResource> getAttachmentById(Integer projectId, Integer taskId,
                                                        Integer attachId, Integer userId);

    ResponseMsg addAttachmentToTask(Integer projectId, Integer taskId,
                                    MultipartFile file, Integer userId);

    ResponseMsg deleteAttachmentFromTask(Integer projectId, Integer taskId,
                                        Integer attachId, Integer userId);

}

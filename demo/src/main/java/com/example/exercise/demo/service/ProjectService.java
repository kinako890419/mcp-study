package com.example.exercise.demo.service;

import com.example.exercise.demo.dto.*;
import com.example.exercise.demo.dto.respMsgs.ResponseMsg;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectService {

    /** View personal projects or ADMIN view all projects */
    List<ProjDetailsResp> viewAllProjects(String sortBy, String order,
                                          Integer page, Integer pageSize,
                                          String statusName, Integer userId, String userRole);

    ProjDetailsResp getProjByProjectId(Integer projectId, Integer userId, String userRole);

    /** Create a new project (pending) */
    ResponseMsg createProject(CreateProjectReq req, Integer userId);

    /** invite user (existed proj) and set role in the project */
    ResponseMsg assignProjectMember(Integer projId, List<SetProjectMemberRoleRq> req, Integer userId);

    ResponseMsg editProject(Integer projId, EditProjectReq req, Integer userId);

    ResponseMsg exitProject(Integer projId, Integer userId, Integer id);

    ResponseMsg setProjectUserRole(Integer projId, SetProjectMemberRoleRq req, Integer id);

    ResponseMsg deleteProject(Integer projId, Integer userId);

    ProjectTagsListResp getProjectTags(Integer projId, Integer userId);

    ResponseMsg addProjectTag(Integer id, @Valid ProjTagContentReq req, Integer userId);

    ResponseMsg editProjectTag(Integer id, Integer tagId, @Valid ProjTagContentReq req, Integer userId);

    ResponseMsg deleteProjectTag(Integer id, Integer tagId, Integer userId);

}

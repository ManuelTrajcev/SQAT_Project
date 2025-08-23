package mk.ukim.finki.wp.workspaces.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mk.ukim.finki.wp.workspaces.dto.DisplayWorkspaceDto;
import mk.ukim.finki.wp.workspaces.dto.EditWorkspaceDto;
import mk.ukim.finki.wp.workspaces.dto.WorkspaceWithRoleDto;
import mk.ukim.finki.wp.workspaces.model.domain.User;
import mk.ukim.finki.wp.workspaces.model.exceptions.AccessDeniedException;
import mk.ukim.finki.wp.workspaces.service.application.WorkspaceApplicationService;
import mk.ukim.finki.wp.workspaces.service.application.impl.WorkspaceApplicationServiceImpl;
import mk.ukim.finki.wp.workspaces.service.domain.WorkspaceService;
import org.springdoc.api.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspace")
@Tag(name = "Workspace API", description = "Endpoints for accessing workspaces")
public class WorkspaceController {
    private final WorkspaceApplicationService workspaceApplicationService;

    public WorkspaceController(WorkspaceApplicationService workspaceApplicationService) {
        this.workspaceApplicationService = workspaceApplicationService;
    }

    @Operation(summary = "Get all workspaces", description = "Retrieves a list of all workspace.")
    @GetMapping
    public List<DisplayWorkspaceDto> findAll() {
        return workspaceApplicationService.findAll();
    }

    @Operation(summary = "Get my workspaces", description = "Retrieves a list of all of my workspaces.")
    @GetMapping("/my-workspaces")
    public List<WorkspaceWithRoleDto> findMyWorkspaces() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();

        return workspaceApplicationService.findAllPerUser(loggedInUser.getId());
    }

    @Operation(summary = "Access a workspaces", description = "Access one workspace.")
    @GetMapping("/{id}")
    public ResponseEntity<DisplayWorkspaceDto> accessWorkspace(@PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();

        return workspaceApplicationService.openWorkspace(id, loggedInUser.getId())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @Operation(summary = "Edit a workspaces", description = "Edit one workspace.")
    @PostMapping("/edit/{id}")
    public ResponseEntity<EditWorkspaceDto> editWorkspace(@PathVariable Long id,  @RequestBody EditWorkspaceDto editWorkspaceDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = (User) authentication.getPrincipal();

        return workspaceApplicationService.editWorkspace(id, loggedInUser.getId(), editWorkspaceDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }
}

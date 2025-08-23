package mk.ukim.finki.wp.workspaces.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.workspaces.model.domain.Workspace;
import mk.ukim.finki.wp.workspaces.model.exceptions.AccessDeniedException;
import mk.ukim.finki.wp.workspaces.security.JwtConstants;
import mk.ukim.finki.wp.workspaces.security.JwtHelper;
import mk.ukim.finki.wp.workspaces.service.domain.WorkspaceService;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WorkspaceAccessFilter extends OncePerRequestFilter {
    private final JwtHelper jwtHelper;
    private final WorkspaceService workspaceService;

    public WorkspaceAccessFilter(JwtHelper jwtHelper, WorkspaceService workspaceService) {
        this.jwtHelper = jwtHelper;
        this.workspaceService = workspaceService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return !(path.matches("/api/workspace/\\d+") || path.matches("/api/workspace/edit/\\d+"));
    }


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();


        if (path.matches("/api/workspace/\\d+")) {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
                String token = authHeader.substring(JwtConstants.TOKEN_PREFIX.length());
                Map<Long, String> claims = jwtHelper.extractWorkspaceAccess(token);

                Pattern pattern = Pattern.compile("/api/workspace/(\\d+)");
                Matcher matcher = pattern.matcher(path);

                if (matcher.find()) {
                    Long workspaceId = Long.parseLong(matcher.group(1));
                    Optional<Workspace> workspace = workspaceService.findById(workspaceId);

                    if (workspace.isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("Workspace does not exists ");
                        return;
                    }

                    if (!claims.containsKey(workspaceId)) {
//                        throw new AccessDeniedException("Access denied: you don't have permission for the workspace " + workspace.get().getName(), HttpServletResponse.SC_FORBIDDEN);
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("Access denied: you don't have permission for the workspace " + workspace.get().getName());
                        return;
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Missing or invalid Authorization header");
                return;
            }
        } else if (path.matches("/api/workspace/edit/\\d+")) {
            String authHeader = request.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith(JwtConstants.TOKEN_PREFIX)) {
                String token = authHeader.substring(JwtConstants.TOKEN_PREFIX.length());
                Map<Long, String> claims = jwtHelper.extractWorkspaceAccess(token);

                Pattern pattern = Pattern.compile("/api/workspace/edit/(\\d+)");
                Matcher matcher = pattern.matcher(path);

                if (matcher.find()) {
                    Long workspaceId = Long.parseLong(matcher.group(1));
                    Optional<Workspace> workspace = workspaceService.findById(workspaceId);

                    if (workspace.isEmpty()) {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        response.getWriter().write("Workspace does not exists ");
                        return;
                    }

                    if (!claims.containsKey(workspaceId)) {
                        //TODO throw exception with custom message to be catched in the GlobalExceptionHandler
//                        throw new AccessDeniedException("Access denied: you don't have permission for the workspace " + workspace.get().getName(), HttpServletResponse.SC_FORBIDDEN);
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("Access denied: you don't have permission for the workspace " + workspace.get().getName());
                        return;
                    }

                    String role = claims.get(workspaceId);
                    if (role == null || !role.equalsIgnoreCase("ROLE_ADMIN")) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Access denied: you must be an ADMIN for workspace " + workspace.get().getName() + " to edit it");
                        return;
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Missing or invalid Authorization header");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

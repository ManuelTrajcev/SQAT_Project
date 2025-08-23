package mk.ukim.finki.wp.workspaces.model.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.workspaces.model.context.WorkspaceContext;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@Component
public class WorkspaceContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String workspaceIdHeader = request.getHeader("X-Workspace-Id");

            if (workspaceIdHeader != null) {
                Long workspaceId = Long.parseLong(workspaceIdHeader);
                WorkspaceContext.setCurrentWorkspaceId(workspaceId);
            }

            filterChain.doFilter(request, response);
        } finally {
            WorkspaceContext.clear();
        }
    }
}

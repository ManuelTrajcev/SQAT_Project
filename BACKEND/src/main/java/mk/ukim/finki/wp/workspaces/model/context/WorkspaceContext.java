package mk.ukim.finki.wp.workspaces.model.context;

public class WorkspaceContext {

    private static final ThreadLocal<Long> currentWorkspaceId = new ThreadLocal<>();

    public static void setCurrentWorkspaceId(Long id) {
        currentWorkspaceId.set(id);
    }

    public static Long getCurrentWorkspaceId() {
        return currentWorkspaceId.get();
    }

    public static void clear() {
        currentWorkspaceId.remove();
    }
}

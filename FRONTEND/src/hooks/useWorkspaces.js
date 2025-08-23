import {useCallback, useEffect, useState} from "react";
import workspaceRepository from "../repository/workspaceRepository.js";

const initialState = {
    allWorkspaces: [],
    myWorkspaces: [],
    loading: true,
};

const useWorkspaces = () => {
    const [state, setState] = useState(initialState);

    const fetchAllWorkspaces = useCallback(() => {
        setState((prev) => ({...prev, loading: true}));
        workspaceRepository
            .findAll()
            .then((response) => {
                setState((prev) => ({
                    ...prev,
                    allWorkspaces: response.data,
                    loading: false,
                }));
            })
            .catch((error) => {
                console.error("Error fetching all workspaces:", error);
                setState((prev) => ({...prev, loading: false}));
            });
    }, []);

    const fetchMyWorkspaces = useCallback(() => {
        setState((prev) => ({...prev, loading: true}));
        workspaceRepository
            .findMine()
            .then((response) => {
                setState((prev) => ({
                    ...prev,
                    myWorkspaces: response.data,
                    loading: false,
                }));
            })
            .catch((error) => {
                console.error("Error fetching my workspaces:", error);
                setState((prev) => ({...prev, loading: false}));
            });
    }, []);

    const onAccess = useCallback(async (id) => {
        try {
            const response = await workspaceRepository.accessWorkspace(id);
            return response.data;
        } catch (error) {
            console.error(`Error accessing workspace with ID ${id}:`, error);
            throw error;
        }
    }, []);

    const onEdit = useCallback(async (id, data) => {
        console.log(data)
        try {
            const response = await workspaceRepository.editWorkspace(id, data);
            fetchMyWorkspaces();
            return response.data;
        } catch (error) {
            console.error(`Error editing workspace with ID ${id}:`, error);
            throw error;
        }
    }, [fetchMyWorkspaces]);

    useEffect(() => {
        fetchAllWorkspaces();
        fetchMyWorkspaces();
    }, [fetchAllWorkspaces, fetchMyWorkspaces]);

    return {
        ...state,
        fetchAll: fetchAllWorkspaces,
        fetchMine: fetchMyWorkspaces,
        onAccess: onAccess,
        onEdit: onEdit,
    };
};

export default useWorkspaces;

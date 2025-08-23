import React, {useState} from 'react';
import {
    Box,
    Button,
    CircularProgress,
    Tabs,
    Tab,
    Typography,
} from "@mui/material";
import useWorkspaces from "../../../hooks/useWorkspaces.js";
import WorkspacesGrid from "../../components/workspaces/WorkspaceGrid/WorkspacesGrid.jsx";

const ProductsPage = () => {
    const {allWorkspaces, myWorkspaces, loading, onAccess, onEdit} = useWorkspaces();
    const [selectedTab, setSelectedTab] = useState(0); // 0 = All, 1 = Mine

    const handleTabChange = (event, newValue) => {
        setSelectedTab(newValue);
    };

    const workspacesToShow = selectedTab === 0 ? allWorkspaces : myWorkspaces;

    return (
        <Box className="products-box">
            <Tabs value={selectedTab} onChange={handleTabChange} sx={{ mb: 2 }}>
                <Tab label="All Workspaces" />
                <Tab label="My Workspaces" />
            </Tabs>

            {loading ? (
                <Box className="progress-box">
                    <CircularProgress />
                </Box>
            ) : (
                <WorkspacesGrid workspaces={workspacesToShow} onEdit={onEdit} onAccess={onAccess} />
            )}
        </Box>
    );
};

export default ProductsPage;

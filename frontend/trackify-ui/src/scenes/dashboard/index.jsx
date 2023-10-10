import { Box } from "@mui/material";
import Header from "../../components/Header";
import { useEffect } from "react";
import { get } from "../../utils/axiosUtils";

const Dashboard = () => {
  useEffect(() => {
    get("user/organization/test-user")
      .then((response) => {
        console.log(response);
      })
      .catch((error) => {
        console.log(error);
      });
  });
  return (
    <Box m="20px">
      <Box display="flex" justifyContent="space-between" alignItems="center">
        <Header title="DASHBOARD" subtitle="Welcome to your Dashboard" />
      </Box>
    </Box>
  );
};
export default Dashboard;

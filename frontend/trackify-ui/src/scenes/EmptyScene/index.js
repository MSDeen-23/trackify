import { Box } from "@mui/material";
import "react-toastify/dist/ReactToastify.css";
import Cookies from "js-cookie";
import { useEffect } from "react";
import { get } from "../../utils/axiosUtils";
import { useDispatch } from "react-redux";
import { addUser, changeLoginState } from "../../features/login/loginSlice";
import { useNavigate } from "react-router-dom";

const EmptyScene = (props) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  useEffect(() => {
    const token = Cookies.get("authToken");
    if (token != null) {
      get("/user/auth/init-validate-token")
        .then((response) => {
          dispatch(changeLoginState(true));
          dispatch(addUser(response));
        })
        .catch((error) => {
          if (error?.response?.status === 451) {
            navigate("/verify");
          }
          dispatch(changeLoginState(false));
          navigate("/login");
        });
    } else {
    }
  });

  return <Box></Box>;
};

export default EmptyScene;

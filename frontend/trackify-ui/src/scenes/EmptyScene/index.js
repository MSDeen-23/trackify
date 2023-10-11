import { Box } from "@mui/material";
import "react-toastify/dist/ReactToastify.css";
import Cookies from "js-cookie";
import { useEffect } from "react";
import { get } from "../../utils/axiosUtils";
import { useDispatch } from "react-redux";
import { addUser, changeLoginState } from "../../features/login/loginSlice";

const EmptyScene = (props) => {
  const dispatch = useDispatch();

  useEffect(() => {
    const token = Cookies.get("authToken");
    if (token != null) {
      get("/user/auth/init-validate-token")
        .then((response) => {
          dispatch(changeLoginState(true));
          dispatch(addUser(response));
        })
        .catch((error) => {
          dispatch(changeLoginState(false));
          window.location.reload();
        });
    } else {
    }
  });

  return <Box></Box>;
};

export default EmptyScene;

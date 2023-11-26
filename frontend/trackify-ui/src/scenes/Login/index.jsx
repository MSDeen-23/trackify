import {
  Box,
  TextField,
  Button,
  useTheme,
  Typography,
  IconButton,
} from "@mui/material";
import { Formik } from "formik";
import * as yup from "yup";
import { tokens } from "../../theme";
import { alpha } from "@mui/material/styles";
import InputAdornment from "@mui/material/InputAdornment";
import AccountCircleOutlinedIcon from "@mui/icons-material/AccountCircleOutlined";
import KeyOutlined from "@mui/icons-material/KeyOutlined";
import { post } from "../../utils/axiosUtils";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Cookies from "js-cookie";
import VisibilityOutlinedIcon from "@mui/icons-material/VisibilityOutlined";
import VisibilityOffOutlinedIcon from "@mui/icons-material/VisibilityOffOutlined";
import { useState } from "react";
import { Link } from "react-router-dom";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { get } from "../../utils/axiosUtils";
import { useDispatch } from "react-redux";
import { addUser, changeLoginState } from "../../features/login/loginSlice";
const Login = (props) => {
  // setting themes
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);

  // setting initial values
  const initialValuesForLogin = {
    email: "",
    password: "",
  };

  const dispatch = useDispatch();
  // defining validations
  const userSchema = yup.object().shape({
    email: yup
      .string()
      .email("Email is not valid")
      .required("Email is required"),
    password: yup.string().required("Password is required"),
  });


  const [showPassword, setShowPassword] = useState(false);
  const navigate = useNavigate();
  // init call
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
        });
    } else {

    }
  }, [dispatch, navigate]);

  const handleLogin = (values) => {
    post("user/auth/login", values)
      .then((response) => {
        Cookies.set("authToken", response.jwtToken, {
          secure: true,
          sameSite: "strict",
          expires: 7,
        });
        get("/user/auth/init-validate-token")
          .then((response) => {
            toast.success("Welcome back " + response.firstName, { theme: "dark" });
            dispatch(changeLoginState(true));
            dispatch(addUser(response));
            navigate("/home");
          })
          .catch((error) => {
            if (error?.response?.status === 451) {
              navigate("/verify");
            }
          });
      })
      .catch((error) => {
        console.log(error?.response);
        if (error?.response?.status === 451) {
          navigate("/verify?email=" + values.email);
        }
        toast.error(
          error?.response?.data?.message
            ? error.response.data.message
            : "An error occurred",
          { theme: "dark" }
        );
      });
  };


  return (

    <Box>
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="100vh"
      >
        <Formik
          onSubmit={handleLogin}
          initialValues={initialValuesForLogin}
          validationSchema={userSchema}
        >
          {({
            values,
            errors,
            touched,
            handleBlur,
            handleChange,
            handleSubmit,
          }) => (
            <form onSubmit={handleSubmit}>
              <Box
                backgroundColor={alpha(colors.grey[700], 0.3)}
                width="500px"
                display="flex"
                justifyContent="center"
                alignItems="center"
                borderRadius="4px"
                sx={{ boxShadow: 4 }}
              >
                <Box
                  display="flex"
                  flexDirection="column"
                  width="370px"
                  margin="25px 0"
                  gap="20px"
                >
                  <Typography variant="h2">Welcome back!</Typography>
                  {/* Email */}
                  <TextField
                    variant="outlined"
                    type="text"
                    label="Email"
                    onBlur={handleBlur}
                    onChange={handleChange}
                    value={values.email}
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">
                          <AccountCircleOutlinedIcon />
                        </InputAdornment>
                      ),
                    }}
                    name="email"
                    error={!!touched.email && !!errors.email}
                    helperText={touched.email && errors.email}
                  ></TextField>
                  {/* Password */}
                  <TextField
                    variant="outlined"
                    type={showPassword ? "text" : "password"}
                    label="Password"
                    onBlur={handleBlur}
                    onChange={handleChange}
                    value={values.password}
                    name="password"
                    error={!!touched.password && !!errors.password}
                    helperText={touched.password && errors.password}
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">
                          <KeyOutlined />
                        </InputAdornment>
                      ),
                      endAdornment: (
                        <InputAdornment position="end">
                          <IconButton
                            onClick={() => setShowPassword(!showPassword)}
                            onMouseDown={() => setShowPassword(!showPassword)}
                          >
                            {showPassword ? (
                              <VisibilityOutlinedIcon />
                            ) : (
                              <VisibilityOffOutlinedIcon />
                            )}
                          </IconButton>
                        </InputAdornment>
                      ),
                    }}
                  ></TextField>


                  <Box display="flex" justifyContent="end">
                    <Button type="submit" color="secondary" variant="contained" fullWidth>
                      Login
                    </Button>
                  </Box>

                  <Box display="flex">
                    <Button variant="text" fullWidth >
                      <Link style={{ color: `${colors.grey[600]}` }} to="/reset-password">Reset password</Link>
                    </Button>
                    <Button variant="text" fullWidth >
                      <Link to="/register" style={{ color: `${colors.grey[600]}` }}>Don't have an account?</Link>
                    </Button>
                  </Box>



                </Box>

              </Box>

            </form>
          )}

        </Formik>

      </Box>

    </Box >


  );
};

export default Login;

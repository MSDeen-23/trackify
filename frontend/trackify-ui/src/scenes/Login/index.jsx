import { Box, TextField, Button, useTheme, Typography } from "@mui/material";
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

const Login = (props) => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);
  const initialValuesForLogin = {
    email: "",
    password: "",
  };
  const userSchema = yup.object().shape({
    email: yup
      .string()
      .email("Email is not valid")
      .required("Email is required"),
    password: yup.string().required("Password is required"),
  });

  const handleLogin = (values) => {
    // props.handleLogin();
    post("user/auth/login", values)
      .then((response) => {
        Cookies.set("authToken", response.token, {
          secure: true,
          sameSite: "strict",
          expires: 7,
        });
        toast.success("Welcome back", { theme: "dark" });
        props.handleLogin();
      })
      .catch((error) => {
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
                  width="300px"
                  margin="25px 0"
                  gap="30px"
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
                    type="password"
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
                    }}
                  ></TextField>
                  <Box display="flex" justifyContent="end" mt="20px">
                    <Button type="submit" color="secondary" variant="contained">
                      Login
                    </Button>
                  </Box>
                </Box>
              </Box>
            </form>
          )}
        </Formik>
      </Box>
    </Box>
  );
};

export default Login;

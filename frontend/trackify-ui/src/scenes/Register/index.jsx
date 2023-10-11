import { Box, TextField, Button, useTheme, Typography, InputAdornment, IconButton } from "@mui/material";
import { Formik } from "formik";
import { tokens } from "../../theme";
import { post } from "../../utils/axiosUtils";
import Cookies from "js-cookie";
import { toast } from "react-toastify";
import CorporateFareOutlinedIcon from '@mui/icons-material/CorporateFareOutlined';
import AbcOutlinedIcon from '@mui/icons-material/AbcOutlined';
import AlternateEmailOutlinedIcon from '@mui/icons-material/AlternateEmailOutlined';
import VisibilityOutlinedIcon from "@mui/icons-material/VisibilityOutlined";
import VisibilityOffOutlinedIcon from "@mui/icons-material/VisibilityOffOutlined";
import { useState } from "react";
import * as yup from "yup";
import KeyOutlined from "@mui/icons-material/KeyOutlined";
import { alpha } from "@mui/material";
import { useDispatch } from "react-redux";
import { addUser, changeLoginState } from "../../features/login/loginSlice";
import { useNavigate } from "react-router-dom";

const Register = (props) => {
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);

  // initial values
  const initialValuesForRegister = {
    first_name: "",
    last_name: "",
    email: "",
    password: "",
    organization_name: "",
  };

  const [showPassword, setShowPassword] = useState(false);

  // yup validations 
  const userSchema = yup.object().shape({
    email: yup
      .string()
      .email("Email is not valid")
      .required("Email is required"),
    first_name: yup.string().required("First name is required"),
    last_name: yup.string().required("Last name is required"),
    organization_name: yup.string().required("Organization name is required"),
    password: yup.string().required("Password is required")
  })
  const dispatch = useDispatch();
  const navigate = useNavigate();
  // register the new admin user
  const handleRegister = (values) => {
    post("user/auth/register-admin", values)
      .then((response) => {
        Cookies.set("authToken", response.jwtToken, {
          secure: true,
          sameSite: "strict",
          expires: 7
        });
        toast.success("We are all set! " + response.firstName, { theme: "dark" });
        dispatch(changeLoginState(true));
        dispatch(addUser(response));
        navigate("/home");


      })
      .catch((error) => {
        toast.error(
          error?.response?.data?.message
            ? error.response.data.message
            : "An error occurred",
          { theme: "dark" }
        );
      })
  }

  return (
    <Box>
      {/*  Register box */}
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="100vh"
      >
        <Formik
          onSubmit={handleRegister}
          initialValues={initialValuesForRegister}
          validationSchema={userSchema}
        >
          {({ values,
            errors,
            touched,
            handleBlur,
            handleChange,
            handleSubmit
          }) => (

            <form onSubmit={handleSubmit}>
              <Box
                backgroundColor={alpha(colors.grey[700], 0.3)}
                width="800px"
                display="flex"
                justifyContent="center"
                alignItems="center"
                borderRadius="4px"
                sx={{ boxShadow: 4 }}
              >
                <Box
                  display="flex"
                  flexDirection="column"
                  width="700px"
                  margin="25px 0"
                  gap="30px"
                >
                  <Typography variant="h2">Register your organization</Typography>


                  {/* Organization name */}
                  <TextField
                    variant="outlined"
                    type="text"
                    label="Organization name"
                    onBlur={handleBlur}
                    onChange={handleChange}
                    value={values.organization_name}
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">
                          <CorporateFareOutlinedIcon />
                        </InputAdornment>
                      )
                    }}
                    name="organization_name"
                    error={!!touched.organization_name && !!errors.organization_name}
                    helperText={touched.organization_name && errors.organization_name}
                  ></TextField>

                  <Box display="grid" gridTemplateColumns="1fr 1fr" gap="16px">

                    {/* First name */}
                    <TextField
                      variant="outlined"
                      type="text"
                      label="First name"
                      onBlur={handleBlur}
                      onChange={handleChange}
                      value={values.first_name}
                      InputProps={{
                        startAdornment: (
                          <InputAdornment position="start">
                            <AbcOutlinedIcon />
                          </InputAdornment>
                        )
                      }}
                      name="first_name"
                      error={!!touched.first_name && !!errors.first_name}
                      helperText={touched.first_name && errors.first_name}
                    ></TextField>


                    {/* Last name */}
                    <TextField
                      variant="outlined"
                      type="text"
                      label="Last name"
                      onBlur={handleBlur}
                      onChange={handleChange}
                      value={values.last_name}
                      InputProps={{
                        startAdornment: (
                          <InputAdornment position="start">
                            <AbcOutlinedIcon />
                          </InputAdornment>
                        )
                      }}
                      name="last_name"
                      error={!!touched.last_name && !!errors.last_name}
                      helperText={touched.last_name && errors.last_name}
                    ></TextField>
                  </Box>

                  {/* Email */}
                  <TextField
                    variant="outlined"
                    type="text"
                    label="Email address"
                    onBlur={handleBlur}
                    onChange={handleChange}
                    value={values.email}
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">
                          <AlternateEmailOutlinedIcon />
                        </InputAdornment>
                      )
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
                  <Box display="flex" justifyContent="end" mt="20px">
                    <Button type="submit" color="secondary" variant="contained">
                      Register
                    </Button>
                  </Box>
                </Box>
              </Box>
            </form>
          )}

        </Formik>
      </Box>
    </Box>
  )

};


export default Register;
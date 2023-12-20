import {
  Box,
  TextField,
  Button,
  useTheme,
  Typography,
} from "@mui/material";
import { Formik } from "formik";
import * as yup from "yup";
import { tokens } from "../../theme";
import { alpha } from "@mui/material/styles";
import InputAdornment from "@mui/material/InputAdornment";
import { post } from "../../utils/axiosUtils";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Cookies from "js-cookie";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import { useDispatch } from "react-redux";
import { addUser, changeLoginState } from "../../features/login/loginSlice";
import AccountCircleOutlinedIcon from "@mui/icons-material/AccountCircleOutlined";
import { HttpStatusCode } from "axios";
const Verify = (props) => {
  // setting themes
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);

  // setting initial values
  const initialValuesForLogin = {
    otp: "",
  };

  const dispatch = useDispatch();
  // defining validations
  const userSchema = yup.object().shape({
    otp: yup
      .string()
      .required("OTP is required")
      .matches(/^[0-9]+$/, "Must be only digits")
      .min(6, 'Must be exactly 6 digits')
      .max(6, 'Must be exactly 6 digits')
  });


  const navigate = useNavigate();


  const handleLogin = (values) => {

    post("user/auth/verify-otp", values)
      .then((response) => {
        toast.success(response, { theme: "dark" });
        dispatch(changeLoginState(true));
        dispatch(addUser(response));
        navigate("/home");
      })
      .catch((error) => {
        if (error?.response?.status === HttpStatusCode.ExpectationFailed) {
          toast.error(error?.response?.data?.message, { theme: "dark" });
        }
        else {
          toast.error("Error sending OTP. Try after sometime", { theme: "dark" });
        }
      })
  };

  const resendOtp = () => {
    post("user/auth/resend-otp")
      .then((response) => {
        toast.success("Check your mail for the OTP!", { theme: "dark" });
      })
      .catch((error) => {
        toast.error("Error sending OTP. Try after sometime", { theme: "dark" });
      })

  }


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
                  <Typography variant="h2">Verify your email!</Typography>
                  {/* Email */}
                  <TextField
                    variant="outlined"
                    type="text"
                    label="Email OTP"
                    onBlur={handleBlur}
                    onChange={handleChange}
                    value={values.otp}
                    InputProps={{
                      startAdornment: (
                        <InputAdornment position="start">
                          <AccountCircleOutlinedIcon />
                        </InputAdornment>
                      ),
                    }}
                    name="otp"
                    error={!!touched.otp && !!errors.otp}
                    helperText={touched.otp && errors.otp}
                  ></TextField>



                  <Box display="flex" justifyContent="end">
                    <Button type="submit" color="secondary" variant="contained" fullWidth>
                      Verify
                    </Button>
                  </Box>
                  <Box>
                    <Button style={{ color: `${colors.grey[100]}` }} variant="text" fullWidth onClick={resendOtp}>
                      Resend otp
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

export default Verify;

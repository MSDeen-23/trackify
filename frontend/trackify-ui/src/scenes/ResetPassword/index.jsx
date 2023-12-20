import {
  Box,
  TextField,
  Button,
  useTheme,
  Typography,
} from "@mui/material";
import { tokens } from "../../theme";
import { alpha } from "@mui/material/styles";
import InputAdornment from "@mui/material/InputAdornment";
import { post } from "../../utils/axiosUtils";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useNavigate } from "react-router-dom";
import AccountCircleOutlinedIcon from "@mui/icons-material/AccountCircleOutlined";
import { useState } from "react";
import VisibilityOutlinedIcon from "@mui/icons-material/VisibilityOutlined";
import KeyOutlined from "@mui/icons-material/KeyOutlined";
import VisibilityOffOutlinedIcon from "@mui/icons-material/VisibilityOffOutlined";
import IconButton from "@mui/material/IconButton";
import { HttpStatusCode } from "axios";

const ResetPassword = () => {


  const [isOtpSent, setIsOtpSent] = useState(false);
  const [currentState, setCurrentState] = useState("Send OTP");
  const [email, setEmail] = useState("");
  const [verifiedEmail, setVerifiedEmail] = useState("");
  const [otp, setOTP] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);


  // setting themes
  const theme = useTheme();
  const colors = tokens(theme.palette.mode);




  const navigate = useNavigate();


  const resetPassword = () => {
    if (currentState === "Send OTP") {
      let data = {
        email: email
      }
      post("user/auth/reset-password", data)
        .then((response) => {
          toast.success("Check your mail for the OTP!", { theme: "dark" });
          setIsOtpSent(true);
          setCurrentState("Change password");
          setVerifiedEmail(email);
        })
        .catch((error) => {
          toast.error("Error sending OTP. Try after sometime", { theme: "dark" });
        })
    }
    else if (currentState === "Change password") {
      let data = {
        email: verifiedEmail,
        otp: otp,
        password: password
      }
      post("user/auth/set-new-password", data)
        .then((response) => {
          toast.success("Success password changed!", { theme: "dark" });
          navigate("/login")
        })
        .catch((error) => {
          if (error?.response?.status === HttpStatusCode.ExpectationFailed) {
            toast.error(error?.response?.data?.message, { theme: "dark" });
          }
          else {
            toast.error("Error sending OTP. Try after sometime", { theme: "dark" });
          }
        })
    }

  };



  return (

    <Box>
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="100vh"
      >
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
            <Typography variant="h2">Reset password!</Typography>
            <TextField
              variant="outlined"
              type="text"
              label="Email"

              onChange={(e) => setEmail(e.target.value)}
              value={email}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <AccountCircleOutlinedIcon />
                  </InputAdornment>

                ),
                readOnly: isOtpSent ? true : false
              }}
              name="email"
            ></TextField>

            {isOtpSent && (<TextField
              variant="outlined"
              type="text"
              label="Enter OTP"

              onChange={(e) => setOTP(e.target.value)}
              value={otp}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <AccountCircleOutlinedIcon />
                  </InputAdornment>
                ),
              }}
              name="otp"
            ></TextField>)}
            {isOtpSent && (
              <TextField
                variant="outlined"
                type={showPassword ? "text" : "password"}
                label="Password"
                onChange={(e) => setPassword(e.target.value)}
                value={password}
                name="password"

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
            )}
            <Box display="flex" justifyContent="end">
              <Button type="submit" color="secondary" variant="contained" fullWidth onClick={resetPassword}>
                {currentState}
              </Button>
            </Box>
          </Box>
        </Box>

      </Box>

    </Box >


  );
};

export default ResetPassword;

import { ColorModeContext, useMode } from "./theme";
import { CssBaseline, ThemeProvider } from "@mui/material";
import { Routes, Route } from "react-router-dom";
import Topbar from "./scenes/global/Topbar";
import SidebarMenu from "./scenes/global/SidebarMenu";
import Dashboard from "./scenes/dashboard";
import Team from "./scenes/team";
import Invoices from "./scenes/invoices";
import Contacts from "./scenes/contacts";
// import Bar from "./scenes/bar";
import Form from "./scenes/form";
import Login from "./scenes/Login";
import Register from "./scenes/Register";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { useSelector } from "react-redux";
import EmptyScene from "./scenes/EmptyScene";
import Verify from "./scenes/Verify";
import ResetPassword from "./scenes/ResetPassword";
import SockJsClient from "react-stomp";
import { useState } from "react";
import { toast } from "react-toastify";

const SOCKET_URL = process.env.REACT_APP_SOCKET_URL;

function App() {
  const [topics, setTopics] = useState([]);
  const [theme, colorMode] = useMode();

  let onMessageReceived = (msg) => {
    switch (msg.notificationType) {
      case "INFO":
        toast.info(msg.content, { theme: "dark" });
        break;
      case "SUCCESS":
        toast.success(msg.content, { theme: "dark" });
        break;
      case "WARNING":
        toast.warn(msg.content, { theme: "dark" });
        break;
      case "ERROR":
        toast.error(msg.content, { theme: "dark" });
        break;
      default:
        toast.info(msg.content, { theme: "dark" });
        break;
    }
  };

  // const [isLoggedIn, setIsLoggedIn] = useState(false);

  const isLoggedIn = useSelector((state) => state.login.payload);
  const loggedInUser = useSelector((state) => state.user);
  let onConnected = () => {
    setTopics(["/topic/" + loggedInUser.id]);
  };

  return (
    <ColorModeContext.Provider value={colorMode}>
      <ThemeProvider theme={theme}>
        <ToastContainer />
        <CssBaseline />

        {isLoggedIn ? (
          <div className="app">
            <div>
              <SockJsClient
                url={SOCKET_URL}
                topics={topics}
                onConnect={onConnected}
                onMessage={(msg) => onMessageReceived(msg)}
                debug={false}
              />
            </div>
            <SidebarMenu />
            <main className="content">
              <Topbar />

              <Routes>
                <Route path="/" element={<Dashboard />} />
                <Route path="/home" element={<Dashboard />} />
                <Route path="/team" element={<Team />} />
                <Route path="/contacts" element={<Contacts />} />
                <Route path="/invoices" element={<Invoices />} />
                <Route path="/form" element={<Form />} />
              </Routes>
            </main>
          </div>
        ) : (
          <div>
            <Routes>
              <Route path="/" element={<Login />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/verify" element={<Verify />} />
              <Route path="/reset-password" element={<ResetPassword />} />
              <Route path="*" exact={true} element={<Login />} />
            </Routes>
          </div>
        )}
      </ThemeProvider>
    </ColorModeContext.Provider>
  );
}

export default App;

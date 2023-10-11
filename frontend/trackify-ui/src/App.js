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

function App() {
  const [theme, colorMode] = useMode();

  // const [isLoggedIn, setIsLoggedIn] = useState(false);
  const handleLogin = () => {
    console.log("Change it");
  };
  const isLoggedIn = useSelector((state) => state.login.payload);
  console.log(isLoggedIn);
  return (
    <ColorModeContext.Provider value={colorMode}>
      <ThemeProvider theme={theme}>
        <ToastContainer />
        <CssBaseline />

        {isLoggedIn ? (
          <div className="app">
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
              <Route path="/" element={<Login handleLogin={handleLogin} />} />
              <Route
                path="/login"
                element={<Login handleLogin={handleLogin} />}
              />
              <Route
                path="/register"
                element={<Register handleLogin={handleLogin} />}
              />
              <Route path="*" exact={true} element={<EmptyScene />} />
            </Routes>
          </div>
        )}
      </ThemeProvider>
    </ColorModeContext.Provider>
  );
}

export default App;

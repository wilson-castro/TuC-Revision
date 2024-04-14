import { lazy } from "react";
import { ThemeProvider } from "@mui/material";
import { BrowserRouter, Route, Routes } from "react-router-dom";

import theme from "../components/Theme/theme";
import SuspenseLoadingBox from "../components/SuspenseLoadingBox";

import Login from "../pages/Login";
import NotFound from "../pages/NotFound";

const Home = lazy(() => import("../pages/Home"));

function App() {
  return (
    <ThemeProvider theme={theme}>
      <BrowserRouter>
        <Routes>
          <Route
            path="/"
            element={
              <SuspenseLoadingBox>
                <Home />
              </SuspenseLoadingBox>
            }
          />
          <Route path="/login" element={<Login />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </ThemeProvider>
  );
}

export default App;

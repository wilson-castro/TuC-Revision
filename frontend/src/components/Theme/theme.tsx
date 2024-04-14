import { createTheme } from "@mui/material/styles";

const theme = createTheme({
  palette: {
    primary: {
      light: "#33539c",
      main: "#002884",
      dark: "#001c5c",
      contrastText: "#fff",
    },
    secondary: {
      light: "#3492ca",
      main: "#0277bd",
      dark: "#015384",
      contrastText: "#000",
    },
  },
});

export default theme;

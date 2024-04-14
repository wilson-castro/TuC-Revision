import { AppBar, Button, Toolbar, Typography } from "@mui/material";
import { Link } from "react-router-dom";

export default function Home() {
  return (
    <div>
      <AppBar position="static">
        <Toolbar sx={{ gap: "10px" }}>
          <Button color="secondary" variant="contained">
            <Link to="/login" style={{ color: "white" }}>
              Login
            </Link>
          </Button>
          <Button color="error" variant="outlined">
            <Link to="/signup" style={{ color: "white" }}>
              Cadastrar-se
            </Link>
          </Button>
        </Toolbar>
      </AppBar>
      <div>
        <Typography variant="h2" align="center">
          Home
        </Typography>
      </div>
    </div>
  );
}

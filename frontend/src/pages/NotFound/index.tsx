import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import { Link } from "react-router-dom";
import styled from "@emotion/styled";

import image404 from "../../assets/404.png";

// Estilos customizados com styled-components e @emotion/styled
const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
`;

const ErrorHeading = styled(Typography)`
  margin-bottom: 20px;
`;

const ErrorImage = styled.img`
  width: 200px;
  margin-bottom: 20px;
`;

const NotFound = () => {
  return (
    <Container>
      <ErrorImage src={image404} alt="404 Error" />
      <ErrorHeading variant="h4">Oops! Página não encontrada.</ErrorHeading>
      <Typography variant="body1">
        A página que você está procurando pode ter sido removida ou não estar
        disponível temporariamente.
      </Typography>
      <Button
        component={Link}
        to="/"
        variant="contained"
        color="primary"
        style={{ marginTop: "20px" }}
      >
        Voltar para a Página Inicial
      </Button>
    </Container>
  );
};

export default NotFound;

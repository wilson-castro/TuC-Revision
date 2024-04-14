import { Container, Group, Anchor } from "@mantine/core";
import classes from "./Footer.module.css";

const links = [
  { link: "#", label: "Contatos" },
  { link: "#", label: "Privacidade" },
  { link: "#", label: "Blog" },
  { link: "#", label: "Carreiras" },
];

export function Footer() {
  const items = links.map((link) => (
    <Anchor<"a">
      c="dimmed"
      key={link.label}
      href={link.link}
      onClick={(event) => event.preventDefault()}
      size="sm"
    >
      {link.label}
    </Anchor>
  ));

  return (
    <div className={classes.footer}>
      <Container className={classes.inner}>
        Logo
        <Group className={classes.links}>{items}</Group>
      </Container>
    </div>
  );
}

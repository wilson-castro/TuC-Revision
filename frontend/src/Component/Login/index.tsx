import {
  Button,
  Center,
  Checkbox,
  Flex,
  Paper,
  PasswordInput,
  TextInput,
} from "@mantine/core";
import React from "react";
import Link from "@/Component/Link";

import classes from "./Login.module.css";
import Layout from "../Layout";
import { useForm } from "@mantine/form";
import resolveSubmit from "@/utils/resolveSubmit";

export interface Formvalues {
  login: string;
  senha: string;
  lembrarMe: boolean;
}
interface ILoginProps {
  onSubmit?: (values: Formvalues) => boolean | Promise<boolean>;
}
export default function LoginForm(props: ILoginProps) {
  const FieldWidth = "20vw";
  const [isSubmitting, setSubmitting] = React.useState(false);

  const [isLogin, setLogin] = React.useState(true);
  let animationProps = isLogin
    ? {
        initial: { rotateY: 90 },
        animate: { rotateY: 0, transition: { duration: 0.3, ease: "linear" } },
        exit: { rotateY: 90, transition: { duration: 0.3, ease: "linear" } },
      }
    : undefined;

  const form = useForm({
    initialValues: {
      login: "",
      senha: "",
      lembrarMe: false,
    },
    validate: {
      login: (value) => (value.length < 3 ? "Login inválido" : null),
      senha: (value) => (value.length < 3 ? "Senha inválida" : null),
    },
  });

  return (
    <Layout {...animationProps}>
      <Center>
        <Paper
          style={{
            boxShadow: "5px 10px 15px #1d1a1a5e",
          }}
        >
          <Flex
            w={"30vw"}
            direction="column"
            style={{
              borderRadius: "10px",
            }}
            bg={"white"}
            justify={"center"}
            align={"center"}
            p={10}
          >
            <Center>
              <Link
                fz={"3rem"}
                className={classes.link}
                href="/"
                onClick={() => setLogin(false)}
                w={"fit-content"}
              >
                Login
              </Link>
            </Center>
            <Center w={"100%"}>
              <Flex
                align={"center"}
                justify={"center"}
                direction={"column"}
                w={"100%"}
                h={"40vh"}
              >
                <form
                  onSubmit={form.onSubmit(async (values) => {
                    const { animate, submiting, ok } = await resolveSubmit({
                      values,
                      callBack: () => setSubmitting(true),
                      onSubmit: props.onSubmit,
                      runAnimation: setSubmitting,
                    });
                    
                    setLogin(animate);

                    setSubmitting(submiting);
                    return ok;
                  })}
                  style={{
                    alignItems: "center",
                    justifyContent: "center",
                    display: "flex",
                    flexDirection: "column",
                    width: "100%",
                  }}
                >
                  <TextInput
                    id="login"
                    name="login"
                    type="text"
                    placeholder="Usuário"
                    variant="filled"
                    w={FieldWidth}
                    h="50px"
                    mt={"md"}
                    {...form.getInputProps("login")}
                  />
                  <PasswordInput
                    id="senha"
                    name="senha"
                    type="senha"
                    placeholder="Senha"
                    variant="filled"
                    w={FieldWidth}
                    h="50px"
                    mt={"md"}
                    {...form.getInputProps("senha")}
                  />
                  <Flex
                    direction={"row"}
                    justify={"space-between"}
                    w={"60%"}
                    align={"center"}
                    mt={"sm"}
                    c={"gray.5"}
                  >
                    <Checkbox
                      id="lembrarMe"
                      label="Lembrar-me"
                      name="lembrarMe"
                      type="checkbox"
                      size="xs"
                      {...form.getInputProps("lembrarMe", {
                        type: "checkbox",
                      })}
                    />
                    <Link href="/recuperar" c="red.400" fz={".8rem"}>
                      Recuperar senha
                    </Link>
                  </Flex>
                  <Button
                    mt={"md"}
                    color="red"
                    ml={"0"}
                    type="submit"
                    w={"10vw"}
                    style={{ borderRadius: "100px" }}
                    loading={isSubmitting}
                  >
                    Entrar
                  </Button>
                  {/* <Flex
                    fz={"xs"}
                    c={"#707070"}
                    mt={"lg"}
                    w={"100%"}
                    justify={"center"}
                    align={"center"}
                    direction={"column"}
                  >
                    <span>
                      Não possui uma conta?
                      <Link
                        href={"/register"}
                        c="red.6"
                        fz={"xs"}
                        label={" registre-se."}
                      />
                    </span>
                  </Flex> */}
                </form>
              </Flex>
            </Center>
          </Flex>
        </Paper>
      </Center>
    </Layout>
  );
}

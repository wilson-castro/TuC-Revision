import "@mantine/dates/styles.css";

import { Button, Center, Flex, Paper, TextInput } from "@mantine/core";
import React from "react";
import Link from "@/Component/Link";
import Layout from "../Layout";
import classes from "./Recuperar.module.css";
import { useForm } from "@mantine/form";
import { DateInput } from "@mantine/dates";
import resolveSubmit from "@/utils/resolveSubmit";

export interface Formvalues {
  login: string;
  dataNascimento: string;
}
interface ILoginProps {
  onSubmit?: (values: Formvalues) => boolean | Promise<boolean>;
}
export default function RecuperarForm(props: ILoginProps) {
  const FieldWidth = "20vw";
  const [isSubmitting, setSubmitting] = React.useState(false);

  const [isLogin, setLogin] = React.useState(true);
  let animationProps = isLogin
    ? {
        initial: { rotateY: -90 },
        animate: { rotateY: 0, transition: { duration: 0.3, ease: "linear" } },
        exit: { rotateY: -90, transition: { duration: 0.3, ease: "linear" } },
      }
    : undefined;

  const form = useForm({
    initialValues: {
      login: "",
      dataNascimento: "",
    },
    validate: {
      login: (value) => (value.length < 3 ? "Usuário inválido" : null),
      dataNascimento: (value) =>
        value.length < 3 ? "Data de nascimento inválida" : null,
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
                fz={"2rem"}
                className={classes.link}
                href="/"
                onClick={() => setLogin(false)}
              >
                Esqueci a senha
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
                  <DateInput
                    valueFormat="DD/MM/YYYY"
                    id="dataNascimento"
                    name="dataNascimento"
                    placeholder="Data Nascimento"
                    variant="filled"
                    w={FieldWidth}
                    h="50px"
                    mt={"md"}
                    {...form.getInputProps("dataNascimento")}
                  />
                  <Button
                    mt={"md"}
                    color="red"
                    ml={"0"}
                    type="submit"
                    w={"10vw"}
                    style={{ borderRadius: "100px" }}
                    loading={isSubmitting}
                  >
                    Encontrar conta
                  </Button>
                  <Flex
                    fz={"xs"}
                    c={"#707070"}
                    mt={"lg"}
                    w={"100%"}
                    justify={"center"}
                    align={"center"}
                    direction={"column"}
                  >
                    <span>
                      Ir para
                      <Link
                        href={"/login"}
                        c="red.6"
                        fz={"xs"}
                        label={" Login."}
                      />
                    </span>
                  </Flex>
                </form>
              </Flex>
            </Center>
          </Flex>
        </Paper>
      </Center>
    </Layout>
  );
}

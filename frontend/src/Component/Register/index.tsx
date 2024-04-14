import "@mantine/dates/styles.css";
import { DateInput } from "@mantine/dates";

import {
  Button,
  Center,
  Flex,
  Paper,
  Select,
  Text,
  TextInput,
} from "@mantine/core";
import React from "react";

import classes from "./Register.module.css";
import PasswordWithStrengthChecker from "../PasswordWithStrengthChecker";
import { useForm } from "@mantine/form";
import resolveSubmit from "@/utils/resolveSubmit";

export interface RegisterFormvalues {
  login: string;
  nome: string;
  senha: string;
  dataNascimento: string;
  role: "USER" | "ADMIN";
}
interface IRegisterProps {
  onSubmit?: (values: RegisterFormvalues) => boolean | Promise<boolean>;
}

const RegisterForm: React.FC<IRegisterProps> = (props) => {
  const [isSubmitting, setSubmitting] = React.useState(false);

  const TextInputWidth = "20vw";

  const form = useForm<RegisterFormvalues>({
    initialValues: {
      login: "",
      senha: "",
      nome: "",
      dataNascimento: "",
      role: "USER",
    },
    validate: {
      login: (value) => (value.length < 3 ? "Login inválido" : null),
      senha: (value) => (value.length < 3 ? "Senha inválida" : null),
      dataNascimento: (value) =>
        value.length < 3 ? "Data de nascimento inválida" : null,
      nome: (value) => (value.length < 3 ? "Nome inválido" : null),
      role: (value) => (value.length < 3 ? "Role inválida" : null),
    },
  });
  return (
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
            <Text fz={"2rem"} w={"fit-content"} className={classes.link}>
              Novo usuário
            </Text>
          </Center>
          <Center w={"100%"}>
            <form
              onSubmit={form.onSubmit(async (values) => {
                const { submiting, ok } =
                  await resolveSubmit<RegisterFormvalues>({
                    values,
                    callBack: () => setSubmitting(true),
                    onSubmit: props.onSubmit,
                    runAnimation: setSubmitting,
                  });

                setSubmitting(submiting);
                return ok;
              })}
            >
              <Flex
                align={"center"}
                justify={"center"}
                direction={"column"}
                w={"100%"}
                h={"50vh"}
              >
                <TextInput
                  id="name"
                  name="name"
                  type="text"
                  placeholder="Nome"
                  label="Nome"
                  withAsterisk
                  variant="filled"
                  w={TextInputWidth}
                  height="50px"
                  mt={"sm"}
                  {...form.getInputProps("nome")}
                />
                <Select
                  id="role"
                  name="role"
                  placeholder="Cargo"
                  label="Cargo"
                  withAsterisk
                  variant="filled"
                  w={TextInputWidth}
                  height="50px"
                  mt={"sm"}
                  data={["ADMIN", "USER"]}
                  {...form.getInputProps("role")}
                />
                <TextInput
                  id="login"
                  name="login"
                  type="text"
                  placeholder="Login"
                  label="Login"
                  withAsterisk
                  variant="filled"
                  w={TextInputWidth}
                  height="50px"
                  mt={"md"}
                  {...form.getInputProps("login")}
                />
                <PasswordWithStrengthChecker
                  id="password"
                  name="password"
                  type="password"
                  placeholder="Senha"
                  label="Sua senha"
                  withAsterisk
                  variant="filled"
                  w={TextInputWidth}
                  height="50px"
                  mt={"sm"}
                  {...form.getInputProps("senha")}
                />
                <DateInput
                  valueFormat="DD/MM/YYYY"
                  id="birthDate"
                  label="Data de nascimento"
                  name="birthDate"
                  withAsterisk
                  placeholder="Data nascimento"
                  variant="filled"
                  w={TextInputWidth}
                  height="50px"
                  mt={"sm"}
                  mb={"md"}
                  {...form.getInputProps("dataNascimento")}
                />
                <Flex
                  justify={"flex-end"}
                  align={"flex-end"}
                  direction={"row"}
                  m={10}
                  mt={"md"}
                  w={"100%"}
                >
                  <Button
                    color="red"
                    ml={"auto"}
                    type="submit"
                    w={"100%"}
                    style={{ borderRadius: 100 }}
                    loading={isSubmitting}
                  >
                    Registrar
                  </Button>
                </Flex>
              </Flex>
            </form>
          </Center>
        </Flex>
      </Paper>
    </Center>
  );
};

export default RegisterForm;

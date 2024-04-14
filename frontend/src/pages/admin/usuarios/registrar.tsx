import { RegisterResponse } from "@/@Types/Register";
import Layout from "@/Component/Layout";
import RegisterForm, { RegisterFormvalues } from "@/Component/Register";
import fetchApi from "@/utils/fetcher";
import { Flex } from "@mantine/core";
import { notifications } from "@mantine/notifications";
import Head from "next/head";
import React from "react";

const Register: React.FC = () => {
  const onSubmit = async (values: RegisterFormvalues) => {
    try {
      const response = await fetchApi("/users", {
        method: "POST",
        body: JSON.stringify(values),
      });
      if (!response.ok) {
        throw response;
      }

      const result: RegisterResponse = await response.json();

      notifications.show({
        title: "Sucesso",
        color: "green",
        message: "Cadastrado com sucesso. Id:" + result.id,
      });

      return true;
    } catch (result) {
      if (result instanceof Error) {
        notifications.show({
          title: "Erro",
          color: "red",
          message: result + "",
        });
        return false;
      }
      const data = await (result as Response).json();
      notifications.show({
        title: "Erro",
        color: "red",
        message: data.message,
      });
      return false;
    }
  };
  return (
    <Flex justify="center" align="center" h={"100vh"} bg={"red.4"} w="100vw">
      <Head>
        <title>Registrar</title>
      </Head>
      <RegisterForm onSubmit={onSubmit} />
    </Flex>
  );
};
export default Register;

import { Flex } from "@mantine/core";
import Head from "next/head";
import React from "react";
import LoginForm, { Formvalues } from "@/Component/Login";
import { notifications } from "@mantine/notifications";
import { LoginRet } from "@/@Types/Login";
import cookie from "js-cookie";
import { useRouter } from "next/router";
import { useSetAtom } from "jotai";
import { userAtom } from "@/atoms/auth";

export default function Login() {
  const router = useRouter();
  const setUser = useSetAtom(userAtom);
  const onSubmit = async (values: Formvalues): Promise<boolean> => {
    try {
      const response = await fetch(
        process.env.NEXT_PUBLIC_API_PATH + "/login",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json",
          },
          body: JSON.stringify(values),
        }
      );
      if (!response.ok) {
        throw response;
      }

      const result = await response.json();

      const data: LoginRet = result as LoginRet;
      const exp = new Date(data.refresh_token);
      cookie.set("user", JSON.stringify(data), { expires: exp });

      setUser(data);

      notifications.show({
        title: "Sucesso",
        color: "green",
        message: "Bem vindo," + data.name + ".",
      });

      router.push("/");
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
        <title>Entrar</title>
      </Head>
      <LoginForm onSubmit={onSubmit} />
    </Flex>
  );
}

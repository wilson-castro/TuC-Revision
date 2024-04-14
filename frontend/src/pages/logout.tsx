import { Flex } from "@mantine/core";
import Head from "next/head";
import React from "react";
import { useRouter } from "next/router";
import { useSetAtom } from "jotai";
import { userAtom } from "@/atoms/auth";

export default function Login() {
  const router = useRouter();
  const setUser = useSetAtom(userAtom);

  React.useEffect(() => {
    setUser(null);
    router.push("/");
  }, [router, setUser]);
  return (
    <Flex justify="center" align="center" h={"50vh"} w="100vw">
      <Head>
        <title>Logout</title>
      </Head>
      Saindo...
    </Flex>
  );
}

import { Button } from "@mantine/core";
import { notifications } from "@mantine/notifications";
import Head from "next/head";
import generateVehicles from "@/utils/faker";
import { useAtomValue } from "jotai";
import { userAtom } from "@/atoms/auth";
import React from "react";

export default function Home() {
  const user = useAtomValue(userAtom);

  return (
    <>
      <Head>
        <title>Home | Inicio</title>
        <meta name="description" content="Tela inicial" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Button
        onClick={() => {
          if (user) generateVehicles(user);
          else notifications.show({ message: "Faça login para gerar dados" });
        }}
      >
        Gerar dados
      </Button>
    </>
  );
}

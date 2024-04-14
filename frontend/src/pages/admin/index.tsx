import { ActionsGrid } from "@/Component/admin/actions";
import { Container } from "@mantine/core";
import Head from "next/head";
import React from "react";

export default function Admin() {
  return (
    <Container size={"xs"} mt={50}>
      <Head>
        <title>Administração</title>
        <meta name="description" content="Painel administrativo" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <ActionsGrid />
    </Container>
  );
}

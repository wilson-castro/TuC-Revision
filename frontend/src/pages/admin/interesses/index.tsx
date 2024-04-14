import { Interesse } from "@/@Types/Interesse";
import { LoginRet } from "@/@Types/Login";
import { Content } from "@/@Types/Page";
import { RegisterResponse } from "@/@Types/Register";
import { User } from "@/@Types/User";
import RegisterForm, { RegisterFormvalues } from "@/Component/Register";
import { InterestTable } from "@/Component/Table/Interest";
import { UsersTable } from "@/Component/Table/User";
import fetchApi from "@/utils/fetcher";
import {
  Button,
  Container,
  Flex,
  Grid,
  Pagination,
  Paper,
  TextInput,
} from "@mantine/core";
import Head from "next/head";
import React from "react";

export default function Admin() {
  const [interesses, setUsers] = React.useState<Content<Interesse>>();
  const [page, setPage] = React.useState(1);

  const getData = React.useCallback(async () => {
    try {
      const res = await fetchApi("/interests?page=" + (page - 1));

      if (!res.ok) {
        throw new Error(res.statusText);
      }
      const interesses: Content<Interesse> = await res.json();
      setUsers(interesses);
    } catch (e) {}
  }, [page]);

  React.useEffect(() => {
    getData();
  }, [getData]);
  return (
    <Grid mt={"xl"}>
      <Head>
        <title>Interesses | Administração</title>
        <meta name="description" content="Painel administrativo para carros" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Grid.Col span={2}>
        <Paper ml={"xl"} mt={"xl"}>
          <TextInput
            placeholder="Nome ou telefone"
            mt={"sm"}
            rightSection={<Button>Buscar</Button>}
          />
        </Paper>
      </Grid.Col>
      <Grid.Col span={9}>
        <Container>
          <Flex justify={"space-between"}>
            <Pagination
              mb={"xl"}
              size="xs"
              total={interesses?.totalPages || 0}
              siblings={3}
              defaultValue={page}
              onChange={(page) => {
                setPage(page);
              }}
            />
          </Flex>
          <InterestTable
            data={interesses?.content}
            onDelete={() => {
              getData();
            }}
            onSwitch={() => {
              getData();
            }}
          />
        </Container>
      </Grid.Col>
    </Grid>
  );
}

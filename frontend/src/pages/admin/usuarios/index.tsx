import { LoginRet } from "@/@Types/Login";
import { Content } from "@/@Types/Page";
import { RegisterResponse } from "@/@Types/Register";
import { User } from "@/@Types/User";
import RegisterForm, { RegisterFormvalues } from "@/Component/Register";
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
import { modals } from "@mantine/modals";
import { notifications } from "@mantine/notifications";
import { IconPlus } from "@tabler/icons-react";
import Head from "next/head";
import React from "react";

export default function Admin() {
  const [users, setUsers] = React.useState<Content<User>>();
  const [page, setPage] = React.useState(1);
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
      await getData();
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
    } finally {
      modals.closeAll();
    }
  };

  const getData = React.useCallback(async () => {
    try {
      const res = await fetchApi("/users?page=" + (page - 1));

      if (!res.ok) {
        throw new Error(res.statusText);
      }
      const users: Content<User> = await res.json();
      setUsers(users);
    } catch (e) {}
  }, [page]);

  React.useEffect(() => {
    getData();
  }, [getData]);
  return (
    <Grid mt={"xl"}>
      <Head>
        <title>Carros | Administração</title>
        <meta name="description" content="Painel administrativo para carros" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Grid.Col span={2}>
        <Paper ml={"xl"} mt={"xl"}>
          <TextInput
            placeholder="Nome ou Login"
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
              total={users?.totalPages || 0}
              siblings={3}
              defaultValue={page}
              onChange={(page) => {
                setPage(page);
              }}
            />
            <Button
              leftSection={<IconPlus />}
              color="green"
              size="xs"
              onClick={() => {
                modals.open({
                  withCloseButton: false,
                  children: <RegisterForm onSubmit={onSubmit} />,
                  size: "xl",
                  // fullScreen: true,
                  styles: {
                    body: {
                      backgroundColor: "transparent",
                      border: "none",
                      boxShadow: "none",
                    },
                    content: {
                      backgroundColor: "transparent",
                      border: "none",
                      boxShadow: "none",
                    },
                    header: {
                      backgroundColor: "transparent",
                      border: "none",
                      boxShadow: "none",
                    },
                  },
                });
              }}
            >
              Novo
            </Button>
          </Flex>
          <UsersTable users={users?.content} />
        </Container>
      </Grid.Col>
    </Grid>
  );
}

"use client";
import { Content } from "@/@Types/Page";
import { Carro } from "@/@Types/Carro";
import React from "react";
import CardCarro from "@/Component/Card";
import {
  Button,
  Checkbox,
  Grid,
  Pagination,
  Paper,
  ScrollArea,
  SimpleGrid,
  TextInput,
  Title,
} from "@mantine/core";
import Head from "next/head";

import { LoginRet } from "@/@Types/Login";
import fetchApi from "@/utils/fetcher";
import { notifications } from "@mantine/notifications";
import { GetServerSideProps, InferGetServerSidePropsType } from "next";
import { useRouter } from "next/router";
import { useDebouncedState } from "@mantine/hooks";

let lastPosition = { x: 0, y: 0 };
const Page = ({
  marcas,
  response,
  marcasSelecionadas: selected,
  modelo: mod,
}: InferGetServerSidePropsType<typeof getServerSideProps>) => {
  const router = useRouter();

  const viewport = React.useRef<HTMLDivElement>(null);

  const totalPages = React.useMemo(
    () => response.totalPages,
    [response.totalPages]
  );

  const [modelo, setModelo] = useDebouncedState<string | undefined>(mod, 200);

  const content = React.useMemo(
    () => (response?.ok ? response?.content : []),
    [response]
  );

  const [marcasSelecionadas, setMarcasSelecionadas] = React.useState<string[]>(
    selected.length > 0 ? selected : []
  );

  const filteredContent = content.filter((item) => {
    if (marcas.length > 0) return marcas.includes(item.marca);
    else return true;
  });

  React.useEffect(() => {
    viewport.current!.scrollTo({ top: lastPosition.y, behavior: "instant" });
  }, []);

  return (
    <div>
      <Head>
        <title>Carros | Inicio</title>
        <meta name="description" content="Lista de carros disponiveis" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Grid mt={"xl"}>
        <Grid.Col span={2}>
          <Paper ml={"xl"} mih={150}>
            <Checkbox.Group
              label="Marcas"
              description="Filtro por marcas"
              onChange={(values) => {
                setMarcasSelecionadas(values);
                router.push(
                  {
                    pathname: "/carros",
                    query: { ...router.query, marcas: values, page: 0 },
                  },
                  undefined,
                  { scroll: false }
                );
              }}
              value={marcasSelecionadas}
            >
              <ScrollArea
                h={200}
                onScrollPositionChange={(position) => {
                  lastPosition = position;
                }}
                viewportRef={viewport}
              >
                {marcas?.map((marca) => {
                  return (
                    <Checkbox
                      value={marca}
                      label={marca}
                      key={marca}
                      my={10}
                      checked={marcasSelecionadas.includes(marca)}
                    />
                  );
                })}
              </ScrollArea>
            </Checkbox.Group>
          </Paper>
          <Paper ml={"xl"} mt={"xl"}>
            <TextInput
              placeholder="Modelo"
              mt={"sm"}
              w={"11vw"}
              defaultValue={modelo}
              rightSection={
                <Button
                  onClick={() => {
                    router.push(
                      {
                        pathname: "/carros",
                        query: { ...router.query, modelo },
                      },
                      undefined,
                      { scroll: false }
                    );
                  }}
                >
                  Buscar
                </Button>
              }
              onChange={(e) => {
                setModelo(e.target.value);
              }}
            />
          </Paper>
        </Grid.Col>
        <Grid.Col
          span={9}
          style={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "flex-start",
          }}
        >
          <Title order={1}>Carros</Title>
          <Title order={6} c={"gray.3"} fw={"lighter"} mb={"md"}>
            Clique em &quot;mais detalhes&quot; para mais informações
          </Title>

          <Pagination
            mb={"xl"}
            total={totalPages}
            siblings={3}
            defaultValue={response.page + 1}
            onChange={(page) => {
              router.push({
                pathname: "/carros",
                query: { ...router.query, page: page - 1 },
              });
            }}
          />
          <Title order={6} c="red">
            {response?.totalPages === 0 && !response?.ok
              ? response?.message
              : null}
          </Title>
          <SimpleGrid
            cols={{ base: 1, sm: 3, md: 3, xs: 1, lg: 4 }}
            w={"100%"}
            spacing={{ base: 10, sm: "xl" }}
            verticalSpacing={{ base: "md", sm: "xs" }}
          >
            {filteredContent.map((carro) => (
              <CardCarro key={carro.id} {...carro} />
            ))}
          </SimpleGrid>
        </Grid.Col>
      </Grid>
    </div>
  );
};

type Fulfilled = {
  ok: true;
  message: string;
  page: number;
} & Content<Carro>;

type Rejected = {
  ok: false;
  message: string;
  totalPages: number;
  page: number;
};

type Response = Fulfilled | Rejected;

const getMarcas = async () => {
  try {
    const response = await fetchApi("/cars/marcas");
    const data: string[] = await response.json();
    return data;
  } catch (error) {
    notifications.show({
      message: "Erro ao buscar marcas:" + error,
      color: "red",
    });
    return [];
  }
};

export const getServerSideProps = (async (context) => {
  const { token } = JSON.parse(context.req.cookies["user"] || "{}") as LoginRet;
  const headers = new Headers();

  let page = context.query.page || 0;
  page = Math.abs(Number(page));

  let modelo = context.query.modelo;
  let marcasSelecionadas = context.query.marcas;

  const params = new URLSearchParams();

  params.append("page", String(page));

  if (modelo && typeof modelo == "string") {
    params.append("modelo", modelo);
  } else {
    modelo = "";
  }
  let value: string[] = [];
  if (Array.isArray(marcasSelecionadas)) {
    value = marcasSelecionadas;
  }

  if (typeof marcasSelecionadas == "string") value.push(marcasSelecionadas);

  marcasSelecionadas = value;
  marcasSelecionadas.forEach((marca) => {
    params.append("marca", marca);
  });

  const url = "/cars" + "?" + params;

  const marcas = await getMarcas();
  try {
    headers.append("Content-Type", "application/json");
    if (token) {
      headers.append("Authorization", "Bearer " + token);
    }
    const res = await fetchApi(url, {
      headers: headers,
    });

    if (!res.ok) {
      throw new Error(res.statusText);
    }
    const data: Content<Carro> = await res.json();

    const response: Response = {
      ok: true,
      message: "",
      ...data,
      page: data.number,
    };

    return {
      props: {
        response,
        marcas,
        marcasSelecionadas,
        modelo,
      },
    };
  } catch (error) {
    const response: Response = {
      ok: false,
      message: String(error),
      totalPages: 0,
      page: 0,
    };
    return {
      props: {
        response,
        marcas,
        marcasSelecionadas,
        modelo,
      },
    };
  }
}) satisfies GetServerSideProps<{
  response: Response;
  marcas: string[];
  marcasSelecionadas: string[];
  modelo: string | undefined;
}>;

export default Page;

import { Carro } from "@/@Types/Carro";
import { LoginRet } from "@/@Types/Login";
import { Content } from "@/@Types/Page";
import { CarTable } from "@/Component/Table/Cars";
import fetchApi from "@/utils/fetcher";
import {
  Button,
  Checkbox,
  Container,
  Flex,
  Grid,
  Pagination,
  Paper,
  ScrollArea,
  TextInput,
} from "@mantine/core";
import { useDebouncedState } from "@mantine/hooks";
import { notifications } from "@mantine/notifications";
import { IconPlus } from "@tabler/icons-react";
import Head from "next/head";
import { useRouter } from "next/router";
import React from "react";

export default function Admin() {
  const router = useRouter();
  const input = React.useRef<HTMLInputElement>(null);
  const [data, setData] = React.useState<Content<Carro>>();
  const [marcas, setMarcas] = React.useState<string[]>();

  const [page, setPage] = React.useState(1);
  const [modelo, setModelo] = useDebouncedState<string | undefined>("", 200);

  const [marcasSelecionadas, setMarcasSelecionadas] = React.useState<string[]>(
    []
  );

  React.useEffect(() => {
    getMarcas().then(setMarcas);
  }, []);

  React.useEffect(() => {
    getCarros({ page, marcasSelecionadas, modelo }).then(setData);
  }, [marcasSelecionadas, modelo, page]);

  return (
    <Grid mt={"xl"}>
      <Head>
        <title>Carros | Administração</title>
        <meta name="description" content="Painel administrativo para carros" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="icon" href="/favicon.ico" />
      </Head>
      <Grid.Col span={2}>
        <Paper ml={"xl"} mih={150}>
          <Checkbox.Group
            label="Marcas"
            autoFocus
            data-focus
            description="Filtro por marcas"
            onChange={(values) => {
              setPage(1);
              setMarcasSelecionadas(values);
            }}
            value={marcasSelecionadas}
          >
            <ScrollArea h={200}>
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
            defaultValue={modelo}
            ref={input}
            onChange={(e) => {
              setModelo(e.target.value);
              setPage(1);
            }}
          />
        </Paper>
      </Grid.Col>
      <Grid.Col span={9}>
        <Container mt={50} size={"xl"}>
          <Flex justify={"space-between"}>
            <Pagination
              total={data?.totalPages || 0}
              value={page}
              size="xs"
              onChange={(page) => {
                setPage(page);
              }}
            />
            <Button
              leftSection={<IconPlus />}
              color="green"
              size="xs"
              onClick={() => {
                router.push({
                  pathname: "/admin/carros/novo",
                });
              }}
            >
              Novo
            </Button>
          </Flex>
          <CarTable
            data={data?.content}
            onDelete={() => {
              getCarros({ page, marcasSelecionadas, modelo }).then((data) => {
                setData(data);
                if (data?.totalPages && data.totalPages <= page) {
                  setPage(data.totalPages);
                }
              });
            }}
          />
        </Container>
      </Grid.Col>
    </Grid>
  );
}

const getMarcas = async () => {
  try {
    const response = await fetchApi("/cars/marcas");
    const data: string[] = await response.json();
    return data;
  } catch (error) {
    return [];
  }
};
interface IGetData {
  page: number;
  modelo?: string;
  marcasSelecionadas?: string[];
}

const getCarros = async ({ page, marcasSelecionadas, modelo }: IGetData) => {
  const params = new URLSearchParams();

  params.append("page", String(page - 1));

  if (modelo && typeof modelo == "string" && modelo.length > 0) {
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

  params.append("all", "yes");

  const url = "/cars" + "?" + params;

  try {
    const data: Content<Carro> = await (await fetchApi(url)).json();

    return data;
  } catch (error) {
    return undefined;
  }
};

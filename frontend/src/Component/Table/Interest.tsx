import cx from "clsx";
import { useState } from "react";
import {
  Table,
  ScrollArea,
  ActionIcon,
  Flex,
  Badge,
  Anchor,
  Switch,
} from "@mantine/core";
import classes from "./TableScrollArea.module.css";
import { BrDateFormat } from "@/utils/Formatter";
import { IconAdjustments, IconTrash } from "@tabler/icons-react";
import { modals } from "@mantine/modals";
import fetchApi from "@/utils/fetcher";
import { notifications } from "@mantine/notifications";
import { useRouter } from "next/router";
import { Interesse } from "@/@Types/Interesse";
import Link from "next/link";

interface InterestTableProps {
  data?: Interesse[];
  onDelete?: (id: number | undefined) => void;
  onEdit?: (id: number | undefined) => void;
  onSwitch?: (id: number | undefined) => void;
}
export function InterestTable({
  data,
  onDelete,
  onSwitch,
  onEdit,
}: InterestTableProps) {
  const [scrolled, setScrolled] = useState(false);
  const rows = data?.map((row) => (
    <Table.Tr key={row.id}>
      <Table.Td>{row.id}</Table.Td>
      <Table.Td>{row.nome}</Table.Td>
      <Table.Td>{row.telefone}</Table.Td>
      <Table.Td>{BrDateFormat.format(new Date(row.dataInteresse))}</Table.Td>
      <Table.Td>
        <Switch
          size="lg"
          onLabel="Ativo"
          offLabel="Inativo"
          defaultChecked={row.ativo}
          onChange={(e) => {
            fetchApi(`/interests/${row.id}`, {
              method: "PUT",
              body: JSON.stringify({
                ativo: e.currentTarget.checked,
              }),
            }).then(async (res) => {
              if (!res.ok) {
                throw await res.json();
              }
              notifications.show({
                message: "Atualizado com sucesso",
                color: "green",
              });
              onSwitch?.(row.id);
            });
            return e;
          }}
        />
      </Table.Td>
      <Table.Td>
        <Anchor
          component={Link}
          href={{ pathname: "/carros/[id]", query: { id: row.carro.id } }}
        >
          {row.carro.marca + "-" + row.carro.modelo}
        </Anchor>
      </Table.Td>
      <Table.Td>
        <Flex justify={"space-around"} direction={"row"}>
          <ActionIcon>
            <IconAdjustments />
          </ActionIcon>
          <ActionIcon
            color="red"
            onClick={() => {
              modals.openConfirmModal({
                title: "Por favor, confirme sua ação",
                children: <>Confirma a deleção?</>,
                confirmProps: {
                  color: "red",
                },
                labels: { confirm: "Deletar", cancel: "Cancelar" },
                onCancel: () => console.log("Cancelado"),
                onConfirm: () =>
                  fetchApi(`/interests/${row.id}`, { method: "DELETE" })
                    .then(async (res) => {
                      if (!res.ok) {
                        throw await res.json();
                      }
                      notifications.show({
                        message: "Deletado com sucesso",
                        color: "green",
                      });
                      onDelete?.(row.id);
                    })
                    .catch(async (err) => {
                      if (err instanceof Error) {
                        notifications.show({
                          title: "Erro",
                          color: "red",
                          message: err + "",
                        });
                        return false;
                      }
                      const data = await (err as Response).json();
                      notifications.show({
                        title: "Erro",
                        color: "red",
                        message: data.message,
                      });
                    }),
              });
            }}
          >
            <IconTrash />
          </ActionIcon>
        </Flex>
      </Table.Td>
    </Table.Tr>
  ));

  return (
    <ScrollArea
      h={300}
      onScrollPositionChange={({ y }) => setScrolled(y !== 0)}
    >
      <Table miw={800}>
        <Table.Thead
          className={cx(classes.header, { [classes.scrolled]: scrolled })}
          style={{ zIndex: 2 }}
        >
          <Table.Tr>
            <Table.Th>ID</Table.Th>
            <Table.Th>Nome</Table.Th>
            <Table.Th>Telefone</Table.Th>
            <Table.Th>Data interesse</Table.Th>
            <Table.Th>Ativo</Table.Th>
            <Table.Th>Carro</Table.Th>
            <Table.Th>Ações</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>{rows}</Table.Tbody>
      </Table>
    </ScrollArea>
  );
}

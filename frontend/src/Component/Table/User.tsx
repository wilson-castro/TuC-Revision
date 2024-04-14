import cx from "clsx";
import classes from "./TableScrollArea.module.css";
import { User } from "@/@Types/User";
import { BrDateFormat } from "@/utils/Formatter";
import {
  Avatar,
  Badge,
  Table,
  Group,
  Text,
  ScrollArea,
  ActionIcon,
  Flex,
} from "@mantine/core";
import React from "react";
import { IconAdjustments, IconTrash } from "@tabler/icons-react";
import { modals } from "@mantine/modals";
import fetchApi from "@/utils/fetcher";
import { useRouter } from "next/router";
import { notifications } from "@mantine/notifications";

export function UsersTable({ users }: { users?: User[] }) {
  const [scrolled, setScrolled] = React.useState(false);
  const router = useRouter();

  const rows = users?.map((item) => (
    <Table.Tr key={item.id}>
      <Table.Td>{item.id}</Table.Td>
      <Table.Td>
        <Group gap="sm">
          <Avatar size={40} radius={40} />
          <div>
            <Text fz="sm" fw={500}>
              {item.nome}
            </Text>
            <Text fz="xs" c="dimmed">
              {item.login}
            </Text>
          </div>
        </Group>
      </Table.Td>
      <Table.Td>{item.role}</Table.Td>
      <Table.Td>{BrDateFormat.format(new Date(item.dataCadastro))}</Table.Td>
      <Table.Td>{BrDateFormat.format(new Date(item.dataNascimento))}</Table.Td>
      <Table.Td>
        {item.ativo ? (
          <Badge fullWidth variant="light">
            Ativo
          </Badge>
        ) : (
          <Badge color="gray" fullWidth variant="light">
            Desabilitado
          </Badge>
        )}
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
                  fetchApi(`/users/${item.id}`, { method: "DELETE" })
                    .then(async (res) => {
                      if (!res.ok) {
                        throw await res.json();
                      }
                      notifications.show({
                        message: "Deletado com sucesso",
                        color: "green",
                      });

                      router.push({
                        ...router,
                      });
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
            <Table.Th>Usuário</Table.Th>
            <Table.Th>Cargo</Table.Th>
            <Table.Th>Data cadastro</Table.Th>
            <Table.Th>Data nascimento</Table.Th>
            <Table.Th>Status</Table.Th>
            <Table.Th>Ações</Table.Th>
          </Table.Tr>
        </Table.Thead>
        <Table.Tbody>{rows}</Table.Tbody>
      </Table>
    </ScrollArea>
  );
}

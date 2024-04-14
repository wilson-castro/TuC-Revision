import {
  Card,
  Text,
  SimpleGrid,
  UnstyledButton,
  Anchor,
  Group,
  useMantineTheme,
} from "@mantine/core";
import { IconCar, IconUserExclamation, IconUsers } from "@tabler/icons-react";
import classes from "./ActionsGrid.module.css";
import { useRouter } from "next/router";
import { IconUserHeart } from "@tabler/icons-react";

const mockdata = [
  { title: "Usuários", icon: IconUsers, color: "red", href: "/admin/usuarios" },
  { title: "Carros", icon: IconCar, color: "blue", href: "/admin/carros" },
  {
    title: "Interesses",
    icon: IconUserHeart,
    color: "grape",
    href: "/admin/interesses",
  },
];

export function ActionsGrid() {
  const theme = useMantineTheme();
  const router = useRouter();

  const items = mockdata.map((item) => (
    <UnstyledButton
      key={item.title}
      className={classes.item}
      onClick={() => {
        router.push(item.href);
      }}
    >
      <item.icon color={theme.colors[item.color][6]} size="2rem" />
      <Text size="xs" mt={7}>
        {item.title}
      </Text>
    </UnstyledButton>
  ));

  return (
    <Card withBorder radius="md" className={classes.card}>
      <Group justify="space-between">
        <Text className={classes.title}>Menus</Text>
        {items.length > 9 && (
          <Anchor size="xs" c="dimmed" style={{ lineHeight: 1 }}>
            + {items.length - 10} other services
          </Anchor>
        )}
      </Group>
      <SimpleGrid cols={3} mt="md">
        {items.slice(0, 9)}
      </SimpleGrid>
    </Card>
  );
}

import {
  Card,
  Image,
  Text,
  Badge,
  Button,
  Group,
  CardSection,
} from "@mantine/core";
import { Carro } from "@/@Types/Carro";
import Link from "next/link";
import { BRlFormat } from "@/utils/Formatter";

export default function CardCarro(props: Carro) {
  return (
    <Card shadow="sm" padding="lg" radius="xs" withBorder maw={"100vw"}>
      <CardSection>
        <Image src={props.image} height={160} alt="Norway" />
      </CardSection>

      <Group justify="space-between" mt="md" mb="xs">
        <Text fw={500}>
          {props.marca} {props.modelo} {props.anoModelo.slice(0, 4)}
        </Text>
        <Badge color="green" variant="light" size="lg">
          {BRlFormat.format(props.valor)}
        </Badge>
      </Group>

      <Text size="sm" c="dimmed" lineClamp={3} mah={60} h={60}>
        {props.descricao}
      </Text>
      <Text size="sm" c="cyan">
        Fabricação: {props.anoFabricacao.slice(0, 4)}
      </Text>

      <Button
        variant="light"
        color="blue"
        fullWidth
        mt="md"
        radius="md"
        component={Link}
        href={`/carros/${props.id}`}
      >
        Mais informações
      </Button>
    </Card>
  );
}

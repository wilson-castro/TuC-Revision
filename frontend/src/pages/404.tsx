import { Flex, Text } from "@mantine/core";
import React from "react";

export default function Error() {
  return (
    <Flex
      justify="center"
      align="center"
      direction="column"
      style={{ height: "50vh" }}
    >
      <Text c={"red.5"} size={"9rem"} fw={"bolder"}>
        404
      </Text>
      <Text size={"xl"} c={"gray.6"}>
        Pagina não encontrada
      </Text>
    </Flex>
  );
}

import React, { ReactNode, useState } from "react";
import { useSpring, a } from "@react-spring/web";

import styles from "./styles.module.css";
import { Box } from "@mantine/core";

interface IProps {
  front: ReactNode;
  back: ReactNode;
  flipped: boolean;
}
export default function FlipAnimation(props: IProps) {
  const { transform, opacity } = useSpring({
    opacity: props.flipped ? 1 : 0,
    transform: `perspective(600px) rotateX(${props.flipped ? 180 : 0}deg)`,
    config: { mass: 5, tension: 500, friction: 80 },
  });

  return (
    <Box w={"100%"}>
      <a.div
        style={{ opacity: opacity.to((o) => 1 - o), transform }}
        hidden={props.flipped}
      >
        {props.back}
      </a.div>
      <a.div
        hidden={!props.flipped}
        style={{
          opacity,
          transform,
          rotateX: "180deg",
        }}
      >
        {props.front}
      </a.div>
    </Box>
  );
}

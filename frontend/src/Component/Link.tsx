import { UnstyledButton } from "@mantine/core";
import type { UnstyledButtonProps } from "@mantine/core";
import NextLink from "next/link";

import type {
  ReactNode,
  FC,
  PropsWithChildren,
  MouseEventHandler,
} from "react";
interface IProps extends UnstyledButtonProps {
  href: string;
  label?: string;
  onClick?: MouseEventHandler<HTMLAnchorElement> | undefined;
}
export default function Link(props: PropsWithChildren<IProps>): ReactNode {
  const { children, label } = props;
  return (
    <UnstyledButton component={NextLink} {...props}>
      {children ? children : label}
    </UnstyledButton>
  );
}

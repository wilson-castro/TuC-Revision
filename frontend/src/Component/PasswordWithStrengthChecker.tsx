import { useState } from "react";
import { IconX, IconCheck } from "@tabler/icons-react";
import {
  PasswordInput,
  Progress,
  Text,
  Popover,
  Box,
  rem,
  PasswordInputProps,
  Title,
} from "@mantine/core";

function PasswordRequirement({
  meets,
  label,
}: {
  meets: boolean;
  label: string;
}) {
  return (
    <Text
      c={meets ? "teal" : "red"}
      style={{ display: "flex", alignItems: "center" }}
      mt={7}
      size="sm"
    >
      {meets ? (
        <IconCheck style={{ width: rem(14), height: rem(14) }} />
      ) : (
        <IconX style={{ width: rem(14), height: rem(14) }} />
      )}{" "}
      <Box ml={10}>{label}</Box>
    </Text>
  );
}

const requirements = [
  { re: /[0-9]/, label: "Precisa incluir ao menos um numero" },
  { re: /[a-z]/, label: "Precisa incluir ao menos uma letra minuscula" },
  { re: /[A-Z]/, label: "Precisa incluir ao menos uma letra maiscula" },
  {
    re: /[$&+,:;=?@#|'<>.^*()%!-]/,
    label:
      "Precisa incluir ao menos um simbolo. Ex: !@#$%^&*()_+-=[]{}\\|;':\",./<>? ",
  },
];

function getStrength(password: string) {
  let multiplier = password.length > 5 ? 0 : 1;

  requirements.forEach((requirement) => {
    if (!requirement.re.test(password)) {
      multiplier += 1;
    }
  });

  return Math.max(100 - (100 / (requirements.length + 1)) * multiplier, 10);
}

export default function PasswordWithStrengthChecker(
  props: Omit<PasswordInputProps, "value">
) {
  const [popoverOpened, setPopoverOpened] = useState(false);
  const [value, setValue] = useState("");
  const checks = requirements.map((requirement, index) => (
    <PasswordRequirement
      key={index}
      label={requirement.label}
      meets={requirement.re.test(value)}
    />
  ));

  const strength = getStrength(value);
  const color = strength === 100 ? "teal" : strength > 50 ? "yellow" : "red";

  return (
    <Popover
      opened={popoverOpened}
      position="bottom"
      width="target"
      transitionProps={{ transition: "pop" }}
    >
      <Popover.Target>
        <div
          onFocusCapture={() => setPopoverOpened(true)}
          onBlurCapture={() => setPopoverOpened(false)}
        >
          <PasswordInput
            withAsterisk
            label="Your password"
            placeholder="Your password"
            {...props}
            value={value}
            onChange={(event) => {
              setValue(event.currentTarget.value);
              props.onChange?.(event);
            }}
          />
        </div>
      </Popover.Target>
      <Popover.Dropdown>
        <Progress color={color} value={strength} size={5} mb="xs" />
        <Title order={6}> Recomendado:</Title>
        <PasswordRequirement
          label="Precisa de incluir no minimo 6 caracteres"
          meets={value.length > 5}
        />
        {checks}
      </Popover.Dropdown>
    </Popover>
  );
}

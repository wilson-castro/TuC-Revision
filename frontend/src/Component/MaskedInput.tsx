import { Input, InputWrapperProps, InputProps } from "@mantine/core";
import { IMaskInput } from "react-imask";
import type { IMaskInputProps } from "react-imask";

type IMaskedInput = InputProps &
  Omit<InputWrapperProps, "children"> &
  Omit<IMaskInputProps<any>, "instance" | "ref"> & { value?: string };
export const MaskedInput = (props: IMaskedInput) => {
  const {
    onChange,
    onBlur,
    onFocus,
    value,
    error,
    errorProps,
    mask,
    withAsterisk,
    id,
    placeholder,
    disabled,
    variant,
    onAccept,
    ...rest
  } = props;
  return (
    <Input.Wrapper
      {...rest}
      error={error}
      withAsterisk={withAsterisk}
      errorProps={errorProps}
    >
      <Input<typeof IMaskInput>
        id={id}
        component={IMaskInput}
        {...{
          onChange,
          onAccept,
          onBlur,
          onFocus,
          value,
          mask,
          error,
          placeholder,
          disabled,
          variant,
        }}
      />
    </Input.Wrapper>
  );
};

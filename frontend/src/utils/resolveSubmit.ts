const resolveSubmit = async <T>({
  values,
  callBack,
  onSubmit,
}: {
  values: T;
  onSubmit?: (values: T) => boolean | Promise<boolean>;
  callBack?: () => void;
  runAnimation?: boolean | ((x: boolean) => void);
}) => {
  if (!onSubmit) return { animate: true, submiting: false, ok: false };
  callBack?.();
  const handler = onSubmit;
  let ret = { animate: true, submiting: true, ok: false };
  if (handler.constructor.name === "AsyncFunction") {
    const asyncFunc = await handler(values);

    if (asyncFunc) {
      ret.animate = false;
    }
    ret.ok = asyncFunc;
    ret.submiting = false;
    return ret;
  }
  const result = handler(values) as boolean;
  if (result) {
    ret.animate = false;
  }

  ret.ok = result;
  ret.submiting = false;
  return ret;
};
export default resolveSubmit;

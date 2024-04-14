export default function parseSearchParams() {
  if (typeof window === "undefined") return {};
  const searchParams = new URLSearchParams(window.location.search);
  const searchParamsObject: { [x: string]: string } = {};
  searchParams.forEach((value, key) => {
    searchParamsObject[key] = value;
  });
  return searchParamsObject;
}

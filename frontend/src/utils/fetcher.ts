import { userData } from "@/atoms/auth";
import { isExpired, refreshToken } from "@/refreshToken";

export default async function fetchApi(
  input: RequestInfo | URL,
  init?: RequestInit
) {
  let user = userData;

  const isExp = isExpired(user?.exp || 0);
  if (isExp) {
    const { data, ok } = await refreshToken(
      user?.refresh_token,
      user?.exp_refresh || 0
    );
    if (ok) {
      user = data;
    }
  }
  const thisInit: RequestInit = { ...init };

  let headers = new Headers(init?.headers);
  headers.append("Content-Type", "application/json");
  headers.append("Accept", "application/json");

  if (user?.token) {
    headers.append("Authorization", `Bearer ${user.token}`);
  }

  input = treatInput(input, headers);
  thisInit.headers = headers;
  return fetch(input, thisInit);
}

function treatInput(input: RequestInfo | URL, headers: Headers) {
  if (typeof input == "object") {
    const thisInput = input as Writeable<Request>;
    thisInput.url = process.env.NEXT_PUBLIC_API_PATH + thisInput.url;
    thisInput.headers = headers;
    return thisInput;
  } else {
    return process.env.NEXT_PUBLIC_API_PATH + input;
  }
}
type Writeable<T> = { -readonly [P in keyof T]: T[P] };

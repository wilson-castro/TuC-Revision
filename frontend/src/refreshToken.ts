import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import { LoginRet } from "./@Types/Login";
import { myStore, userAtom } from "./atoms/auth";
import Cookies from "js-cookie";
const pagesBlockedWheLoged = ["/login", "/register", "/recuperar"];
const authOnlyPages = ["^/admin.*$"];
const adminOnlyPage = ["^/admin.*$"];
// This function can be marked `async` if using `await` inside
export async function middleware(pathName: string) {
  let cookie = Cookies.get("user");
  const isAuth = CheckIfAuthenticated();

  if (!isAuth) {
    // Checa se é uma pagina que necessita de autenticação
    for (const rule of authOnlyPages) {
      if (checkRule(rule, pathName)) {
        console.log("Não autenticado");
        // Se não estiver autenticado, redireciona para a pagina de login
        return logout();
      }
    }
    // Se não for uma pagina que necessita de autenticação, permite a entrada
    return pathName;
  }
  const userData = JSON.parse(cookie || "{}") as LoginRet;
  const { exp, role, refresh_token, exp_refresh } = userData;

  const IsExpired = isExpired(exp);

  if (IsExpired) {
    const { data, ok } = await refreshToken(refresh_token, exp_refresh);
    if (ok) {
      console.log("token atualizado");
      login(data as LoginRet);
      return pathName;
    }
    return logout();
  }

  if (pagesBlockedWheLoged.includes(pathName)) {
    console.log(
      `Não é possivel ir para a pagina "${pathName}" enquanto logado! Faça logout primeiro!`
    );
    return "/";
  }

  for (const rule of adminOnlyPage) {
    // Bloqueia para todos os usuários que não são admin
    if (checkRule(rule, pathName) && role.toLocaleLowerCase() != "admin") {
      console.log("Não é admin");
      return "/";
    }
  }
  const userAtomData = myStore.get(userAtom);

  if (userAtomData == null) {
    console.log("Dados não encontrados, redefinindo...");
    myStore.set(userAtom, userData);
  }
  console.log("Logado", role);
  return pathName;
}

export async function refreshToken(token: string | undefined, exp: number) {
  const isExp = isExpired(exp);
  if (isExp) return { ok: false, data: null };
  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  const resFetch = await fetch(process.env.NEXT_PUBLIC_API_PATH + "/refresh", {
    method: "POST",
    headers: myHeaders,
    body: JSON.stringify({
      token,
    }),
    redirect: "follow",
  });

  if (!resFetch.ok) {
    return { ok: false, data: null };
  }
  const result = await resFetch.json();
  const data: LoginRet = result as LoginRet;
  return { ok: true, data };
}

export function isExpired(exp: number) {
  const expDate = new Date(exp);
  return expDate.getTime() < Date.now();
}

function CheckIfAuthenticated() {
  let cookie = Cookies.get("user");
  if (!cookie) return false;
  return true;
}

function login(user: LoginRet) {
  myStore.set(userAtom, user);
  Cookies.set("user", JSON.stringify(user), {
    expires: new Date(user.exp_refresh),
  });
}

function logout() {
  Cookies.remove("user");
  myStore.set(userAtom, null);
  console.log("Logout realizado");
  return "/";
}

function checkRule(str: string, pathName: string) {
  const regex = new RegExp(str);
  return regex.test(pathName);
}
// See "Matching Paths" below to learn more
export const config = {
  matcher: ["/:path*"],
};

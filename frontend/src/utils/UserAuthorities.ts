import { LoginRet } from "@/@Types/Login";

interface Authorities {
  isAdmin: boolean;
  isNormalUser: boolean;
  isAnonymous: boolean;
}
export default function userAuthorities(user: LoginRet | null) {
  const authorities: Authorities = {
    isAdmin: false,
    isNormalUser: false,
    isAnonymous: false,
  };
  if (user?.role.toLocaleLowerCase() == "admin") authorities.isAdmin = true;
  if (user?.role.toLocaleLowerCase() == "user") authorities.isNormalUser = true;
  if (!user) authorities.isAnonymous = true;
  return authorities;
}

import "@mantine/core/styles.css";
import "@mantine/notifications/styles.css";
import "@mantine/nprogress/styles.css";

import { AnimatePresence } from "framer-motion";
import { MantineProvider } from "@mantine/core";
import { ModalsProvider } from "@mantine/modals";
import { Notifications } from "@mantine/notifications";
import { nprogress, NavigationProgress } from "@mantine/nprogress";
import type { AppProps } from "next/app";
import { HeaderMenu } from "@/Component/Header/Header";
import { Router } from "next/router";
import Check from "@/utils/CheckPage";
import { Roboto_Mono } from "next/font/google";
import { Provider } from "jotai";
import React from "react";
import { Footer } from "@/Component/Footer/Footer";
import { myStore } from "@/atoms/auth";
import { middleware } from "@/refreshToken";

const font = Roboto_Mono({
  subsets: ["latin"],
  weight: "400",
});

Router.events.on("routeChangeStart", () => {
  nprogress.start();
});
Router.events.on("routeChangeComplete", () => nprogress.complete());
Router.events.on("routeChangeError", () => nprogress.complete());

export default function App({ Component, pageProps, router }: AppProps) {
  // const idle = useIdle(1000, { initialState: false });

  const isNavbarVisible = Check(router.pathname, "navbar");
  const isFooterVisible = Check(router.pathname, "footer");

  React.useEffect(() => {
    middleware(router.asPath).then((path) => {
      router.push(path);
    });
  }, [router, router.pathname]);
  return (
    <Provider store={myStore}>
      <MantineProvider>
        <ModalsProvider>
          <NavigationProgress color="cyan" />

          {isNavbarVisible ? <HeaderMenu /> : null}

          <Notifications position="top-right" zIndex={1000} />
          <AnimatePresence mode="wait" initial={false}>
            <div className={font.className} key={router.asPath}>
              <Component {...pageProps} />
            </div>
          </AnimatePresence>
          {isFooterVisible ? <Footer /> : null}
        </ModalsProvider>
      </MantineProvider>
    </Provider>
  );
}

import React from "react";
import { PDFViewer } from "@react-pdf/renderer";
import { MyDocument } from "@/Component/Print";
import { Box } from "@mantine/core";
import { LoginRet } from "@/@Types/Login";
import fetchApi from "@/utils/fetcher";
import { Interesse } from "@/@Types/Interesse";
import { Content } from "@/@Types/Page";
import { GetServerSideProps, InferGetServerSidePropsType } from "next";
export default function Relatorio({
  data,
}: InferGetServerSidePropsType<typeof getServerSideProps>) {
  const [isWeb, setIsWeb] = React.useState(false);
  React.useEffect(() => {
    setIsWeb(typeof window !== "undefined");
  }, []);
  return isWeb ? (
    <div>
      {/* {JSON.stringify(data)} */}
      <Box component={PDFViewer} h={"100vh"} w={"100%"}>
        <MyDocument data={data} />
      </Box>
    </div>
  ) : null;
}

export const getServerSideProps = (async (context) => {
  const { token } = JSON.parse(context.req.cookies["user"] || "{}") as LoginRet;
  const headers = new Headers();

  try {
    headers.append("Content-Type", "application/json");
    if (token) {
      headers.append("Authorization", "Bearer " + token);
    }
    const res = await fetchApi("/interests", {
      headers: headers,
    });

    if (!res.ok) {
      throw new Error(res.statusText);
    }
    const data: Content<Interesse> = await res.json();

    return {
      props: {
        data,
      },
    };
  } catch (error) {
    return {
      props: {
        data: null,
      },
    };
  }
}) satisfies GetServerSideProps<{
  data: Content<Interesse> | null;
}>;

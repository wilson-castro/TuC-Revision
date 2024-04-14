import { PropsWithChildren, Suspense } from "react";
import { Backdrop, Box, CircularProgress } from "@mui/material";

export default function SuspenseLoadingBox({
  children,
}: Readonly<PropsWithChildren>) {
  return (
    <Suspense
      fallback={
        <Box
          sx={{
            zIndex: 0,
            width: "100%",
            minHeight: "100vh",
            position: "relative",
          }}
        >
          <Backdrop
            sx={{
              color: "#fff",
              position: "absolute",
              backgroundColor: "rgb(0 0 0 / 20%)",
            }}
            open={true}
          >
            <CircularProgress color="inherit" size={70} />
          </Backdrop>
        </Box>
      }
    >
      {children}
    </Suspense>
  );
}

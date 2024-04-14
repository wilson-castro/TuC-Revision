import { HTMLMotionProps, Variants, motion } from "framer-motion";
import { PropsWithChildren, ReactNode } from "react";

const Layout: React.FC<PropsWithChildren<Variants>> = ({
  children,
  ...rest
}) => {
  return (
    <motion.div variants={rest} initial="initial" animate="animate" exit="exit">
      {children}
    </motion.div>
  );
};
export default Layout;

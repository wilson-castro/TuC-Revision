import React from "react";
import { Page, Text, View, Document, StyleSheet } from "@react-pdf/renderer";

interface Interesse {
  id: number;
  dataInteresse: string;
  carro: Carro;
  nome: string;
  telefone: string;
  ativo: boolean;
}

interface Carro {
  id?: number;
  marca: string;
  modelo: string;
  anoFabricacao: string;
  anoModelo: string;
  valor: number;
  descricao: string;
  image: string;
}

const BrDateFormat = new Intl.DateTimeFormat("pt-BR", {
  dateStyle: "short",
  timeStyle: "short",
});

// Create styles
const styles = StyleSheet.create({
  page: {
    flexDirection: "column",
    backgroundColor: "#ffffff",
  },
  section: {
    margin: 5,
    padding: 10,
    flexDirection: "row",
    justifyContent: "space-between",
    backgroundColor: "#707070",
  },
  top: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    backgroundColor: "#1b1a1a",
    color: "#ffffff",
    height: "50px",
    fontSize: "20px",
    fontWeight: "bold",
  },
});

// Create Document Component
export const MyDocument = ({ data }: { data: Content<Interesse> | null }) => (
  <Document>
    <Page size="A4" style={styles.page}>
      <View style={styles.top}>
        <Text>Relatorio de interesses</Text>
      </View>
      <View
        style={{
          padding: 10,
          flexDirection: "row",
          justifyContent: "space-between",
          alignItems: "center",
          backgroundColor: "#1b1a1a",
          color: "#FFF",
        }}
      >
        <Text style={{ width: "20px" }}>#</Text>
        <Text style={{ width: "80px" }}>Nome</Text>
        <Text style={{ width: "120px" }}>Telefone</Text>
        <Text style={{ width: "80px", fontSize: "13pt" }}>Dt.Inclusão</Text>
        <Text style={{ width: "90px" }}>Carro</Text>
      </View>
      {data?.content.map((item, key) => {
        const color = key % 2 == 0 ? "#ececec" : "#636363";
        styles.section = { ...styles.section, backgroundColor: color };
        return (
          <View style={styles.section} key={item.id}>
            <Text style={{ width: "20px", fontSize: "14pt" }}>{item.id}</Text>
            <Text style={{ width: "80px", fontSize: "14pt" }}>
              {item.nome.trim()}
            </Text>
            <Text style={{ width: "120px", fontSize: "14pt" }}>
              {item.telefone.trim()}
            </Text>
            <Text style={{ width: "80px", fontSize: "14pt" }}>
              {BrDateFormat.format(new Date(item.dataInteresse)).slice(0, 10)}
            </Text>
            <Text style={{ width: "90px", fontSize: "14pt" }}>
              {(item.carro.marca + "-" + item.carro.modelo).trim()}
            </Text>
          </View>
        );
      })}
    </Page>
  </Document>
);

interface Content<T> {
  content: T[];
  pageable: Pageable;
  last: boolean;
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  sort: Sort2;
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}

interface Pageable {
  pageNumber: number;
  pageSize: number;
  sort: Sort;
  offset: number;
  unpaged: boolean;
  paged: boolean;
}

interface Sort {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

interface Sort2 {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

import { Carro } from "./Carro";

export interface Interesse {
  id: number;
  dataInteresse: string;
  carro: Carro;
  nome: string;
  telefone: string;
  ativo: boolean;
}

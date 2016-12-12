import {Report} from "./report";
export class Product {
  constructor(public productId: number = 0,
              public productName: string,
              public token: string,
              public report: Report[]) {
  }
}

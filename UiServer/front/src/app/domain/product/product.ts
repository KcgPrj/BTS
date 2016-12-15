import {Report} from "../report/report";
import {ProductToken} from "./product-token";
export class Product {
  constructor(public productId: number = 0,
              public productName: string,
              public token: ProductToken,
              public report: Report[]) {
  }
}

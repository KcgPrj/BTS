import {User} from "./user";
import {Product} from "./product";
export class Report {
  constructor(public reportId: number = 0,
              public title: string,
              public description: string,
              public createdAt: string,
              public assign: User,
              public version: string,
              public stacktrace: string,
              public log: string,
              public runtimeInfo: string,
              public product: Product,
              public state: string) {
  }
}

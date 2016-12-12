import {User} from "./user";
import {Product} from "./product";
export class Team {
  constructor(public teamId: string,
              public teamName: string,
              public owner: User,
              public member: User[],
              public product: Product[]) {

  }
}

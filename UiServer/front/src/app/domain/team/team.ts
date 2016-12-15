import {User} from "../user/user";
import {Product} from "../product/product";
export class Team {
  constructor(public teamId: string,
              public teamName: string,
              public owner: User,
              public member: User[],
              public product: Product[]) {

  }
}

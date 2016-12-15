export class ProductToken {
  private constructor(public readonly token: string){}

  static of(token: string) {
    return new ProductToken(token);
  }
  
}

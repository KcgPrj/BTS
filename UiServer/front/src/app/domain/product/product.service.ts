import {HttpWrapperService} from "../../http-wrapper.service";
import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {Product} from "./product";
import {Team} from "../team/team";
@Injectable()
export class ProductService {

  constructor(private http: HttpWrapperService) {
  }

  showProducts(teamId: string): Observable<Product[]> {
    return this.http.get(`/api/${teamId}/products/show`).map(res => res.json() as Product[])
  }

  showProduct(teamId: string, id: number): Observable<Product> {
    return this.http.get(`/api/${teamId}/products/show/${id}`).map(res => res.json() as Product);
  }

  createProduct(teamId: string, product: Product): Observable<Team> {
    return this.http.post(`/api/${teamId}/products/create`, {productName: product.productName}).map(res => res.json() as Team)
  }

  deleteProduct(teamId: string, product: Product): Observable<Team> {
    return this.http.delete(`/api/${teamId}/products/delete?productId=${product.productId}`).map(res => res.json() as Team)
  }

  update(teamId: string, product: Product): Observable<Product> {
    return this.http.post(`/api/${teamId}/products/update`, {productId: product.productId, newName: product.productName}).map(res => res.json() as Product)
  }

  regenerateToken(teamId: string, product: Product): Observable<Product> {
    return this.http.post(`/api/${teamId}/products/token/regenerate`, {productId: product}).map(res => res.json() as Product)
  }

}

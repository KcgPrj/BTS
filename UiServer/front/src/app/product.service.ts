import {HttpWrapperService} from "./http-wrapper.service";
import {Injectable} from '@angular/core';
import {Http, Response, RequestOptions, Headers} from "@angular/http";
import {Observable} from "rxjs";
import {Product} from "./product";
import {CreateProductReq} from "./create-product-req";
import {DeleteProductReq} from "./delete-product-req";
import {Team} from "./team";
import {UpdateProductReq} from "./update-product-req";
import {RegenerateTokenReq} from "./regenerate-token-req";
@Injectable()
export class ProductService {

  constructor(private http: HttpWrapperService) {
  }

  showProducts(teamId: string): Observable<Product[]> {
    return this.http.get('/api/' + teamId + '/products/show').map(res => res.json() as Product[])
  }

  showProduct(teamId: string, id: number): Observable<Product> {
    return this.http.get('/api/' + teamId + '/products/show/' + id).map(res => res.json() as Product)
  }

  createProduct(teamId: string, req: CreateProductReq): Observable<Team> {
    return this.http.post('/api/' + teamId + '/products/create', req).map(res => res.json() as Team)
  }

  deleteProduct(teamId: string, productId: number): Observable<Team> {
    return this.http.delete('/api/' + teamId + '/products/delete?productId=' + productId).map(res => res.json() as Team)
  }

  update(teamId: string, req: UpdateProductReq): Observable<Product> {
    return this.http.post('/api/' + teamId + '/products/update', req).map(res => res.json() as Product)
  }

  regenerateToken(teamId: string, req: RegenerateTokenReq): Observable<Product> {
    return this.http.post('/api/' + teamId + '/products/token/regenerate', req).map(res => res.json() as Product)
  }

}

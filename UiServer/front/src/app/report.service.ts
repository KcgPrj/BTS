import {Injectable} from '@angular/core';
import {Http, Response, RequestOptions, Headers} from "@angular/http";
import {Observable} from "rxjs";
import {HttpWrapperService} from "./http-wrapper.service";
import {CreateReportReq} from "./create-report-req";
import {Report} from "./report";
import {UpdateReportReq} from "./update-report-req";
import {ReportReq} from "./report-req";

@Injectable()
export class ReportService {

  constructor(private http: HttpWrapperService) {
  }

  createReport(createReportReq: CreateReportReq): Observable<Report> {
    return this.http.post('/api/report/create', createReportReq).map(res => res.json() as Report)
  }

  list(productId: number = null, productToken: string = null): Observable<Report> {
    if (productId != null)
      return this.http.get('/api/report/list?productId=' + productId).map(res => res.json() as Report);

    if (productToken != null)
      return this.http.get('/api/report/list?productToken=' + productToken).map(res => res.json() as Report);

    throw new Error("productIdとproductToken両方にnullが")
  }

  update(req: UpdateReportReq): Observable<Report> {
    return this.http.post('/api/report/update', req).map(res => res.json() as Report);
  }

  open(req: ReportReq): Observable<Report> {
    return this.http.post('/api/report/open', req).map(res => res.json() as Report);
  }

  close(req: ReportReq): Observable<Report> {
    return this.http.post('/api/report/close', req).map(res => res.json() as Report);
  }

}

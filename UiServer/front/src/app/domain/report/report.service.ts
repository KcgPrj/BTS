import {Injectable} from '@angular/core';
import {Observable} from "rxjs";
import {HttpWrapperService} from "../../http-wrapper.service";
import {Report} from "./report";
import {Product} from "../product/product";
import {ProductToken} from "../product/product-token";

@Injectable()
export class ReportService {

  constructor(private http: HttpWrapperService) {
  }

  createReport(productToken: ProductToken, report: Report): Observable<Report> {
    return this.http.post('/api/report/create', {
      productToken: productToken.token,
      assignUserId: report.assign.id,
      title: report.title,
      description: report.description,
      version: report.version,
      stacktrace: report.stacktrace,
      log: report.log,
      runTimeInfo: report.runtimeInfo
    }).map(res => res.json() as Report)
  }

  list(productId: number): Observable<Report[]> {
    return this.http.get(`/api/report/list?productId=${productId}`).map(res => res.json() as Report[]);
  }

  update(report: Report): Observable<Report> {
    return this.http.post('/api/report/update', {
      reportId: report.reportId,
      newDescription: report.description,
      newAssignUserId: report.assign
    }).map(res => res.json() as Report);
  }

  open(report: Report): Observable<Report> {
    return this.http.post('/api/report/open', {reportId: report.reportId}).map(res => res.json() as Report);
  }

  close(report: Report): Observable<Report> {
    return this.http.post('/api/report/close', {reportId: report.reportId}).map(res => res.json() as Report);
  }

}

export class CreateReportReq {
  constructor(public productToken: string,
              public assignUserId: number,
              public title: string,
              public description: string,
              public version: string,
              public stacktrace: string,
              public log: string,
              public runTimeInfo: string) {
  }
}

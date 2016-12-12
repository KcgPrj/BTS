export class UpdateReportReq {
  constructor(public reportId: number,
              public newDescription: string,
              public newAssignUserId: number) {
  }
}

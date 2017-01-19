import { Injectable } from '@angular/core';

@Injectable()
export class ModalService {

  private _memberModalOpen: boolean = false;
  private _productModalOpen: boolean = false;
  private _teamModalOpen: boolean = false;

  constructor() { }

  openMemberModal() {
    this._memberModalOpen = true;
    this._productModalOpen = false;
    this._teamModalOpen = false;
  }

  openProductModalOpen() {
    this._productModalOpen = true;
    this._memberModalOpen = false;
    this._teamModalOpen = false;
  }

  openTeamModal() {
    this._teamModalOpen = true;
    this._memberModalOpen = false;
    this._productModalOpen = false;
  }

  closeAll() {
    this.closeMemberModal();
    this.closeProductModal();
    this.closeTeamModal();
  }

  closeMemberModal() {
    this._memberModalOpen = false;
  }

  closeProductModal() {
    this._productModalOpen = false;
  }

  closeTeamModal() {
    this._teamModalOpen = false;
  }


  get memberModalOpen(): boolean {
    return this._memberModalOpen;
  }

  get productModalOpen(): boolean {
    return this._productModalOpen;
  }

  get teamModalOpen(): boolean {
    return this._teamModalOpen;
  }
}

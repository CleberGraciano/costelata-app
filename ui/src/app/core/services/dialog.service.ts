import { Injectable } from '@angular/core';
import { ConfirmationService } from 'primeng/api';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';

@Injectable({
  providedIn: 'root'
})
export class DialogServiceApp {
  ref?: DynamicDialogRef;

  constructor(
    private confirmationService: ConfirmationService,
    private dialogService: DialogService
  ) {}

  confirm(message: string, accept: () => void, reject?: () => void): void {
    this.confirmationService.confirm({
      message,
      header: 'Confirmação',
      icon: 'pi pi-exclamation-triangle',
      acceptLabel: 'Sim',
      rejectLabel: 'Não',
      accept,
      reject,
    });
  }

  info(title: string, message: string, onClose?: () => void): void {
    this.confirmationService.confirm({
      message,
      header: title,
      icon: 'pi pi-info-circle',
      acceptLabel: 'OK',
      rejectVisible: false,
      accept: () => onClose?.(),
    });
  }

  infoVerificacao(
    title: string,
    message: string,
    btnOk: string,
    btnCancel: string,
    onConfirm: () => void,
    onCancel?: () => void
  ): void {
    this.confirmationService.confirm({
      header: title,
      message,
      icon: 'pi pi-question-circle',
      acceptLabel: btnOk,
      rejectLabel: btnCancel,
      accept: () => onConfirm(),
      reject: () => onCancel?.()
    });
  }

  // confirmWithText(title: string, placeholder: string, callback: (value: string) => void): void {
  //   this.ref = this.dialogService.open(TextConfirmComponent, {
  //     header: title,
  //     width: '400px',
  //     data: { placeholder },
  //     closable: false,
  //     dismissableMask: true,
  //     styleClass: 'p-dialog-text-confirm'
  //   });

  //   this.ref.onClose.subscribe((value: string) => {
  //     if (value) {
  //       callback(value);
  //     }
  //   });
  // }

  closeDialog(): void {
    this.ref?.close();
  }
}

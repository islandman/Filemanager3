import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDirectory } from '../directory.model';
import { DirectoryService } from '../service/directory.service';

@Component({
  templateUrl: './directory-delete-dialog.component.html',
})
export class DirectoryDeleteDialogComponent {
  directory?: IDirectory;

  constructor(protected directoryService: DirectoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.directoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

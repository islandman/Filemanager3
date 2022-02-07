import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDirectory, Directory } from '../directory.model';
import { DirectoryService } from '../service/directory.service';

@Component({
  selector: 'jhi-directory-update',
  templateUrl: './directory-update.component.html',
})
export class DirectoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uuid: [],
    parentUuid: [],
    name: [],
    tenantId: [],
    createDate: [],
    userName: [],
    numFiles: [],
    isDir: [],
    virtualPath: [],
  });

  constructor(protected directoryService: DirectoryService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ directory }) => {
      if (directory.id === undefined) {
        const today = dayjs().startOf('day');
        directory.createDate = today;
      }

      this.updateForm(directory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const directory = this.createFromForm();
    if (directory.id !== undefined) {
      this.subscribeToSaveResponse(this.directoryService.update(directory));
    } else {
      this.subscribeToSaveResponse(this.directoryService.create(directory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDirectory>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(directory: IDirectory): void {
    this.editForm.patchValue({
      id: directory.id,
      uuid: directory.uuid,
      parentUuid: directory.parentUuid,
      name: directory.name,
      tenantId: directory.tenantId,
      createDate: directory.createDate ? directory.createDate.format(DATE_TIME_FORMAT) : null,
      userName: directory.userName,
      numFiles: directory.numFiles,
      isDir: directory.isDir,
      virtualPath: directory.virtualPath,
    });
  }

  protected createFromForm(): IDirectory {
    return {
      ...new Directory(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      parentUuid: this.editForm.get(['parentUuid'])!.value,
      name: this.editForm.get(['name'])!.value,
      tenantId: this.editForm.get(['tenantId'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      userName: this.editForm.get(['userName'])!.value,
      numFiles: this.editForm.get(['numFiles'])!.value,
      isDir: this.editForm.get(['isDir'])!.value,
      virtualPath: this.editForm.get(['virtualPath'])!.value,
    };
  }
}

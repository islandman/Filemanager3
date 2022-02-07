import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFile, File } from '../file.model';
import { FileService } from '../service/file.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-file-update',
  templateUrl: './file-update.component.html',
})
export class FileUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uuid: [],
    directoryUuid: [],
    name: [],
    remotePath: [],
    tenantId: [],
    contentType: [],
    sizz: [],
    createDate: [],
    userName: [],
    content: [],
    contentContentType: [],
    virtualPath: [],
    isDir: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected fileService: FileService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ file }) => {
      if (file.id === undefined) {
        const today = dayjs().startOf('day');
        file.createDate = today;
      }

      this.updateForm(file);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('filemanager3App.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const file = this.createFromForm();
    if (file.id !== undefined) {
      this.subscribeToSaveResponse(this.fileService.update(file));
    } else {
      this.subscribeToSaveResponse(this.fileService.create(file));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFile>>): void {
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

  protected updateForm(file: IFile): void {
    this.editForm.patchValue({
      id: file.id,
      uuid: file.uuid,
      directoryUuid: file.directoryUuid,
      name: file.name,
      remotePath: file.remotePath,
      tenantId: file.tenantId,
      contentType: file.contentType,
      sizz: file.sizz,
      createDate: file.createDate ? file.createDate.format(DATE_TIME_FORMAT) : null,
      userName: file.userName,
      content: file.content,
      contentContentType: file.contentContentType,
      virtualPath: file.virtualPath,
      isDir: file.isDir,
    });
  }

  protected createFromForm(): IFile {
    return {
      ...new File(),
      id: this.editForm.get(['id'])!.value,
      uuid: this.editForm.get(['uuid'])!.value,
      directoryUuid: this.editForm.get(['directoryUuid'])!.value,
      name: this.editForm.get(['name'])!.value,
      remotePath: this.editForm.get(['remotePath'])!.value,
      tenantId: this.editForm.get(['tenantId'])!.value,
      contentType: this.editForm.get(['contentType'])!.value,
      sizz: this.editForm.get(['sizz'])!.value,
      createDate: this.editForm.get(['createDate'])!.value ? dayjs(this.editForm.get(['createDate'])!.value, DATE_TIME_FORMAT) : undefined,
      userName: this.editForm.get(['userName'])!.value,
      contentContentType: this.editForm.get(['contentContentType'])!.value,
      content: this.editForm.get(['content'])!.value,
      virtualPath: this.editForm.get(['virtualPath'])!.value,
      isDir: this.editForm.get(['isDir'])!.value,
    };
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDirectory, getDirectoryIdentifier } from '../directory.model';

export type EntityResponseType = HttpResponse<IDirectory>;
export type EntityArrayResponseType = HttpResponse<IDirectory[]>;

@Injectable({ providedIn: 'root' })
export class DirectoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/directories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(directory: IDirectory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(directory);
    return this.http
      .post<IDirectory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(directory: IDirectory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(directory);
    return this.http
      .put<IDirectory>(`${this.resourceUrl}/${getDirectoryIdentifier(directory) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(directory: IDirectory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(directory);
    return this.http
      .patch<IDirectory>(`${this.resourceUrl}/${getDirectoryIdentifier(directory) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDirectory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDirectory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDirectoryToCollectionIfMissing(
    directoryCollection: IDirectory[],
    ...directoriesToCheck: (IDirectory | null | undefined)[]
  ): IDirectory[] {
    const directories: IDirectory[] = directoriesToCheck.filter(isPresent);
    if (directories.length > 0) {
      const directoryCollectionIdentifiers = directoryCollection.map(directoryItem => getDirectoryIdentifier(directoryItem)!);
      const directoriesToAdd = directories.filter(directoryItem => {
        const directoryIdentifier = getDirectoryIdentifier(directoryItem);
        if (directoryIdentifier == null || directoryCollectionIdentifiers.includes(directoryIdentifier)) {
          return false;
        }
        directoryCollectionIdentifiers.push(directoryIdentifier);
        return true;
      });
      return [...directoriesToAdd, ...directoryCollection];
    }
    return directoryCollection;
  }

  protected convertDateFromClient(directory: IDirectory): IDirectory {
    return Object.assign({}, directory, {
      createDate: directory.createDate?.isValid() ? directory.createDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createDate = res.body.createDate ? dayjs(res.body.createDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((directory: IDirectory) => {
        directory.createDate = directory.createDate ? dayjs(directory.createDate) : undefined;
      });
    }
    return res;
  }
}

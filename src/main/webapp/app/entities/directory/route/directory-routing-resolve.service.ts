import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDirectory, Directory } from '../directory.model';
import { DirectoryService } from '../service/directory.service';

@Injectable({ providedIn: 'root' })
export class DirectoryRoutingResolveService implements Resolve<IDirectory> {
  constructor(protected service: DirectoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDirectory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((directory: HttpResponse<Directory>) => {
          if (directory.body) {
            return of(directory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Directory());
  }
}

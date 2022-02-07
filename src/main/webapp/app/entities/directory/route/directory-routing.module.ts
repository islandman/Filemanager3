import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DirectoryComponent } from '../list/directory.component';
import { DirectoryDetailComponent } from '../detail/directory-detail.component';
import { DirectoryUpdateComponent } from '../update/directory-update.component';
import { DirectoryRoutingResolveService } from './directory-routing-resolve.service';

const directoryRoute: Routes = [
  {
    path: '',
    component: DirectoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DirectoryDetailComponent,
    resolve: {
      directory: DirectoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DirectoryUpdateComponent,
    resolve: {
      directory: DirectoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DirectoryUpdateComponent,
    resolve: {
      directory: DirectoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(directoryRoute)],
  exports: [RouterModule],
})
export class DirectoryRoutingModule {}

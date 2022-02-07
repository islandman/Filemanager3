import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DirectoryComponent } from './list/directory.component';
import { DirectoryDetailComponent } from './detail/directory-detail.component';
import { DirectoryUpdateComponent } from './update/directory-update.component';
import { DirectoryDeleteDialogComponent } from './delete/directory-delete-dialog.component';
import { DirectoryRoutingModule } from './route/directory-routing.module';

@NgModule({
  imports: [SharedModule, DirectoryRoutingModule],
  declarations: [DirectoryComponent, DirectoryDetailComponent, DirectoryUpdateComponent, DirectoryDeleteDialogComponent],
  entryComponents: [DirectoryDeleteDialogComponent],
})
export class DirectoryModule {}

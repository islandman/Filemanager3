import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'file',
        data: { pageTitle: 'filemanager3App.file.home.title' },
        loadChildren: () => import('./file/file.module').then(m => m.FileModule),
      },
      {
        path: 'directory',
        data: { pageTitle: 'filemanager3App.directory.home.title' },
        loadChildren: () => import('./directory/directory.module').then(m => m.DirectoryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

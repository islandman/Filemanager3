import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDirectory } from '../directory.model';

@Component({
  selector: 'jhi-directory-detail',
  templateUrl: './directory-detail.component.html',
})
export class DirectoryDetailComponent implements OnInit {
  directory: IDirectory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ directory }) => {
      this.directory = directory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

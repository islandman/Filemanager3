import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DirectoryDetailComponent } from './directory-detail.component';

describe('Directory Management Detail Component', () => {
  let comp: DirectoryDetailComponent;
  let fixture: ComponentFixture<DirectoryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DirectoryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ directory: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DirectoryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DirectoryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load directory on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.directory).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

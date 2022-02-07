import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DirectoryService } from '../service/directory.service';
import { IDirectory, Directory } from '../directory.model';

import { DirectoryUpdateComponent } from './directory-update.component';

describe('Directory Management Update Component', () => {
  let comp: DirectoryUpdateComponent;
  let fixture: ComponentFixture<DirectoryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let directoryService: DirectoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [DirectoryUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DirectoryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DirectoryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    directoryService = TestBed.inject(DirectoryService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const directory: IDirectory = { id: 456 };

      activatedRoute.data = of({ directory });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(directory));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Directory>>();
      const directory = { id: 123 };
      jest.spyOn(directoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ directory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: directory }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(directoryService.update).toHaveBeenCalledWith(directory);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Directory>>();
      const directory = new Directory();
      jest.spyOn(directoryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ directory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: directory }));
      saveSubject.complete();

      // THEN
      expect(directoryService.create).toHaveBeenCalledWith(directory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Directory>>();
      const directory = { id: 123 };
      jest.spyOn(directoryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ directory });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(directoryService.update).toHaveBeenCalledWith(directory);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});

import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDirectory, Directory } from '../directory.model';

import { DirectoryService } from './directory.service';

describe('Directory Service', () => {
  let service: DirectoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IDirectory;
  let expectedResult: IDirectory | IDirectory[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DirectoryService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      uuid: 'AAAAAAA',
      parentUuid: 'AAAAAAA',
      name: 'AAAAAAA',
      tenantId: 0,
      createDate: currentDate,
      userName: 'AAAAAAA',
      numFiles: 0,
      isDir: false,
      virtualPath: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          createDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Directory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          createDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Directory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Directory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uuid: 'BBBBBB',
          parentUuid: 'BBBBBB',
          name: 'BBBBBB',
          tenantId: 1,
          createDate: currentDate.format(DATE_TIME_FORMAT),
          userName: 'BBBBBB',
          numFiles: 1,
          isDir: true,
          virtualPath: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Directory', () => {
      const patchObject = Object.assign(
        {
          uuid: 'BBBBBB',
          parentUuid: 'BBBBBB',
          name: 'BBBBBB',
          numFiles: 1,
          isDir: true,
        },
        new Directory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          createDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Directory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          uuid: 'BBBBBB',
          parentUuid: 'BBBBBB',
          name: 'BBBBBB',
          tenantId: 1,
          createDate: currentDate.format(DATE_TIME_FORMAT),
          userName: 'BBBBBB',
          numFiles: 1,
          isDir: true,
          virtualPath: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          createDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Directory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDirectoryToCollectionIfMissing', () => {
      it('should add a Directory to an empty array', () => {
        const directory: IDirectory = { id: 123 };
        expectedResult = service.addDirectoryToCollectionIfMissing([], directory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(directory);
      });

      it('should not add a Directory to an array that contains it', () => {
        const directory: IDirectory = { id: 123 };
        const directoryCollection: IDirectory[] = [
          {
            ...directory,
          },
          { id: 456 },
        ];
        expectedResult = service.addDirectoryToCollectionIfMissing(directoryCollection, directory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Directory to an array that doesn't contain it", () => {
        const directory: IDirectory = { id: 123 };
        const directoryCollection: IDirectory[] = [{ id: 456 }];
        expectedResult = service.addDirectoryToCollectionIfMissing(directoryCollection, directory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(directory);
      });

      it('should add only unique Directory to an array', () => {
        const directoryArray: IDirectory[] = [{ id: 123 }, { id: 456 }, { id: 5804 }];
        const directoryCollection: IDirectory[] = [{ id: 123 }];
        expectedResult = service.addDirectoryToCollectionIfMissing(directoryCollection, ...directoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const directory: IDirectory = { id: 123 };
        const directory2: IDirectory = { id: 456 };
        expectedResult = service.addDirectoryToCollectionIfMissing([], directory, directory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(directory);
        expect(expectedResult).toContain(directory2);
      });

      it('should accept null and undefined values', () => {
        const directory: IDirectory = { id: 123 };
        expectedResult = service.addDirectoryToCollectionIfMissing([], null, directory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(directory);
      });

      it('should return initial array if no Directory is added', () => {
        const directoryCollection: IDirectory[] = [{ id: 123 }];
        expectedResult = service.addDirectoryToCollectionIfMissing(directoryCollection, undefined, null);
        expect(expectedResult).toEqual(directoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});

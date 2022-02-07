import dayjs from 'dayjs/esm';

export interface IFile {
  id?: number;
  uuid?: string | null;
  directoryUuid?: string | null;
  name?: string | null;
  remotePath?: string | null;
  tenantId?: number | null;
  contentType?: string | null;
  sizz?: number | null;
  createDate?: dayjs.Dayjs | null;
  userName?: string | null;
  contentContentType?: string | null;
  content?: string | null;
  virtualPath?: string | null;
  isDir?: boolean | null;
}

export class File implements IFile {
  constructor(
    public id?: number,
    public uuid?: string | null,
    public directoryUuid?: string | null,
    public name?: string | null,
    public remotePath?: string | null,
    public tenantId?: number | null,
    public contentType?: string | null,
    public sizz?: number | null,
    public createDate?: dayjs.Dayjs | null,
    public userName?: string | null,
    public contentContentType?: string | null,
    public content?: string | null,
    public virtualPath?: string | null,
    public isDir?: boolean | null
  ) {
    this.isDir = this.isDir ?? false;
  }
}

export function getFileIdentifier(file: IFile): number | undefined {
  return file.id;
}

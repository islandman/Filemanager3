import dayjs from 'dayjs/esm';

export interface IDirectory {
  id?: number;
  uuid?: string | null;
  parentUuid?: string | null;
  name?: string | null;
  tenantId?: number | null;
  createDate?: dayjs.Dayjs | null;
  userName?: string | null;
  numFiles?: number | null;
  isDir?: boolean | null;
  virtualPath?: string | null;
}

export class Directory implements IDirectory {
  constructor(
    public id?: number,
    public uuid?: string | null,
    public parentUuid?: string | null,
    public name?: string | null,
    public tenantId?: number | null,
    public createDate?: dayjs.Dayjs | null,
    public userName?: string | null,
    public numFiles?: number | null,
    public isDir?: boolean | null,
    public virtualPath?: string | null
  ) {
    this.isDir = this.isDir ?? false;
  }
}

export function getDirectoryIdentifier(directory: IDirectory): number | undefined {
  return directory.id;
}

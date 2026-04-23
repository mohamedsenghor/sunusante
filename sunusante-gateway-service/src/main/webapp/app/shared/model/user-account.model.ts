import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { UserRole } from 'app/shared/model/enumerations/user-role.model';

export interface IUserAccount {
  id?: number;
  mfaSecret?: string | null;
  role?: keyof typeof UserRole | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  internalUser?: IUser | null;
}

export const defaultValue: Readonly<IUserAccount> = {};

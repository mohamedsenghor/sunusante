import dayjs from 'dayjs';
import { IdentifierType } from 'app/shared/model/enumerations/identifier-type.model';

export interface IPatient {
  id?: number;
  login?: string;
  pseudo?: string | null;
  firstName?: string;
  lastName?: string;
  birthDate?: dayjs.Dayjs;
  idType?: keyof typeof IdentifierType | null;
  idValue?: string | null;
}

export const defaultValue: Readonly<IPatient> = {};

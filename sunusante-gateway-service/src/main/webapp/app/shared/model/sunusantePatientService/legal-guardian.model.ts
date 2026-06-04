import dayjs from 'dayjs';
import { IPatient } from 'app/shared/model/sunusantePatientService/patient.model';

export interface ILegalGuardian {
  id?: number;
  guardianType?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  dependent?: IPatient | null;
}

export const defaultValue: Readonly<ILegalGuardian> = {};

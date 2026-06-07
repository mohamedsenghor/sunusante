import dayjs from 'dayjs';
import { ConsentStatus } from 'app/shared/model/enumerations/consent-status.model';

export interface IPatientConsent {
  id?: number;
  patientPseudo?: string;
  doctorLogin?: string;
  consentDate?: dayjs.Dayjs | null;
  expiryDate?: dayjs.Dayjs | null;
  status?: keyof typeof ConsentStatus | null;
}

export const defaultValue: Readonly<IPatientConsent> = {};

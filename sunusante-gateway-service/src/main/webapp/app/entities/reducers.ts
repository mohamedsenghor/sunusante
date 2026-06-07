import userAccount from 'app/entities/user-account/user-account.reducer';
import patient from 'app/entities/sunusantePatientService/patient/patient.reducer';
import legalGuardian from 'app/entities/sunusantePatientService/legal-guardian/legal-guardian.reducer';
import patientConsent from 'app/entities/sunusantePatientService/patient-consent/patient-consent.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  userAccount,
  patient,
  legalGuardian,
  patientConsent,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;

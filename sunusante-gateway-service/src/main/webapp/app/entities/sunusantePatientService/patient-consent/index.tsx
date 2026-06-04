import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PatientConsent from './patient-consent';
import PatientConsentDetail from './patient-consent-detail';
import PatientConsentUpdate from './patient-consent-update';
import PatientConsentDeleteDialog from './patient-consent-delete-dialog';

const PatientConsentRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PatientConsent />} />
    <Route path="new" element={<PatientConsentUpdate />} />
    <Route path=":id">
      <Route index element={<PatientConsentDetail />} />
      <Route path="edit" element={<PatientConsentUpdate />} />
      <Route path="delete" element={<PatientConsentDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PatientConsentRoutes;

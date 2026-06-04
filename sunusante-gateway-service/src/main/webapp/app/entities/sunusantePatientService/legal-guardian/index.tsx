import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import LegalGuardian from './legal-guardian';
import LegalGuardianDetail from './legal-guardian-detail';
import LegalGuardianUpdate from './legal-guardian-update';
import LegalGuardianDeleteDialog from './legal-guardian-delete-dialog';

const LegalGuardianRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<LegalGuardian />} />
    <Route path="new" element={<LegalGuardianUpdate />} />
    <Route path=":id">
      <Route index element={<LegalGuardianDetail />} />
      <Route path="edit" element={<LegalGuardianUpdate />} />
      <Route path="delete" element={<LegalGuardianDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default LegalGuardianRoutes;

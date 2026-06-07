import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import { ReducersMapObject, combineReducers } from '@reduxjs/toolkit';

import getStore from 'app/config/store';

import entitiesReducers from './reducers';

import UserAccount from './user-account';
import Patient from './sunusantePatientService/patient';
import LegalGuardian from './sunusantePatientService/legal-guardian';
import PatientConsent from './sunusantePatientService/patient-consent';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  const store = getStore();
  store.injectReducer('sunusantegatewayservice', combineReducers(entitiesReducers as ReducersMapObject));
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="user-account/*" element={<UserAccount />} />
        <Route path="patient/*" element={<Patient />} />
        <Route path="legal-guardian/*" element={<LegalGuardian />} />
        <Route path="patient-consent/*" element={<PatientConsent />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};

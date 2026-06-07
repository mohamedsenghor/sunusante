import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPatientConsent } from 'app/shared/model/sunusantePatientService/patient-consent.model';
import { ConsentStatus } from 'app/shared/model/enumerations/consent-status.model';
import { getEntity, updateEntity, createEntity, reset } from './patient-consent.reducer';

export const PatientConsentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const patientConsentEntity = useAppSelector(state => state.sunusantegatewayservice.patientConsent.entity);
  const loading = useAppSelector(state => state.sunusantegatewayservice.patientConsent.loading);
  const updating = useAppSelector(state => state.sunusantegatewayservice.patientConsent.updating);
  const updateSuccess = useAppSelector(state => state.sunusantegatewayservice.patientConsent.updateSuccess);
  const consentStatusValues = Object.keys(ConsentStatus);

  const handleClose = () => {
    navigate('/patient-consent' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.consentDate = convertDateTimeToServer(values.consentDate);
    values.expiryDate = convertDateTimeToServer(values.expiryDate);

    const entity = {
      ...patientConsentEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          consentDate: displayDefaultDateTime(),
          expiryDate: displayDefaultDateTime(),
        }
      : {
          status: 'ACTIVE',
          ...patientConsentEntity,
          consentDate: convertDateTimeFromServer(patientConsentEntity.consentDate),
          expiryDate: convertDateTimeFromServer(patientConsentEntity.expiryDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.home.createOrEditLabel"
            data-cy="PatientConsentCreateUpdateHeading"
          >
            <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.home.createOrEditLabel">
              Create or edit a PatientConsent
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="patient-consent-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.patientPseudo')}
                id="patient-consent-patientPseudo"
                name="patientPseudo"
                data-cy="patientPseudo"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.doctorLogin')}
                id="patient-consent-doctorLogin"
                name="doctorLogin"
                data-cy="doctorLogin"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.consentDate')}
                id="patient-consent-consentDate"
                name="consentDate"
                data-cy="consentDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.expiryDate')}
                id="patient-consent-expiryDate"
                name="expiryDate"
                data-cy="expiryDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServicePatientConsent.status')}
                id="patient-consent-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {consentStatusValues.map(consentStatus => (
                  <option value={consentStatus} key={consentStatus}>
                    {translate('sunusanteGatewayServiceApp.ConsentStatus.' + consentStatus)}
                  </option>
                ))}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/patient-consent" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PatientConsentUpdate;

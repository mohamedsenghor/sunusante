import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPatient } from 'app/shared/model/sunusantePatientService/patient.model';
import { IdentifierType } from 'app/shared/model/enumerations/identifier-type.model';
import { getEntity, updateEntity, createEntity, reset } from './patient.reducer';

export const PatientUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const patientEntity = useAppSelector(state => state.sunusantegatewayservice.patient.entity);
  const loading = useAppSelector(state => state.sunusantegatewayservice.patient.loading);
  const updating = useAppSelector(state => state.sunusantegatewayservice.patient.updating);
  const updateSuccess = useAppSelector(state => state.sunusantegatewayservice.patient.updateSuccess);
  const identifierTypeValues = Object.keys(IdentifierType);

  const handleClose = () => {
    navigate('/patient' + location.search);
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

    const entity = {
      ...patientEntity,
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
      ? {}
      : {
          idType: 'NIN',
          ...patientEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sunusanteGatewayServiceApp.sunusantePatientServicePatient.home.createOrEditLabel" data-cy="PatientCreateUpdateHeading">
            <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatient.home.createOrEditLabel">
              Create or edit a Patient
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
                  id="patient-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServicePatient.login')}
                id="patient-login"
                name="login"
                data-cy="login"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServicePatient.pseudo')}
                id="patient-pseudo"
                name="pseudo"
                data-cy="pseudo"
                type="text"
                validate={{}}
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServicePatient.firstName')}
                id="patient-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServicePatient.lastName')}
                id="patient-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServicePatient.birthDate')}
                id="patient-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServicePatient.idType')}
                id="patient-idType"
                name="idType"
                data-cy="idType"
                type="select"
              >
                {identifierTypeValues.map(identifierType => (
                  <option value={identifierType} key={identifierType}>
                    {translate('sunusanteGatewayServiceApp.IdentifierType.' + identifierType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServicePatient.idValue')}
                id="patient-idValue"
                name="idValue"
                data-cy="idValue"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/patient" replace color="info">
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

export default PatientUpdate;

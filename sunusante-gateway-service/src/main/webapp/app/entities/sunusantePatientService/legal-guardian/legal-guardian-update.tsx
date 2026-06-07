import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPatient } from 'app/shared/model/sunusantePatientService/patient.model';
import { getEntities as getPatients } from 'app/entities/sunusantePatientService/patient/patient.reducer';
import { ILegalGuardian } from 'app/shared/model/sunusantePatientService/legal-guardian.model';
import { getEntity, updateEntity, createEntity, reset } from './legal-guardian.reducer';

export const LegalGuardianUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const patients = useAppSelector(state => state.sunusantegatewayservice.patient.entities);
  const legalGuardianEntity = useAppSelector(state => state.sunusantegatewayservice.legalGuardian.entity);
  const loading = useAppSelector(state => state.sunusantegatewayservice.legalGuardian.loading);
  const updating = useAppSelector(state => state.sunusantegatewayservice.legalGuardian.updating);
  const updateSuccess = useAppSelector(state => state.sunusantegatewayservice.legalGuardian.updateSuccess);

  const handleClose = () => {
    navigate('/legal-guardian' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPatients({}));
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...legalGuardianEntity,
      ...values,
      dependent: patients.find(it => it.id.toString() === values.dependent?.toString()),
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
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          ...legalGuardianEntity,
          createdDate: convertDateTimeFromServer(legalGuardianEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(legalGuardianEntity.lastModifiedDate),
          dependent: legalGuardianEntity?.dependent?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2
            id="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.home.createOrEditLabel"
            data-cy="LegalGuardianCreateUpdateHeading"
          >
            <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.home.createOrEditLabel">
              Create or edit a LegalGuardian
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
                  id="legal-guardian-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.guardianType')}
                id="legal-guardian-guardianType"
                name="guardianType"
                data-cy="guardianType"
                type="text"
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.createdBy')}
                id="legal-guardian-createdBy"
                name="createdBy"
                data-cy="createdBy"
                type="text"
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.createdDate')}
                id="legal-guardian-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.lastModifiedBy')}
                id="legal-guardian-lastModifiedBy"
                name="lastModifiedBy"
                data-cy="lastModifiedBy"
                type="text"
              />
              <ValidatedField
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.lastModifiedDate')}
                id="legal-guardian-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="legal-guardian-dependent"
                name="dependent"
                data-cy="dependent"
                label={translate('sunusanteGatewayServiceApp.sunusantePatientServiceLegalGuardian.dependent')}
                type="select"
              >
                <option value="" key="0" />
                {patients
                  ? patients.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/legal-guardian" replace color="info">
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

export default LegalGuardianUpdate;

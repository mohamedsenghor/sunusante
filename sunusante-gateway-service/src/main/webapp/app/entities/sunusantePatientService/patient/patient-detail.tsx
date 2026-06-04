import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './patient.reducer';

export const PatientDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const patientEntity = useAppSelector(state => state.sunusantegatewayservice.patient.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="patientDetailsHeading">
          <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatient.detail.title">Patient</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{patientEntity.id}</dd>
          <dt>
            <span id="login">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatient.login">Login</Translate>
            </span>
          </dt>
          <dd>{patientEntity.login}</dd>
          <dt>
            <span id="pseudo">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatient.pseudo">Pseudo</Translate>
            </span>
          </dt>
          <dd>{patientEntity.pseudo}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatient.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{patientEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatient.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{patientEntity.lastName}</dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatient.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>
            {patientEntity.birthDate ? <TextFormat value={patientEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="idType">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatient.idType">Id Type</Translate>
            </span>
          </dt>
          <dd>{patientEntity.idType}</dd>
          <dt>
            <span id="idValue">
              <Translate contentKey="sunusanteGatewayServiceApp.sunusantePatientServicePatient.idValue">Id Value</Translate>
            </span>
          </dt>
          <dd>{patientEntity.idValue}</dd>
        </dl>
        <Button tag={Link} to="/patient" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/patient/${patientEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PatientDetail;
